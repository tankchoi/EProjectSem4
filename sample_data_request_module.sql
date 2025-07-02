-- =====================================================
-- SCRIPT TẠO DỮ LIỆU MẪU CHO MODULE REQUEST
-- =====================================================

-- 1. Tạo dữ liệu mẫu cho Models
INSERT INTO Models (name, createdAt, updatedAt) VALUES
('HP ProBook 450 G9', NOW(), NOW()),
('HP EliteBook 840 G8', NOW(), NOW()),
('HP Pavilion 15', NOW(), NOW()),
('HP Envy 13', NOW(), NOW()),
('HP Omen 15', NOW(), NOW()),
('HP Spectre x360', NOW(), NOW());

-- 2. Tạo dữ liệu mẫu cho Laptops
INSERT INTO Laptops (name, modelId, warrantyPeriod, imgUrl, createdAt, updatedAt) VALUES
('HP ProBook 450 G9 Core i5', 1, 24, '/images/hp-probook-450.jpg', NOW(), NOW()),
('HP ProBook 450 G9 Core i7', 1, 24, '/images/hp-probook-450-i7.jpg', NOW(), NOW()),
('HP EliteBook 840 G8 Core i5', 2, 36, '/images/hp-elitebook-840.jpg', NOW(), NOW()),
('HP EliteBook 840 G8 Core i7', 2, 36, '/images/hp-elitebook-840-i7.jpg', NOW(), NOW()),
('HP Pavilion 15 Core i5', 3, 12, '/images/hp-pavilion-15.jpg', NOW(), NOW()),
('HP Pavilion 15 Core i7', 3, 12, '/images/hp-pavilion-15-i7.jpg', NOW(), NOW()),
('HP Envy 13 Core i5', 4, 24, '/images/hp-envy-13.jpg', NOW(), NOW()),
('HP Envy 13 Core i7', 4, 24, '/images/hp-envy-13-i7.jpg', NOW(), NOW()),
('HP Omen 15 Gaming', 5, 24, '/images/hp-omen-15.jpg', NOW(), NOW()),
('HP Spectre x360 13', 6, 36, '/images/hp-spectre-x360.jpg', NOW(), NOW());

-- 3. Tạo dữ liệu mẫu cho Users (Customers)
INSERT INTO Users (username, password, fullname, email, phone, img_link, role, status, createdAt, updatedAt) VALUES
-- Customers
('customer1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9C2D1dJM8LQ3aH.', 'Nguyễn Văn An', 'nguyenvanan@email.com', '0123456789', '/images/avatar1.jpg', 'CUSTOMER', 'ACTIVE', NOW(), NOW()),
('customer2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9C2D1dJM8LQ3aH.', 'Trần Thị Bình', 'tranthibinh@email.com', '0123456790', '/images/avatar2.jpg', 'CUSTOMER', 'ACTIVE', NOW(), NOW()),
('customer3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9C2D1dJM8LQ3aH.', 'Lê Hoàng Cường', 'lehoangcuong@email.com', '0123456791', '/images/avatar3.jpg', 'CUSTOMER', 'ACTIVE', NOW(), NOW()),
('customer4', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9C2D1dJM8LQ3aH.', 'Phạm Thị Dung', 'phamthidung@email.com', '0123456792', '/images/avatar4.jpg', 'CUSTOMER', 'ACTIVE', NOW(), NOW()),
('customer5', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9C2D1dJM8LQ3aH.', 'Hoàng Văn Đức', 'hoangvanduc@email.com', '0123456793', '/images/avatar5.jpg', 'CUSTOMER', 'ACTIVE', NOW(), NOW()),

-- Technicians/Staff
('tech1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9C2D1dJM8LQ3aH.', 'Kỹ thuật viên Minh', 'ktvminh@company.com', '0987654321', '/images/tech1.jpg', 'STAFF', 'ACTIVE', NOW(), NOW()),
('tech2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9C2D1dJM8LQ3aH.', 'Kỹ thuật viên Hùng', 'ktvhung@company.com', '0987654322', '/images/tech2.jpg', 'STAFF', 'ACTIVE', NOW(), NOW()),
('tech3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9C2D1dJM8LQ3aH.', 'Kỹ thuật viên Lan', 'ktvlan@company.com', '0987654323', '/images/tech3.jpg', 'STAFF', 'ACTIVE', NOW(), NOW()),
('tech4', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9C2D1dJM8LQ3aH.', 'Kỹ thuật viên Tâm', 'ktvtam@company.com', '0987654324', '/images/tech4.jpg', 'STAFF', 'ACTIVE', NOW(), NOW());

-- 4. Tạo dữ liệu mẫu cho CustomerLaptops
INSERT INTO CustomerLaptops (customerId, laptopId, serialNumber, createdAt, updatedAt) VALUES
-- Customer 1 có 2 laptop
(1, 1, 'HP450G9-001234', NOW(), NOW()),
(1, 3, 'HP840G8-005678', NOW(), NOW()),

-- Customer 2 có 1 laptop
(2, 2, 'HP450G9-002345', NOW(), NOW()),

-- Customer 3 có 2 laptop
(3, 4, 'HP840G8-006789', NOW(), NOW()),
(3, 5, 'HP15PAV-003456', NOW(), NOW()),

-- Customer 4 có 1 laptop
(4, 6, 'HP15PAV-007890', NOW(), NOW()),

-- Customer 5 có 3 laptop
(5, 7, 'HPENVY13-004567', NOW(), NOW()),
(5, 8, 'HPENVY13-008901', NOW(), NOW()),
(5, 9, 'HPOMEN15-005678', NOW(), NOW());

-- 5. Tạo dữ liệu mẫu cho Requests
INSERT INTO Requests (customerLaptopId, fullname, email, phone, address, description, bookingDate, status, technicianId, createdAt, updatedAt) VALUES

-- Requests đã hoàn thành
(1, 'Nguyễn Văn An', 'nguyenvanan@email.com', '0123456789', '123 Nguyễn Trãi, Q.5, TP.HCM', 
 'Laptop bị lỗi màn hình xanh, tự động restart liên tục. Nghi ngờ lỗi RAM hoặc ổ cứng.', 
 '2024-06-15', 'COMPLETED', 6, DATE_SUB(NOW(), INTERVAL 15 DAY), NOW()),

(2, 'Nguyễn Văn An', 'nguyenvanan@email.com', '0123456789', '123 Nguyễn Trãi, Q.5, TP.HCM', 
 'Bàn phím không hoạt động, một số phím bị liệt. Cần thay thế bàn phím mới.', 
 '2024-06-20', 'COMPLETED', 7, DATE_SUB(NOW(), INTERVAL 10 DAY), NOW()),

-- Requests đang xử lý
(3, 'Trần Thị Bình', 'tranthibinh@email.com', '0123456790', '456 Lê Văn Sỹ, Q.3, TP.HCM', 
 'Máy chạy chậm, quạt hoạt động ồn ào. Nghi ngờ cần vệ sinh tản nhiệt và thay keo tản nhiệt.', 
 '2024-07-01', 'IN_PROGRESS', 6, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()),

(4, 'Lê Hoàng Cường', 'lehoangcuong@email.com', '0123456791', '789 Cách Mạng Tháng 8, Q.10, TP.HCM', 
 'Pin laptop không sạc được, cắm sạc không nhận. Cần kiểm tra jack sạc và pin.', 
 '2024-07-02', 'APPROVED', 7, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()),

-- Requests chờ duyệt
(5, 'Lê Hoàng Cường', 'lehoangcuong@email.com', '0123456791', '789 Cách Mạng Tháng 8, Q.10, TP.HCM', 
 'Laptop không bật được, đèn nguồn không sáng. Nghi ngờ lỗi mainboard hoặc nguồn.', 
 '2024-07-05', 'PENDING', NULL, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),

(6, 'Phạm Thị Dung', 'phamthidung@email.com', '0123456792', '321 Võ Văn Tần, Q.3, TP.HCM', 
 'Màn hình laptop bị vỡ góc, hiển thị bị nhiễu. Cần thay thế màn hình mới.', 
 '2024-07-03', 'PENDING', NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()),

-- Requests hôm nay
(7, 'Hoàng Văn Đức', 'hoangvanduc@email.com', '0123456793', '654 Điện Biên Phủ, Q.Bình Thạnh, TP.HCM', 
 'Laptop phát ra tiếng bíp liên tục khi khởi động, không thể vào Windows. Nghi ngờ lỗi RAM.', 
 '2024-07-02', 'PENDING', NULL, NOW(), NOW()),

(8, 'Hoàng Văn Đức', 'hoangvanduc@email.com', '0123456793', '654 Điện Biên Phủ, Q.Bình Thạnh, TP.HCM', 
 'Ổ cứng SSD bị lỗi, không thể truy cập dữ liệu. Cần backup và thay ổ cứng mới.', 
 '2024-07-04', 'APPROVED', 8, NOW(), NOW()),

-- Requests bị từ chối
(9, 'Hoàng Văn Đức', 'hoangvanduc@email.com', '0123456793', '654 Điện Biên Phủ, Q.Bình Thạnh, TP.HCM', 
 'Laptop bị vào nước, cần kiểm tra và sửa chữa toàn bộ bo mạch.', 
 '2024-06-25', 'REJECTED', NULL, DATE_SUB(NOW(), INTERVAL 8 DAY), NOW()),

-- Thêm một số requests khác để test phân trang
(1, 'Nguyễn Văn An', 'nguyenvanan@email.com', '0123456789', '123 Nguyễn Trãi, Q.5, TP.HCM', 
 'Cổng USB không nhận thiết bị ngoài, cần kiểm tra và sửa chữa.', 
 '2024-06-10', 'COMPLETED', 9, DATE_SUB(NOW(), INTERVAL 20 DAY), NOW()),

(2, 'Nguyễn Văn An', 'nguyenvanan@email.com', '0123456789', '123 Nguyễn Trãi, Q.5, TP.HCM', 
 'Webcam không hoạt động, không thể sử dụng cho họp online.', 
 '2024-06-05', 'COMPLETED', 6, DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),

(3, 'Trần Thị Bình', 'tranthibinh@email.com', '0123456790', '456 Lê Văn Sỹ, Q.3, TP.HCM', 
 'Wi-Fi không ổn định, thường xuyên mất kết nối internet.', 
 '2024-07-06', 'PENDING', NULL, NOW(), NOW());

-- =====================================================
-- VERIFICATION QUERIES (Chạy để kiểm tra dữ liệu)
-- =====================================================

-- Kiểm tra số lượng records
SELECT 'Models' as TableName, COUNT(*) as RecordCount FROM Models
UNION ALL
SELECT 'Laptops', COUNT(*) FROM Laptops
UNION ALL
SELECT 'Users', COUNT(*) FROM Users WHERE role IN ('CUSTOMER', 'STAFF')
UNION ALL
SELECT 'CustomerLaptops', COUNT(*) FROM CustomerLaptops
UNION ALL
SELECT 'Requests', COUNT(*) FROM Requests;

-- Kiểm tra thông tin requests với join
SELECT 
    r.id,
    r.fullname,
    r.email,
    r.status,
    r.bookingDate,
    CONCAT(u.fullname, ' (', l.name, ' - ', cl.serialNumber, ')') as laptop_info,
    COALESCE(t.fullname, 'Chưa gán') as technician
FROM Requests r
LEFT JOIN CustomerLaptops cl ON r.customerLaptopId = cl.id
LEFT JOIN Users u ON cl.customerId = u.id
LEFT JOIN Laptops l ON cl.laptopId = l.id
LEFT JOIN Users t ON r.technicianId = t.id
ORDER BY r.createdAt DESC;

-- Kiểm tra phân bố trạng thái requests
SELECT 
    status,
    COUNT(*) as count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM Requests), 2) as percentage
FROM Requests 
GROUP BY status
ORDER BY count DESC;

-- =====================================================
-- EXTENDED DATA FOR COMPLETE REQUEST MODULE TESTING
-- =====================================================

-- 6. Tạo dữ liệu mẫu cho PartTypes
INSERT INTO PartTypes (name, createdAt, updatedAt) VALUES
('RAM', NOW(), NOW()),
('SSD', NOW(), NOW()),
('HDD', NOW(), NOW()),
('CPU', NOW(), NOW()),
('Mainboard', NOW(), NOW()),
('VGA', NOW(), NOW()),
('Quạt tản nhiệt', NOW(), NOW()),
('Bàn phím', NOW(), NOW()),
('Màn hình', NOW(), NOW()),
('Pin', NOW(), NOW()),
('Adapter sạc', NOW(), NOW()),
('Webcam', NOW(), NOW()),
('Loa', NOW(), NOW()),
('Card mạng WiFi', NOW(), NOW()),
('Ổ CD/DVD', NOW(), NOW());

-- 7. Tạo dữ liệu mẫu cho Parts
INSERT INTO Parts (partTypeId, laptopId, name, description, price, quantity, warrantyPeriod, imgUrl, createdAt, updatedAt) VALUES
-- RAM
(1, 1, 'RAM DDR4 8GB 2666MHz', 'Bộ nhớ DDR4 8GB cho HP ProBook 450 G9', 850000, 25, 24, '/images/parts/ram-ddr4-8gb.jpg', NOW(), NOW()),
(1, 2, 'RAM DDR4 16GB 3200MHz', 'Bộ nhớ DDR4 16GB cho HP EliteBook 840 G8', 1500000, 15, 24, '/images/parts/ram-ddr4-16gb.jpg', NOW(), NOW()),
(1, NULL, 'RAM DDR4 4GB 2400MHz', 'Bộ nhớ DDR4 4GB tương thích đa dòng laptop HP', 450000, 30, 24, '/images/parts/ram-ddr4-4gb.jpg', NOW(), NOW()),

-- SSD
(2, 1, 'SSD Samsung 980 500GB', 'Ổ cứng SSD NVMe 500GB cho HP ProBook 450 G9', 1200000, 20, 36, '/images/parts/ssd-samsung-980-500gb.jpg', NOW(), NOW()),
(2, 2, 'SSD Kingston NV2 1TB', 'Ổ cứng SSD NVMe 1TB cho HP EliteBook 840 G8', 2100000, 12, 36, '/images/parts/ssd-kingston-nv2-1tb.jpg', NOW(), NOW()),
(2, NULL, 'SSD WD Blue 250GB', 'Ổ cứng SSD SATA 250GB tương thích đa dòng', 650000, 18, 24, '/images/parts/ssd-wd-blue-250gb.jpg', NOW(), NOW()),

-- HDD
(3, 3, 'HDD Seagate 1TB 5400RPM', 'Ổ cứng HDD 1TB cho HP Pavilion 15', 950000, 15, 24, '/images/parts/hdd-seagate-1tb.jpg', NOW(), NOW()),
(3, NULL, 'HDD WD Blue 500GB', 'Ổ cứng HDD 500GB tương thích đa dòng', 750000, 20, 24, '/images/parts/hdd-wd-blue-500gb.jpg', NOW(), NOW()),

-- Bàn phím
(8, 1, 'Bàn phím HP ProBook 450 G9', 'Bàn phím thay thế cho HP ProBook 450 G9', 350000, 10, 12, '/images/parts/keyboard-hp-probook-450.jpg', NOW(), NOW()),
(8, 2, 'Bàn phím HP EliteBook 840 G8', 'Bàn phím thay thế cho HP EliteBook 840 G8', 450000, 8, 12, '/images/parts/keyboard-hp-elitebook-840.jpg', NOW(), NOW()),
(8, 3, 'Bàn phím HP Pavilion 15', 'Bàn phím thay thế cho HP Pavilion 15', 280000, 12, 12, '/images/parts/keyboard-hp-pavilion-15.jpg', NOW(), NOW()),

-- Màn hình
(9, 1, 'Màn hình LCD 15.6" FHD HP ProBook', 'Màn hình LCD 15.6 inch Full HD cho HP ProBook', 2200000, 5, 12, '/images/parts/lcd-156-fhd-hp.jpg', NOW(), NOW()),
(9, 2, 'Màn hình LCD 14" FHD HP EliteBook', 'Màn hình LCD 14 inch Full HD cho HP EliteBook', 2800000, 3, 12, '/images/parts/lcd-14-fhd-hp.jpg', NOW(), NOW()),

-- Pin
(10, 1, 'Pin HP ProBook 450 G9', 'Pin Lithium-ion cho HP ProBook 450 G9', 1250000, 8, 12, '/images/parts/battery-hp-probook-450.jpg', NOW(), NOW()),
(10, 2, 'Pin HP EliteBook 840 G8', 'Pin Lithium-ion cho HP EliteBook 840 G8', 1450000, 6, 12, '/images/parts/battery-hp-elitebook-840.jpg', NOW(), NOW()),
(10, 5, 'Pin HP Omen 15', 'Pin Lithium-ion cho HP Omen 15 Gaming', 1350000, 4, 12, '/images/parts/battery-hp-omen-15.jpg', NOW(), NOW()),

-- Adapter sạc
(11, NULL, 'Adapter HP 65W', 'Adapter sạc 65W tương thích đa dòng HP', 550000, 15, 12, '/images/parts/adapter-hp-65w.jpg', NOW(), NOW()),
(11, NULL, 'Adapter HP 90W', 'Adapter sạc 90W cho các dòng cao cấp', 750000, 10, 12, '/images/parts/adapter-hp-90w.jpg', NOW(), NOW()),

-- Quạt tản nhiệt
(7, 1, 'Quạt tản nhiệt HP ProBook 450 G9', 'Quạt tản nhiệt và tỏa nhiệt cho HP ProBook 450 G9', 450000, 6, 12, '/images/parts/fan-hp-probook-450.jpg', NOW(), NOW()),
(7, 5, 'Quạt tản nhiệt HP Omen 15', 'Quạt tản nhiệt gaming cho HP Omen 15', 650000, 4, 12, '/images/parts/fan-hp-omen-15.jpg', NOW(), NOW()),

-- Webcam
(12, NULL, 'Webcam HP HD', 'Camera HD tích hợp cho laptop HP', 320000, 12, 12, '/images/parts/webcam-hp-hd.jpg', NOW(), NOW()),

-- Card WiFi
(14, NULL, 'Card WiFi Intel AX200', 'Card mạng WiFi 6 Intel AX200', 450000, 8, 24, '/images/parts/wifi-intel-ax200.jpg', NOW(), NOW());

-- 8. Tạo dữ liệu mẫu cho RequestDetails (linh kiện được yêu cầu thay trong các request)
INSERT INTO RequestDetails (requestId, partId, quantity, createdAt, updatedAt) VALUES
-- Request 1 (COMPLETED) - Lỗi màn hình xanh
(1, 1, 1, DATE_SUB(NOW(), INTERVAL 15 DAY), NOW()), -- RAM DDR4 8GB
(1, 4, 1, DATE_SUB(NOW(), INTERVAL 15 DAY), NOW()), -- SSD Samsung 500GB

-- Request 2 (COMPLETED) - Bàn phím lỗi  
(2, 9, 1, DATE_SUB(NOW(), INTERVAL 10 DAY), NOW()), -- Bàn phím HP ProBook 450 G9

-- Request 3 (IN_PROGRESS) - Máy chạy chậm, quạt ồn
(3, 17, 1, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()), -- Quạt tản nhiệt HP ProBook 450 G9

-- Request 4 (APPROVED) - Pin không sạc
(4, 13, 1, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()), -- Pin HP EliteBook 840 G8
(4, 18, 1, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()), -- Adapter HP 90W

-- Request 8 (APPROVED) - SSD lỗi
(8, 5, 1, NOW(), NOW()), -- SSD Kingston NV2 1TB

-- Request 10 (COMPLETED) - USB không nhận
(10, 2, 1, DATE_SUB(NOW(), INTERVAL 20 DAY), NOW()), -- RAM DDR4 16GB (để test đa linh kiện)

-- Request 11 (COMPLETED) - Webcam không hoạt động
(11, 19, 1, DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()); -- Webcam HP HD

-- 9. Tạo dữ liệu mẫu cho RequestImgs (hình ảnh minh chứng)
INSERT INTO RequestImgs (requestId, imgUrl, createdAt) VALUES
-- Request 1 - Lỗi màn hình xanh
(1, '/images/requests/request1_bluescreen.jpg', DATE_SUB(NOW(), INTERVAL 15 DAY)),
(1, '/images/requests/request1_error_msg.jpg', DATE_SUB(NOW(), INTERVAL 15 DAY)),

-- Request 2 - Bàn phím lỗi
(2, '/images/requests/request2_keyboard_broken.jpg', DATE_SUB(NOW(), INTERVAL 10 DAY)),

-- Request 3 - Máy chạy chậm, quạt ồn
(3, '/images/requests/request3_overheating.jpg', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(3, '/images/requests/request3_dust_fan.jpg', DATE_SUB(NOW(), INTERVAL 5 DAY)),

-- Request 4 - Pin không sạc
(4, '/images/requests/request4_battery_not_charging.jpg', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(4, '/images/requests/request4_adapter_issue.jpg', DATE_SUB(NOW(), INTERVAL 3 DAY)),

-- Request 5 - Laptop không bật
(5, '/images/requests/request5_no_power.jpg', DATE_SUB(NOW(), INTERVAL 2 DAY)),

-- Request 6 - Màn hình vỡ
(6, '/images/requests/request6_cracked_screen.jpg', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(6, '/images/requests/request6_display_issue.jpg', DATE_SUB(NOW(), INTERVAL 1 DAY)),

-- Request 7 - Tiếng bíp khi khởi động
(7, '/images/requests/request7_beep_error.jpg', NOW()),

-- Request 8 - SSD lỗi
(8, '/images/requests/request8_ssd_failure.jpg', NOW()),
(8, '/images/requests/request8_disk_error.jpg', NOW()),

-- Request 9 - Laptop vào nước (REJECTED)
(9, '/images/requests/request9_water_damage.jpg', DATE_SUB(NOW(), INTERVAL 8 DAY)),
(9, '/images/requests/request9_corrosion.jpg', DATE_SUB(NOW(), INTERVAL 8 DAY)),

-- Request 12 - WiFi không ổn định
(12, '/images/requests/request12_wifi_disconnection.jpg', NOW());

-- 10. Tạo dữ liệu mẫu cho Invoices (hóa đơn cho các request đã hoàn thành)
INSERT INTO Invoices (requestId, totalPrice, status, createdAt, updatedAt) VALUES
-- Invoice cho Request 1 (COMPLETED)
(1, 2050000, 'PAID', DATE_SUB(NOW(), INTERVAL 10 DAY), NOW()),

-- Invoice cho Request 2 (COMPLETED) 
(2, 350000, 'PAID', DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()),

-- Invoice cho Request 10 (COMPLETED)
(10, 1500000, 'UNPAID', DATE_SUB(NOW(), INTERVAL 15 DAY), NOW()),

-- Invoice cho Request 11 (COMPLETED)
(11, 320000, 'PAID', DATE_SUB(NOW(), INTERVAL 20 DAY), NOW());

-- =====================================================
-- EXTENDED VERIFICATION QUERIES
-- =====================================================

-- Kiểm tra số lượng records cho tất cả bảng
SELECT 'Models' as TableName, COUNT(*) as RecordCount FROM Models
UNION ALL
SELECT 'Laptops', COUNT(*) FROM Laptops
UNION ALL
SELECT 'Users (Customers)', COUNT(*) FROM Users WHERE role = 'CUSTOMER'
UNION ALL
SELECT 'Users (Staff)', COUNT(*) FROM Users WHERE role = 'STAFF'
UNION ALL
SELECT 'CustomerLaptops', COUNT(*) FROM CustomerLaptops
UNION ALL
SELECT 'Requests', COUNT(*) FROM Requests
UNION ALL
SELECT 'PartTypes', COUNT(*) FROM PartTypes
UNION ALL
SELECT 'Parts', COUNT(*) FROM Parts
UNION ALL
SELECT 'RequestDetails', COUNT(*) FROM RequestDetails
UNION ALL
SELECT 'RequestImgs', COUNT(*) FROM RequestImgs
UNION ALL
SELECT 'Invoices', COUNT(*) FROM Invoices;

-- Kiểm tra thông tin request đầy đủ với parts và images
SELECT 
    r.id,
    r.fullname,
    r.status,
    r.bookingDate,
    CONCAT(u.fullname, ' (', l.name, ')') as customer_laptop,
    COALESCE(t.fullname, 'Chưa gán') as technician,
    GROUP_CONCAT(DISTINCT p.name ORDER BY p.name SEPARATOR ', ') as required_parts,
    COUNT(DISTINCT ri.id) as image_count,
    COALESCE(i.totalPrice, 0) as invoice_total,
    COALESCE(i.status, 'No Invoice') as invoice_status
FROM Requests r
LEFT JOIN CustomerLaptops cl ON r.customerLaptopId = cl.id
LEFT JOIN Users u ON cl.customerId = u.id
LEFT JOIN Laptops l ON cl.laptopId = l.id
LEFT JOIN Users t ON r.technicianId = t.id
LEFT JOIN RequestDetails rd ON r.id = rd.requestId
LEFT JOIN Parts p ON rd.partId = p.id
LEFT JOIN RequestImgs ri ON r.id = ri.requestId
LEFT JOIN Invoices i ON r.id = i.requestId
GROUP BY r.id, r.fullname, r.status, r.bookingDate, u.fullname, l.name, t.fullname, i.totalPrice, i.status
ORDER BY r.createdAt DESC;

-- Kiểm tra phân bố parts theo loại
SELECT 
    pt.name as part_type,
    COUNT(p.id) as part_count,
    AVG(p.price) as avg_price,
    SUM(p.quantity) as total_quantity
FROM PartTypes pt
LEFT JOIN Parts p ON pt.id = p.partTypeId
GROUP BY pt.id, pt.name
ORDER BY part_count DESC;

-- Kiểm tra requests có nhiều parts nhất
SELECT 
    r.id,
    r.fullname,
    r.description,
    COUNT(rd.id) as part_count,
    COUNT(ri.id) as image_count
FROM Requests r
LEFT JOIN RequestDetails rd ON r.id = rd.requestId
LEFT JOIN RequestImgs ri ON r.id = ri.requestId
GROUP BY r.id, r.fullname, r.description
HAVING part_count > 0 OR image_count > 0
ORDER BY part_count DESC, image_count DESC;
