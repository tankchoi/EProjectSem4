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
        if (args.length == 0) {
            return joinPoint.proceed();
        }

        // Gọi hàm update trước để lấy kết quả
        Object result = joinPoint.proceed();

        // Chỉ audit nếu có kết quả trả về (là entity)
        if (result == null) {
            return result;
        }

        try {
            // Lấy ID từ kết quả trả về (entity đã được update)
            Long id = getEntityId(result);

            // Tìm entity cũ để so sánh (nếu cần)
            Object oldEntity = null;
            try {
                oldEntity = entityManager.find(result.getClass(), id);
                // Refresh để lấy trạng thái trước khi update
                if (oldEntity != null) {
                    entityManager.detach(oldEntity);
                }
            } catch (Exception e) {
                // Ignore errors when fetching old entity
            }

            String oldJson = oldEntity == null ? null : objectMapper.writeValueAsString(oldEntity);
            String newJson = objectMapper.writeValueAsString(result);

            auditLogService.log(
                    currentUser,
                    result.getClass().getSimpleName(),
                    id,
                    "UPDATE",
                    oldJson,
                    newJson);
        } catch (Exception e) {
            // Log the audit error but don't fail the update operation
            System.err.println("Audit logging failed for update operation: " + e.getMessage());
        }

        return result;
    }

    @Before("execution(* vn.aptech.java.services..*.delete*(..))")
    public void beforeDelete(JoinPoint joinPoint) throws Exception {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return; // Bỏ qua audit khi chưa có user đăng nhập
        }

        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || args[0] == null) {
            return;
        }

        try {
            Object firstArg = args[0];

            System.out.println("=== DELETE AUDIT DEBUG ===");
            System.out.println("Method: " + joinPoint.getSignature().getName());
            System.out.println("Class: " + joinPoint.getTarget().getClass().getSimpleName());
            System.out.println("FirstArg type: " + firstArg.getClass().getSimpleName());
            System.out.println("FirstArg value: " + firstArg);

            // Kiểm tra nếu argument đầu tiên là ID (Long, Integer, etc.)
            if (firstArg instanceof Number) {
                Long id = ((Number) firstArg).longValue();

                // Cần tìm cách xác định loại entity từ tên method hoặc class
                String methodName = joinPoint.getSignature().getName();
                String className = joinPoint.getTarget().getClass().getSimpleName();

                // Suy luận loại entity từ tên service class hoặc method
                String entityName = extractEntityNameFromServiceOrMethod(className, methodName);
                System.out.println("Extracted entity name: " + entityName);

                // Tìm entity class tương ứng
                Class<?> entityClass = findEntityClass(entityName);
                System.out
                        .println("Found entity class: " + (entityClass != null ? entityClass.getSimpleName() : "null"));

                if (entityClass == null) {
                    System.out.println("Skipping audit - could not determine entity class");
                    return; // Không thể xác định entity class
                }

                // Lấy entity trước khi xóa để audit
                Object entityToDelete = entityManager.find(entityClass, id);
                if (entityToDelete != null) {
                    String oldJson = objectMapper.writeValueAsString(entityToDelete);

                    auditLogService.log(
                            currentUser,
                            entityClass.getSimpleName(),
                            id,
                            "DELETE",
                            oldJson,
                            null);
                    System.out.println("Audit logged successfully for ID: " + id);
                } else {
                    System.out.println("Entity not found for ID: " + id);
                }
            } else {
                // Trường hợp argument đầu tiên là entity object
                System.out.println("Handling entity object (not ID)");
                Long id = getEntityId(firstArg);
                String oldJson = objectMapper.writeValueAsString(firstArg);

                auditLogService.log(
                        currentUser,
                        firstArg.getClass().getSimpleName(),
                        id,
                        "DELETE",
                        oldJson,
                        null);
                System.out.println("Audit logged successfully for entity ID: " + id);
            }
        } catch (Exception e) {
            // Log the audit error but don't fail the delete operation
            System.err.println("Audit logging failed for delete operation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Long getEntityId(Object entity) throws Exception {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        try {
            Method getIdMethod = entity.getClass().getMethod("getId");
            Object id = getIdMethod.invoke(entity);
            if (id instanceof Long) {
                return (Long) id;
            } else if (id instanceof Number) {
                return ((Number) id).longValue();
            } else {
                throw new IllegalArgumentException(
                        "Entity ID must be a number, got: " + (id != null ? id.getClass() : "null"));
            }
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    "Entity " + entity.getClass().getSimpleName() + " does not have a getId() method", e);
        }
    }

    private String extractEntityNameFromServiceOrMethod(String className, String methodName) {
        // Ví dụ: ModelServiceImpl -> Model, deleteModel -> Model
        if (className.endsWith("ServiceImpl")) {
            String serviceName = className.replace("ServiceImpl", "");
            return serviceName;
        } else if (className.endsWith("Service")) {
            String serviceName = className.replace("Service", "");
            return serviceName;
        } else if (methodName.startsWith("delete")) {
            // deleteModel -> Model, deleteCustomer -> Customer
            String entityName = methodName.replace("delete", "");
            return entityName;
        }
        return null;
    }

    private Class<?> findEntityClass(String entityName) {
        if (entityName == null || entityName.isEmpty()) {
            return null;
        }

        try {
            // Thử tìm class trong package models
            String fullClassName = "vn.aptech.java.models." + entityName;
            return Class.forName(fullClassName);
        } catch (ClassNotFoundException e) {
            // Nếu không tìm thấy, thử với các tên thông dụng
            try {
                if ("Model".equalsIgnoreCase(entityName)) {
                    return vn.aptech.java.models.Model.class;
                } else if ("Customer".equalsIgnoreCase(entityName)) {
                    return Class.forName("vn.aptech.java.models.Customer");
                } else if ("Laptop".equalsIgnoreCase(entityName)) {
                    return Class.forName("vn.aptech.java.models.Laptop");
                } else if ("Part".equalsIgnoreCase(entityName)) {
                    return Class.forName("vn.aptech.java.models.Part");
                } else if ("PartType".equalsIgnoreCase(entityName)) {
                    return Class.forName("vn.aptech.java.models.PartType");
                } else if ("Staff".equalsIgnoreCase(entityName)) {
                    return Class.forName("vn.aptech.java.models.Staff");
                } else if ("Receipt".equalsIgnoreCase(entityName)) {
                    return Class.forName("vn.aptech.java.models.Receipt");
                } else if ("Request".equalsIgnoreCase(entityName)) {
                    return Class.forName("vn.aptech.java.models.Request");
                }
            } catch (ClassNotFoundException ex) {
                System.err.println("Could not find entity class for: " + entityName);
            }
        }
        return null;
    }
}
