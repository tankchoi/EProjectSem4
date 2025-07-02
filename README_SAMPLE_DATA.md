# HƯỚNG DẪN SỬ DỤNG DỮ LIỆU MẪU CHO MODULE REQUEST

## Tổng quan
File `sample_data_request_module.sql` chứa dữ liệu mẫu hoàn chỉnh để test module quản lý yêu cầu bảo hành (Request Module).

## Cấu trúc dữ liệu mẫu

### 1. Models (6 records)
- HP ProBook 450 G9
- HP EliteBook 840 G8  
- HP Pavilion 15
- HP Envy 13
- HP Omen 15
- HP Spectre x360

### 2. Laptops (10 records)
- Các biến thể khác nhau của từng model (Core i5, i7, Gaming...)
- Thời gian bảo hành từ 12-36 tháng

### 3. Users (9 records)
**Customers (5 users):**
- customer1 - Nguyễn Văn An
- customer2 - Trần Thị Bình  
- customer3 - Lê Hoàng Cường
- customer4 - Phạm Thị Dung
- customer5 - Hoàng Văn Đức

**Staff/Technicians (4 users):**
- tech1 - Kỹ thuật viên Minh
- tech2 - Kỹ thuật viên Hùng
- tech3 - Kỹ thuật viên Lan
- tech4 - Kỹ thuật viên Tâm

**Mật khẩu mặc định:** `password123` (đã mã hóa bcrypt)

### 4. CustomerLaptops (9 records)
- Mỗi customer sở hữu 1-3 laptop với serial number khác nhau
- Liên kết customer với laptop cụ thể

### 5. Requests (12 records)
**Phân bố theo trạng thái:**
- COMPLETED: 4 requests
- IN_PROGRESS: 1 request  
- APPROVED: 2 requests
- PENDING: 4 requests
- REJECTED: 1 request

**Các loại lỗi thường gặp:**
- Lỗi màn hình xanh
- Bàn phím hỏng
- Máy chạy chậm, quạt ồn
- Pin không sạc
- Laptop không bật
- Màn hình vỡ
- SSD lỗi
- WiFi không ổn định

### 6. PartTypes (15 records)
- RAM, SSD, HDD, CPU, Mainboard
- VGA, Quạt tản nhiệt, Bàn phím
- Màn hình, Pin, Adapter sạc
- Webcam, Loa, Card mạng WiFi, Ổ CD/DVD

### 7. Parts (21 records)
- Linh kiện tương thích với các dòng laptop
- Giá từ 280,000đ - 2,800,000đ
- Thời gian bảo hành 12-36 tháng
- Số lượng tồn kho đa dạng

### 8. RequestDetails (8 records)  
- Linh kiện được yêu cầu thay thế trong từng request
- Liên kết request với part cụ thể
- Số lượng yêu cầu

### 9. RequestImgs (14 records)
- Hình ảnh minh chứng cho các request
- Mỗi request có 1-3 hình ảnh
- URL hình ảnh mẫu

### 10. Invoices (4 records)
- Hóa đơn cho các request đã hoàn thành
- Trạng thái PAID/UNPAID
- Tổng tiền từ 320,000đ - 2,050,000đ

## Cách import dữ liệu

### 1. Sử dụng MySQL Workbench
```sql
-- Mở MySQL Workbench
-- Kết nối tới database
-- Mở file sample_data_request_module.sql
-- Chạy toàn bộ script
```

### 2. Sử dụng Command Line
```bash
mysql -u [username] -p [database_name] < sample_data_request_module.sql
```

### 3. Sử dụng phpMyAdmin
- Vào phần Import
- Chọn file sample_data_request_module.sql
- Nhấn Go

## Kiểm tra dữ liệu sau import

Chạy các truy vấn verification có sẵn trong file SQL:

```sql
-- Kiểm tra số lượng records
SELECT 'Models' as TableName, COUNT(*) as RecordCount FROM Models
UNION ALL
SELECT 'Laptops', COUNT(*) FROM Laptops
-- ... (các truy vấn khác)

-- Kiểm tra thông tin request đầy đủ
SELECT 
    r.id,
    r.fullname,
    r.status,
    -- ... (truy vấn chi tiết)
```

## Kết quả mong đợi sau import

### Số lượng records:
- Models: 6
- Laptops: 10  
- Users (Customers): 5
- Users (Staff): 4
- CustomerLaptops: 9
- Requests: 12
- PartTypes: 15
- Parts: 21
- RequestDetails: 8
- RequestImgs: 14
- Invoices: 4

### Test cases có thể thực hiện:

1. **Tạo yêu cầu mới**
   - Chọn customer laptop
   - Gán technician
   - Upload hình ảnh

2. **Quản lý trạng thái request**
   - PENDING → APPROVED → IN_PROGRESS → COMPLETED
   - PENDING → REJECTED

3. **Quản lý linh kiện**
   - Thêm part vào request
   - Cập nhật số lượng
   - Kiểm tra tồn kho

4. **Tạo hóa đơn**
   - Tính tổng tiền từ các part
   - Cập nhật trạng thái thanh toán

5. **Tìm kiếm và lọc**
   - Theo trạng thái
   - Theo ngày tạo
   - Theo customer
   - Theo technician

## Lưu ý quan trọng

1. **Backup database** trước khi import
2. **Kiểm tra foreign key constraints** đã được enable
3. **Đảm bảo charset UTF-8** để hiển thị tiếng Việt đúng
4. **Kiểm tra timezone** cho các trường datetime
5. **Test upload/hiển thị hình ảnh** sau khi import

## Troubleshooting

### Lỗi foreign key constraint
```sql
SET FOREIGN_KEY_CHECKS = 0;
-- Import data
SET FOREIGN_KEY_CHECKS = 1;
```

### Lỗi charset
```sql
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
```

### Lỗi duplicate entry
- Xóa dữ liệu cũ trước khi import
- Hoặc sử dụng INSERT IGNORE thay vì INSERT

## Liên hệ hỗ trợ

Nếu gặp vấn đề trong quá trình import hoặc test, vui lòng:
1. Kiểm tra log lỗi MySQL
2. Đảm bảo version MySQL/MariaDB tương thích
3. Kiểm tra quyền user database
4. Xem lại cấu trúc bảng trong file schema SQL
