-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th7 02, 2025 lúc 07:57 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `sem4`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `audit_logs`
--

CREATE TABLE `audit_logs` (
  `id` bigint(20) NOT NULL,
  `action_type` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `new_value` text DEFAULT NULL,
  `old_value` text DEFAULT NULL,
  `record_id` bigint(20) DEFAULT NULL,
  `table_name` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `audit_logs`
--

INSERT INTO `audit_logs` (`id`, `action_type`, `created_at`, `new_value`, `old_value`, `record_id`, `table_name`, `user_id`) VALUES
(1, 'CREATE', '2025-07-02 00:44:18.000000', '{\"id\":1,\"partType\":{\"id\":1,\"name\":\"RAM\",\"createdAt\":1751391684000,\"updatedAt\":1751391684000},\"laptop\":null,\"name\":\"Ram SSD5 8GB\",\"price\":1000000.0,\"quantity\":1,\"warrantyPeriod\":2,\"imgUrl\":\"/images/parts/1751391858959_2030.png\",\"createdAt\":1751391858975,\"updatedAt\":1751391858975}', NULL, 1, 'Part', 2),
(2, 'CREATE', '2025-07-02 00:45:16.000000', '{\"id\":1,\"name\":\"Dell inspiron 5510\",\"model\":{\"id\":2,\"name\":\"Dell Inspiron 15\",\"createdAt\":1751391683000,\"updatedAt\":1751391683000},\"warrantyPeriod\":2,\"imgUrl\":null,\"createdAt\":1751391916829,\"updatedAt\":1751391916829}', NULL, 1, 'Laptop', 2),
(3, 'UPDATE', '2025-07-02 00:58:14.000000', '{\"id\":1,\"name\":\"RAMs\",\"createdAt\":1751391684000,\"updatedAt\":1751391684000}', '{\"id\":1,\"name\":\"RAMs\",\"createdAt\":1751391684000,\"updatedAt\":1751391684000}', 1, 'PartType', 2),
(4, 'UPDATE', '2025-07-02 01:20:41.000000', '{\"part_id\":1,\"entity_type\":\"Part\"}', '{\"part_id\":1,\"entity_type\":\"Part\"}', 1, 'Part', 2),
(5, 'CREATE', '2025-07-02 02:03:11.000000', '{\"id\":1,\"customerLaptop\":{\"id\":1,\"customer\":{\"id\":1,\"username\":\"admin\",\"password\":\"$2a$10$rwA/nbn6xsod9i0pfOwX9ub2fMB2tvx9Eqg2BRySi2y7NBJq4cweO\",\"fullname\":\"Administrator\",\"email\":\"admin@company.com\",\"phone\":\"0123456789\",\"img_link\":\"/assets/images/default-avatar.png\",\"role\":\"ADMIN\",\"status\":\"ACTIVE\",\"createdAt\":1751391684000,\"updatedAt\":1751391684000},\"laptop\":{\"id\":1,\"name\":\"Dell inspiron 5510\",\"model\":{\"id\":2,\"name\":\"Dell Inspiron 15\",\"createdAt\":1751391683000,\"updatedAt\":1751391683000},\"warrantyPeriod\":2,\"imgUrl\":null,\"createdAt\":1751391916000,\"updatedAt\":1751391916000},\"serialNumber\":\"HP450G9-001234\",\"createdAt\":1751396511000,\"updatedAt\":1751396511000},\"fullname\":\"pham nhat anh\",\"email\":\"pna120104@gmail.com\",\"phone\":\"0865380226\",\"address\":\"77 ngo 98 thai ha\",\"description\":\"Máy hỏng ram\",\"bookingDate\":1751389200000,\"status\":\"PENDING\",\"technician\":{\"id\":19,\"username\":\"staff\",\"password\":\"$2a$10$sTZDHnOgy.C/V/4XrUQ5Nue.LBrbDfdEolLl0BK.J7JFsRFfNLL.C\",\"fullname\":\"Test Staff\",\"email\":\"staff@company.com\",\"phone\":\"0123456001\",\"img_link\":\"/assets/images/default-avatar.png\",\"role\":\"STAFF\",\"status\":\"ACTIVE\",\"createdAt\":1751391686000,\"updatedAt\":1751391686000},\"createdAt\":1751396591200,\"updatedAt\":1751396591200}', NULL, 1, 'Request', 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `customer_laptops`
--

CREATE TABLE `customer_laptops` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `serial_number` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `customer_id` bigint(20) NOT NULL,
  `laptop_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `customer_laptops`
--

INSERT INTO `customer_laptops` (`id`, `created_at`, `serial_number`, `updated_at`, `customer_id`, `laptop_id`) VALUES
(1, '2025-07-02 02:01:51.000000', 'HP450G9-001234', '2025-07-02 02:01:51.000000', 1, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `invoices`
--

CREATE TABLE `invoices` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `total_price` double DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `request_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `laptops`
--

CREATE TABLE `laptops` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `warranty_period` int(11) DEFAULT NULL,
  `model_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `laptops`
--

INSERT INTO `laptops` (`id`, `created_at`, `img_url`, `name`, `updated_at`, `warranty_period`, `model_id`) VALUES
(1, '2025-07-02 00:45:16.000000', NULL, 'Dell inspiron 5510', '2025-07-02 00:45:16.000000', 2, 2);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `models`
--

CREATE TABLE `models` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `models`
--

INSERT INTO `models` (`id`, `created_at`, `name`, `updated_at`) VALUES
(1, '2025-07-02 00:41:23.000000', 'HP ProBook 450', '2025-07-02 00:41:23.000000'),
(2, '2025-07-02 00:41:23.000000', 'Dell Inspiron 15', '2025-07-02 00:41:23.000000'),
(3, '2025-07-02 00:41:23.000000', 'Lenovo ThinkPad E14', '2025-07-02 00:41:23.000000'),
(4, '2025-07-02 00:41:23.000000', 'ASUS VivoBook S15', '2025-07-02 00:41:23.000000'),
(5, '2025-07-02 00:41:23.000000', 'Acer Aspire 7', '2025-07-02 00:41:23.000000');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `parts`
--

CREATE TABLE `parts` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `warranty_period` int(11) DEFAULT NULL,
  `laptop_id` bigint(20) DEFAULT NULL,
  `part_type_id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `parts`
--

INSERT INTO `parts` (`id`, `created_at`, `img_url`, `name`, `price`, `quantity`, `updated_at`, `warranty_period`, `laptop_id`, `part_type_id`, `description`) VALUES
(1, '2025-07-02 00:44:18.000000', '/images/parts/1751394041535_5542.png', 'Ram SSD5 8GB', 1000000, 1, '2025-07-02 01:20:41.000000', 3, NULL, 1, '');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `part_types`
--

CREATE TABLE `part_types` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `part_types`
--

INSERT INTO `part_types` (`id`, `created_at`, `name`, `updated_at`) VALUES
(1, '2025-07-02 00:41:24.000000', 'RAM', '2025-07-02 00:41:24.000000'),
(2, '2025-07-02 00:41:24.000000', 'SSD', '2025-07-02 00:41:24.000000'),
(3, '2025-07-02 00:41:24.000000', 'HDD', '2025-07-02 00:41:24.000000'),
(4, '2025-07-02 00:41:24.000000', 'VGA', '2025-07-02 00:41:24.000000'),
(5, '2025-07-02 00:41:24.000000', 'CPU', '2025-07-02 00:41:24.000000');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `requests`
--

CREATE TABLE `requests` (
  `id` bigint(20) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `booking_date` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `customer_laptop_id` bigint(20) DEFAULT NULL,
  `technician_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `requests`
--

INSERT INTO `requests` (`id`, `address`, `booking_date`, `created_at`, `description`, `email`, `fullname`, `phone`, `status`, `updated_at`, `customer_laptop_id`, `technician_id`) VALUES
(1, '77 ngo 98 thai ha', '2025-07-02 00:00:00.000000', '2025-07-02 02:03:11.000000', 'Máy hỏng ram', 'pna120104@gmail.com', 'pham nhat anh', '0865380226', 'PENDING', '2025-07-02 02:03:11.000000', 1, 19);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `request_details`
--

CREATE TABLE `request_details` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `part_id` bigint(20) NOT NULL,
  `request_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `request_imgs`
--

CREATE TABLE `request_imgs` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `img_url` varchar(255) NOT NULL,
  `request_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `img_link` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','CUSTOMER','STAFF','TECHNICIAN') NOT NULL,
  `status` enum('ACTIVE','BANNED') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `created_at`, `email`, `fullname`, `img_link`, `password`, `phone`, `role`, `status`, `updated_at`, `username`) VALUES
(1, '2025-07-02 00:41:24.000000', 'admin@company.com', 'Administrator', '/assets/images/default-avatar.png', '$2a$10$rwA/nbn6xsod9i0pfOwX9ub2fMB2tvx9Eqg2BRySi2y7NBJq4cweO', '0123456789', 'ADMIN', 'ACTIVE', '2025-07-02 00:41:24.000000', 'admin'),
(2, '2025-07-02 00:41:24.000000', 'testadmin@company.com', 'Test Administrator', '/assets/images/default-avatar.png', '$2a$10$6vzxDzYRytybJUtVDTQk4uarq9Te/ZpjZINq1jd3bgDT5A1gvJ1mq', '0123456788', 'ADMIN', 'ACTIVE', '2025-07-02 00:41:24.000000', 'testadmin'),
(3, '2025-07-02 00:41:24.000000', 'staff1@company.com', 'Nguyễn Văn An', '/assets/images/default-avatar.png', '$2a$10$pe4pnj8ll6xi5NAAFFGAouOB81EPWbGjNscL4.vGJzbh9g/oeUCp2', '0987654321', 'STAFF', 'BANNED', '2025-07-02 11:12:12.000000', 'staff1'),
(4, '2025-07-02 00:41:24.000000', 'staff2@company.com', 'Trần Thị Bình', '/assets/images/default-avatar.png', '$2a$10$6XuDxJSq.0sm/sQ7dwrJFOn2ge35Y9LxqPUv3zSAA/lHlZ6rGW2DO', '0976543210', 'STAFF', 'ACTIVE', '2025-07-02 00:41:24.000000', 'staff2'),
(5, '2025-07-02 00:41:24.000000', 'staff3@company.com', 'Lê Văn Cường', '/assets/images/default-avatar.png', '$2a$10$lLP/ubpLi77tXj9pkySPQeC/QaDflAHeZJSM/iAqKjEepuBhuk.0e', '0965432109', 'STAFF', 'ACTIVE', '2025-07-02 00:41:24.000000', 'staff3'),
(6, '2025-07-02 00:41:24.000000', 'staff4@company.com', 'Phạm Thị Dung', '/assets/images/default-avatar.png', '$2a$10$rZKzIyllFn6Gog9pMq8nbeJitC4T0Yrw66YH4VLiu/4nEwEc.S/xO', '0954321098', 'STAFF', 'ACTIVE', '2025-07-02 00:41:24.000000', 'staff4'),
(7, '2025-07-02 00:41:24.000000', 'staff5@company.com', 'Hoàng Văn Em', '/assets/images/default-avatar.png', '$2a$10$gV.1PuzEDqtMtDajwAosme7RmbzCF2BMONTIdufvnTF36yhGGXbfG', '0943210987', 'STAFF', 'ACTIVE', '2025-07-02 00:41:24.000000', 'staff5'),
(8, '2025-07-02 00:41:25.000000', 'staff6@company.com', 'Nguyễn Thị Phương', '/assets/images/default-avatar.png', '$2a$10$dIyC.DmTJ8gb7O0b76kEn.LdCE8.y4MOLUbFIDBpdyBw5ZyAUgdcW', '0932109876', 'STAFF', 'BANNED', '2025-07-02 00:41:25.000000', 'staff6'),
(9, '2025-07-02 00:41:25.000000', 'customer1@gmail.com', 'Nguyễn Văn Khách', '/assets/images/default-avatar.png', '$2a$10$d84Rldcyx5PQCXrq8CH72OD/ZLWxBL4WIyeIjCMt485.oLbURhXEa', '0123456780', 'CUSTOMER', 'ACTIVE', '2025-07-02 00:41:25.000000', 'customer1'),
(10, '2025-07-02 00:41:25.000000', 'customer2@gmail.com', 'Trần Thị Lan', '/assets/images/default-avatar.png', '$2a$10$U098B1U.wf7PMnO2TIonqeI8nD3bUVTE3chrETrVjVihnR0FUyzq.', '0123456781', 'CUSTOMER', 'ACTIVE', '2025-07-02 00:41:25.000000', 'customer2'),
(11, '2025-07-02 00:41:25.000000', 'customer3@gmail.com', 'Lê Văn Minh', '/assets/images/default-avatar.png', '$2a$10$drKZGXybEyEnCh3nQob9.OZKdfdCeGicfbrjhnVBxcecUsbdy5JvO', '0123456782', 'CUSTOMER', 'ACTIVE', '2025-07-02 00:41:25.000000', 'customer3'),
(12, '2025-07-02 00:41:25.000000', 'customer4@gmail.com', 'Phạm Thị Nga', '/assets/images/default-avatar.png', '$2a$10$fCcV37yy4hPkMlY2KXhVNus6m7KPz5iBgjfdr9gQKv0BKGrGwE3bq', '0123456783', 'CUSTOMER', 'ACTIVE', '2025-07-02 00:41:25.000000', 'customer4'),
(13, '2025-07-02 00:41:25.000000', 'customer5@gmail.com', 'Hoàng Văn Phong', '/assets/images/default-avatar.png', '$2a$10$846r.Emljf0HgXF14UslJ.YPllqmF3bi2yqHQ9YAONSlEJTue44pu', '0123456784', 'CUSTOMER', 'ACTIVE', '2025-07-02 00:41:25.000000', 'customer5'),
(14, '2025-07-02 00:41:25.000000', 'customer6@gmail.com', 'Đỗ Thị Quỳnh', '/assets/images/default-avatar.png', '$2a$10$IZG2fMP.uvs4231J5iy/hONRcm7hYSl7aMyYBDRxvo4b38dOlbs6C', '0123456785', 'CUSTOMER', 'ACTIVE', '2025-07-02 00:41:25.000000', 'customer6'),
(15, '2025-07-02 00:41:25.000000', 'customer7@gmail.com', 'Vũ Văn Sơn', '/assets/images/default-avatar.png', '$2a$10$1vv55r0SOC0an3Fpmj5GSuDrrlWUUfC0mR4eJcKAIMhEOmSMxhkym', '0123456786', 'CUSTOMER', 'ACTIVE', '2025-07-02 00:41:25.000000', 'customer7'),
(16, '2025-07-02 00:41:25.000000', 'customer8@gmail.com', 'Nguyễn Thị Thu', '/assets/images/default-avatar.png', '$2a$10$XfxQ8DfJviSIgRhXCtvehu8SMovtl274KgRZJBRWrmb2mPJqeI.xe', '0123456787', 'CUSTOMER', 'ACTIVE', '2025-07-02 11:12:52.000000', 'customer8'),
(17, '2025-07-02 00:41:25.000000', 'customer9@gmail.com', 'Lê Văn Tùng', '/assets/images/default-avatar.png', '$2a$10$zcFXOGboSoxtkOH1Ba30feJ4gQmfmHlFBU8YWsyQVACnF569.s4ge', '0123456788', 'CUSTOMER', 'ACTIVE', '2025-07-02 00:41:25.000000', 'customer9'),
(18, '2025-07-02 00:41:26.000000', 'customer@gmail.com', 'Test Customer', '/assets/images/default-avatar.png', '$2a$10$1S6a0e8tP5nig6T3EpXjy.qUIeOmijn.w4TvLChRt33aNIqEGvRMG', '0123456000', 'CUSTOMER', 'ACTIVE', '2025-07-02 00:41:26.000000', 'customer'),
(19, '2025-07-02 00:41:26.000000', 'staff@company.com', 'Test Staff', '/assets/images/default-avatar.png', '$2a$10$sTZDHnOgy.C/V/4XrUQ5Nue.LBrbDfdEolLl0BK.J7JFsRFfNLL.C', '0123456001', 'STAFF', 'ACTIVE', '2025-07-02 00:41:26.000000', 'staff');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `audit_logs`
--
ALTER TABLE `audit_logs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKjs4iimve3y0xssbtve5ysyef0` (`user_id`);

--
-- Chỉ mục cho bảng `customer_laptops`
--
ALTER TABLE `customer_laptops`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKh32mmknslwpa2jh98uhahwxf9` (`customer_id`),
  ADD KEY `FK4eb26tcr9hv0d977i8ecptqjj` (`laptop_id`);

--
-- Chỉ mục cho bảng `invoices`
--
ALTER TABLE `invoices`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK2xtywjxaesmqa7b760pv8hdq2` (`request_id`);

--
-- Chỉ mục cho bảng `laptops`
--
ALTER TABLE `laptops`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKhcbaxm1uyplsnm18dbvagc894` (`model_id`);

--
-- Chỉ mục cho bảng `models`
--
ALTER TABLE `models`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `parts`
--
ALTER TABLE `parts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKsodgvyktua7phiqhtixfgmqm4` (`laptop_id`),
  ADD KEY `FKckbn2w3lt07i9k861v0n6t1cv` (`part_type_id`);

--
-- Chỉ mục cho bảng `part_types`
--
ALTER TABLE `part_types`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `requests`
--
ALTER TABLE `requests`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKjkbx18pm6iegw2hv5823fr69w` (`customer_laptop_id`),
  ADD KEY `FK5d0qc1jlb3iy8r678k7dltddo` (`technician_id`);

--
-- Chỉ mục cho bảng `request_details`
--
ALTER TABLE `request_details`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK9fmb6msgglpyvp65otfjv0v88` (`part_id`),
  ADD KEY `FK7siyp69nykrgh6y0lebsx9s0x` (`request_id`);

--
-- Chỉ mục cho bảng `request_imgs`
--
ALTER TABLE `request_imgs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKaro841eljdimgdpcb09cihy1n` (`request_id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `audit_logs`
--
ALTER TABLE `audit_logs`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `customer_laptops`
--
ALTER TABLE `customer_laptops`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `invoices`
--
ALTER TABLE `invoices`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `laptops`
--
ALTER TABLE `laptops`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `models`
--
ALTER TABLE `models`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `parts`
--
ALTER TABLE `parts`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `part_types`
--
ALTER TABLE `part_types`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `requests`
--
ALTER TABLE `requests`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `request_details`
--
ALTER TABLE `request_details`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `request_imgs`
--
ALTER TABLE `request_imgs`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `audit_logs`
--
ALTER TABLE `audit_logs`
  ADD CONSTRAINT `FKjs4iimve3y0xssbtve5ysyef0` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `customer_laptops`
--
ALTER TABLE `customer_laptops`
  ADD CONSTRAINT `FK4eb26tcr9hv0d977i8ecptqjj` FOREIGN KEY (`laptop_id`) REFERENCES `laptops` (`id`),
  ADD CONSTRAINT `FKh32mmknslwpa2jh98uhahwxf9` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `invoices`
--
ALTER TABLE `invoices`
  ADD CONSTRAINT `FKof1pf5aiok9okvbyg6grmm5od` FOREIGN KEY (`request_id`) REFERENCES `requests` (`id`);

--
-- Các ràng buộc cho bảng `laptops`
--
ALTER TABLE `laptops`
  ADD CONSTRAINT `FKhcbaxm1uyplsnm18dbvagc894` FOREIGN KEY (`model_id`) REFERENCES `models` (`id`);

--
-- Các ràng buộc cho bảng `parts`
--
ALTER TABLE `parts`
  ADD CONSTRAINT `FKckbn2w3lt07i9k861v0n6t1cv` FOREIGN KEY (`part_type_id`) REFERENCES `part_types` (`id`),
  ADD CONSTRAINT `FKsodgvyktua7phiqhtixfgmqm4` FOREIGN KEY (`laptop_id`) REFERENCES `laptops` (`id`);

--
-- Các ràng buộc cho bảng `requests`
--
ALTER TABLE `requests`
  ADD CONSTRAINT `FK5d0qc1jlb3iy8r678k7dltddo` FOREIGN KEY (`technician_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKjkbx18pm6iegw2hv5823fr69w` FOREIGN KEY (`customer_laptop_id`) REFERENCES `customer_laptops` (`id`);

--
-- Các ràng buộc cho bảng `request_details`
--
ALTER TABLE `request_details`
  ADD CONSTRAINT `FK7siyp69nykrgh6y0lebsx9s0x` FOREIGN KEY (`request_id`) REFERENCES `requests` (`id`),
  ADD CONSTRAINT `FK9fmb6msgglpyvp65otfjv0v88` FOREIGN KEY (`part_id`) REFERENCES `parts` (`id`);

--
-- Các ràng buộc cho bảng `request_imgs`
--
ALTER TABLE `request_imgs`
  ADD CONSTRAINT `FKaro841eljdimgdpcb09cihy1n` FOREIGN KEY (`request_id`) REFERENCES `requests` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
