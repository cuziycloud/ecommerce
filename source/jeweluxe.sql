-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th5 03, 2025 lúc 08:05 AM
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
-- Cơ sở dữ liệu: `jeweluxe`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cart`
--

CREATE TABLE `cart` (
  `id` int(11) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `wants_gift_wrap` tinyint(1) DEFAULT 0,
  `wants_insurance` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `cart`
--

INSERT INTO `cart` (`id`, `quantity`, `product_id`, `user_id`, `wants_gift_wrap`, `wants_insurance`) VALUES
(8, 1, 3, 2, 0, 1),
(9, 1, 6, 2, 1, 1),
(12, 1, 5, 2, 0, 0),
(13, 1, 9, 2, 0, 0),
(14, 1, 14, 2, 1, 0);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `category`
--

CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `image_name` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `category`
--

INSERT INTO `category` (`id`, `image_name`, `is_active`, `name`) VALUES
(1, 'rings.png', b'1', 'Rings'),
(2, 'necklaces.png', b'1', 'Necklaces'),
(3, 'earrings.png', b'1', 'Earrings'),
(4, 'bracelets.png', b'1', 'Bracelets');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `order_address`
--

CREATE TABLE `order_address` (
  `id` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `mobile_no` varchar(255) DEFAULT NULL,
  `pincode` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `order_address`
--

INSERT INTO `order_address` (`id`, `address`, `city`, `email`, `first_name`, `last_name`, `mobile_no`, `pincode`, `state`) VALUES
(1, '123 Diamond Ln', 'Sparkle Town', 'alice.s@email.com', 'Alice', 'Smith', '0123456789', '10001', 'NY'),
(2, '456 Gold St', 'Metalville', 'bob.j@email.com', 'Bob', 'Johnson', '0112233445', '75001', 'TX'),
(3, '789 Silver Ave', 'Gemstone City', 'admin@jewelryshop.com', 'Jewelry', 'Admin', '0987654321', '90210', 'CA'),
(4, '101 Pearl Rd', 'Sparkle Town', 'alice.s@email.com', 'Alice', 'Smith', '0123456789', '10001', 'NY'),
(5, '123 Admin St', 'HCM', 'tranmyvan6157@gmail.com', 'My', 'Van', '0932112585', '71000', 'Hanoi'),
(6, '336 Phạm Hữu Lầu, Ấp 4, xã Phước Kiển, huyện Nhà Bè', 'TP Hồ Chí Minh', 'tranmyvan6157@gmail.com', 'Trần', 'Vân', '0932112585', '71000', 'Hanoi'),
(7, '336 Phạm Hữu Lầu, Ấp 4, xã Phước Kiển, huyện Nhà Bè', 'TP Hồ Chí Minh', 'tranmyvan6157@gmail.com', 'Trần', 'Vân', '0932112585', '71000', 'Hanoi'),
(8, '336 Phạm Hữu Lầu, Ấp 4, xã Phước Kiển, huyện Nhà Bè', 'TP Hồ Chí Minh', 'tranmyvan6154@gmail.com', 'Trần', 'Vân', '0932112585', '71000', 'Hanoi'),
(9, '336 Phạm Hữu Lầu, Ấp 4, xã Phước Kiển, huyện Nhà Bè', 'TP Hồ Chí Minh', 'tranmyvan6157@gmail.com', 'Trần', 'Vân', '0932112585', '', ''),
(10, '336 Phạm Hữu Lầu, Ấp 4, xã Phước Kiển, huyện Nhà Bè', 'TP Hồ Chí Minh', 'tranmyvan6157@gmail.com', 'Trần', 'Vân', '0932112585', '', '');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `order_item`
--

CREATE TABLE `order_item` (
  `id` int(11) NOT NULL,
  `order_date` date DEFAULT NULL,
  `order_id` varchar(255) DEFAULT NULL,
  `payment_type` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `order_address_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `is_gift_wrap` tinyint(1) DEFAULT 0,
  `has_insurance` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `order_item`
--

INSERT INTO `order_item` (`id`, `order_date`, `order_id`, `payment_type`, `price`, `quantity`, `status`, `order_address_id`, `product_id`, `user_id`, `is_gift_wrap`, `has_insurance`) VALUES
(1, '2025-04-18', 'ORD-JEWEL-001', 'Card', 114, 1, 'DELIVERED', 1, 3, 2, 0, 0),
(2, '2025-04-19', 'ORD-JEWEL-002', 'PayPal', 150, 1, 'DELIVERED', 2, 4, NULL, 0, 0),
(3, '2025-04-20', 'ORD-JEWEL-003', 'COD', 4500, 1, 'CANCELLED', 4, 1, 2, 0, 0),
(4, '2025-04-19', 'ORD-JEWEL-002', 'PayPal', 200, 1, 'OUT_FOR_DELIVERY', 2, 8, NULL, 0, 0),
(5, '2025-04-23', NULL, 'COD', 4500, 1, 'CANCELLED', 5, 1, 2, 0, 0),
(6, '2025-04-23', NULL, 'ONLINE', 3990, 2, 'CANCELLED', 6, 6, 2, 0, 0),
(7, '2025-04-23', NULL, 'ONLINE', 350, 1, 'IN_PROGRESS', 7, 7, 2, 0, 0),
(8, '2025-04-23', NULL, 'COD', 50, 1, 'IN_PROGRESS', 8, 10, 2, 0, 0),
(9, '2025-04-29', 'ORD-A57148E4', NULL, 850, 1, 'IN_PROGRESS', 9, 2, 2, 1, 1),
(10, '2025-04-29', 'ORD-A57148E4', NULL, 4500, 1, 'DELIVERED', 10, 1, 2, 1, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `product`
--

CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `category` varchar(255) DEFAULT NULL,
  `description` varchar(5000) DEFAULT NULL,
  `discount` int(11) NOT NULL DEFAULT 0,
  `discount_price` double DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT b'1',
  `price` double DEFAULT NULL,
  `stock` int(11) NOT NULL DEFAULT 0,
  `title` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `product`
--

INSERT INTO `product` (`id`, `category`, `description`, `discount`, `discount_price`, `image`, `is_active`, `price`, `stock`, `title`) VALUES
(1, 'Rings', 'Classic elegance, featuring a brilliant-cut 1 carat diamond set in a 14k white gold band. GIA certified.', 15, 425000, 'diamond_ring_01.jpg', b'1', 500000, 5, '1 Carat Diamond Solitaire Ring'),
(2, 'Necklaces', 'A timeless 18-inch rope chain necklace crafted from lustrous 18k yellow gold. Perfect for layering or wearing alone.', 0, 850000, 'gold_chain_01.jpg', b'1', 850000, 25, 'Gold Chain Necklace'),
(3, 'Earrings', 'Simple yet sophisticated freshwater pearl stud earrings (7mm) with sterling silver posts.', 5, 114000, 'pearl_earrings_01.jpg', b'1', 120000, 50, 'Pearl Stud Earrings'),
(4, 'Bracelets', 'A delightful sterling silver charm bracelet ready for personalization. Includes a heart charm.', 0, 150000, 'silver_bracelet_01.jpg', b'1', 150000, 30, 'Silver Charm Bracelet'),
(5, 'Necklaces', 'Stunning oval-cut blue sapphire surrounded by a halo of sparkling diamonds, set in platinum. Chain included.', 15, 187000, 'sapphire_pendant_01.jpg', b'1', 220000, 8, 'Sapphire and Diamond Pendant'),
(6, 'Rings', 'Exquisite emerald-cut diamond engagement ring (0.75 carat) with diamond side stones on a platinum band.', 5, 399000, 'emerald_ring_01.jpg', b'1', 420000, 3, 'Emerald Cut Engagement Ring'),
(7, 'Earrings', 'Trendy and elegant medium-sized hoop earrings crafted from polished 14k rose gold.', 0, 350000, 'rose_gold_hoops_01.jpg', b'1', 350000, 40, 'Rose Gold Hoop Earrings'),
(8, 'Rings', 'Durable and stylish 8mm tungsten carbide wedding band with a brushed finish.', 0, 200000, 'tungsten_band_01.jpg', b'1', 200000, 60, 'Men\'s Tungsten Wedding Band'),
(9, 'Necklaces', 'Delicate sterling silver necklace featuring a small infinity symbol pendant.', 10, 1080000, 'infinity_necklace.jpg', b'1', 1200000, 15, 'Silver Infinity Necklace'),
(10, 'Earrings', 'Sparkling cubic zirconia stud earrings, perfect for everyday wear.', 0, 500000, 'cz_studs.jpg', b'1', 500000, 100, 'CZ Stud Earrings'),
(11, 'Bracelets', 'Leather wrap bracelet with silver bead accents.', 0, 750000, 'leather_bracelet.jpg', b'1', 750000, 22, 'Leather Wrap Bracelet'),
(12, 'Rings', 'Vintage-inspired amethyst cocktail ring in sterling silver.', 20, 160000, 'amethyst_ring.jpg', b'1', 200000, 7, 'Amethyst Cocktail Ring'),
(13, 'Necklaces', 'Personalized initial pendant necklace in 14k gold.', 0, 250000, 'initial_necklace.jpg', b'1', 250000, 40, 'Initial Pendant Necklace (Gold)'),
(14, 'Earrings', 'Gold drop earrings with small ruby accents.', 15, 297500, 'ruby_drop_earrings.jpg', b'1', 350000, 18, 'Ruby Drop Earrings'),
(15, 'Bracelets', 'Adjustable silver bangle bracelet with engraved pattern.', 0, 130000, 'silver_bangle.jpg', b'1', 130000, 35, 'Engraved Silver Bangle');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `account_non_locked` bit(1) DEFAULT b'1',
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `failed_attempt` int(11) DEFAULT 0,
  `is_enable` bit(1) DEFAULT b'1',
  `lock_time` datetime(6) DEFAULT NULL,
  `mobile_number` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `pincode` varchar(255) DEFAULT NULL,
  `profile_image` varchar(255) DEFAULT NULL,
  `reset_token` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `user`
--

INSERT INTO `user` (`id`, `account_non_locked`, `address`, `city`, `email`, `failed_attempt`, `is_enable`, `lock_time`, `mobile_number`, `name`, `password`, `pincode`, `profile_image`, `reset_token`, `role`, `state`) VALUES
(1, b'1', '123 Admin St', 'HCM', 'tranmyvan6157@gmail.com', 0, b'1', NULL, '0932112585', 'My Van', '$2a$10$ePqV7yIJ05S6XyH5/sTph.T3ccpVmCvIhY.pwrTf7En8obJvBBvGW', '71000', 'ava1.jpg', NULL, 'ROLE_ADMIN', 'Hanoi'),
(2, b'1', '123 Diamond Ln', 'Sparkle Town', 'ayjssi0109@gmail.com', 3, b'1', NULL, '0123456789', 'Customer 1', '$2a$10$lhRxMJnaiLJvBe9.jktzZ.N7CrFQmwzisKXpTYGQzoYukMrcXz5oy', '10001', 'ava1.jpg', NULL, 'ROLE_USER', 'NY'),
(8, b'1', '123 Admin St', 'HCM', 'tranmyvan6154@gmail.com', 0, b'1', NULL, '0932112585', 'My Van', '$2a$10$fHyKi4Z8UOfR/dHZ0NUf.O/VyBs78yUGSV2WOjNBCzUO83O2KwR7q', '71000', 'ava3.jpg', NULL, 'ROLE_USER', 'Hanoi');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cart_product_fk` (`product_id`),
  ADD KEY `cart_user_fk_new` (`user_id`);

--
-- Chỉ mục cho bảng `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Chỉ mục cho bảng `order_address`
--
ALTER TABLE `order_address`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `order_item`
--
ALTER TABLE `order_item`
  ADD PRIMARY KEY (`id`),
  ADD KEY `oi_order_id_idx` (`order_id`),
  ADD KEY `oi_address_fk` (`order_address_id`),
  ADD KEY `oi_product_fk` (`product_id`),
  ADD KEY `oi_user_fk` (`user_id`);

--
-- Chỉ mục cho bảng `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `cart`
--
ALTER TABLE `cart`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT cho bảng `category`
--
ALTER TABLE `category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `order_address`
--
ALTER TABLE `order_address`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `order_item`
--
ALTER TABLE `order_item`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `product`
--
ALTER TABLE `product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT cho bảng `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `cart_product_fk` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `cart_user_fk_new` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `order_item`
--
ALTER TABLE `order_item`
  ADD CONSTRAINT `oi_address_fk` FOREIGN KEY (`order_address_id`) REFERENCES `order_address` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `oi_product_fk` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `oi_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
