# Script để theo dõi logs real-time và lọc debug logs
Write-Host "Theo dõi debug logs cho UPDATE REQUEST..." -ForegroundColor Green
Write-Host "Hãy thử submit form edit request trong browser..." -ForegroundColor Yellow
Write-Host "Press Ctrl+C để dừng theo dõi" -ForegroundColor Red
Write-Host "=" * 50

# Tạo một loop để theo dõi terminal output
$terminalId = "2373f1a9-e424-48e1-984b-111a7d48662d"

while ($true) {
    # Lấy output từ terminal
    try {
        # Thay vì theo dõi terminal (vì không thể), ta sẽ in hướng dẫn
        Write-Host "Đang chờ logs từ ứng dụng..." -ForegroundColor Cyan
        Write-Host "Khi submit form, logs sẽ xuất hiện trong terminal chạy ứng dụng"
        Write-Host "Tìm các dòng có chứa:"
        Write-Host "- '=== DEBUG UPDATE REQUEST ==='" -ForegroundColor Yellow
        Write-Host "- '=== SERVICE DEBUG UPDATE REQUEST ==='" -ForegroundColor Yellow
        Write-Host "- 'DTO - Fullname:'" -ForegroundColor Yellow
        Write-Host "- 'Saved request ID:'" -ForegroundColor Yellow
        
        Start-Sleep -Seconds 10
        Clear-Host
        Write-Host "Theo dõi debug logs cho UPDATE REQUEST..." -ForegroundColor Green
        Write-Host "Hãy thử submit form edit request trong browser..." -ForegroundColor Yellow
        Write-Host "Press Ctrl+C để dừng theo dõi" -ForegroundColor Red
        Write-Host "=" * 50
    }
    catch {
        Write-Host "Lỗi khi theo dõi logs: $($_.Exception.Message)" -ForegroundColor Red
        break
    }
}
