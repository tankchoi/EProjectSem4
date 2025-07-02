@echo off
echo =====================================================
echo IMPORT DU LIEU MAU CHO MODULE REQUEST
echo =====================================================
echo.

echo Dang ket noi toi MySQL va import du lieu...
echo.

REM Import du lieu mau vao database sem4
mysql -u root -p --database=sem4 < sample_data_request_module.sql

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✓ THANH CONG: Du lieu mau da duoc import vao database sem4
    echo.
    echo Thong tin tai khoan test:
    echo - Admin: admin / 123456
    echo - Staff: staff / 123456  
    echo - Customer: customer / 123456
    echo.
    echo Hoac su dung cac tai khoan mau tu file SQL:
    echo - customer1, customer2, customer3, customer4, customer5
    echo - tech1, tech2, tech3, tech4
    echo - Mat khau tat ca: password123
    echo.
    echo Ban co the truy cap ung dung tai: http://localhost:8080
    echo.
) else (
    echo.
    echo ✗ LOI: Khong the import du lieu mau
    echo Vui long kiem tra:
    echo 1. MySQL server dang chay
    echo 2. Database 'sem4' da ton tai
    echo 3. User 'root' co quyen truy cap
    echo 4. File sample_data_request_module.sql ton tai
    echo.
)

echo.
echo Bam phim bat ky de dong...
pause > nul
