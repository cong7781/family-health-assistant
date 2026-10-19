-- ============================================================
--  家庭健康助手 · 数据库初始化脚本
--  MySQL 8.0+
--
--  用法：
--    mysql -u root -p < docs/schema.sql
--  或在 Navicat / DataGrip 里整段执行。
--
--  说明：列名与后端 Mapper 中的 SQL 完全一致（驼峰式），
--        不要自行改成下划线命名，否则 MyBatis 映射会失效。
-- ============================================================

CREATE DATABASE IF NOT EXISTS familyhealthdb
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE familyhealthdb;

-- 先删子表再删主表，避免外键约束报错
DROP TABLE IF EXISTS record;
DROP TABLE IF EXISTS member;

-- ------------------------------------------------------------
-- 家庭成员表
-- ------------------------------------------------------------
CREATE TABLE member (
    id             INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    account        VARCHAR(50)  NOT NULL                COMMENT '登录账号',
    password       VARCHAR(255) NOT NULL                COMMENT '密码哈希（PBKDF2，绝不存明文）',
    name           VARCHAR(50)  NOT NULL                COMMENT '姓名',
    gender         VARCHAR(10)  DEFAULT NULL            COMMENT '性别',
    phone          VARCHAR(20)  DEFAULT NULL            COMMENT '联系电话',
    birthday       DATE         DEFAULT NULL            COMMENT '生日',
    bloodType      VARCHAR(10)  DEFAULT NULL            COMMENT '血型',
    illnessHistory VARCHAR(500) DEFAULT NULL            COMMENT '病史',
    allergy        VARCHAR(500) DEFAULT NULL            COMMENT '过敏史',
    createTime     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_member_account (account)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '家庭成员';

-- ------------------------------------------------------------
-- 健康记录表
-- ------------------------------------------------------------
CREATE TABLE record (
    id          INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    temperature DECIMAL(4, 1) DEFAULT NULL           COMMENT '体温 ℃',
    bpHigh      INT           DEFAULT NULL           COMMENT '收缩压（高压）mmHg',
    bpLow       INT           DEFAULT NULL           COMMENT '舒张压（低压）mmHg',
    bloodOxygen INT           DEFAULT NULL           COMMENT '血氧 %',
    weight      DECIMAL(5, 2) DEFAULT NULL           COMMENT '体重 kg',
    bloodSugar  DECIMAL(4, 1) DEFAULT NULL           COMMENT '血糖 mmol/L',
    remark      VARCHAR(500)  DEFAULT NULL           COMMENT '备注',
    memberid    INT          NOT NULL                COMMENT '所属家庭成员 id',
    createTime  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
    PRIMARY KEY (id),
    KEY idx_record_member (memberid),
    KEY idx_record_time (createTime),
    CONSTRAINT fk_record_member FOREIGN KEY (memberid)
        REFERENCES member (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '健康档案记录';

-- ============================================================
--  说明：演示账号不在这里插入。
--  后端首次启动时（member 表为空）会自动创建：
--      账号 demo  密码 123456
--  实现见 backend/.../commom/DataInitializer.java
--  这样库里不会出现任何明文密码，也方便别人 clone 后直接跑起来。
-- ============================================================
