-- 创建数据库
CREATE DATABASE IF NOT EXISTS hotel_pms DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE hotel_pms;

-- 酒店表
CREATE TABLE IF NOT EXISTS hotel (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '酒店ID',
    hotel_name VARCHAR(100) NOT NULL COMMENT '酒店名称',
    address VARCHAR(255) NOT NULL COMMENT '酒店地址',
    phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    description TEXT COMMENT '酒店描述',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- 客房表
CREATE TABLE IF NOT EXISTS room (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '客房ID',
    room_number VARCHAR(20) NOT NULL UNIQUE COMMENT '房间号',
    room_type VARCHAR(50) NOT NULL COMMENT '房间类型',
    status INT NOT NULL DEFAULT 1 COMMENT '状态：1-空闲, 2-已预订, 3-已入住, 4-打扫中',
    price DOUBLE NOT NULL COMMENT '价格',
    description TEXT COMMENT '房间描述',
    hotel_id BIGINT COMMENT '所属酒店ID',
    floor INT COMMENT '楼层',
    orientation VARCHAR(20) COMMENT '朝向',
    facilities VARCHAR(255) COMMENT '设施',
    max_occupancy INT COMMENT '最大入住人数',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (hotel_id) REFERENCES hotel(id) COMMENT '外键关联酒店表'
);

-- 客户表
CREATE TABLE IF NOT EXISTS customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '客户ID',
    name VARCHAR(50) NOT NULL COMMENT '客户姓名',
    id_card VARCHAR(20) NOT NULL UNIQUE COMMENT '身份证号',
    phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    gender VARCHAR(10) COMMENT '性别',
    birth_date DATE COMMENT '出生日期',
    address VARCHAR(255) COMMENT '地址',
    member_id VARCHAR(50) COMMENT '会员ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- 会员表
CREATE TABLE IF NOT EXISTS member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会员ID',
    member_id VARCHAR(50) NOT NULL UNIQUE COMMENT '会员卡号',
    name VARCHAR(50) NOT NULL COMMENT '会员姓名',
    phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    level VARCHAR(20) NOT NULL DEFAULT '普通' COMMENT '会员等级',
    points INT DEFAULT 0 COMMENT '积分',
    join_date DATE DEFAULT CURRENT_DATE() COMMENT '加入日期',
    expire_date DATE COMMENT '过期日期',
    status VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '状态',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    room_id BIGINT NOT NULL COMMENT '房间ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    check_in_date DATETIME NOT NULL COMMENT '入住时间',
    check_out_date DATETIME NOT NULL COMMENT '退房时间',
    days INT NOT NULL COMMENT '入住天数',
    total_price DOUBLE NOT NULL COMMENT '总价格',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '订单状态',
    payment_status VARCHAR(20) NOT NULL DEFAULT 'unpaid' COMMENT '支付状态',
    payment_method VARCHAR(20) COMMENT '支付方式',
    notes TEXT COMMENT '备注',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (room_id) REFERENCES room(id) COMMENT '外键关联房间表',
    FOREIGN KEY (customer_id) REFERENCES customer(id) COMMENT '外键关联客户表'
);

-- 消费记录表
CREATE TABLE IF NOT EXISTS consumption (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消费记录ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    item_name VARCHAR(100) NOT NULL COMMENT '消费项目',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    price DOUBLE NOT NULL COMMENT '单价',
    total_amount DOUBLE NOT NULL COMMENT '总金额',
    consume_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '消费时间',
    FOREIGN KEY (order_id) REFERENCES `order`(id) COMMENT '外键关联订单表'
);

-- 角色表
CREATE TABLE IF NOT EXISTS role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称',
    description VARCHAR(255) COMMENT '角色描述',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    status INT NOT NULL DEFAULT 1 COMMENT '状态：1-启用, 0-禁用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (role_id) REFERENCES role(id) COMMENT '外键关联角色表'
);

-- 初始化数据
INSERT INTO hotel (hotel_name, address, phone, email, description) VALUES
('测试酒店', '北京市朝阳区建国路88号', '010-12345678', 'info@testhotel.com', '这是一家测试酒店');

INSERT INTO room (room_number, room_type, status, price, description, hotel_id, floor, orientation, facilities, max_occupancy) VALUES
('101', 'single', 1, 200, '标准单人间', 1, 1, '南', '空调,电视,WiFi', 1),
('102', 'double', 3, 300, '标准双人间', 1, 1, '北', '空调,电视,WiFi', 2),
('201', 'luxury', 2, 500, '豪华单人间', 1, 2, '东', '空调,电视,WiFi,迷你吧', 1),
('202', 'suite', 4, 800, '豪华套房', 1, 2, '西', '空调,电视,WiFi,迷你吧,浴缸', 2),
('301', 'single', 1, 200, '标准单人间', 1, 3, '南', '空调,电视,WiFi', 1);

INSERT INTO customer (name, id_card, phone, gender, birth_date, address) VALUES
('张三', '110101199001011234', '13800138001', '男', '1990-01-01', '北京市朝阳区'),
('李四', '110101199001011235', '13800138002', '女', '1990-01-02', '北京市海淀区'),
('王五', '110101199001011236', '13800138003', '男', '1990-01-03', '北京市西城区');

INSERT INTO member (member_id, name, phone, email, level, points, join_date, expire_date, status) VALUES
('M001', '张三', '13800138001', 'zhangsan@example.com', '银卡', 1000, '2024-01-01', '2025-01-01', 'active'),
('M002', '李四', '13800138002', 'lisi@example.com', '金卡', 2000, '2024-01-02', '2025-01-02', 'active');

INSERT INTO `order` (order_no, room_id, customer_id, check_in_date, check_out_date, days, total_price, status, payment_status, payment_method) VALUES
('20240101001', 2, 1, '2024-01-01 14:00:00', '2024-01-03 12:00:00', 2, 600, 'completed', 'paid', '支付宝'),
('20240101002', 3, 2, '2024-01-02 14:00:00', '2024-01-04 12:00:00', 2, 1000, 'pending', 'unpaid', NULL);

INSERT INTO consumption (order_id, item_name, quantity, price, total_amount) VALUES
(1, '早餐', 2, 30, 60),
(1, '矿泉水', 2, 5, 10);

-- 初始化角色数据
INSERT INTO role (role_name, description) VALUES
('admin', '超级管理员'),
('user', '普通用户');

-- 初始化用户数据 (密码为 123456，实际生产环境应使用加密密码)
INSERT INTO user (username, password, name, phone, email, role_id, status) VALUES
('admin', '123456', '管理员', '13800138000', 'admin@example.com', 1, 1),
('user', '123456', '普通用户', '13800138001', 'user@example.com', 2, 1);