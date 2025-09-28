package vn.aptech.java.aspects;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
        Object entity = result;
        Long id = getEntityId(entity);
        if (id == null) {
            return;
        }
        String newJson = objectMapper.writeValueAsString(entity);

        auditLogService.log(
                userService.getCurrentUser(),
                entity.getClass().getSimpleName(),
                id,
                "CREATE",
                null,
                newJson);
    }

    @Around("execution(* vn.aptech.java.services..*.update*(..))")
    public Object aroundUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || args[0] == null) {
            return joinPoint.proceed();
        }

        Object dto = args[0]; // DTO object
        Long id = getEntityId(dto); // Lấy ID từ DTO

        if (id == null) {
            return joinPoint.proceed();
        }

        // Xác định Entity class từ method name
        String methodName = joinPoint.getSignature().getName();
        Class<?> entityClass = getEntityClassFromMethodName(methodName);

        if (entityClass == null) {
            return joinPoint.proceed();
        }

        // Lấy old entity từ database
        Object oldEntity = entityManager.find(entityClass, id);
        String oldJson = oldEntity == null ? null : objectMapper.writeValueAsString(oldEntity);

        // Thực hiện update
        Object result = joinPoint.proceed();

        // Lấy new entity sau khi update
        Object newEntity = entityManager.find(entityClass, id);
        String newJson = newEntity == null ? null : objectMapper.writeValueAsString(newEntity);

        auditLogService.log(
                userService.getCurrentUser(),
                entityClass.getSimpleName(),
                id,
                "UPDATE",
                oldJson,
                newJson);

        return result;
    }

    @Before("execution(* vn.aptech.java.services..*.delete*(..))")
    public void beforeDelete(JoinPoint joinPoint) throws Exception {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || args[0] == null) {
            return;
        }

        Long id = (Long) args[0]; // args[0] là ID

        // Xác định Entity class từ method name
        String methodName = joinPoint.getSignature().getName();
        Class<?> entityClass = getEntityClassFromMethodName(methodName);

        if (entityClass == null) {
            return;
        }

        // Lấy entity từ database bằng ID
        Object entity = entityManager.find(entityClass, id);
        if (entity == null) {
            return;
        }

        String oldJson = objectMapper.writeValueAsString(entity);

        auditLogService.log(
                userService.getCurrentUser(),
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

    private Class<?> getEntityClassFromMethodName(String methodName) {
        try {
            if (methodName.toLowerCase().contains("model")) {
                return Class.forName("vn.aptech.java.models.Model");
            }
            if (methodName.toLowerCase().contains("laptop")) {
                return Class.forName("vn.aptech.java.models.Laptop");
            }
            if (methodName.toLowerCase().contains("user") || methodName.toLowerCase().contains("customer")
                    || methodName.toLowerCase().contains("staff")) {
                return Class.forName("vn.aptech.java.models.User");
            }
            if (methodName.toLowerCase().contains("invoice")) {
                return Class.forName("vn.aptech.java.models.Invoice");
            }
            if (methodName.toLowerCase().contains("requestdetail")) {
                return Class.forName("vn.aptech.java.models.RequestDetail");
            }
            if (methodName.toLowerCase().contains("request")) {
                return Class.forName("vn.aptech.java.models.Request");
            }
            if (methodName.toLowerCase().contains("requestimg")) {
                return Class.forName("vn.aptech.java.models.RequestImg");
            }
            if (methodName.toLowerCase().contains("part")) {
                return Class.forName("vn.aptech.java.models.Part");
            }
            if (methodName.toLowerCase().contains("parttype")) {
                return Class.forName("vn.aptech.java.models.PartType");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Entity class not found: " + e.getMessage());
        }
        return null;
    }
}