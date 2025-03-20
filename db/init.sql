CREATE TABLE `user` (
  -- 主键
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  uuid bigint NOT NULL COMMENT '用户ID',
  -- 名字
  `name` VARCHAR(255) NOT NULL COMMENT '名字',
  -- 邮箱
  `email` VARCHAR(64) NOT NULL COMMENT '邮箱',
  `password_hash` VARCHAR(255) NOT NULL COMMENT '哈希值',
  `salt` VARCHAR(255) NOT NULL COMMENT '哈希盐值',
  -- 状态 1 正常 0 无效
  `status` tinyint(2) NOT NULL COMMENT '状态 1 正常 0 无效 2 已禁用（触发审计规则）',
  -- 邀请人ID
  `inviter_user_id` bigint COMMENT '邀请人ID',
  -- 邀请代码
  `invite_code` VARCHAR(255) COMMENT '邀请代码',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(2) DEFAULT '0' COMMENT '是否已删除 1：已删除 0：未删除',
  PRIMARY KEY (`id`),
  PRIMARY KEY (`uuid`),
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '用户表';

CREATE TABLE `roles` (
  `id` bigint AUTO_INCREMENT PRIMARY KEY,
  `user_id` bigint NOT NULL UNIQUE COMMENT '用户ID',
  `role_name` varchar(16) NOT NULL COMMENT 'admin:管理员，user（普通用户）'
);

CREATE TABLE `package_item` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `package_name` varchar(100) NOT NULL COMMENT '套餐名称',
  `package_desc` text DEFAULT NULL COMMENT '套餐描述',
  `original_price` decimal(10, 2) NOT NULL COMMENT '商品原价',
  `package_status` int NOT NULL DEFAULT '0' COMMENT '状态。 0: 未启用 1：活动，2：下架',
  `sale_price` decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '商品销售价',
  `discount` decimal(5, 2) DEFAULT NULL COMMENT '折扣百分比',
  `discount_start_date` timestamp NULL DEFAULT NULL COMMENT '折扣开始日期',
  `discount_end_date` timestamp NULL DEFAULT NULL COMMENT '折扣结束日期',
  `data_allowance` bigint NOT NULL COMMENT '数据流量限额（单位：B）',
  `device_limit` int DEFAULT NULL COMMENT '设备数量限制',
  `speed_limit` bigint unsigned DEFAULT NULL COMMENT '流量速率限额（单位：B）',
  `deleted` tinyint DEFAULT '0' COMMENT '是否已删除 1：已删除 0：未删除',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 订单相关-start
CREATE TABLE pay_order (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  order_code varchar(255) NOT NULL COMMENT '订单号',
  trade_no varchar(255) COMMENT '外部支付系统交易号',
  -- 外键
  user_id bigint NOT NULL COMMENT '用户ID',
  -- 外键
  package_id bigint NOT NULL COMMENT '套餐计划主键',
  package_unit INT NOT NULL DEFAULT '0' COMMENT '计费周期。单位：月份',
  order_create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建日期',
  order_expire_time timestamp DEFAULT NULL COMMENT '订单过期日期',
  order_status varchar(16) NOT NULL DEFAULT 'WAIT_PAY' COMMENT '订单状态，WAIT_PAY(待支付)、PAID(已支付)、
  REFUNDING(退款中)、REFUNDED(已退款)、CLOSED(订单关闭)、CANCELED(订单取消)、COMPLETE(已完成)',
  order_remark varchar(128) COMMENT '订单备注',
  -- 支付
  order_amount decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '订单金额',
  pay_amount decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '支付金额',
  pay_time timestamp COMMENT '支付时间',
  pay_way varchar(32) COMMENT '支付方式: wxpay(微信支付)、alipay支付宝支付),USTD(加密货币交易)',
  pay_seene varchar(32) COMMENT 'ONLINE_PAY(在线支付)、QRCODE_SCAN_PAY（扫码支付), QRCODE_SHOW_PAY(付款码支付)',
  pay_status varchar(16) DEFAULT 'waiting' COMMENT '支付状态， waiting(待支付)、success(支付成功)，failed(支付失败)',
  `coupon_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '优惠券编码',
  `coupon_amount` decimal(10, 2) DEFAULT NULL COMMENT '已优惠金额',
  `supplier_id` varchar(32) COMMENT '收款商户ID',
  -- 退款 
  `refund_no` timestamp NULL DEFAULT NULL COMMENT '退款单号',
  `refund_req_time` timestamp NULL DEFAULT NULL COMMENT '退款请求时间',
  `refund_time` timestamp NULL DEFAULT NULL COMMENT '退款时间',
  `refund_amount` decimal(10, 2) DEFAULT '0.00' COMMENT '退款金额',
  refund_status varchar(16) COMMENT '退款状态，refunding(退款中)、part_refunded(部分退款)、all_refunded(全部退款)、rejected(已拒绝',
  `deleted` tinyint(2) DEFAULT '0' COMMENT '是否已删除 1：已删除 0：未删除',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY `order_code`
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '订单表';

CREATE TABLE `pay_order_refund` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `order_code` varchar(32) NOT NULL COMMENT '业务订单号',
  `refund_no` varchar(32) NOT NULL COMMENT '退款单号',
  `refund_amount` decimal(10, 2) unsigned NOT NULL COMMENT '退款金额',
  `refund_req_time` TIMESTAMP NOT NULL COMMENT '退款请求时间',
  `refund_finish_time` TIMESTAMP NOT NULL COMMENT '退款完成时间',
  `create_time` TIMESTAMP NOT NULL COMMENT '数据创建时间',
  `update_time` TIMESTAMP NOT NULL COMMENT '数据最近一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单退款表';

CREATE TABLE `pay_order_item` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_code` varchar(50) NOT NULL COMMENT '订单号',
  `package_id` bigint NOT NULL COMMENT '套餐主键',
  `package_unit` INT NOT NULL DEFAULT '0' COMMENT '计费周期。单位：月份',
  `package_name` varchar(100) NOT NULL COMMENT '套餐名称',
  `package_desc` text COMMENT '套餐描述',
  `original_price` decimal(10, 2) NOT NULL COMMENT '商品原价',
  `package_status` int NOT NULL DEFAULT '0' COMMENT '状态。 0: 未启用 1：活动，2：下架',
  `sale_price` decimal(10, 2) NOT NULL DEFAULT '0.00' COMMENT '商品销售价',
  `discount` decimal(5, 2) DEFAULT NULL COMMENT '折扣百分比',
  `discount_start_date` timestamp NULL DEFAULT NULL COMMENT '折扣开始日期',
  `discount_end_date` timestamp NULL DEFAULT NULL COMMENT '折扣结束日期',
  `data_allowance` bigint NOT NULL COMMENT '数据流量限额（单位：B）',
  `device_limit` int DEFAULT NULL COMMENT '设备数量限制',
  `speed_limit` bigint unsigned DEFAULT NULL COMMENT '流量速率限额（单位：B）',
  `deleted` tinyint DEFAULT '0' COMMENT '是否已删除 1：已删除 0：未删除',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单项表';

CREATE TABLE `usage_record` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `package_id` bigint NOT NULL COMMENT '套餐计划主键',
  `order_code` varchar(50) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `purchase_status` tinyint NOT NULL DEFAULT '0' COMMENT '套餐状态 0:未开始 1：生效中 2：流量已用尽 3：已过期 4. 已取消',
  `purchase_start_time` timestamp NOT NULL COMMENT '开始日期',
  `purchase_end_time` timestamp NOT NULL COMMENT '结束日期',
  `data_allowance` bigint unsigned NOT NULL COMMENT '数据流量限额（单位：B）',
  `consumed_data_transfer` bigint unsigned DEFAULT '0' COMMENT '用户已消耗的流量（单位：B）',
  `consumed_data_upload` bigint unsigned DEFAULT '0' COMMENT '用户已消耗的上行流量（单位：B）',
  `consumed_data_download` bigint unsigned DEFAULT '0' COMMENT '用户已消耗的下行流量（单位：B）',
  `speed_limit` bigint unsigned DEFAULT NULL COMMENT '流量速率限额（单位：B）',
  `device_num` int DEFAULT NULL COMMENT '在线设备数量',
  `device_limit` int DEFAULT NULL COMMENT '在线设备数量限额',
  `next_reset_date` timestamp NULL DEFAULT NULL COMMENT '下次流量重置日期',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '是否已删除 1：已删除 0：未删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_code`
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 订单相关-end 
CREATE TABLE `foreign_server` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `server_name` varchar(32) NOT NULL COMMENT '服务器的名称',
  `supplier` varchar(32) NOT NULL COMMENT '服务器的服务商',
  `domain_name` varchar(255) DEFAULT NULL COMMENT '服务器的域名(会变动)',
  `ip_address` varchar(32) NOT NULL COMMENT '服务器的IP地址(会变动)',
  `port` smallint DEFAULT NULL COMMENT '服务器的端口号(会变动)',
  `start_date` timestamp NOT NULL COMMENT '服务器启动日期',
  `monthly_fee` decimal(10, 2) NOT NULL COMMENT '每月费用，单位（美元）',
  `total_monthly_data_transfer` bigint unsigned NOT NULL COMMENT '服务器每月默认总流量（单位：B）',
  `consumed_data_transfer` bigint unsigned NOT NULL DEFAULT '0' COMMENT '服务器已消耗的流量（单位：B）',
  `operating_system` varchar(255) DEFAULT NULL COMMENT '服务器的操作系统',
  `cpu_cores` tinyint DEFAULT NULL COMMENT '服务器的CPU核心数',
  `ram_gb` decimal(10, 0) DEFAULT NULL COMMENT '服务器的总RAM大小（单位：GB）',
  `remaining_ram_gb` decimal(10, 0) DEFAULT NULL COMMENT '服务器剩余的RAM大小（单位：GB）',
  `storage_gb` decimal(10, 2) DEFAULT NULL COMMENT '服务器的总存储大小（单位：GB）',
  `consumed_storage_gb` decimal(10, 2) DEFAULT NULL COMMENT '服务器已使用的存储大小（单位：GB）',
  `status` tinyint(1) NOT NULL COMMENT '服务器的状态。0: 停止 1：活动，2：过期',
  `is_beyond_transfer` tinyint DEFAULT NULL COMMENT '是否超过默认流量包限额(1：是，0：否)',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否已删除 1：已删除 0：未删除',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- 钱包、返利--start 
CREATE TABLE `wallet` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `balance` decimal(10, 2) NOT NULL COMMENT '余额',
  `currency` tinyint NOT NULL DEFAULT '1' COMMENT '货币类型（1：人民币 2: USDT） ',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '钱包表';

-- 钱包、返利--end
CREATE TABLE promotion_links (
  id bigint AUTO_INCREMENT COMMENT '主键ID',
  promotion_code VARCHAR(255) NOT NULL COMMENT '链接代码',
  user_id bigint NOT NULL COMMENT '用户ID',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0：暂停，1：活动）',
  registration_time INT NOT NULL DEFAULT 0 COMMENT '注册成功次数',
  purchase_time INT NOT NULL DEFAULT 0 COMMENT '购买次数',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '推广链接表';

CREATE TABLE audit_rule (
  id bigint UNSIGNED AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL COMMENT '名称',
  rule_name VARCHAR(255) NOT NULL COMMENT '规则',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY uk_rule (rule_name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '审计规则表';

CREATE TABLE audit_log (
  id bigint UNSIGNED AUTO_INCREMENT,
  user_id bigint NOT NULL COMMENT '用户ID',
  node_name VARCHAR(255) NOT NULL COMMENT '节点名称',
  rule_name VARCHAR(255) NOT NULL COMMENT '规则名称',
  trigger_count bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '触发次数',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '审计记录表';

CREATE TABLE `config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `code` varchar(255) NOT NULL DEFAULT '' COMMENT '配置项',
  `value` varchar(2048) NOT NULL DEFAULT '' COMMENT '配置值',
  `is_public` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '是否为公共参数',
  `type` varchar(16) NOT NULL DEFAULT '' COMMENT '配置值类型',
  `mark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `item` (`item`),
  KEY `class` (`class`),
  KEY `is_public` (`is_public`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '配置表';

CREATE TABLE `link` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `token` varchar(255) NOT NULL DEFAULT '' COMMENT '订阅token',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '用户ID',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `token` (`token`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订阅链接表';

CREATE TABLE `user_coupon` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '优惠码ID',
  `code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '优惠码',
  `content` json NOT NULL COMMENT '优惠码内容',
  `usage_limit` json NOT NULL COMMENT '优惠码限制',
  `use_count` int unsigned NOT NULL DEFAULT '0' COMMENT '累计使用次数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `expire_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `expire_time` (`expire_time`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;