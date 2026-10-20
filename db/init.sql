-- ============================================================
-- Hs Pro 培训机构管理系统 —— 数据库初始化脚本
-- 数据库: MySQL 8.x
-- 数据库名: hs
-- ============================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `hs` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `hs`;

-- ============================================================
-- 1. 部门表
-- ============================================================
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
    `id`          INT          NOT NULL AUTO_INCREMENT COMMENT '部门ID',
    `name`        VARCHAR(100) NOT NULL              COMMENT '部门名称',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ============================================================
-- 2. 员工表
-- ============================================================
DROP TABLE IF EXISTS `emp`;
CREATE TABLE `emp` (
    `id`          INT           NOT NULL AUTO_INCREMENT COMMENT '员工ID',
    `username`    VARCHAR(50)   NOT NULL              COMMENT '用户名',
    `password`    VARCHAR(255)  NOT NULL              COMMENT '密码',
    `name`        VARCHAR(50)   NOT NULL              COMMENT '姓名',
    `gender`      TINYINT       NOT NULL              COMMENT '性别: 1-男, 2-女',
    `phone`       VARCHAR(20)   DEFAULT NULL          COMMENT '手机号',
    `job`         TINYINT       DEFAULT NULL          COMMENT '职位: 1-班主任, 2-讲师, 3-学工主管, 4-教研主管, 5-咨询师, 6-管理人员',
    `role`        TINYINT       DEFAULT NULL          COMMENT '角色: 1-超级管理员, 2-管理员, 3-普通员工',
    `salary`      INT           DEFAULT NULL          COMMENT '薪资',
    `image`       VARCHAR(500)  DEFAULT NULL          COMMENT '头像路径',
    `entry_date`  DATE          DEFAULT NULL          COMMENT '入职日期',
    `dept_id`     INT           DEFAULT NULL          COMMENT '部门ID',
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_dept_id` (`dept_id`),
    CONSTRAINT `fk_emp_dept` FOREIGN KEY (`dept_id`) REFERENCES `dept`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- ============================================================
-- 3. 员工工作经历表
-- ============================================================
DROP TABLE IF EXISTS `emp_expr`;
CREATE TABLE `emp_expr` (
    `id`      INT           NOT NULL AUTO_INCREMENT COMMENT '工作经历ID',
    `emp_id`  INT           NOT NULL              COMMENT '员工ID',
    `begin`   DATE          DEFAULT NULL          COMMENT '开始时间',
    `end`     DATE          DEFAULT NULL          COMMENT '结束时间',
    `company` VARCHAR(200)  DEFAULT NULL          COMMENT '公司名称',
    `job`     VARCHAR(100)  DEFAULT NULL          COMMENT '职位',
    PRIMARY KEY (`id`),
    KEY `idx_emp_id` (`emp_id`),
    CONSTRAINT `fk_emp_expr_emp` FOREIGN KEY (`emp_id`) REFERENCES `emp`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工工作经历表';

-- ============================================================
-- 4. 班级表
-- ============================================================
DROP TABLE IF EXISTS `clazz`;
CREATE TABLE `clazz` (
    `id`          INT           NOT NULL AUTO_INCREMENT COMMENT '班级ID',
    `name`        VARCHAR(100)  NOT NULL              COMMENT '班级名称',
    `room`        VARCHAR(50)   DEFAULT NULL          COMMENT '教室',
    `begin_date`  DATE          DEFAULT NULL          COMMENT '开课时间',
    `end_date`    DATE          DEFAULT NULL          COMMENT '结课时间',
    `master_id`   INT           DEFAULT NULL          COMMENT '班主任ID（关联emp.id）',
    `subject`     INT           DEFAULT NULL          COMMENT '学科',
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_master_id` (`master_id`),
    CONSTRAINT `fk_clazz_master` FOREIGN KEY (`master_id`) REFERENCES `emp`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- ============================================================
-- 5. 学生表
-- ============================================================
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
    `id`              INT           NOT NULL AUTO_INCREMENT COMMENT '学生ID',
    `name`            VARCHAR(50)   NOT NULL              COMMENT '姓名',
    `no`              VARCHAR(50)   DEFAULT NULL          COMMENT '学号',
    `gender`          TINYINT       DEFAULT NULL          COMMENT '性别: 1-男, 2-女',
    `phone`           VARCHAR(20)   DEFAULT NULL          COMMENT '手机号',
    `id_card`         VARCHAR(18)   DEFAULT NULL          COMMENT '身份证号',
    `is_college`      TINYINT       DEFAULT NULL          COMMENT '是否来自院校: 1-是, 0-否',
    `address`         VARCHAR(300)  DEFAULT NULL          COMMENT '联系地址',
    `degree`          TINYINT       DEFAULT NULL          COMMENT '最高学历: 1-初中, 2-高中, 3-大专, 4-本科, 5-硕士, 6-博士',
    `graduation_date` DATE          DEFAULT NULL          COMMENT '毕业时间',
    `clazz_id`        INT           DEFAULT NULL          COMMENT '班级ID',
    `violation_count` SMALLINT      DEFAULT 0             COMMENT '违纪次数',
    `violation_score` SMALLINT      DEFAULT 0             COMMENT '违纪扣分',
    `create_time`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_clazz_id` (`clazz_id`),
    CONSTRAINT `fk_student_clazz` FOREIGN KEY (`clazz_id`) REFERENCES `clazz`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- ============================================================
-- 6. 操作日志表
-- ============================================================
DROP TABLE IF EXISTS `operate_log`;
CREATE TABLE `operate_log` (
    `id`               INT           NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `operate_emp_id`   INT           DEFAULT NULL          COMMENT '操作人ID',
    `operate_emp_name` VARCHAR(50)   DEFAULT NULL          COMMENT '操作人姓名',
    `operate_time`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `class_name`       VARCHAR(200)  DEFAULT NULL          COMMENT '操作的类名',
    `method_name`      VARCHAR(100)  DEFAULT NULL          COMMENT '操作的方法名',
    `method_params`    TEXT          DEFAULT NULL          COMMENT '方法参数',
    `return_value`     TEXT          DEFAULT NULL          COMMENT '返回值',
    `cost_time`        BIGINT        DEFAULT NULL          COMMENT '操作耗时（毫秒）',
    PRIMARY KEY (`id`),
    KEY `idx_operate_emp_id` (`operate_emp_id`),
    KEY `idx_operate_time` (`operate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ============================================================
-- 初始化数据
-- ============================================================

-- 部门数据
INSERT INTO `dept` (`name`) VALUES
('学工部'),
('教研部'),
('咨询部'),
('就业部'),
('人事部');

-- 员工数据（密码均为 123456 的加密值，实际请使用项目加密方式替换）
-- 注：以下密码为明文 "123456"，实际项目中使用的是加密后的值
INSERT INTO `emp` (`username`, `password`, `name`, `gender`, `phone`, `job`, `role`, `salary`, `entry_date`, `dept_id`) VALUES
('admin',    '123456', '管理员',    1, '13800001111', 6, 1, 15000, '2020-01-01', 5),
('zhangsan', '123456', '张三',      1, '13800002222', 2, 2, 12000, '2021-03-15', 2),
('lisi',     '123456', '李四',      1, '13800003333', 1, 3, 8000,  '2022-06-01', 1),
('wangwu',   '123456', '王五',      1, '13800004444', 1, 3, 8000,  '2022-06-10', 1),
('zhaoliu',  '123456', '赵六',      2, '13800005555', 3, 3, 9000,  '2021-09-01', 1);

-- 员工工作经历数据
INSERT INTO `emp_expr` (`emp_id`, `begin`, `end`, `company`, `job`) VALUES
(3, '2018-07-01', '2022-05-31', '某科技有限公司', 'Java开发工程师'),
(3, '2015-07-01', '2018-06-30', '某软件公司',     '初级程序员'),
(4, '2019-07-01', '2022-05-31', '某教育集团',     '教务管理');

-- 班级数据
INSERT INTO `clazz` (`name`, `room`, `begin_date`, `end_date`, `master_id`, `subject`) VALUES
('Java开发一班',     '101', '2025-03-01', '2025-09-01', 3, 1),
('Java开发二班',     '102', '2025-04-01', '2025-10-01', 4, 1),
('前端开发班',       '201', '2025-03-15', '2025-08-15', 3, 2),
('UI设计班',         '301', '2025-05-01', '2025-11-01', 4, 3);

-- 学生数据
INSERT INTO `student` (`name`, `no`, `gender`, `phone`, `id_card`, `is_college`, `address`, `degree`, `graduation_date`, `clazz_id`, `violation_count`, `violation_score`) VALUES
('小红',   'S2025001', 2, '13900001111', '320123200001010011', 1, '江苏省南京市', 4, '2023-06-30', 1, 0, 0),
('小明',   'S2025002', 1, '13900002222', '320123200002020022', 1, '江苏省苏州市', 4, '2023-06-30', 1, 1, 5),
('小刚',   'S2025003', 1, '13900003333', '320123200003030033', 0, '江苏省无锡市', 3, '2022-06-30', 2, 0, 0),
('小美',   'S2025004', 2, '13900004444', '320123200004040044', 1, '江苏省常州市', 4, '2024-06-30', 2, 0, 0),
('小华',   'S2025005', 1, '13900005555', '320123200005050055', 1, '江苏省徐州市', 3, '2023-06-30', 3, 0, 0),
('小丽',   'S2025006', 2, '13900006666', '320123200006060066', 0, '江苏省南通市', 2, '2022-06-30', 3, 2, 10);
