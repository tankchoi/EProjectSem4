package vn.aptech.java.aspects;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.aptech.java.models.User;
import vn.aptech.java.services.AuditLogService;
import vn.aptech.java.services.UserService;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterReturning(pointcut = "execution(* vn.aptech.java.services..*.create*(..))", returning = "result")
    public void afterCreate(JoinPoint joinPoint, Object result) throws Exception {
        // Kiểm tra nếu không có user đăng nhập thì bỏ qua audit
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return; // Bỏ qua audit khi chưa có user đăng nhập
        }

        Object entity = result;
        Long id = getEntityId(entity);

        String newJson = objectMapper.writeValueAsString(entity);

        auditLogService.log(
                currentUser,
                entity.getClass().getSimpleName(),
                id,
                "CREATE",
                null,
                newJson);
    }

    @Around("execution(* vn.aptech.java.services..*.update*(..))")
    public Object aroundUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return joinPoint.proceed(); // Bỏ qua audit khi chưa có user đăng nhập
        }

        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || args[0] == null) {
            return joinPoint.proceed();
        }

        Object newEntity = args[0];
        Long id = getEntityId(newEntity);

        Object oldEntity = entityManager.find(newEntity.getClass(), id);
        String oldJson = oldEntity == null ? null : objectMapper.writeValueAsString(oldEntity);

        Object result = joinPoint.proceed(); // Gọi hàm update

        String newJson = objectMapper.writeValueAsString(newEntity);

        auditLogService.log(
                currentUser,
                newEntity.getClass().getSimpleName(),
                id,
                "UPDATE",
                oldJson,
                newJson);

        return result;
    }

    @Before("execution(* vn.aptech.java.services..*.delete*(..))")
    public void beforeDelete(JoinPoint joinPoint) throws Exception {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return; // Bỏ qua audit khi chưa có user đăng nhập
        }

        Object entity = joinPoint.getArgs()[0];
        Long id = getEntityId(entity);
        String oldJson = objectMapper.writeValueAsString(entity);

        auditLogService.log(
                currentUser,
                entity.getClass().getSimpleName(),
                id,
                "DELETE",
                oldJson,
                null);
    }

    private Long getEntityId(Object entity) throws Exception {
        Method getIdMethod = entity.getClass().getMethod("getId");
        Object id = getIdMethod.invoke(entity);
        return (Long) id;
    }
}
