# Sửa lỗi chức năng cập nhật yêu cầu bảo hành

## Vấn đề gặp phải
- Khi cập nhật yêu cầu bảo hành, có thông báo thành công nhưng dữ liệu không được lưu vào cơ sở dữ liệu
- Nguyên nhân: AuditAspect đang can thiệp vào transaction context

## Nguyên nhân 
1. **AuditAspect conflict**: AuditAspect sử dụng `@Around` advice đã can thiệp vào quá trình transaction của update method
2. **EntityManager conflict**: Trong AuditAspect, sau khi save entity, lại cố gắng fetch entity cũ bằng `entityManager.find()` gây conflict với Hibernate session
3. **Transaction rollback**: Do conflict này, transaction có thể bị rollback âm thầm

## Giải pháp đã áp dụng

### 1. Sửa AuditAspect
**Tệp:** `src/main/java/vn/aptech/java/aspects/AuditAspect.java`

**Thay đổi:**
- Đổi từ `@Around` sang `@AfterReturning` cho update methods
- Loại bỏ việc fetch old entity để tránh conflict với transaction
- Chỉ log thông tin entity sau khi update thành công

```java
// Trước (có vấn đề)
@Around("execution(* vn.aptech.java.services..*.update*(..))")
public Object aroundUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
    // Gọi method update
    Object result = joinPoint.proceed();
    
    // Fetch old entity - GÂY CONFLICT
    Object oldEntity = entityManager.find(result.getClass(), id);
    
    return result;
}

// Sau (đã sửa)
@AfterReturning(pointcut = "execution(* vn.aptech.java.services..*.update*(..))", returning = "result")
public void afterUpdate(JoinPoint joinPoint, Object result) throws Exception {
    // Chỉ log sau khi update thành công, không fetch old entity
    // Không can thiệp vào transaction process
}
```

### 2. Làm sạch code debug
**Tệp:** 
- `src/main/java/vn/aptech/java/services/impl/RequestServiceImpl.java`
- `src/main/java/vn/aptech/java/controllers/admin/RequestController.java`

**Thay đổi:**
- Loại bỏ các debug logs không cần thiết
- Đơn giản hóa logic update

## Cách test

### 1. Khởi động ứng dụng
```bash
mvn spring-boot:run
```

### 2. Test chức năng cập nhật
1. Đăng nhập vào admin panel
2. Vào mục "Yêu cầu bảo hành" (Requests)
3. Chọn một yêu cầu và click "Chỉnh sửa"
4. Thay đổi thông tin:
   - Họ tên
   - Email  
   - Số điện thoại
   - Địa chỉ
   - Mô tả
   - Trạng thái
   - Kỹ thuật viên
5. Click "Cập nhật yêu cầu"
6. Kiểm tra:
   - ✅ Có thông báo thành công
   - ✅ Quay về danh sách requests
   - ✅ Dữ liệu đã được cập nhật trong danh sách
   - ✅ Khi vào chỉnh sửa lại, thấy dữ liệu mới

### 3. Kiểm tra database (nếu cần)
```sql
SELECT * FROM Requests WHERE id = [request_id];
```

## Kết quả mong đợi
- ✅ Cập nhật thành công với thông báo "Yêu cầu bảo hành '[tên]' đã được cập nhật thành công!"
- ✅ Dữ liệu được lưu vào database
- ✅ Không còn conflict transaction
- ✅ AuditLog vẫn hoạt động bình thường

## Các file đã thay đổi
1. `src/main/java/vn/aptech/java/aspects/AuditAspect.java` - Sửa logic audit cho update
2. `src/main/java/vn/aptech/java/services/impl/RequestServiceImpl.java` - Làm sạch debug logs
3. `src/main/java/vn/aptech/java/controllers/admin/RequestController.java` - Làm sạch debug logs

## Lưu ý
- Giải pháp này vẫn giữ được chức năng audit logging
- Không ảnh hưởng đến các chức năng khác
- Performance được cải thiện do giảm thiểu database queries không cần thiết
