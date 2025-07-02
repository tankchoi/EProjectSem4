# =====================================================
# IMPORT DỮ LIỆU MẪU CHO MODULE REQUEST
# =====================================================

Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host "IMPORT DỮ LIỆU MẪU CHO MODULE REQUEST" -ForegroundColor Cyan
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host ""

# Kiểm tra file SQL có tồn tại không
if (-not (Test-Path "sample_data_request_module.sql")) {
    Write-Host "✗ LỖI: Không tìm thấy file sample_data_request_module.sql" -ForegroundColor Red
    Write-Host "Vui lòng đảm bảo file này tồn tại trong thư mục hiện tại" -ForegroundColor Yellow
    Read-Host "Bấm Enter để thoát"
    exit 1
}

Write-Host "Đang kết nối tới MySQL và import dữ liệu..." -ForegroundColor Yellow
Write-Host ""

# Thực hiện import

try {
    # Import dữ liệu
    cmd /c "mysql -u root -p --database=sem4 < sample_data_request_module.sql"
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "✓ THÀNH CÔNG: Dữ liệu mẫu đã được import vào database sem4" -ForegroundColor Green
        Write-Host ""
        Write-Host "Thông tin tài khoản test:" -ForegroundColor Cyan
        Write-Host "- Admin: admin / 123456" -ForegroundColor White
        Write-Host "- Staff: staff / 123456" -ForegroundColor White
        Write-Host "- Customer: customer / 123456" -ForegroundColor White
        Write-Host ""
        Write-Host "Hoặc sử dụng các tài khoản mẫu từ file SQL:" -ForegroundColor Cyan
        Write-Host "- customer1, customer2, customer3, customer4, customer5" -ForegroundColor White
        Write-Host "- tech1, tech2, tech3, tech4" -ForegroundColor White
        Write-Host "- Mật khẩu tất cả: password123" -ForegroundColor White
        Write-Host ""
        Write-Host "Bạn có thể truy cập ứng dụng tại: http://localhost:8080" -ForegroundColor Green
        Write-Host ""
        
        # Hiển thị thông tin về dữ liệu đã import
        Write-Host "Dữ liệu đã được import:" -ForegroundColor Cyan
        Write-Host "- 6 Models laptop HP" -ForegroundColor White
        Write-Host "- 10 Laptops với các cấu hình khác nhau" -ForegroundColor White
        Write-Host "- 9 Users (5 customers + 4 technicians)" -ForegroundColor White
        Write-Host "- 9 CustomerLaptops" -ForegroundColor White
        Write-Host "- 12 Requests với đầy đủ các trạng thái" -ForegroundColor White
        Write-Host "- 15 PartTypes" -ForegroundColor White
        Write-Host "- 21 Parts (linh kiện)" -ForegroundColor White
        Write-Host "- 8 RequestDetails" -ForegroundColor White
        Write-Host "- 14 RequestImgs" -ForegroundColor White
        Write-Host "- 4 Invoices" -ForegroundColor White
        
    }
    else {
        Write-Host ""
        Write-Host "✗ LỖI: Không thể import dữ liệu mẫu" -ForegroundColor Red
        Write-Host "Vui lòng kiểm tra:" -ForegroundColor Yellow
        Write-Host "1. MySQL server đang chạy" -ForegroundColor White
        Write-Host "2. Database 'sem4' đã tồn tại" -ForegroundColor White
        Write-Host "3. User 'root' có quyền truy cập" -ForegroundColor White
        Write-Host "4. File sample_data_request_module.sql tồn tại" -ForegroundColor White
        Write-Host ""
    }
}
catch {
    Write-Host ""
    Write-Host "✗ LỖI: Có lỗi xảy ra khi thực hiện import" -ForegroundColor Red
    Write-Host "Chi tiết lỗi: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
}

Write-Host ""
Read-Host "Bấm Enter để đóng"
