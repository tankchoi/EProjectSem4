# 🎯 HƯỚNG DẪN NHANH: TESTING MODULE REQUEST

## 🚀 Bước 1: Import dữ liệu mẫu

### Cách 1: Sử dụng PowerShell (Khuyến nghị)
```powershell
.\import_sample_data.ps1
```

### Cách 2: Sử dụng Command Prompt  
```cmd
import_sample_data.bat
```

### Cách 3: Manual MySQL
```sql
mysql -u root -p --database=sem4 < sample_data_request_module.sql
```

## 🔐 Thông tin đăng nhập

### Tài khoản có sẵn (từ hệ thống):
- **Admin**: `admin` / `123456`
- **Staff**: `staff` / `123456` 
- **Customer**: `customer` / `123456`

### Tài khoản mẫu (từ file SQL):
- **Customers**: `customer1`, `customer2`, `customer3`, `customer4`, `customer5`
- **Technicians**: `tech1`, `tech2`, `tech3`, `tech4`
- **Mật khẩu tất cả**: `password123`

## 🧪 Test Cases Chính

### 1. **Đăng nhập và kiểm tra Dashboard**
- URL: `http://localhost:8080`
- Đăng nhập với tài khoản admin
- Kiểm tra menu điều hướng

### 2. **Quản lý Requests**
- Vào `/admin/request`
- Xem danh sách 12 requests với đầy đủ trạng thái
- Test search, filter, pagination

### 3. **Tạo Request mới**
- Vào `/admin/request/create`
- Chọn customer laptop từ dropdown
- Điền thông tin và mô tả lỗi
- Test validation form

### 4. **Xem chi tiết Request**
- Click vào request bất kỳ
- Kiểm tra thông tin đầy đủ
- Xem parts được yêu cầu
- Xem hình ảnh minh chứng

### 5. **Cập nhật trạng thái Request**
- Chuyển từ `PENDING` → `APPROVED`
- Gán technician
- Chuyển `APPROVED` → `IN_PROGRESS`
- Hoàn thành: `IN_PROGRESS` → `COMPLETED`

### 6. **Quản lý Parts**
- Vào `/admin/part`
- Xem 21 parts đã được tạo
- Test thêm part vào request
- Kiểm tra tồn kho

### 7. **Quản lý Customers**
- Vào `/admin/customer`
- Xem 5 customers và laptops của họ
- Test tạo customer mới
- Xem lịch sử requests

### 8. **Tạo Invoice**
- Với requests đã `COMPLETED`
- Tính tổng tiền từ parts
- Cập nhật trạng thái thanh toán

## 📊 Dữ liệu Test có sẵn

| Bảng | Số lượng | Mô tả |
|-------|----------|--------|
| Models | 6 | HP ProBook, EliteBook, Pavilion, Envy, Omen, Spectre |
| Laptops | 10 | Các cấu hình i5, i7 khác nhau |
| Users | 9 | 5 customers + 4 technicians |
| CustomerLaptops | 9 | Laptops thuộc sở hữu customers |
| Requests | 12 | Đầy đủ trạng thái PENDING → COMPLETED |
| PartTypes | 15 | RAM, SSD, HDD, CPU, Bàn phím, Màn hình... |
| Parts | 21 | Linh kiện với giá và tồn kho thực tế |
| RequestDetails | 8 | Parts yêu cầu trong requests |
| RequestImgs | 14 | Hình ảnh minh chứng lỗi |
| Invoices | 4 | Hóa đơn cho requests hoàn thành |

## 🔍 Scenarios Test chi tiết

### Scenario 1: Customer yêu cầu sửa laptop
1. Customer có laptop bị lỗi
2. Tạo request với mô tả và hình ảnh
3. Admin duyệt và gán technician
4. Technician xác định parts cần thay
5. Thực hiện sửa chữa
6. Tạo invoice và thanh toán

### Scenario 2: Request bị từ chối
1. Request laptop bị vào nước (request #9)
2. Admin đánh giá không thể sửa
3. Chuyển trạng thái thành `REJECTED`
4. Gửi thông báo cho customer

### Scenario 3: Request phức tạp nhiều parts
1. Laptop lỗi nghiêm trọng
2. Cần thay nhiều linh kiện
3. Technician cập nhật từng part
4. Tính tổng chi phí
5. Customer xác nhận và thanh toán

## 🚨 Các điểm cần test đặc biệt

### UI/UX Testing:
- ✅ Responsive design trên mobile/tablet
- ✅ Form validation và error messages
- ✅ Loading states và feedback
- ✅ Material Icons thay vì Bootstrap/FontAwesome

### Functional Testing:
- ✅ CRUD operations cho tất cả entities
- ✅ File upload cho hình ảnh
- ✅ Search và filter chính xác
- ✅ Pagination hoạt động đúng
- ✅ Status transitions logic

### Security Testing:
- ✅ Authentication và authorization
- ✅ Role-based access control
- ✅ XSS và SQL injection protection
- ✅ CSRF protection

### Performance Testing:
- ✅ Load time với dữ liệu lớn
- ✅ Database query optimization
- ✅ Image loading và caching

## 📝 Notes quan trọng

1. **Database**: Đảm bảo MySQL đang chạy và database `sem4` tồn tại
2. **Images**: Các URL hình ảnh trong dữ liệu mẫu là placeholder, có thể upload hình thật để test
3. **Email**: Tất cả email trong dữ liệu mẫu đều fake, phù hợp cho testing
4. **Dates**: Dữ liệu mẫu sử dụng ngày tương đối (NOW(), DATE_SUB) để luôn relevant

## 🔧 Troubleshooting

### Lỗi thường gặp:
1. **MySQL connection refused**: Kiểm tra MySQL service
2. **Access denied**: Kiểm tra username/password MySQL
3. **Foreign key constraint**: Chạy với `SET FOREIGN_KEY_CHECKS = 0`
4. **Charset issues**: Đảm bảo database dùng UTF-8

### Debug tips:
- Check application logs trong terminal
- Sử dụng browser developer tools
- Kiểm tra Hibernate SQL queries
- Test với Postman cho API endpoints
