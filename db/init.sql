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

CREATE TABLE package_item (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  package_name VARCHAR(100) NOT NULL COMMENT '套餐名称',
  package_desc VARCHAR(100) COMMENT '套餐描述',
  original_price decimal(10, 6) NOT NULL COMMENT '商品原价',
  package_status INT NOT NULL DEFAULT '0' COMMENT '状态。 0: 未启用 1：活动，2：下架（用户无法使用，需检测） 3. 隐藏（不展示在前端）',
  sale_price decimal(10, 6) NOT NULL DEFAULT '0.000000' COMMENT '商品销售价',
  discount DECIMAL(5, 2) COMMENT '折扣百分比',
  discount_start_date TIMESTAMP COMMENT '折扣开始日期',
  discount_end_date TIMESTAMP COMMENT '折扣结束日期',
  data_allowance INT NOT NULL COMMENT '数据流量限额（单位：GB）',
  device_limit INT COMMENT '设备数量限制',
  speed_limit INT COMMENT '速率限制（单位：Mbps）',
  `deleted` tinyint(2) DEFAULT '0' COMMENT '是否已删除 1：已删除 0：未删除',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '套餐表';

-- 订单相关-start
CREATE TABLE pay_order (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  order_code varchar(50) NOT NULL COMMENT '订单号',
  trade_no varchar(50) COMMENT '外部支付系统交易号',
  -- 外键
  user_id bigint NOT NULL COMMENT '用户ID',
  -- 外键
  package_id bigint NOT NULL COMMENT '套餐计划主键',
  package_unit INT NOT NULL DEFAULT '0' COMMENT '计费周期。单位：月份',
  order_create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建日期',
  order_expire_time timestamp DEFAULT NULL COMMENT '订单过期日期',
  order_status varchar(16) NOT NULL DEFAULT 'wait_pay' COMMENT '订单状态， wait_pay(待支付)、
  paid(已支付)refunding退款中)、refunded(已退款)、closed(订单关闭)',
  order_remark varchar(128) COMMENT '订单备注',
  pay_amount decimal(10, 6) NOT NULL DEFAULT '0.000000' COMMENT '支付金额',
  -- 支付
  pay_time timestamp COMMENT '支付时间',
  pay_way varchar(32) COMMENT '支付方式: wxpay(微信支付)、alipay支付宝支付),USTD(加密货币交易)',
  pay_seene varchar(32) COMMENT 'ONLINE_PAY(在线支付)、QRCODE_SCAN_PAY（扫码支), QRCODE_SHOW_PAY(付款码支付)',
  pay_status varchar(16) DEFAULT 'waiting' COMMENT '支付状态， waiting(待支付)、success(支付成功)，failed(支付失败)',
  `platform_coupon_id` varchar(32) DEFAULT NULL COMMENT '平台优惠券ID',
  `platform_coupon_amount` decimal(10, 6) DEFAULT NULL COMMENT '平台优惠券优惠金额',
  `supplier_id` varchar(32) COMMENT '收款商户ID',
  -- 退款 
  `refund_no` timestamp NULL DEFAULT NULL COMMENT '退款单号',
  `refund_req_time` timestamp NULL DEFAULT NULL COMMENT '退款请求时间',
  `refund_time` timestamp NULL DEFAULT NULL COMMENT '退款时间',
  `refund_amount` decimal(10, 6) DEFAULT '0.000000' COMMENT '退款金额',
  refund_status varchar(16) COMMENT '退款状态，refunding(退款中)、part_refunded(部分退款)、all_refunded(全部退款)、rejected(已拒绝',
  `deleted` tinyint(2) DEFAULT '0' COMMENT '是否已删除 1：已删除 0：未删除',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY `order_code` (`order_code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '订单表';

CREATE TABLE `pay_order_refund` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `order_code` varchar(32) NOT NULL COMMENT '业务订单号',
  `refund_no` varchar(32) NOT NULL COMMENT '退款单号',
  `refund_amount` decimal(8, 2) unsigned NOT NULL COMMENT '退款金额',
  `refund_req_time` TIMESTAMP NOT NULL COMMENT '退款请求时间',
  `refund_finish_time` TIMESTAMP NOT NULL COMMENT '退款完成时间',
  `create_time` TIMESTAMP NOT NULL COMMENT '数据创建时间',
  `update_time` TIMESTAMP NOT NULL COMMENT '数据最近一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '订单退款表';

CREATE TABLE pay_order_item (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  order_code varchar(50) NOT NULL COMMENT '订单号',
  package_id bigint NOT NULL COMMENT '套餐主键',
  package_name VARCHAR(100) NOT NULL COMMENT '套餐名称',
  package_desc VARCHAR(100) COMMENT '套餐描述',
  package_unit INT NOT NULL DEFAULT '0' COMMENT '计费周期。单位：月份',
  original_price decimal(10, 6) NOT NULL COMMENT '商品原价',
  sale_price decimal(10, 6) NOT NULL DEFAULT '0.000000' COMMENT '商品销售价',
  discount DECIMAL(5, 2) COMMENT '折扣百分比',
  discount_start_date TIMESTAMP COMMENT '折扣开始日期',
  discount_end_date TIMESTAMP COMMENT '折扣结束日期',
  data_allowance INT NOT NULL COMMENT '数据流量限额（单位：GB）',
  device_limit INT COMMENT '设备数量限制',
  speed_limit INT COMMENT '速率限制（单位：Mbps）',
  `deleted` tinyint(2) DEFAULT '0' COMMENT '是否已删除 1：已删除 0：未删除',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) COMMENT '订单项表';

CREATE TABLE usage_record (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  -- 外键 TODO 需要套餐快照
  package_id bigint NOT NULL COMMENT '套餐计划主键',
  order_code varchar(50) NOT NULL COMMENT '订单号',
  -- 外键
  user_id bigint NOT NULL COMMENT '用户ID',
  purchase_status INT NOT NULL DEFAULT '0' COMMENT '套餐状态 0:未开始 1：生效中 2：流量已用尽 3：已过期 ',
  purchase_start_time TIMESTAMP NOT NULL COMMENT '开始日期',
  purchase_end_time TIMESTAMP NOT NULL COMMENT '结束日期',
  speed_limit INT COMMENT '速率限制（单位：Mbps）',
  data_allowance INT NOT NULL COMMENT '数据流量限额（单位：GB）',
  consumed_data_transfer INT COMMENT '用户已消耗的流量（以MB为单位）',
  device_num INT COMMENT '在线的设备数量',
  subscription_link VARCHAR(255) COMMENT '订阅链接',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(2) DEFAULT '0' COMMENT '是否已删除 1：已删除 0：未删除',
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '用户已购套餐记录表';

-- 订单相关-end
-- 服务器、容器-start
CREATE TABLE foreign_server (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  server_name VARCHAR(32) NOT NULL COMMENT '服务器的名称',
  supplier VARCHAR(32) NOT NULL COMMENT '服务器的服务商',
  domain_name VARCHAR(255) COMMENT '服务器的域名(会变动)',
  ip_address VARCHAR(32) NOT NULL COMMENT '服务器的IP地址(会变动)',
  port int unsigned DEFAULT NULL COMMENT '服务器的端口号',
  monthly_fee decimal(10, 6) NOT NULL COMMENT '每月费用，单位（美元）',
  -- discount DECIMAL(5, 2) COMMENT '折扣百分比',
  -- actual_fee decimal(10, 6) NOT NULL COMMENT '实际费用，单位（美元）',
  total_monthly_data_transfer INT COMMENT '服务器每月的总流量（以GB为单位）',
  consumed_data_transfer INT COMMENT '服务器已消耗的流量（以GB为单位）',
  operating_system VARCHAR(255) COMMENT '服务器的操作系统',
  cpu_cores INT COMMENT '服务器的CPU核心数',
  ram_gb INT COMMENT '服务器的总RAM大小（以GB为单位）',
  remaining_ram_gb INT COMMENT '服务器剩余的RAM大小（以GB为单位）',
  storage_gb INT COMMENT '服务器的总存储大小（以GB为单位）',
  consumed_storage_gb INT COMMENT '服务器已使用的存储大小（以GB为单位）',
  status INT NOT NULL COMMENT '服务器的状态。0: 停止 1：活动，2：过期  ',
  `deleted` tinyint(2) DEFAULT '0' COMMENT '是否已删除 1：已删除 0：未删除',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '服务器信息表';

-- CREATE TABLE docker_container (
--   `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
--   container_name VARCHAR(255) NOT NULL COMMENT '容器的名称',
--   -- 外键
--   server_id bigint NOT NULL COMMENT '关联的服务器ID',
--   -- 外键
--   package_id bigint NOT NULL COMMENT '套餐计划主键。取速度限制，设备限制等信息',
--   -- 创建后更新
--   bound_port INT NOT NULL COMMENT '与容器绑定的端口',
--   `deleted` tinyint(2) DEFAULT '0' COMMENT '是否已删除 1：已删除 0：未删除',
--   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
--   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
--   PRIMARY KEY (id)
-- ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'docker容器信息表';
-- 服务器、容器-end
-- 钱包、返利--start
CREATE TABLE wallets (
  id bigint AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  user_id bigint NOT NULL COMMENT '用户ID',
  balance DECIMAL(10, 6) NOT NULL COMMENT '余额',
  currency VARCHAR(10) NOT NULL DEFAULT '1' COMMENT '货币类型（1：人民币 2: USDT） ',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '钱包表';

CREATE TABLE promotion_links (
  id bigint AUTO_INCREMENT COMMENT '主键ID',
  promotion_code VARCHAR(255) NOT NULL COMMENT '链接代码',
  user_id bigint NOT NULL COMMENT '用户ID',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态（0：暂停，1：活动）',
  registration_time INT NOT NULL DEFAULT 0 COMMENT '注册成功次数',
  purchase_time INT NOT NULL DEFAULT 0 COMMENT '购买次数',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '推广链接表';

CREATE TABLE rebate_records (
  id bigint AUTO_INCREMENT COMMENT '主键ID',
  user_id bigint NOT NULL COMMENT '用户ID',
  promotion_code VARCHAR(255) NOT NULL COMMENT '推广链接代码',
  package_id INT NOT NULL COMMENT '套餐ID',
  rebate_amount DECIMAL(10, 6) NOT NULL COMMENT '返利金额',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '返利记录表';

-- 钱包、返利--end
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
  `item` varchar(255) NOT NULL DEFAULT '' COMMENT '配置项',
  `value` varchar(2048) NOT NULL DEFAULT '' COMMENT '配置值',
  `is_public` tinyint(1) unsigned NOT NULL DEFAULT 0 COMMENT '是否为公共参数',
  `type` varchar(16) NOT NULL DEFAULT '' COMMENT '配置值类型',
  `mark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `item` (`item`),
  KEY `class` (`class`),
  KEY `is_public` (`is_public`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '配置表';

CREATE TABLE `link` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `token` varchar(255) NOT NULL DEFAULT '' COMMENT '订阅token',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '用户ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `token` (`token`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订阅链接表';