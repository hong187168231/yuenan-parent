/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server GameType    : MySQL
 Source Server Version : 80013
 Source Host           : localhost:3306
 Source Schema         : live

 Target Server GameType    : MySQL
 Target Server Version : 80013
 File Encoding         : 65001

 Date: 30/03/2020 20:10:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- use live;


-- ----------------------------
-- Table structure for bas_article
-- ----------------------------
DROP TABLE IF EXISTS `bas_article`;
CREATE TABLE `bas_article`  (
  `articleid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图文id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `introduction` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '简介',
  `arttype` decimal(1, 0) NULL DEFAULT NULL COMMENT '图文类型 1 图文 2 短视频',
  `videoid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频id',
  `picids` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片id 多张图片id以 “,” 英文逗号区分',
  `usertags` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户标签 多个标签以“,”分隔',
  `praisenum` bigint(20) NULL DEFAULT NULL COMMENT '点赞量',
  `sharenum` bigint(20) NULL DEFAULT NULL COMMENT '分享量',
  `viewnum` bigint(20) NULL DEFAULT NULL COMMENT '收藏量',
  `seenum` bigint(20) NULL DEFAULT NULL COMMENT '浏览数量',
  `istophome` decimal(1, 0) NULL DEFAULT NULL COMMENT '置顶 上首页 0置顶 9非置顶',
  `ishot` decimal(1, 0) NULL DEFAULT NULL COMMENT '热门精华 热门 0是 9否',
  `sortby` int(11) NULL DEFAULT NULL COMMENT '排序权重',
  `checkstatus` decimal(1, 0) NULL DEFAULT NULL COMMENT '审核状态 1未审核  8审核通过 9审核未通过',
  `checknote` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核说明',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`articleid`) USING BTREE,
  KEY `idx_accno` (`accno`,`arttype`,`is_delete`,`create_time`) USING BTREE,
  KEY `idx_type` (`arttype`,`is_delete`,`create_time`) USING BTREE,
  KEY `idx_accno_checkstatus` (`accno`,`checkstatus`,`is_delete`,`create_time`) USING BTREE,
  KEY `idx_checkstatus` (`checkstatus`,`is_delete`,`create_time`) USING BTREE,
  KEY `idx_delete` (`is_delete`,`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '图文信息表';

-- ----------------------------
-- Table structure for bas_investors
-- ----------------------------
DROP TABLE IF EXISTS `bas_investors`;
CREATE TABLE `bas_investors`  (
  `investorsid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '冠名商家id',
  `investname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家名称',
  `investlog` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家头像',
  `investdesc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家备注',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`investorsid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '冠名商家';

-- ----------------------------
-- Table structure for bas_video
-- ----------------------------
DROP TABLE IF EXISTS `bas_video`;
CREATE TABLE `bas_video`  (
  `videoid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '视频id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `videoname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频名称',
  `videoinfo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频简介',
  `videoimg` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频封面',
  `videourl` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频url',
  `videosize` decimal(6, 2) NULL DEFAULT NULL COMMENT '视频大小 单位M',
  `viewnum` bigint(20) NULL DEFAULT NULL COMMENT '收藏量',
  `praisenum` bigint(20) NULL DEFAULT NULL COMMENT '点赞量',
  `sharenum` bigint(20) NULL DEFAULT NULL COMMENT '分享量',
  `seenum` bigint(20) NULL DEFAULT NULL COMMENT '浏览数量',
  `sortby` int(11) NULL DEFAULT NULL COMMENT '排序权重',
  `checkstatus` decimal(1, 0) NULL DEFAULT NULL COMMENT '审核状态 1未审核  8审核通过 9审核未通过',
  `checknote` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核说明',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`videoid`) USING BTREE,
  INDEX `idx_delete` (`is_delete`,`checkstatus`) USING BTREE,
  INDEX `idx_accno` (`accno`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '点播视频';

-- ----------------------------
-- Table structure for bd_bannerpicinfo
-- ----------------------------
DROP TABLE IF EXISTS `bd_bannerpicinfo`;
CREATE TABLE `bd_bannerpicinfo`  (
  `bannerpicid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '广告详情id',
  `bseatid` bigint(20) NULL DEFAULT NULL COMMENT '位置id',
  `investorsid` bigint(20) NULL DEFAULT NULL COMMENT '冠名商家id',
  `linktype` decimal(1, 0) NULL DEFAULT NULL COMMENT '链接跳转类型   （默认）1 web / 2 直播间 / 3 短视讯详细页 / 4 充值页 /',
  `bndispic` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '广告图片id',
  `bndisptxt` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '广告显示文字',
  `expirydates` datetime(0) NULL DEFAULT NULL COMMENT '有效期起',
  `expirydatee` datetime(0) NULL DEFAULT NULL COMMENT '有效期止',
  `bndlink` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接',
  `specparame` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '特殊参数',
  `sortby` int(11) NULL DEFAULT NULL COMMENT '排序权重',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`bannerpicid`) USING BTREE,
  INDEX `fk_relationship_100`(`bseatid`) USING BTREE,
  INDEX `fk_reference_32`(`investorsid`) USING BTREE
--   CONSTRAINT `fk_reference_32` FOREIGN KEY (`investorsid`) REFERENCES `bas_investors` (`investorsid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
--   CONSTRAINT `fk_relationship_100` FOREIGN KEY (`bseatid`) REFERENCES `bd_bannerseat` (`bseatid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '广告(banner)图片详情';

-- ----------------------------
-- Table structure for bd_bannerseat
-- ----------------------------
DROP TABLE IF EXISTS `bd_bannerseat`;
CREATE TABLE `bd_bannerseat`  (
  `bseatid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '位置id',
  `sitearea` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所在地(国家省市区)',
  `seatname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '位置名称',
  `seatcode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '位置代码,此编码固定，比如apphome标示app主页上banner广告，pchome标示web网站主页上banner广告',
  `seatdesc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '位置说明',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_enable` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否启用（0未启用，1启用）',
  PRIMARY KEY (`bseatid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '广告(banner)位置';

-- ----------------------------
-- Table structure for bd_user
-- ----------------------------
DROP TABLE IF EXISTS `bd_user`;
CREATE TABLE `bd_user`  (
  `bduserid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `bdusername` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `phoneno` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `wechat` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信号 多条以“，”分隔',
  `email` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`bduserid`) USING BTREE,
  KEY `idx_no` (`accno`,`is_delete`,`create_time`),
  KEY `idx_phone` (`phoneno`,`is_delete`,`create_time`),
  KEY `idx_delete` (`is_delete`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台系统用户信息';

-- ----------------------------
-- Table structure for inf_push
-- ----------------------------
DROP TABLE IF EXISTS `inf_push`;
CREATE TABLE `inf_push`  (
  `bdpushid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '推送id',
  `pushtype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '推送类型',
  `pushtitle` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '推送标题',
  `pushbody` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '推送摘要',
  `pushdate` datetime(0) NULL DEFAULT NULL COMMENT '推送时间',
  `sortby` int(11) NULL DEFAULT NULL COMMENT '排序权重',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`bdpushid`) USING BTREE,
  KEY `idx1` (`pushtype`,`is_delete`,`create_time`) USING BTREE,
  KEY `idx2` (`is_delete`,`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'APP系统推送';

-- ----------------------------
-- Table structure for inf_sysnotice
-- ----------------------------
DROP TABLE IF EXISTS `inf_sysnotice`;
CREATE TABLE `inf_sysnotice`  (
  `noticeid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '公告id',
  `type` decimal(1, 0) NULL DEFAULT NULL COMMENT '消息类型 1直播间滚动消息 2弹窗公告 3首页公告 4短视讯公告',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息标题',
  `notebody` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息内容',
  `params` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息参数',
  `expirydates` datetime(0) NULL DEFAULT NULL COMMENT '有效期起',
  `expirydatee` datetime(0) NULL DEFAULT NULL COMMENT '有效期止',
  `sortby` int(11) NULL DEFAULT NULL COMMENT '排序权重',
  `work_status` bit(1) NOT NULL DEFAULT b'1' COMMENT '0 关闭  1 开启',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`noticeid`) USING BTREE,
  KEY `idx1` (`type`,`accno`,`is_delete`,`expirydates`,`expirydatee`) USING BTREE,
  KEY `idx2` (`type`,`is_delete`,`expirydates`,`expirydatee`) USING BTREE,
  KEY `idx3` (`accno`,`is_delete`,`expirydates`,`expirydatee`) USING BTREE,
  KEY `idx4` (`is_delete`,`expirydates`,`expirydatee`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统公告';

-- ----------------------------
-- Table structure for inf_sysremindinfo
-- ----------------------------
DROP TABLE IF EXISTS `inf_sysremindinfo`;
CREATE TABLE `inf_sysremindinfo`  (
  `rmdid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `bdpushid` bigint(20) NULL DEFAULT NULL COMMENT '推送id',
  `sender` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送者accno',
  `rmtype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提醒信息类型：order订单提醒  pay 支付消息，auditvideo视频审核提醒 ,auditimg图文审核提醒 other通用提醒comment评论system系统systemnotice 主播发送微信',
  `recipienter` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收人：accno',
  `uuid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '识别码（群发标识符）',
  `rmpics` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通知图片 多张以“,”分隔',
  `rmtitle` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息标题',
  `senddate` datetime(0) NULL DEFAULT NULL COMMENT '发送时间',
  `remindtxt` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提醒消息',
  `refparm` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关参数id、code',
  `refaddlink` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关链接',
  `rmdateexp` datetime(0) NULL DEFAULT NULL COMMENT '提醒失效时间 一般提醒7天 重要提醒30天',
  `issee` decimal(1, 0) NULL DEFAULT NULL COMMENT '是否查看0已查看 9未查看',
  `istodo` decimal(1, 0) NULL DEFAULT NULL COMMENT '是否处理0已处理 9未处理',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`rmdid`) USING BTREE,
  INDEX `fk_relationship_337`(`bdpushid`) USING BTREE
--   CONSTRAINT `fk_relationship_337` FOREIGN KEY (`bdpushid`) REFERENCES `inf_push` (`bdpushid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统提醒消息';

-- ----------------------------
-- Table structure for mem_bankaccount
-- ----------------------------
DROP TABLE IF EXISTS `mem_bankaccount`;
CREATE TABLE `mem_bankaccount`  (
  `bankaccid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '银行账户id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `familyid` bigint(20) NULL DEFAULT NULL COMMENT '家族id',
  `accounttype` decimal(1, 0) NULL DEFAULT NULL COMMENT '账号类型  1支付宝 2微信   3银联',
  `bankaddress` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户行 如 某某支行',
  `bankname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '银行名称标识符 如ICBC',
  `accountname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '银行/支付宝/微信开户人姓名',
  `accountno` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '银行卡/支付宝/微信账号',
  `accidcardno` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '持卡人身份证号',
  `idcardpic1` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '持卡人身份证正面图',
  `idcardpic2` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '持卡人身份证背面图',
  `checkstatus` decimal(1, 0) NULL DEFAULT NULL COMMENT '审核状态 1未审核  8审核通过 9审核未通过',
  `checknote` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核说明',
  `emailaddress` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱地址',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`bankaccid`) USING BTREE,
  KEY `idx1` (`accno`,`accounttype`,`is_delete`) USING BTREE,
  KEY `idx2` (`familyid`,`is_delete`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '提现账户';

-- ----------------------------
-- Table structure for mem_baseinfo
-- ----------------------------
DROP TABLE IF EXISTS `mem_baseinfo`;
CREATE TABLE `mem_baseinfo`  (
  `memid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `nickname` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '呢称',
  `memname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `mobileno` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `sex` decimal(1, 0) NULL DEFAULT NULL COMMENT '性别 1男 2女 3保密',
  `idcardtype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '证件类型DDINFO',
  `idcardno` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '证件号码',
  `idcardfront` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '证件照片正面',
  `idcardback` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '证件照片反面',
  `birthday` date NULL DEFAULT NULL COMMENT '出生日期',
  `nationality` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '国籍DDINFO',
  `headimg` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `registerdate` datetime(0) NULL DEFAULT NULL COMMENT '注册日期',
  `recomcode` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '推荐码',
  `describes` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录地址城市',
  `clintipadd` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录ip地址' ,
  `logincountry` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录国家',
  `memfeatures` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员特征(兴趣)',
  `memorgin` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源 recommend推荐  regist注册  operator运营人员',
  `fansnum` bigint(20) NULL DEFAULT NULL COMMENT '粉丝数量',
  `goldnum` decimal(16, 3) NULL DEFAULT NULL COMMENT '用户乐币数量',
  `wait_amount` DECIMAL ( 20, 2 ) NOT NULL DEFAULT '0.00' COMMENT '待开奖金额（元）',
  `withdrawal_remainder` DECIMAL ( 20, 2 ) NOT NULL DEFAULT '0.00' COMMENT '可提现余额' ,
  `bet_amount` DECIMAL ( 20, 0 ) NOT NULL DEFAULT '0' COMMENT '累计投注  (元)' ,
  `pay_amount` DECIMAL ( 20, 0 ) NOT NULL DEFAULT '0' COMMENT '累计充值（元）' ,
  `withdrawal_amount` DECIMAL ( 20, 0 ) NOT NULL DEFAULT '0' COMMENT '累计提现（元)' ,
  `no_withdrawal_amount` DECIMAL ( 20, 0 ) NOT NULL DEFAULT '0' COMMENT '不可提现金额(元)' ,
  `chat_status` INT ( 1 ) DEFAULT '1' COMMENT '聊天状态: 0,不允许;1,允许' ,
  `freeze_status` INT ( 1 ) DEFAULT '0' COMMENT '冻结状态: 0,不冻结;1,冻结' ,
  `bet_status` INT ( 1 ) DEFAULT '1' COMMENT '投注状态: 0,不允许;1,允许' ,
  `backwater_status` INT ( 1 ) DEFAULT '1' COMMENT '返水状态: 0,不允许;1,允许' ,
  `share_order_status` INT ( 1 ) DEFAULT '1' COMMENT '晒单状态(圈子使用): 0,不允许;1,允许'  ,
  `logintype` DECIMAL ( 2, 0 ) DEFAULT NULL COMMENT '账户类型  普通会员1      主播2   家族长3   运营后台管理员8    第三方登录7   服务注册中心管理员9  聚合站点后台管理员10' ,
  `openId` VARCHAR ( 128 ) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '第三方登录时返回的唯一标识' ,
  `sitearea` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所在地(省市区)12位区域编码code',
  `wechat` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信号',
  `chatnickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信昵称',
  `forbid_talk_type` int(1) DEFAULT NULL COMMENT '禁言类型 0未禁言1临时2永久',
  `forbid_talk_start` datetime NULL DEFAULT NULL COMMENT '禁言开始时间',
  `forbid_talk_end` datetime NULL DEFAULT NULL COMMENT '禁言结束时间',
  `forbid_in_type` int(1) DEFAULT NULL COMMENT '禁入类型 0未禁入1临时2永久',
  `forbid_in_start` datetime NULL DEFAULT NULL COMMENT '禁入开始时间',
  `forbid_in_end` datetime NULL DEFAULT NULL COMMENT '禁入结束时间',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`memid`) USING BTREE,
  UNIQUE INDEX `i_accno`(`accno`,`is_delete`) USING BTREE,
  UNIQUE INDEX `uk_recomcode`(`recomcode`) USING BTREE,
  KEY `idx_name_mb_d` (`nickname`,`mobileno`,`is_delete`) USING BTREE,
  KEY `idx_mobile_d` (`mobileno`,`is_delete`) USING BTREE,
  KEY `idx_nick_d` (`nickname`,`is_delete`) USING BTREE,
  KEY `idx_delete_free` (`is_delete`,`freeze_status`) USING BTREE,
  KEY `idx_sts_free` (`freeze_status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员基本信息';

-- ----------------------------
-- Table structure for mem_certification
-- ----------------------------
DROP TABLE IF EXISTS `mem_certification`;
CREATE TABLE `mem_certification`  (
  `certid` bigint(20) NOT NULL AUTO_INCREMENT,
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `certstatus` decimal(1, 0) NULL DEFAULT NULL COMMENT '认证状态 0已通过 1待审核 9未通过',
  `certdesc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '认证说明（未通过原因等）',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`certid`) USING BTREE,
  KEY `idx1` (`accno`,`certstatus`,`is_delete`,`create_time`) USING BTREE,
  KEY `idx2` (`accno`,`is_delete`,`create_time`) USING BTREE,
  KEY `idx3` (`is_delete`,`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '主播认证表';

-- ----------------------------
-- Table structure for mem_faceset
-- ----------------------------
DROP TABLE IF EXISTS `mem_faceset`;
CREATE TABLE `mem_faceset`  (
  `faceid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '美颜id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `beauty` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '美颜参数设置 （json）',
  `filter` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '滤镜参数设置（json）',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`faceid`) USING BTREE,
  KEY `idx1` (`accno`,`is_delete`,`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '美颜滤镜设置表';

-- ----------------------------
-- Table structure for mem_family
-- ----------------------------
DROP TABLE IF EXISTS `mem_family`;
CREATE TABLE `mem_family`  (
  `familyid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '家族id',
  `familyname` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '家族名称',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识符',
  `familyman` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '家族长姓名',
  `idcardno` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '证件号码',
  `telephone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '行动电话',
  `memnums` bigint(20) NOT NULL COMMENT '家族主播数量 默认为0',
  `royaltypercent` decimal(16, 2) NULL DEFAULT NULL COMMENT '提成比例',
  `bettingpercentage` decimal(16,3) DEFAULT NULL COMMENT '投注分成比例',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`familyid`) USING BTREE,
  KEY `idx1` (`familyname`,`is_delete`,`create_time`) USING BTREE,
  KEY `idx2` (`accno`,`is_delete`,`create_time`) USING BTREE,
  KEY `idx3` (`is_delete`,`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '家族信息';

-- ----------------------------
-- Table structure for mem_familymem
-- ----------------------------
DROP TABLE IF EXISTS `mem_familymem`;
CREATE TABLE `mem_familymem`  (
  `familymemid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '家族成员id',
  `familyid` bigint(20) NULL DEFAULT NULL COMMENT '家族id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `nickname` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '成员暱称',
  `royaltypercent` decimal(16, 2) NULL DEFAULT NULL COMMENT '提成比例 两位小数',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`familymemid`) USING BTREE,
  INDEX `fk_reference_42`(`familyid`) USING BTREE
--   CONSTRAINT `fk_reference_42` FOREIGN KEY (`familyid`) REFERENCES `mem_family` (`familyid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '家族成员信息';

-- ----------------------------
-- Table structure for mem_follow
-- ----------------------------
DROP TABLE IF EXISTS `mem_follow`;
CREATE TABLE `mem_follow`  (
  `followid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关注id',
  `memid` bigint(20) NULL DEFAULT NULL COMMENT '被关注用户id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`followid`) USING BTREE,
  KEY `fk_reference_37` (`memid`,`is_delete`,`create_time`) USING BTREE,
  KEY `idx2` (`accno`,`is_delete`,`create_time`) USING BTREE
--   CONSTRAINT `fk_reference_37` FOREIGN KEY (`memid`) REFERENCES `mem_baseinfo` (`memid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员关注';

-- ----------------------------
-- Table structure for mem_goldchange
-- ----------------------------
DROP TABLE IF EXISTS `mem_goldchange`;
CREATE TABLE `mem_goldchange`  (
  `goldchangid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '金币变动明细id',
  `refid` bigint(20) NULL DEFAULT NULL COMMENT '相关id 如充值订单id 、 打赏id 、彩票派奖id(ksorderid)  代理结算id',
  `refaccno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '乐币来源主播accno',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `changetype` decimal(2, 0) NULL DEFAULT NULL COMMENT '变动类型 1充值 2打赏 3赠送 4签到奖励 5发帖奖励 6 发视频奖励 7 邀请用户 8充值附赠 9 主播分成 10 提现申请 11提现取消 12已提现 13彩票派奖 14彩票下注 15彩票下注取消 16稽核手续费 17稽核手续费取消\r\n18 棋牌转出 19棋牌转入 20代理结算\r\n',
  `goldnum` decimal(16, 3) NULL DEFAULT NULL COMMENT '充值/提现前播币数',
  `quantity` decimal(16, 3) NULL DEFAULT NULL COMMENT '充值/提现播币数量',
  `amount` decimal(16, 3) NULL DEFAULT NULL COMMENT '金额',
  `recgoldnum` decimal(16, 3) NULL DEFAULT NULL COMMENT '充值/提现后播币数',
  `pre_cgdml` decimal(16, 3) DEFAULT NULL COMMENT '变动前打码量',
  `after_cgdml` decimal(16, 3) DEFAULT NULL COMMENT '变动后打码量',
  `opnote` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作说明',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `snow_sn` bigint(20) DEFAULT '0' COMMENT '雪花排序号',
   `source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '来源：Android | IOS | WEB',
  PRIMARY KEY (`goldchangid`) USING BTREE,
  INDEX `fk_relationship_28`(`refid`,`is_delete`) USING BTREE,
  KEY `idx_accno_type_time` (`accno`,`changetype`,`create_time`) USING BTREE,
  KEY `idx_type_delete` (`changetype`,`is_delete`) USING BTREE,
  KEY `idx_time` (`create_time`) USING BTREE,
  KEY `idx_refaccno` (`refaccno`,`changetype`,`is_delete`,`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员金币变动明细';

-- ----------------------------
-- Table structure for mem_hotsearch
-- ----------------------------
DROP TABLE IF EXISTS `mem_hotsearch`;
CREATE TABLE `mem_hotsearch`  (
  `searchid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '热词id',
  `searchname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '热词名称',
  `searchtype` decimal(1, 0) NULL DEFAULT NULL COMMENT '热词类型: 1图文  2短视频 3 直播',
  `searchnums` bigint(20) NULL DEFAULT NULL COMMENT '搜索次数(可作为展示排序)',
  PRIMARY KEY (`searchid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '搜索热词';

-- ----------------------------
-- Table structure for mem_level
-- ----------------------------
DROP TABLE IF EXISTS `mem_level`;
CREATE TABLE `mem_level`  (
  `levelid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '等级id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `memlevel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前等级',
  `memscore` decimal(16, 2) NULL DEFAULT NULL COMMENT '会员当前积分',
  `nextlevscore` decimal(16, 2) NULL DEFAULT NULL COMMENT '距离下一级所需积分',
  `levellog` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '等级log',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`levelid`,`is_delete`) USING BTREE,
  INDEX `tp_reference_02`(`accno`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员等级';

-- ----------------------------
-- Table structure for mem_login
-- ----------------------------
DROP TABLE IF EXISTS `mem_login`;
CREATE TABLE `mem_login`  (
  `loginid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '登录账号id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员标识号',
  `acclogin` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员账号(登录用)',
  `logintype` decimal(2, 0) NULL DEFAULT NULL COMMENT '账户类型  普通会员1      主播2   家族长3   运营后台管理员8    第三方登录7   服务注册中心管理员9  聚合站点后台管理员10',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登陆密码',
  `passwordmd5` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码MD5',
  `paypassword` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付密码md5',
  `accstatus` decimal(1, 0) NOT NULL COMMENT '账号状态 1正常 9禁止登陆 ',
  `lastlogindate` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `loginnum` int(11) NULL DEFAULT NULL COMMENT '登录总次数',
  `clintipadd` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录ip地址',
  PRIMARY KEY (`loginid`) USING BTREE,
  UNIQUE INDEX `i_loginacc`(`acclogin`, `logintype`) USING BTREE,
  KEY `idx1` (`logintype`,`acclogin`,`accstatus`) USING BTREE,
  KEY `idx2` (`accstatus`) USING BTREE,
  KEY `idx3` (`accno`) USING BTREE,
  KEY `Unique_acclogin` (`acclogin`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员登录账号';

-- ----------------------------
-- Table structure for mem_relationship
-- ----------------------------
DROP TABLE IF EXISTS `mem_relationship`;
CREATE TABLE `mem_relationship`  (
  `relaid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关系id',
  `refaccno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '推荐人标识号',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `memname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`relaid`) USING BTREE,
   INDEX `idx1` (`accno`,`is_delete`) USING BTREE,
   INDEX `idx2` (`refaccno`,`is_delete`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员推荐关系';

-- ----------------------------
-- Table structure for mem_signin
-- ----------------------------
DROP TABLE IF EXISTS `mem_signin`;
CREATE TABLE `mem_signin`  (
  `signinid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '签到id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `signinnum` int(11) NULL DEFAULT NULL COMMENT '连续签到天数',
  `signintime` datetime(0) NULL DEFAULT NULL COMMENT '签到时间',
  PRIMARY KEY (`signinid`) USING BTREE,
  KEY `idx1` (`accno`,`signintime`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员签到记录表';

-- ----------------------------
-- Table structure for org_adminprivilege
-- ----------------------------
DROP TABLE IF EXISTS `org_adminprivilege`;
CREATE TABLE `org_adminprivilege`  (
  `ogadid` bigint(20) NOT NULL COMMENT '子账号id',
  `sfunid` bigint(20) NOT NULL COMMENT '功能id',
  PRIMARY KEY (`ogadid`, `sfunid`) USING BTREE,
  INDEX `fk_reference_33`(`sfunid`) USING BTREE
--   CONSTRAINT `fk_reference_33` FOREIGN KEY (`sfunid`) REFERENCES `sys_functionorg` (`sfunid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '子账号功能权限';

-- ----------------------------
-- Table structure for platform_goldchange
-- ----------------------------
DROP TABLE IF EXISTS `platform_goldchange`;
CREATE TABLE `platform_goldchange`  (
  `pgoldid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '抽成记录id',
  `refid` bigint(20) NULL DEFAULT NULL COMMENT '相关id 如充值订单id 、 打赏id',
  `refaccno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '乐币来源主播accno',
  `scale` decimal(16, 2) NULL DEFAULT NULL COMMENT '提成比例（如0.25）',
  `goldnum` decimal(16, 2) NULL DEFAULT NULL COMMENT '提成获得乐币数',
  `opnote` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作说明',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`pgoldid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '平台抽成记录';

-- ----------------------------
-- Table structure for sys_agentinfo
-- ----------------------------
DROP TABLE IF EXISTS `sys_agentinfo`;
CREATE TABLE `sys_agentinfo`  (
  `agentid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '代理id',
  `agentname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代理名称',
  `minamt` decimal(16, 3) NULL DEFAULT NULL COMMENT '业绩最低标准额(日)',
  `maxamt` decimal(16, 3) NULL DEFAULT NULL COMMENT '业绩最高标准额',
  `commission` decimal(5, 2) NULL DEFAULT NULL COMMENT '返佣比(%)',
  `sortby` int(11) NULL DEFAULT NULL COMMENT '排序权重',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`agentid`) USING BTREE,
  KEY `idx1` (`agentname`,`is_delete`,`create_time`) USING BTREE,
  KEY `idx2` (`agentid`,`is_delete`,`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '推广代理表';


-- ----------------------------
-- Table structure for sys_app_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_info`;
CREATE TABLE `sys_app_info`  (
  `appid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '升级id',
  `appname` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'app名称',
  `version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本',
  `versionname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本名称',
  `appsize` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'app安装包大小（单位M）',
  `versioninfo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本说明',
  `isnew` int(1) NULL DEFAULT 9 COMMENT '是否最新版本 0 是 9 否',
  `appdownurl` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'app下载地址',
  `is_force` int(1) NULL DEFAULT 9 COMMENT '是否强制 0是 9否',
  `app_type` int(1) NULL DEFAULT NULL COMMENT '平台类型 1 安卓 2 IOS',
  `publishedtime` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`appid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'APP版本升级信息';

-- ----------------------------
-- Table structure for sys_appface
-- ----------------------------
DROP TABLE IF EXISTS `sys_appface`;
CREATE TABLE `sys_appface`  (
  `funbuttonid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '按钮id',
  `funbuttonkind` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '按钮类别  发现页上部功能组 funfxtop',
  `funbuttonname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '按钮名称',
  `funbuttonlog` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '按钮图标',
  `funbuttonlink` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '按钮点击跳转链接',
  `fbrefpara` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关参数',
  `browsnum` bigint(20) NULL DEFAULT NULL COMMENT '浏览量',
  `sortby` int(11) NULL DEFAULT NULL COMMENT '排序权重',
  `buttonnote` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`funbuttonid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'APP界面图标按钮（预留）';

-- ----------------------------
-- Table structure for sys_bduserrole
-- ----------------------------
DROP TABLE IF EXISTS `sys_bduserrole`;
CREATE TABLE `sys_bduserrole`  (
  `refurid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户角色关系id',
  `sysroleid` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`refurid`) USING BTREE,
  INDEX `fk_relationship_259`(`sysroleid`) USING BTREE,
  INDEX `fk_relationship_260`(`accno`,`is_delete`) USING BTREE
--   CONSTRAINT `fk_relationship_259` FOREIGN KEY (`sysroleid`) REFERENCES `sys_roleinfo` (`sysroleid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台用户角色关系';

-- ----------------------------
-- Table structure for sys_busparameter
-- ----------------------------
DROP TABLE IF EXISTS `sys_busparameter`;
CREATE TABLE `sys_busparameter`  (
  `busparamid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '业务参数ID',
  `busparamcode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务参数代码',
  `pbusparamcode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务参数父代码',
  `busparamname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务参数名称',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数说明',
  `status` decimal(1, 0) NULL DEFAULT NULL COMMENT '系统参数启用状态0启用9未启用',
  `sortby` int(11) NOT NULL DEFAULT 1 COMMENT '排序权重',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`busparamid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '业务参数';

-- ----------------------------
-- Table structure for sys_cdn
-- ----------------------------
DROP TABLE IF EXISTS `sys_cdn`;
CREATE TABLE `sys_cdn`  (
  `cdnid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'cdnid',
  `cdnstore` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cdn商家 可以没有',
  `domain` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '域名名称',
  `secretkey` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问加密key',
  `domainkind` int(2) NULL DEFAULT NULL COMMENT '域名类型： 0：app域名 1：管理后台域名 2：web域名 3：下载域名 4：幽兰web域名 5幽兰管理后台域名 6：awsS3视频资源 7：awsS3图片资源  8：三分时时彩',
  `region` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '适用区域或国家 如：北方/南方/柬埔寨....',
  `cname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'CDN CNAME',
  `note` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `status` decimal(1, 0) NULL DEFAULT NULL COMMENT '系统参数启用状态0启用9未启用',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`cdnid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'CDN分发配置表';

-- ----------------------------
-- Table structure for sys_countryareacode
-- ----------------------------
DROP TABLE IF EXISTS `sys_countryareacode`;
CREATE TABLE `sys_countryareacode`  (
  `areacode` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地区代码，共15码，编码规则国家代码3位 + 省2位 + 市2位 + 区县2位 + 街道3位 + 社区3位',
  `parareacode` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地区代码，共15码，编码规则国家代码3位 + 省2位 + 市2位 + 区县2位 + 街道3位 + 社区3位',
  `areaname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地区名称',
  `areashotname` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地区简称',
  `areafullname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地区全称',
  `cityvillagekind` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城乡分类100 城镇\r\n110 城区\r\n111 主城区\r\n112 城乡结合区\r\n120 镇区\r\n121 镇中心区\r\n122 镇乡结合区\r\n123 特殊区域\r\n200 乡村\r\n210 乡中心区\r\n220 村庄',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`areacode`) USING BTREE,
  INDEX `FK_Relationship_120`(`parareacode`) USING BTREE
--   CONSTRAINT `sys_countryareacode_ibfk_1` FOREIGN KEY (`parareacode`) REFERENCES `sys_countryareacode` (`areacode`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务地区';

-- ----------------------------
-- Table structure for sys_errorlog
-- ----------------------------
DROP TABLE IF EXISTS `sys_errorlog`;
CREATE TABLE `sys_errorlog`  (
  `errlogid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `systemname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统代码：来源系统代码',
  `modelname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块名称',
  `optcontent` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '日志内容',
  `optuser` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作用户',
  `optip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作客户端ip',
  `level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志等级DDINFO：     正常normal  系统错误error',
  `serverip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前服务器ip',
  `serverstatus` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前服务器负载:cpu%,io%,网络带宽占用情况等',
  `orginfo` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源信息(md5)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`errlogid`) USING BTREE,
  KEY `idx1` (`systemname`,`modelname`,`serverstatus`,`create_time`) USING BTREE,
  KEY `idx2` (`serverstatus`,`create_time`) USING BTREE,
  KEY `idx3` (`systemname`,`modelname`,`optuser`,`level`,`serverstatus`,`create_time`) USING BTREE,
  KEY `idx4` (`systemname`,`level`,`serverstatus`,`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '异常错误日志';

-- ----------------------------
-- Table structure for sys_feedback
-- ----------------------------
DROP TABLE IF EXISTS `sys_feedback`;
CREATE TABLE `sys_feedback`  (
  `feedid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '意见id',
  `feedtype` decimal(1, 0) NULL DEFAULT NULL COMMENT '意见类型 1普通 2财务',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `feedbody` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '意见内容 10-200字描述',
  `feedimgs` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '反馈图片 多张以“,”分隔',
  `status` decimal(1, 0) NULL DEFAULT NULL COMMENT '处理状态 0已处理 9未处理',
  `handlenote` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理结果说明',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`feedid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '意见反馈';

-- ----------------------------
-- Table structure for sys_funcinterface
-- ----------------------------
DROP TABLE IF EXISTS `sys_funcinterface`;
CREATE TABLE `sys_funcinterface`  (
  `itfcid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '接口id',
  `ofsystem` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属系统 live-manage live-app live',
  `itfcname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口名称',
  `itfcurl` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口地址',
  `itfcdesc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口说明',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`itfcid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台系统接口信息';

-- ----------------------------
-- Table structure for sys_functionorg
-- ----------------------------
DROP TABLE IF EXISTS `sys_functionorg`;
CREATE TABLE `sys_functionorg`  (
  `sfunid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '功能id',
  `parsfunid` bigint(20) NULL DEFAULT NULL COMMENT '父功能id',
  `ofsystem` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属系统  live-manage运营管理后台',
  `sfuntype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '功能类别  menu菜单   button按钮   tabTAB',
  `sfunname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '功能名称',
  `sfunstatus` decimal(1, 0) NULL DEFAULT NULL COMMENT '功能状态 0正常   9停用',
  `sfunurl` varchar(56) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '功能url或参数',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`sfunid`) USING BTREE,
  INDEX `fk_relationship_255`(`parsfunid`,`is_delete`) USING BTREE
--   CONSTRAINT `fk_relationship_255` FOREIGN KEY (`parsfunid`) REFERENCES `sys_functionorg` (`sfunid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台系统(运营后台)功能';

-- ----------------------------
-- Table structure for sys_infolog
-- ----------------------------
DROP TABLE IF EXISTS `sys_infolog`;
CREATE TABLE `sys_infolog`  (
  `logid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作用户',
  `systemname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统代码：来源系统代码',
  `modelname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块名称',
  `optcontent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志内容',
  `optip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作客户端ip',
  `serverip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前服务器ip',
  `longitude` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经度',
  `latitude` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `orginfo` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`logid`) USING BTREE,
  KEY `idx1` (`systemname`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登录信息日志';

-- ----------------------------
-- Table structure for sys_liveserver
-- ----------------------------
DROP TABLE IF EXISTS `sys_liveserver`;
CREATE TABLE `sys_liveserver`  (
  `liveid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '直播服务器id',
  `servername` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务器名称',
  `serverurl` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务器地址',
  `weight` int(4) NOT NULL COMMENT '服务器权重',
  `region` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务器所在区域名称\r\n\r\n南方：江苏 安徽 湖北 重庆 四川 西藏 云南 贵州 湖南 江西 广西 广东 福建 浙江 上海 海南（台港澳）\r\n\r\n北方：山东 河南 山西 陕西 甘肃 青海 新疆 河北 天津 北京 内蒙古 辽宁 吉林 黑龙江 宁夏。',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`liveid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '直播服务器节点表';

-- ----------------------------
-- Table structure for sys_operdatalog
-- ----------------------------
DROP TABLE IF EXISTS `sys_operdatalog`;
CREATE TABLE `sys_operdatalog`  (
  `oplogid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作用户',
  `dbname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据库名称',
  `modulename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块名称',
  `refcollname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表或集合名称',
  `optcontent` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志内容',
  `beforedata` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '操作前数据',
  `lastdate` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '操作后数据',
  `operstatus` decimal(1, 0) NULL DEFAULT NULL COMMENT '操作状态0成功 9失败',
  `operdate` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oplogid`) USING BTREE,
  KEY `idx1` (`accno`,`operstatus`,`operdate`) USING BTREE,
  KEY `idx2` (`modulename`,`operstatus`,`operdate`) USING BTREE,
  KEY `idx3` (`operstatus`,`operdate`) USING BTREE,
  KEY `idx4` (`operdate`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '重要数据操作日志：\r\n重要数据定义：修改金币数量，取消订单、';

-- ----------------------------
-- Table structure for sys_parameter
-- ----------------------------
DROP TABLE IF EXISTS `sys_parameter`;
CREATE TABLE `sys_parameter`  (
  `sysparamid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '系统参数id',
  `sysparamcode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '系统参数代码',
  `sysparamname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统参数名称',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数说明',
  `sysparamvalue` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统参数值',
  `status` decimal(1, 0) NULL DEFAULT NULL COMMENT '系统参数启用状态0启用9未启用',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`sysparamid`) USING BTREE,
  INDEX `FK_sysparam_1`(`sysparamcode`,`is_delete`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统参数';

-- ----------------------------
-- Table structure for sys_payaccount
-- ----------------------------
DROP TABLE IF EXISTS `sys_payaccount`;
CREATE TABLE `sys_payaccount`  (
  `bankid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '银行账户id',
  `accounttype` decimal(1, 0) NULL DEFAULT NULL COMMENT '账号类型  1支付宝 2微信   3银联',
  `accountno` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '银行卡/支付宝/微信账号',
  `accountname` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '银行/支付宝/微信开户人姓名',
  `bankname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '银行名称标识符 如ICBC',
  `bankaddress` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户行 如 某某支行',
  `nickname` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信、支付宝暱称',
  `minamt` decimal(16, 3) NULL DEFAULT NULL COMMENT '单笔最低金额',
  `maxamt` decimal(16, 3) NULL DEFAULT NULL COMMENT '单笔入账最高金额',
  `minmemlevel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '启用会员最低等级',
  `maxmemlevel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '启用会员最高等级',
  `stopamt` decimal(16, 3) NULL DEFAULT NULL COMMENT '停用此卡上限金额',
  `easyrecharge` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '快捷充值套餐金额, 多条以\",\"分隔',
  `isinput` decimal(1, 0) NULL DEFAULT NULL COMMENT '是否允许输入金额  0允许 9禁止 ',
  `status` decimal(1, 0) NOT NULL COMMENT '启用状态: 0启用 9停用',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `sys_status` bit(1) DEFAULT b'1' COMMENT '系统启用禁用',
  `total_amount` decimal(16,2) DEFAULT '0.00' COMMENT '累计充值金额',
  PRIMARY KEY (`bankid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '入款账户表';

-- ----------------------------
-- Table structure for sys_payprovider
-- ----------------------------
DROP TABLE IF EXISTS `sys_payprovider`;
CREATE TABLE `sys_payprovider`  (
  `providerid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '支付商id',
  `provider` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付商名称',
  `providercode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户code',
  `paydns` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付域名',
  `backurl` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '返回地址',
  `allowips` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回调白名单  多个ip以逗号分隔',
  `torderurl` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '三方支付订单查询地址',
  `paygateway` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付网关',
  `accountno` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户ID',
  `secretcode` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户秘钥',
  `pubsecret` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户公钥',
  `prisecret` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户私钥',
  `serversecret` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '伺服器公钥',
  `status` decimal(1, 0) NOT NULL COMMENT '启用状态: 0启用 9停用',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`providerid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付商设置';

-- ----------------------------
-- Table structure for sys_payset
-- ----------------------------
DROP TABLE IF EXISTS `sys_payset`;
CREATE TABLE `sys_payset`  (
  `paysetid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '支付设定id',
  `setname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设置名称',
  `settype` decimal(1, 0) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '设定类型 1线上入款设定(微信/支付宝)  2公司入款设定',
  `rechargetype` decimal(1, 0) NULL DEFAULT NULL COMMENT '入款优惠频率 1首次充值优惠 2每次充值优惠',
  `giftrate` decimal(3, 2) NULL DEFAULT NULL COMMENT '额外赠送率     %',
  `maxgift` decimal(16, 3) NULL DEFAULT NULL COMMENT '优惠上限金额',
  `auditper` decimal(3, 2) NULL DEFAULT NULL COMMENT '常态性稽核（百分比）',
  `auditfree` decimal(16, 3) NOT NULL COMMENT '常态性稽核放宽额度（元）',
  `administrative` decimal(3, 2) NULL DEFAULT NULL COMMENT '常态性稽核行政费率（百分值）',
  `freechargenums` int(4) NULL DEFAULT NULL COMMENT '单日出款免手续费次数',
  `servicecharge` decimal(16, 3) NOT NULL COMMENT '单笔出款手续费（元） 0为不要手续费',
  `maxchargeamt` decimal(16, 3) NULL DEFAULT NULL COMMENT '单笔出款上限金额',
  `minchargeamt` decimal(16, 3) NULL DEFAULT NULL COMMENT '单笔出款下限金额',
  `status` decimal(1, 0) NOT NULL COMMENT '系统参数启用状态0启用9未启用',
  `sortby` int(11) UNSIGNED ZEROFILL NOT NULL COMMENT '排序权重',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`paysetid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付设定(提现)';

-- ----------------------------
-- Table structure for sys_reffuncinitfc
-- ----------------------------
DROP TABLE IF EXISTS `sys_reffuncinitfc`;
CREATE TABLE `sys_reffuncinitfc`  (
  `itfcid` bigint(20) NOT NULL COMMENT '接口id',
  `sfunid` bigint(20) NOT NULL COMMENT '功能id',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`itfcid`, `sfunid`) USING BTREE,
  INDEX `fk_relationship_257`(`sfunid`) USING BTREE
--   CONSTRAINT `fk_relationship_256` FOREIGN KEY (`itfcid`) REFERENCES `sys_funcinterface` (`itfcid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
--   CONSTRAINT `fk_relationship_257` FOREIGN KEY (`sfunid`) REFERENCES `sys_functionorg` (`sfunid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '功能接口关系';

-- ----------------------------
-- Table structure for sys_rolefunc
-- ----------------------------
DROP TABLE IF EXISTS `sys_rolefunc`;
CREATE TABLE `sys_rolefunc`  (
  `rolefuncid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色功能id',
  `sysroleid` bigint(20) NOT NULL COMMENT '角色id',
  `sfunid` bigint(20) NOT NULL COMMENT '功能id',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`rolefuncid`, `sysroleid`, `sfunid`) USING BTREE,
  INDEX `fk_relationship_260`(`sysroleid`) USING BTREE,
  INDEX `fk_relationship_261`(`sfunid`) USING BTREE
--   CONSTRAINT `fk_relationship_260` FOREIGN KEY (`sysroleid`) REFERENCES `sys_roleinfo` (`sysroleid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
--   CONSTRAINT `fk_relationship_261` FOREIGN KEY (`sfunid`) REFERENCES `sys_functionorg` (`sfunid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色拥有功能';

-- ----------------------------
-- Table structure for sys_roleinfo
-- ----------------------------
DROP TABLE IF EXISTS `sys_roleinfo`;
CREATE TABLE `sys_roleinfo`  (
  `sysroleid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `sysrolename` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `sysrolestatus` decimal(1, 0) NULL DEFAULT NULL COMMENT '角色状态  0正常  9停用',
  PRIMARY KEY (`sysroleid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台系统角色';

-- ----------------------------
-- Table structure for sys_shortmsg
-- ----------------------------
DROP TABLE IF EXISTS `sys_shortmsg`;
CREATE TABLE `sys_shortmsg`  (
  `shortmsgid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '短信ID',
  `mobileno` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `msgtype` decimal(1, 0) NULL DEFAULT NULL COMMENT '短信类型 1短信登陆 ;2找回密码 ;3注册；4.修改密码 8提醒  9其他普通短信',
  `masdate` datetime(0) NULL DEFAULT NULL COMMENT '短信发送时间',
  `masbody` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '短信内容',
  `msgcode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关编码 对应存储相关类型的编码，如验证码，提醒相关id或订单编号',
  `masstatus` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态 0发送成功 8已使用 9发送失败 ',
  `ipaddress` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送短信ip地址',
  PRIMARY KEY (`shortmsgid`) USING BTREE,
  KEY `idx1` (`mobileno`,`msgtype`,`msgcode`,`masstatus`) USING BTREE,
  KEY `idx2` (`mobileno`,`msgtype`,`masstatus`) USING BTREE,
  KEY `idx3` (`mobileno`,`msgtype`,`masdate`,`masstatus`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '短信收发记录';

-- ----------------------------
-- Table structure for sys_tags
-- ----------------------------
DROP TABLE IF EXISTS `sys_tags`;
CREATE TABLE `sys_tags`  (
  `tagid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '标签id',
  `tagname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签名称',
  `tagtype` decimal(1, 0) NULL DEFAULT NULL COMMENT '标签分类 1系统标签 2图文主题',
  `sortby` int(11) NULL DEFAULT NULL COMMENT '排序权重',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`tagid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '主题、系统标签';

-- ----------------------------
-- Table structure for sys_tenliveserver
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenliveserver`;
CREATE TABLE `sys_tenliveserver` (
  `tliveid` bigint(20) NOT NULL COMMENT '直播服务器id',
  `servername` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '服务器名称',
  `serverurl` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务器地址',
  `weight` int(4) DEFAULT '1' COMMENT '服务器权重',
  `servertype` decimal(1,0) DEFAULT NULL COMMENT '服务类型  1:  推流域名 2: 播放域名',
  `ptliveid` bigint(20) DEFAULT NULL COMMENT '所属推流服务器id',
  `region` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '服务器所在区域名称',
  `primarykey` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '鉴权主key',
  `backupkey` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '鉴权备用key',
  `status` decimal(1,0) DEFAULT NULL COMMENT '启用状态0启用9未启用',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`tliveid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='腾讯云直播服务器节点表';

-- ----------------------------
-- Table structure for sys_threepayset
-- ----------------------------
DROP TABLE IF EXISTS `sys_threepayset`;
CREATE TABLE `sys_threepayset`  (
  `tpaysetid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '三方账户id',
  `providerid` bigint(20) NULL DEFAULT NULL COMMENT '支付商id',
  `tpayname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设定名称 如 支付宝  微信  银联',
  `paytype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付方式  NETBANK 网银转账  WECHAT 微信收款  ALIPAY 支付宝支付',
  `paycode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付方式  wechatangel 微信话费扫码  wechatangelwap  微信话费H5  alipay   支付宝  alipayangel  支付宝话费扫码H5  alipaylst  支付宝转卡',
  `payvalue` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付设置值',
  `minamt` decimal(16, 3) NULL DEFAULT NULL COMMENT '单笔最低金额',
  `maxamt` decimal(16, 3) NULL DEFAULT NULL COMMENT '单笔入账最高金额',
  `minmemlevel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '启用会员最低等级',
  `maxmemlevel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '启用会员最高等级',
  `stopamt` decimal(16, 3) NULL DEFAULT NULL COMMENT '停用次卡上限金额',
  `easyrecharge` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '快捷充值套餐金额, 多条以\",\"分隔',
  `isinput` decimal(1, 0) NULL DEFAULT NULL COMMENT '是否允许输入金额  0允许 9禁止 ',
  `status` decimal(1, 0) NULL DEFAULT NULL COMMENT '启用状态: 0启用 9停用',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`tpaysetid`) USING BTREE,
  INDEX `fk_reference_75`(`providerid`) USING BTREE
--   CONSTRAINT `fk_reference_75` FOREIGN KEY (`providerid`) REFERENCES `sys_payprovider` (`providerid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '三方支付设定表';

-- ----------------------------
-- Table structure for sys_whitelist
-- ----------------------------
DROP TABLE IF EXISTS `sys_whitelist`;
CREATE TABLE `sys_whitelist`  (
  `whiteid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '白名单id',
  `syscode` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统识别码code',
  `sysname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统名称',
  `ipaddress` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `status` decimal(1, 0) NULL DEFAULT NULL COMMENT '启用状态0启用9未启用',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`whiteid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统登录白名单管理';

-- ----------------------------
-- Table structure for tra_agentclearing
-- ----------------------------
DROP TABLE IF EXISTS `tra_agentclearing`;
CREATE TABLE `tra_agentclearing`  (
  `cleanid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '结算id',
  `agentid` bigint(20) NULL DEFAULT NULL COMMENT '代理id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代理会员标识号',
  `cleantype` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算类型 day日结 week周结 month月结',
  `refids` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联订单id 多条以逗号分隔',
  `chargeamt` decimal(16, 3) NULL DEFAULT NULL COMMENT '充值总金额',
  `reverseamt` decimal(16, 3) NULL DEFAULT NULL COMMENT '返点总金额',
  `commission` decimal(3, 2) NULL DEFAULT NULL COMMENT '当前返佣比(%)',
  `buttonnote` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`cleanid`) USING BTREE,
  KEY `fk_reference_77` (`agentid`,`cleantype`,`is_delete`) USING BTREE
--   CONSTRAINT `fk_reference_77` FOREIGN KEY (`agentid`) REFERENCES `sys_agentinfo` (`agentid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代理结算表';

-- ----------------------------
-- Table structure for tra_anchor
-- ----------------------------
DROP TABLE IF EXISTS `tra_anchor`;
CREATE TABLE `tra_anchor`  (
  `traanchorid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主播结算id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主播会员标识号',
  `apycid` bigint(20) NULL DEFAULT NULL COMMENT '提现申请id',
  `anconlineid` bigint(20) NULL DEFAULT NULL COMMENT '主播在线id',
  `familyid` bigint(20) NULL DEFAULT NULL COMMENT '家族id',
  `giftincome` decimal(16, 3) NULL DEFAULT NULL COMMENT '礼物收入',
  `betsincome` decimal(16, 3) NULL DEFAULT NULL COMMENT '投注分成',
  `buttonnote` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`traanchorid`) USING BTREE,
  KEY `fk_reference_78` (`apycid`,`is_delete`,`create_time`) USING BTREE,
  KEY `fk_reference_79` (`anconlineid`,`is_delete`,`create_time`) USING BTREE,
  KEY `fk_reference_80` (`familyid`,`is_delete`,`create_time`) USING BTREE
--   CONSTRAINT `fk_reference_78` FOREIGN KEY (`apycid`) REFERENCES `tra_applycash` (`apycid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
--   CONSTRAINT `fk_reference_79` FOREIGN KEY (`anconlineid`) REFERENCES `bas_anchoronline` (`anconlineid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
--   CONSTRAINT `fk_reference_80` FOREIGN KEY (`familyid`) REFERENCES `mem_family` (`familyid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '主播结算收入明细';

-- ----------------------------
-- Table structure for tra_anchortracking
-- ----------------------------
DROP TABLE IF EXISTS `tra_anchortracking`;
CREATE TABLE `tra_anchortracking`  (
  `antrackingid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '追踪id',
  `anconlineid` bigint(20) NULL DEFAULT NULL COMMENT '主播在线id',
  `orderid` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`antrackingid`) USING BTREE,
  INDEX `fk_reference_81`(`anconlineid`) USING BTREE,
  INDEX `fk_reference_82`(`orderid`) USING BTREE
--   CONSTRAINT `fk_reference_81` FOREIGN KEY (`anconlineid`) REFERENCES `bas_anchoronline` (`anconlineid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
--   CONSTRAINT `fk_reference_82` FOREIGN KEY (`orderid`) REFERENCES `tra_orderinfom` (`orderid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '主播结算追踪表';

-- ----------------------------
-- Table structure for tra_applyaudit
-- ----------------------------
DROP TABLE IF EXISTS `tra_applyaudit`;
CREATE TABLE `tra_applyaudit`  (
  `apauditid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '提现稽核对账表',
  `apycid` bigint(20) NULL DEFAULT NULL COMMENT '提现申请id',
  `orderid` bigint(20) NULL DEFAULT NULL COMMENT '充值订单id',
  `paysetid` bigint(20) NULL DEFAULT NULL COMMENT '支付设定id',
  `auditamt` decimal(16, 3) NULL DEFAULT NULL COMMENT '常态性稽核费（元）',
  `codesize` decimal(16, 3) NULL DEFAULT NULL COMMENT '打码量',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`apauditid`) USING BTREE,
  INDEX `fk_reference_61`(`apycid`) USING BTREE,
  INDEX `fk_reference_62`(`orderid`) USING BTREE,
  INDEX `fk_reference_74`(`paysetid`) USING BTREE
--   CONSTRAINT `fk_reference_61` FOREIGN KEY (`apycid`) REFERENCES `tra_applycash` (`apycid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
--   CONSTRAINT `fk_reference_62` FOREIGN KEY (`orderid`) REFERENCES `tra_orderinfom` (`orderid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
--   CONSTRAINT `fk_reference_74` FOREIGN KEY (`paysetid`) REFERENCES `sys_payset` (`paysetid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '提现稽核对账表';

-- ----------------------------
-- Table structure for tra_applycash
-- ----------------------------
DROP TABLE IF EXISTS `tra_applycash`;
CREATE TABLE `tra_applycash`  (
  `apycid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '提现申请id',
  `bankaccid` bigint(20) NULL DEFAULT NULL COMMENT '银行账户id',
  `orderid` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `apycdate` datetime(0) NULL DEFAULT NULL COMMENT '申请时间',
  `apycgold` decimal(16, 2) NULL DEFAULT NULL COMMENT '申请总金额',
  `apycamt` decimal(16, 2) NULL DEFAULT NULL COMMENT '打款金额',
  `apycstatus` decimal(1, 0) NULL DEFAULT NULL COMMENT '申请状态  1提交申请  2提现处理中  4已打款    8已到账  9已取消',
  `paymemname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打款人',
  `paydate` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `damaliang` decimal(16,2) DEFAULT '0.00' COMMENT '打码量',
  `xingzhengfei` decimal(16,2) DEFAULT '0.00' COMMENT '行政费',
  `bet_amount` decimal(16,2) DEFAULT '0.00' COMMENT '投注金额充值金额',
  `no_withdrawal_amount` decimal(16,2) DEFAULT '0.00' COMMENT '当前所需打码量',
  PRIMARY KEY (`apycid`) USING BTREE,
  INDEX `fk_reference_25`(`bankaccid`) USING BTREE,
  INDEX `fk_reference_44`(`orderid`) USING BTREE
--   CONSTRAINT `fk_reference_25` FOREIGN KEY (`bankaccid`) REFERENCES `mem_bankaccount` (`bankaccid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
--   CONSTRAINT `fk_reference_44` FOREIGN KEY (`orderid`) REFERENCES `tra_orderinfom` (`orderid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '提现申请';

-- ----------------------------
-- Table structure for tra_orderinfom
-- ----------------------------
DROP TABLE IF EXISTS `tra_orderinfom`;
CREATE TABLE `tra_orderinfom`  (
  `orderid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `mealid` bigint(20) NULL DEFAULT NULL COMMENT '充值套餐id',
  `bankid` bigint(20) NULL DEFAULT NULL COMMENT '入款账户id',
  `roomid` bigint(20) NULL DEFAULT NULL COMMENT '主播房间id',
  `lotkindid` bigint(20) NULL DEFAULT NULL COMMENT '彩种id',
  `sschistoryid` bigint(20) NULL DEFAULT NULL COMMENT '时时彩开奖id',
  `oddsid` bigint(20) NULL DEFAULT NULL COMMENT '投注项id',
  `chekindid` bigint(32) NULL DEFAULT NULL COMMENT '棋牌分类id',
  `tpaysetid` bigint(32) NULL DEFAULT NULL COMMENT '三方支付id',
  `ordertype` decimal(2, 0) NULL DEFAULT NULL COMMENT '订单类型 1在线支付 2线下支付 3在线提现 4线下提现 5彩票购彩 6彩票兑奖 7棋牌上分 8棋牌下分 9其他收入(发帖/推荐)  10其他支出(打赏)  11代理结算收入\r\n',
  `orderno` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号4码年+2码月+8码流水号，生成逻辑：取当前年月，然后从数据库中取当前年月最大订单号，然后将后面8位流水号+1',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `paycode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付标示第三方支付标示号，如支付宝的订单号或微信的prepay_id等',
  `orderdate` datetime(0) NULL DEFAULT NULL COMMENT '订单日期',
  `expiredate` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `paytype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式  JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付   NETBANK 网银转账  WECHAT 微信收款  ALIPAY 支付宝支付',
  `oldamt` decimal(16, 2) NULL DEFAULT NULL COMMENT '订单原价',
  `sumamt` decimal(16, 2) NULL DEFAULT NULL COMMENT '订单总金额',
  `realamt` decimal(16, 2) NULL DEFAULT NULL COMMENT '实付金额',
  `isinvoice` decimal(1, 0) NULL DEFAULT NULL COMMENT '是否开具发票0是 9否',
  `orderstatus` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单状态 ord01新订单 ord04待付款 ord05提现申请 ord06提现取消 ord07提现处理中 ord08已付款  ord09用户取消 ord10已评价  ord11已退款 ord12已提现  ord98已拉取棋牌订单 ord99已过期   ',
  `accountstatus` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算状态 acc04待结算（未打码）  acc08已结算（已打码）  acc99已取消（不可结算）',
  `cancelreason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '取消订单原因',
  `payimg` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付凭证截图 多张以“，”分隔',
  `paywechat` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付处理微信号',
  `paydate` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `payuser` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '汇款姓名',
  `paynote` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '汇款备注',
  `ordernote` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   `source` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '来源 Android | IOS | WEB',
  PRIMARY KEY (`orderid`) USING BTREE,
  INDEX `fk_reference_43`(`mealid`) USING BTREE,
  INDEX `fk_reference_44`(`bankid`) USING BTREE,
  INDEX `fk_reference_59`(`roomid`) USING BTREE,
  INDEX `fk_reference_52`(`lotkindid`) USING BTREE,
  INDEX `fk_reference_58`(`oddsid`) USING BTREE,
  INDEX `fk_reference_64`(`chekindid`) USING BTREE,
  INDEX `fk_reference_76`(`tpaysetid`) USING BTREE,
  INDEX `tp_reference_01`(`ordertype`) USING BTREE,
  INDEX `tp_reference_02` (`orderstatus`,`accountstatus`,`is_delete`) USING BTREE,
  INDEX `tp_reference_03`(`accno`) USING BTREE,
  INDEX `tp_reference_04`(`orderno`) USING BTREE
--   CONSTRAINT `fk_reference_43` FOREIGN KEY (`mealid`) REFERENCES `tra_rechargemeal` (`mealid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
--   CONSTRAINT `tra_orderinfom_ibfk_1` FOREIGN KEY (`bankid`) REFERENCES `sys_payaccount` (`bankid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单信息主';

-- ----------------------------
-- Table structure for tra_ordertracking
-- ----------------------------
DROP TABLE IF EXISTS `tra_ordertracking`;
CREATE TABLE `tra_ordertracking`  (
  `trackid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '追踪id',
  `orderid` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `trackdate` datetime(0) NULL DEFAULT NULL COMMENT '处理时间',
  `trackbody` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理内容',
  `operuse` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `orderstatus` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单状态 ord01新订单 ord04已审核待付款  ord08已付款待评价 ord12已发货 ord16已完成（已收货） ord10已评价 ord99已取消  ord07退款中  ord11已退款  ',
  PRIMARY KEY (`trackid`) USING BTREE,
  INDEX `fk_relationship_37`(`orderid`) USING BTREE
--   CONSTRAINT `fk_relationship_37` FOREIGN KEY (`orderid`) REFERENCES `tra_orderinfom` (`orderid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单追踪';

-- ----------------------------
-- Table structure for tra_paymentinfo
-- ----------------------------
DROP TABLE IF EXISTS `tra_paymentinfo`;
CREATE TABLE `tra_paymentinfo`  (
  `payid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '支付id',
  `orderid` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `paycode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付标示第三方支付标示号，如支付宝的订单号或微信的prepay_id等',
  `paydate` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `paydatee` datetime(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `serialno` bigint(20) NULL DEFAULT NULL COMMENT '流水号：8位年月日+8位数字，如2016052800001200',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识号',
  `orderno` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号4码年+2码月+8码流水号，生成逻辑：取当前年月，然后从数据库中取当前年月最大订单号，然后将后面8位流水号+1',
  `paykind` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付类别 weixin微信支付 alipay支付宝支付',
  `paytype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式  JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付   NETBANK 网银转账',
  `tradingno` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易标识号',
  `payamt` decimal(16, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `payscore` int(11) NULL DEFAULT NULL COMMENT '支付积分',
  `paystatus` decimal(1, 0) NULL DEFAULT NULL COMMENT '支付状态0支付成功/退款成功   1支付中/退款中     9支付失败/退款失败',
  `payerrdesc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付错误描述 不用格式自己定义，如微信支付可以存错误代码$$错误描述',
  `systemname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统代码：来源系统代码',
  `paycodeurl` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付标示二维码(页面)',
  `paynote` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注 退款时把原支付id写到这里',
  `payrefundtype` decimal(1, 0) NULL DEFAULT NULL COMMENT '支付退款类型  0或null 支付  9 退款',
  `refundnote` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退款说明',
  `refundcode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退款标示号 uuid',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`payid`) USING BTREE,
  INDEX `fk_relationship_26`(`orderid`) USING BTREE
--   CONSTRAINT `fk_relationship_26` FOREIGN KEY (`orderid`) REFERENCES `tra_orderinfom` (`orderid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付信息';

-- ----------------------------
-- Table structure for tra_rechargeaudit
-- ----------------------------
DROP TABLE IF EXISTS `tra_rechargeaudit`;
CREATE TABLE `tra_rechargeaudit`  (
  `recauditid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '充值对账id',
  `orderid` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `paysetid` bigint(20) NULL DEFAULT NULL COMMENT '支付设定id',
  `giftamt` decimal(16, 3) NULL DEFAULT NULL COMMENT '优惠赠送金额',
  `buttonnote` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`recauditid`) USING BTREE,
  INDEX `fk_reference_87`(`orderid`) USING BTREE,
  INDEX `fk_reference_88`(`paysetid`) USING BTREE
--   CONSTRAINT `fk_reference_87` FOREIGN KEY (`orderid`) REFERENCES `tra_orderinfom` (`orderid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
--   CONSTRAINT `fk_reference_88` FOREIGN KEY (`paysetid`) REFERENCES `sys_payset` (`paysetid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '入款对账表';

-- ----------------------------
-- Table structure for tra_rechargemeal
-- ----------------------------
DROP TABLE IF EXISTS `tra_rechargemeal`;
CREATE TABLE `tra_rechargemeal`  (
  `mealid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '充值套餐id',
  `realamt` decimal(16, 2) NULL DEFAULT NULL COMMENT '充值金额',
  `rechargegold` decimal(16, 2) NULL DEFAULT NULL COMMENT '充值播币数',
  `givegold` decimal(16, 2) NULL DEFAULT NULL COMMENT '赠送播币数',
  `givepercent` decimal(16, 2) NULL DEFAULT NULL COMMENT '赠送率  两位小数',
  `expirydates` datetime(0) NULL DEFAULT NULL COMMENT '有效期起',
  `expirydatee` datetime(0) NULL DEFAULT NULL COMMENT '有效期止',
  `mealnote` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `sortby` int(11) NULL DEFAULT NULL COMMENT '排序权重 默认为0  数字越大排序越靠前',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`mealid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '充值套餐';


-- 新增cpt相关表
-- ----------------------------
-- Table structure for ae_bet_order
-- ----------------------------
DROP TABLE IF EXISTS `cpt_open_member`;
CREATE TABLE `cpt_open_member` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT 'CPT用户uid',
  `username` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '第三方登入账号(随机生成)',
  `password` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '第三方登入密码(随机生成)',
  `balance` decimal(10,2) DEFAULT NULL COMMENT '第三方余额（元）',
  `type` varchar(10) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '账号类型()',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `login_time` datetime DEFAULT NULL COMMENT '登入时间',
  `layer_no` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户分层编号（代理扩展）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx01` (`user_id`,`type`) USING BTREE,
  KEY `idx02` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;



DROP TABLE IF EXISTS `ae_bet_order`;
CREATE TABLE `ae_bet_order`  (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '注单号',
  `uname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '玩家账号',
  `game_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '游戏ID',
  `battle_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '游戏局号',
  `room_id` int(12) DEFAULT NULL COMMENT '房间ID',
  `bet` decimal(20, 2) DEFAULT NULL COMMENT '有效下注',
  `allbet` decimal(20, 2) DEFAULT NULL COMMENT '总下注',
  `profit` decimal(20, 2) DEFAULT NULL COMMENT '输赢',
  `revenue` decimal(20, 2) DEFAULT NULL COMMENT '抽水',
  `stime` datetime(0) DEFAULT NULL COMMENT '游戏开始时间戳',
  `etime` datetime(0) DEFAULT NULL COMMENT '游戏结束时间戳',
  `playernum` int(12) DEFAULT NULL COMMENT '同局玩家人数',
  `xiqian` decimal(20, 2) DEFAULT NULL COMMENT '洗钱(炸金花拿到豹子时奖励)',
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '游戏结果',
  `chair_id` int(12) DEFAULT NULL COMMENT '玩家座位',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_handle` int(2) DEFAULT 0 COMMENT '打码量：0未处理，1已处理',
  `game_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '游戏名称',
  `room_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '房间名称',
  `user_id` int(12) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_game_id`(`game_id`) USING BTREE,
  INDEX `index_room_id`(`room_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for ae_game
-- ----------------------------
DROP TABLE IF EXISTS `ae_game`;
CREATE TABLE `ae_game`  (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `game_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '游戏ID',
  `game_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '游戏名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_game`(`game_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for ae_room
-- ----------------------------
DROP TABLE IF EXISTS `ae_room`;
CREATE TABLE `ae_room`  (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `room_id` int(12) DEFAULT NULL COMMENT '房间ID',
  `room_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '房间名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_room`(`room_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for ag_bet_order
-- ----------------------------
DROP TABLE IF EXISTS `ag_bet_order`;
CREATE TABLE `ag_bet_order`  (
  `id` int(1) NOT NULL AUTO_INCREMENT,
  `data_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数据类型：BR下注记录，EBR电子游戏下注记录，TR户口转账记录，GR游戏结果，LBR彩票下注记录，LGR彩票结果',
  `bill_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'billNo: 注单流水号(满足平台的唯一\r\n约束条件)',
  `player_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '玩家账号',
  `agent_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '代理商编号',
  `game_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '游戏局号 \r\n[AG 体育]AG 体育的副单号',
  `net_amount` decimal(10, 2) DEFAULT NULL COMMENT '玩家输赢额度',
  `bet_time` datetime(0) DEFAULT NULL COMMENT '投注时间',
  `game_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '游戏类型',
  `bet_amount` decimal(10, 2) DEFAULT NULL COMMENT '投注金额',
  `valid_bet_amount` decimal(10, 2) DEFAULT NULL COMMENT '有效投注额度',
  `flag` int(1) DEFAULT NULL COMMENT '结算状态',
  `play_type` int(1) DEFAULT NULL COMMENT '游戏玩法',
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '货币类型',
  `table_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '桌子编号',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT ' 玩家 IP',
  `recalcu_time` datetime(0) DEFAULT NULL COMMENT '注单重新派彩时间',
  `platform_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台编号(通常为空)',
  `platform_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台类型',
  `stringex` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品附注(通常为空)',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '额外信息\r\nBBIN 平台的数据类型 \r\n1=BB 体育\r\n3=视讯\r\n5=机率\r\n12=彩票\r\n15=3D 厅\r\n30=BB 捕鱼机\r\n99=小费\r\n备注: 如 AGIN 平台 ',
  `round` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台内的大厅类型',
  `result` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '结果',
  `before_credit` decimal(10, 2) DEFAULT NULL COMMENT '玩家下注前的剩余额\r\n度',
  `device_type` int(1) DEFAULT NULL COMMENT '设备类型',
  `bet_amount_bonus` decimal(10, 2) DEFAULT NULL COMMENT ' Jackpot 下注额度',
  `net_amount_bonus` decimal(10, 2) DEFAULT NULL COMMENT 'Jackpot 派彩',
  `game_category` int(1) DEFAULT NULL COMMENT '1 为电子桌面游戏\r\n0 为非电子桌面游戏\r\n备注:适合用 XIN / BG/ ENDO / PT / MG / \r\nPNG',
  `cancel_reason` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '取消原因\r\nAG 体育注单取消的原因,长度为 120\r\n字符',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_handle` int(1) NOT NULL DEFAULT 0 COMMENT '打码量：0未处理，1已处理',
  `game_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '游戏名称',
  `pay_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '玩法名称',
  `platform_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '平台名称',
  `round_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '平台内的大厅名称',
  `user_id` int(12) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `bill_no_unique`(`bill_no`) USING BTREE,
  INDEX `idx01`(`player_name`, `create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = 'AG游戏下注记录';

-- ----------------------------
-- Table structure for ag_game
-- ----------------------------
DROP TABLE IF EXISTS `ag_game`;
CREATE TABLE `ag_game`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `game_type` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '游戏类型',
  `game_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '游戏名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_game`(`game_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for ag_pay_type
-- ----------------------------
DROP TABLE IF EXISTS `ag_pay_type`;
CREATE TABLE `ag_pay_type`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `pay_type` int(20) DEFAULT NULL COMMENT '玩法',
  `pay_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '玩法名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_pay`(`pay_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'AG玩法';

-- ----------------------------
-- Table structure for ag_platform
-- ----------------------------
DROP TABLE IF EXISTS `ag_platform`;
CREATE TABLE `ag_platform`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `platform_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台类型',
  `platform_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_platfrom`(`platform_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'ag平台名称';

-- ----------------------------
-- Table structure for ag_round
-- ----------------------------
DROP TABLE IF EXISTS `ag_round`;
CREATE TABLE `ag_round`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `round` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台内的大厅类型',
  `round_name` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '平台内的大厅类型名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_round`(`round`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for ag_transfer_log
-- ----------------------------
DROP TABLE IF EXISTS `ag_transfer_log`;
CREATE TABLE `ag_transfer_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数据类型',
  `project_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '项目编号',
  `agent_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '代理商编号',
  `transfer_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '转账编号',
  `trade_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '交易编号',
  `platform_id` int(11) DEFAULT NULL COMMENT '平台编号(通常为空)',
  `platform_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台类型',
  `player_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '玩家账户',
  `transfer_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '转账类别',
  `transfer_amount` decimal(10, 2) DEFAULT NULL COMMENT '转账额度 \r\n当 transferType=\"DONATEFEE\",类型\r\n时。transferAmount 一定会是负值,不\r\n页 18 / 53 Copyright©AsiaGaming\r\n会为 0, 表示是支出的小费. 请参考”1. \r\nXml 解析说明',
  `previous_amount` decimal(10, 2) DEFAULT NULL COMMENT '转账前额度',
  `current_amount` decimal(10, 2) DEFAULT NULL COMMENT '当前额度',
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '货币类型',
  `exchange_rate` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '汇率',
  `ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '玩家 IP',
  `flag` int(1) DEFAULT NULL COMMENT '转账状态',
  `creation_time` datetime(0) DEFAULT NULL COMMENT '纪录时间',
  `game_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '游戏局号(通常为空)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = 'AG户口转账记录';

-- ----------------------------
-- Table structure for app_personal_setting
-- ----------------------------
DROP TABLE IF EXISTS `app_personal_setting`;
CREATE TABLE `app_personal_setting`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `setting_id` int(11) NOT NULL COMMENT 'app_setting_type表id',
  `on_off` int(11) DEFAULT 1 COMMENT '开关：1打开0关闭',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_p_s`(`user_id`, `setting_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = 'app个人设置';

-- ----------------------------
-- Table structure for app_setting_type
-- ----------------------------
DROP TABLE IF EXISTS `app_setting_type`;
CREATE TABLE `app_setting_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT 0 COMMENT '父id(存在子设置时用)',
  `classify` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '归类',
  `tag` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '标识',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `sort` int(11) NOT NULL DEFAULT 1 COMMENT '排序',
  `operater` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '操作管理员',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = 'app个人设置类型';

-- ----------------------------
-- Table structure for bet_restrict
-- ----------------------------
DROP TABLE IF EXISTS `bet_restrict`;
CREATE TABLE `bet_restrict`  (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `lottery_id` int(12) DEFAULT NULL COMMENT '彩种ID',
  `play_tag_id` int(12) DEFAULT NULL COMMENT '玩法tagID',
  `max_money` decimal(20, 2) DEFAULT NULL COMMENT '限制额度',
  `crate_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Index_play_lottery`(`lottery_id`, `play_tag_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '投注限制表';

-- ----------------------------
-- Table structure for lottery
-- ----------------------------
DROP TABLE IF EXISTS `lottery`;
CREATE TABLE `lottery`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '彩票名称',
  `icon` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '彩票图标',
  `category_id` int(11) DEFAULT NULL COMMENT '彩票分类id',
  `parent_id` int(11) DEFAULT NULL COMMENT '开奖号码源彩种',
  `startlotto_table` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码表',
  `startlotto_times` int(6) DEFAULT NULL COMMENT '每天/年开奖期数',
  `clearing_tag` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '清算标识',
  `cache_tag` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '缓存标识',
  `max_odds` double(10, 2) NOT NULL DEFAULT 0.00 COMMENT '最大赔率',
  `min_odds` double(10, 2) NOT NULL DEFAULT 0.00 COMMENT '最小赔率',
  `sort` int(6) DEFAULT NULL COMMENT '排序',
  `push_source` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '推送号码来源',
  `push_status` int(1) DEFAULT 0 COMMENT '推送状态;0不推送;1,推送',
  `video_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖视频链接',
  `video_start` int(2) DEFAULT NULL COMMENT '视频打开方式',
  `is_work` int(1) DEFAULT 1 COMMENT '是否开售',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `lottery_id` int(11) DEFAULT NULL COMMENT '彩种编号',
  `end_time` int(11) DEFAULT NULL COMMENT '封盘时间-app端使用',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `CategoryId`(`category_id`) USING BTREE,
  INDEX `ParentId`(`parent_id`) USING BTREE,
  INDEX `idx_lottery_id`(`lottery_id`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE,
  INDEX `idx_is_work`(`is_work`) USING BTREE,
  INDEX `idx_is_delete`(`is_delete`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '彩种';

-- ----------------------------
-- Table structure for lottery_category
-- ----------------------------
DROP TABLE IF EXISTS `lottery_category`;
CREATE TABLE `lottery_category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `alias` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '别名',
  `level` int(11) NOT NULL COMMENT '玩法级别数',
  `is_work` int(1) DEFAULT 1 COMMENT '是否开售',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `category_id` int(11) DEFAULT NULL COMMENT '大类ID',
  `sort` int(6) DEFAULT NULL COMMENT '彩种分类排序',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '彩种类别：LOTTERY(彩票类); QIPAI(棋牌类); ZRSX(真人视讯类); ZUCAI(足彩类);',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE,
  INDEX `idx_is_work`(`is_work`) USING BTREE,
  INDEX `idx_is_delete`(`is_delete`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '彩种分类';

-- ----------------------------
-- Table structure for lottery_favorite
-- ----------------------------
DROP TABLE IF EXISTS `lottery_favorite`;
CREATE TABLE `lottery_favorite`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `lottery_id` int(11) NOT NULL COMMENT '彩票id',
  `update_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_index`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '我的彩票收藏';

-- ----------------------------
-- Table structure for lottery_play
-- ----------------------------
DROP TABLE IF EXISTS `lottery_play`;
CREATE TABLE `lottery_play`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '玩法名称',
  `category_id` int(11) NOT NULL COMMENT '彩种分类id',
  `parent_id` int(11) NOT NULL COMMENT '父级id',
  `sort` int(11) NOT NULL DEFAULT 1 COMMENT '排序',
  `level` int(11) NOT NULL COMMENT '层级',
  `section` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '取值区间',
  `tree` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '玩法节点',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `lottery_id` int(11) DEFAULT NULL COMMENT '彩种ID',
  `play_tag_id` int(11) DEFAULT NULL COMMENT '玩法规则Tag编号',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `idx_lottery_id`(`lottery_id`) USING BTREE,
  INDEX `idx_play_tag_id`(`play_tag_id`) USING BTREE,
  INDEX `idx_is_delete`(`is_delete`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '彩种玩法';

-- ----------------------------
-- Table structure for lottery_play_odds
-- ----------------------------
DROP TABLE IF EXISTS `lottery_play_odds`;
CREATE TABLE `lottery_play_odds`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `setting_id` int(11) NOT NULL COMMENT '玩法id',
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '名称',
  `total_count` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '总柱数',
  `win_count` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '中奖柱数',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `easy_import_flag` int(10) DEFAULT NULL COMMENT 'Excel导入标识(新增或更新）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE,
  INDEX `idx_setting_id`(`setting_id`) USING BTREE,
  INDEX `idx_is_delete`(`is_delete`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '赔率配置表';

-- ----------------------------
-- Table structure for lottery_play_setting
-- ----------------------------
DROP TABLE IF EXISTS `lottery_play_setting`;
CREATE TABLE `lottery_play_setting`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cate_id` int(11) NOT NULL COMMENT '分类id',
  `play_id` int(11) NOT NULL COMMENT '玩法id',
  `total_count` int(11) DEFAULT NULL COMMENT '总注数',
  `win_count` int(11) DEFAULT NULL COMMENT '中奖注数',
  `single_money` double NOT NULL DEFAULT 0.5 COMMENT '单注金额',
  `example` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '投注示例',
  `example_num` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '示例号码',
  `play_remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '玩法说明',
  `play_remark_sx` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '玩法简要说明',
  `reward` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '奖级',
  `matchtype` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '1' COMMENT '匹配规则',
  `win_count_bak` int(11) DEFAULT NULL COMMENT '总注数(后端)',
  `total_count_bak` int(11) DEFAULT NULL COMMENT '中奖注数(后端)',
  `reward_level` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '中奖等级',
  `play_tag_id` int(11) DEFAULT NULL COMMENT '玩法规则Tag编号',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_cate_id`(`cate_id`) USING BTREE,
  INDEX `idx_play_id`(`play_id`) USING BTREE,
  INDEX `idx_play_tag_id`(`play_tag_id`) USING BTREE,
  INDEX `idx_is_delete`(`is_delete`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin;


-- ----------------------------
-- Table structure for member_device_calc
-- ----------------------------
DROP TABLE IF EXISTS `member_device_calc`;
CREATE TABLE `member_device_calc`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `total_count` int(11) NOT NULL DEFAULT 0 COMMENT '总人数',
  `android_count` int(11) NOT NULL DEFAULT 0 COMMENT '安卓在线人数',
  `ios_count` int(11) NOT NULL DEFAULT 0 COMMENT 'ios人数',
  `h5_count` int(11) NOT NULL DEFAULT 0 COMMENT 'h5人数',
  `web_count` int(11) NOT NULL DEFAULT 0 COMMENT 'pc人数',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '在线设备统计(包括游客)';

-- ----------------------------
-- Table structure for member_online_calc
-- ----------------------------
DROP TABLE IF EXISTS `member_online_calc`;
CREATE TABLE `member_online_calc`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `total_count` int(11) NOT NULL DEFAULT 0 COMMENT '总人数',
  `android_count` int(11) NOT NULL DEFAULT 0 COMMENT '安卓在线人数',
  `ios_count` int(11) NOT NULL DEFAULT 0 COMMENT 'ios人数',
  `h5_count` int(11) NOT NULL DEFAULT 0 COMMENT 'h5人数',
  `web_count` int(11) NOT NULL DEFAULT 0 COMMENT 'pc人数',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '在线人数统计';

-- ----------------------------
-- Table structure for member_youke_calc
-- ----------------------------
DROP TABLE IF EXISTS `member_youke_calc`;
CREATE TABLE `member_youke_calc`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `total_count` int(11) NOT NULL DEFAULT 0 COMMENT '总人数',
  `android_count` int(11) NOT NULL DEFAULT 0 COMMENT '安卓在线人数',
  `ios_count` int(11) NOT NULL DEFAULT 0 COMMENT 'ios人数',
  `h5_count` int(11) NOT NULL DEFAULT 0 COMMENT 'h5人数',
  `web_count` int(11) NOT NULL DEFAULT 0 COMMENT 'pc人数',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '在线人数统计(包括游客)';

-- ----------------------------
-- Table structure for order_append_record
-- ----------------------------
DROP TABLE IF EXISTS `order_append_record`;
CREATE TABLE `order_append_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `lottery_id` int(11) NOT NULL COMMENT '彩种id',
  `play_id` int(11) NOT NULL COMMENT '玩法id',
  `setting_id` int(11) NOT NULL COMMENT '配置id',
  `first_issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '第一期期号',
  `bet_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '投注号码',
  `bet_count` int(11) NOT NULL COMMENT '投注注数',
  `bet_price` decimal(10, 2) NOT NULL COMMENT '单注金额',
  `win_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '累计中奖金额',
  `win_count` int(11) NOT NULL DEFAULT 0 COMMENT '累计中奖注数',
  `bet_multiples` double(10, 2) NOT NULL DEFAULT 2.00 COMMENT '投注倍数',
  `double_multiples` double(10, 2) NOT NULL DEFAULT 1.00 COMMENT '翻倍倍数',
  `append_count` int(11) NOT NULL COMMENT '追号期数',
  `appended_count` int(11) NOT NULL DEFAULT 0 COMMENT '已追期数',
  `type` int(11) NOT NULL COMMENT '类型：1 同倍追号 | 2 翻倍追号',
  `win_stop` bit(1) NOT NULL DEFAULT b'1' COMMENT '中奖后停止追号：1停止 | 0不停止',
  `is_stop` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已停止追号',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '追号记录表';

-- ----------------------------
-- Table structure for order_bet_record
-- ----------------------------
DROP TABLE IF EXISTS `order_bet_record`;
CREATE TABLE `order_bet_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `order_id` int(11) NOT NULL COMMENT '投注单id',
  `cate_id` int(11) NOT NULL COMMENT '彩种类别id',
  `lottery_id` int(11) NOT NULL COMMENT '彩种id',
  `play_id` int(11) NOT NULL COMMENT '玩法id',
  `setting_id` int(11) NOT NULL COMMENT '玩法配置id',
  `play_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '玩法名称',
  `issue` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '购买的期号',
  `order_sn` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '订单号',
  `bet_number` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '投注号码',
  `bet_count` int(11) NOT NULL COMMENT '投注总注数',
  `bet_amount` decimal(20,2) NOT NULL COMMENT '投注金额',
  `win_amount` decimal(20,2) NOT NULL DEFAULT '0.00' COMMENT '中奖金额',
  `back_amount` decimal(20,2) NOT NULL DEFAULT '0.00' COMMENT '返点金额',
  `god_order_id` int(11) NOT NULL DEFAULT '0' COMMENT '大神推单id, 0为自主投注',
  `tb_status` varchar(10) COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：中奖:WIN | 未中奖:NO_WIN | 等待开奖:WAIT | 和:HE | 撤单:BACK',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `win_count` varchar(12) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '中奖注数',
  `is_push` int(1) DEFAULT '0' COMMENT '是否推单 0 否 1 是',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `source` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '来源：Android | IOS | WEB',
  `familyid` bigint(20) DEFAULT NULL COMMENT '直播间购彩对应的家族id',
  `room_id` bigint(20) DEFAULT NULL COMMENT '直播房间id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx01_play_id` (`play_id`,`lottery_id`,`tb_status`) USING BTREE,
  KEY `god_order_id` (`god_order_id`,`tb_status`) USING BTREE,
  KEY `idx02_create_time` (`create_time`) USING BTREE,
  KEY `idx04_sort1` (`user_id`,`bet_amount`,`create_time`) USING BTREE,
  KEY `idx05_sort2` (`user_id`,`create_time`) USING BTREE,
  KEY `idx06_sort3` (`user_id`,`win_amount`,`create_time`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx07_lottery_id` (`lottery_id`,`tb_status`) USING BTREE,
  KEY `idx_bet_amount` (`bet_amount`) USING BTREE,
  KEY `idx_win_amount` (`win_amount`) USING BTREE,
  KEY `idx_tb_status` (`tb_status`) USING BTREE,
  KEY `idx_setting_id` (`setting_id`) USING BTREE,
  KEY `idx_is_push` (`is_push`) USING BTREE,
  KEY `idx_update_time` (`update_time`) USING BTREE,
  KEY `idx_cate_id` (`cate_id`),
  KEY `idx_order_sn` (`order_sn`),
  KEY `idx_issue` (`issue`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户投注单-注号记录';


-- ----------------------------
-- Table structure for order_record
-- ----------------------------
DROP TABLE IF EXISTS `order_record`;
CREATE TABLE `order_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '订单号',
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '购买的期号',
  `user_id` int(11) NOT NULL COMMENT '购彩用户id',
  `lottery_id` int(11) NOT NULL COMMENT '彩种id',
  `append_id` int(11) NOT NULL DEFAULT 0 COMMENT '追号id（为0则为非追号订单）',
  `open_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '开奖号码',
  `source` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '来源：Android | IOS | WEB',
  `buy_source` int(2) DEFAULT 0 COMMENT '购彩来源',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'NORMAL' COMMENT '订单状态：正常：NORMAL；撤单：BACK;',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_sn`(`order_sn`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `idx02_issue`(`issue`, `lottery_id`, `status`, `open_number`) USING BTREE,
  INDEX `idx01_count`(`id`, `create_time`) USING BTREE,
  INDEX `idx03_create_time`(`create_time`) USING BTREE,
  INDEX `idx_issue`(`issue`) USING BTREE,
  INDEX `idx_lottery_id`(`lottery_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_open_number`(`open_number`) USING BTREE,
  INDEX `idx_source`(`source`) USING BTREE,
  INDEX `idx_update_time`(`update_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户投注单';

-- ----------------------------
-- Table structure for ausact_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `ausact_lottery_sg`;
CREATE TABLE `ausact_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un01_issue`(`issue`) USING BTREE,
  INDEX `idx01_ideal_time`(`ideal_time`, `open_status`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '澳洲ACT';

-- ----------------------------
-- Table structure for auspks_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `auspks_lottery_sg`;
CREATE TABLE `auspks_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un01_issue`(`issue`) USING BTREE,
  INDEX `idx01_ideal_time`(`ideal_time`, `open_status`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '澳洲F1';

-- ----------------------------
-- Table structure for ausssc_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `ausssc_lottery_sg`;
CREATE TABLE `ausssc_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un01_issue`(`issue`) USING BTREE,
  INDEX `idx01_ideal_time`(`ideal_time`, `open_status`, `issue`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '澳洲时时彩';

-- ----------------------------
-- Table structure for azks_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `azks_lottery_sg`;
CREATE TABLE `azks_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `bai` int(1) DEFAULT NULL COMMENT '百位',
  `shi` int(1) DEFAULT NULL COMMENT '十位',
  `ge` int(1) DEFAULT NULL COMMENT '个位',
  `number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开奖结果',
  `time` datetime(0) DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` datetime(0) DEFAULT NULL COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `status`(`open_status`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '澳洲快三开奖结果';

-- ----------------------------
-- Table structure for bjpks_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `bjpks_lottery_sg`;
CREATE TABLE `bjpks_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码',
  `cpk_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '彩票控开奖结果',
  `kcw_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开彩网开奖结果',
  `time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `idx01`(`ideal_time`, `number`, `open_status`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '北京PK10的开奖结果';

-- ----------------------------
-- Table structure for cqssc_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `cqssc_lottery_sg`;
CREATE TABLE `cqssc_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '日期',
  `issue` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `wan` int(1) DEFAULT NULL COMMENT '万位',
  `qian` int(1) DEFAULT NULL COMMENT '千位',
  `bai` int(1) DEFAULT NULL COMMENT '百位',
  `shi` int(1) DEFAULT NULL COMMENT '十位',
  `ge` int(1) DEFAULT NULL COMMENT '个位',
  `cpk_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '彩票控开奖结果',
  `kcw_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开彩网开奖结果',
  `time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '理想开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  `ideal_date` datetime(0) DEFAULT NULL COMMENT '官方开奖时间',
  `actual_date` datetime(0) DEFAULT NULL COMMENT '实际开奖时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `idx01`(`ideal_time`, `wan`, `open_status`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '重庆时时彩开奖结果';

-- ----------------------------
-- Table structure for dzks_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `dzks_lottery_sg`;
CREATE TABLE `dzks_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `bai` int(1) DEFAULT NULL COMMENT '百位',
  `shi` int(1) DEFAULT NULL COMMENT '十位',
  `ge` int(1) DEFAULT NULL COMMENT '个位',
  `number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开奖结果',
  `time` datetime(0) DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` datetime(0) DEFAULT NULL COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `status`(`open_status`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '德州快三开奖结果';

-- ----------------------------
-- Table structure for dzpcegg_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `dzpcegg_lottery_sg`;
CREATE TABLE `dzpcegg_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '期号',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '开奖号码',
  `time` datetime(0) DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` datetime(0) DEFAULT NULL COMMENT '理想开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `idx2_open_status`(`open_status`) USING BTREE,
  INDEX `idx1_ideal_time`(`ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for dzxyft_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `dzxyft_lottery_sg`;
CREATE TABLE `dzxyft_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '期号',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '开奖号码',
  `time` datetime(0) DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` datetime(0) DEFAULT NULL COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `status`(`open_status`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '德州幸运飞艇的开奖结果';

-- ----------------------------
-- Table structure for fc3d_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `fc3d_lottery_sg`;
CREATE TABLE `fc3d_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '期号',
  `date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '日期',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码',
  `cpk_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '彩票控开奖结果',
  `kcw_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开彩网开奖结果',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `status`(`open_status`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '北京PK10的开奖结果';

-- ----------------------------
-- Table structure for fc7lc_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `fc7lc_lottery_sg`;
CREATE TABLE `fc7lc_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '期号',
  `date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '日期',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码',
  `cpk_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '彩票控开奖结果',
  `kcw_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开彩网开奖结果',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`, `number`, `open_status`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '北京PK10的开奖结果';

-- ----------------------------
-- Table structure for fcssq_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `fcssq_lottery_sg`;
CREATE TABLE `fcssq_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '期号',
  `date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '日期',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码',
  `cpk_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '彩票控开奖结果',
  `kcw_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开彩网开奖结果',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `status`(`open_status`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '北京PK10的开奖结果';

-- ----------------------------
-- Table structure for fivebjpks_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `fivebjpks_lottery_sg`;
CREATE TABLE `fivebjpks_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `idx01`(`ideal_time`, `number`, `open_status`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '北京PK10的开奖结果';

-- ----------------------------
-- Table structure for fivelhc_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `fivelhc_lottery_sg`;
CREATE TABLE `fivelhc_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '日期',
  `issue` int(16) DEFAULT NULL COMMENT '第几期',
  `number` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开奖号码',
  `time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '更新时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `issue`(`issue`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '六合彩的开奖结果';

-- ----------------------------
-- Table structure for fivessc_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `fivessc_lottery_sg`;
CREATE TABLE `fivessc_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '日期',
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `wan` int(1) DEFAULT NULL COMMENT '万位',
  `qian` int(1) DEFAULT NULL COMMENT '千位',
  `bai` int(1) DEFAULT NULL COMMENT '百位',
  `shi` int(1) DEFAULT NULL COMMENT '十位',
  `ge` int(1) DEFAULT NULL COMMENT '个位',
  `number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开奖结果',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  `ideal_date` datetime(0) DEFAULT NULL COMMENT '官方开奖时间',
  `actual_date` datetime(0) DEFAULT NULL COMMENT '实际开奖时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `date`(`date`) USING BTREE,
  INDEX `idx01`(`ideal_time`, `wan`, `open_status`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '天津时时彩开奖结果';

-- ----------------------------
-- Table structure for ftjspks_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `ftjspks_lottery_sg`;
CREATE TABLE `ftjspks_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '极速PK10开奖号码',
  `ft_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '极速PK10番摊号码',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `idx01`(`ideal_time`, `number`, `open_status`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '极速PK10番摊的开奖结果';

-- ----------------------------
-- Table structure for ftjsssc_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `ftjsssc_lottery_sg`;
CREATE TABLE `ftjsssc_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '日期',
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `wan` int(1) DEFAULT NULL COMMENT '万位',
  `qian` int(1) DEFAULT NULL COMMENT '千位',
  `bai` int(1) DEFAULT NULL COMMENT '百位',
  `shi` int(1) DEFAULT NULL COMMENT '十位',
  `ge` int(1) DEFAULT NULL COMMENT '个位',
  `number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开奖结果',
  `ft_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '番摊号码',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `idx01`(`ideal_time`, `wan`, `open_status`) USING BTREE,
  INDEX `date`(`date`, `wan`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '极速时时彩番摊';

-- ----------------------------
-- Table structure for ftxyft_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `ftxyft_lottery_sg`;
CREATE TABLE `ftxyft_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '期号',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '开奖号码',
  `cpk_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '彩票控开奖结果',
  `kcw_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '开彩网开奖结果',
  `ft_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '幸运飞艇番摊号码',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `status`(`open_status`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '幸运飞艇番摊';

-- ----------------------------
-- Table structure for jsbjpks_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `jsbjpks_lottery_sg`;
CREATE TABLE `jsbjpks_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `idx01`(`ideal_time`, `number`, `open_status`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '北京PK10的开奖结果';

-- ----------------------------
-- Table structure for jsssc_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `jsssc_lottery_sg`;
CREATE TABLE `jsssc_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '日期',
  `issue` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `wan` int(1) DEFAULT NULL COMMENT '万位',
  `qian` int(1) DEFAULT NULL COMMENT '千位',
  `bai` int(1) DEFAULT NULL COMMENT '百位',
  `shi` int(1) DEFAULT NULL COMMENT '十位',
  `ge` int(1) DEFAULT NULL COMMENT '个位',
  `number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开奖结果',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  `ideal_date` datetime(0) DEFAULT NULL COMMENT '官方开奖时间',
  `actual_date` datetime(0) DEFAULT NULL COMMENT '实际开奖时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `date`(`date`, `wan`, `open_status`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`, `wan`, `open_status`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '天津时时彩开奖结果';

-- ----------------------------
-- Table structure for lhc_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `lhc_lottery_sg`;
CREATE TABLE `lhc_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `year` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '年',
  `issue` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '第几期',
  `number` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开奖号码',
  `time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '六合彩的开奖结果';

-- ----------------------------
-- Table structure for onelhc_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `onelhc_lottery_sg`;
CREATE TABLE `onelhc_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '日期',
  `issue` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '第几期',
  `number` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开奖号码',
  `time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '更新时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UN1_issue`(`issue`) USING BTREE,
  INDEX `idx1_ideal_time`(`ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '六合彩的开奖结果';

-- ----------------------------
-- Table structure for pcegg_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `pcegg_lottery_sg`;
CREATE TABLE `pcegg_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开奖号码',
  `cpk_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '彩票控开奖结果',
  `kcw_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开彩网开奖结果',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '理想开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `idx2_open_status`(`open_status`) USING BTREE,
  INDEX `idx1_ideal_time`(`ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin;

-- ----------------------------
-- Table structure for amlhc_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `amlhc_lottery_sg`;
CREATE TABLE `amlhc_lottery_sg` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(25) COLLATE utf8mb4_bin NOT NULL COMMENT '日期',
  `issue` varchar(16) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '第几期',
  `number` varchar(25) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开奖号码',
  `time` varchar(25) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '更新时间',
  `ideal_time` varchar(25) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_o_i` (`open_status`,`ideal_time`),
  KEY `idx_o_i_i` (`open_status`,`issue`,`ideal_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='澳门六合彩的开奖结果';

-- ----------------------------
-- Table structure for tc7xc_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `tc7xc_lottery_sg`;
CREATE TABLE `tc7xc_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '期号',
  `date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '日期',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码',
  `cpk_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '彩票控开奖结果',
  `kcw_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开彩网开奖结果',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `status`(`open_status`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '北京PK10的开奖结果';

-- ----------------------------
-- Table structure for tcdlt_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `tcdlt_lottery_sg`;
CREATE TABLE `tcdlt_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '期号',
  `date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '日期',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码',
  `cpk_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '彩票控开奖结果',
  `kcw_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开彩网开奖结果',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `status`(`open_status`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '北京PK10的开奖结果';

-- ----------------------------
-- Table structure for tcpls_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `tcpls_lottery_sg`;
CREATE TABLE `tcpls_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '期号',
  `date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '日期',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码',
  `cpk_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '彩票控开奖结果',
  `kcw_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开彩网开奖结果',
  `time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `status`(`open_status`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '北京PK10的开奖结果';

-- ----------------------------
-- Table structure for tcplw_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `tcplw_lottery_sg`;
CREATE TABLE `tcplw_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '期号',
  `date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '日期',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码',
  `cpk_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '彩票控开奖结果',
  `kcw_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开彩网开奖结果',
  `time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `status`(`open_status`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '北京PK10的开奖结果';

-- ----------------------------
-- Table structure for tenbjpks_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `tenbjpks_lottery_sg`;
CREATE TABLE `tenbjpks_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `idx01`(`ideal_time`, `number`, `open_status`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '北京PK10的开奖结果';

-- ----------------------------
-- Table structure for tenssc_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `tenssc_lottery_sg`;
CREATE TABLE `tenssc_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '日期',
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `wan` int(1) DEFAULT NULL COMMENT '万位',
  `qian` int(1) DEFAULT NULL COMMENT '千位',
  `bai` int(1) DEFAULT NULL COMMENT '百位',
  `shi` int(1) DEFAULT NULL COMMENT '十位',
  `ge` int(1) DEFAULT NULL COMMENT '个位',
  `number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开奖结果',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  `ideal_date` datetime(0) DEFAULT NULL COMMENT '官方开奖时间',
  `actual_date` datetime(0) DEFAULT NULL COMMENT '实际开奖时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `status`(`open_status`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`) USING BTREE,
  INDEX `date`(`date`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '天津时时彩开奖结果';

-- ----------------------------
-- Table structure for tjssc_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `tjssc_lottery_sg`;
CREATE TABLE `tjssc_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '日期',
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `wan` int(1) DEFAULT NULL COMMENT '万位',
  `qian` int(1) DEFAULT NULL COMMENT '千位',
  `bai` int(1) DEFAULT NULL COMMENT '百位',
  `shi` int(1) DEFAULT NULL COMMENT '十位',
  `ge` int(1) DEFAULT NULL COMMENT '个位',
  `cpk_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '彩票控开奖结果',
  `kcw_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开彩网开奖结果',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  `ideal_date` datetime(0) DEFAULT NULL COMMENT '官方开奖时间',
  `actual_date` datetime(0) DEFAULT NULL COMMENT '实际开奖时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un01_issue`(`issue`) USING BTREE,
  INDEX `status`(`open_status`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`) USING BTREE,
  INDEX `date`(`date`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '天津时时彩开奖结果';

-- ----------------------------
-- Table structure for txffc_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `txffc_lottery_sg`;
CREATE TABLE `txffc_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '日期',
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `wan` int(1) DEFAULT NULL COMMENT '万位',
  `qian` int(1) DEFAULT NULL COMMENT '千位',
  `bai` int(1) DEFAULT NULL COMMENT '百位',
  `shi` int(1) DEFAULT NULL COMMENT '十位',
  `ge` int(1) DEFAULT NULL COMMENT '个位',
  `cpk_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '网址一采集赛果',
  `kcw_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '网址二采集赛果',
  `time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `idx01`(`ideal_time`, `wan`, `open_status`) USING BTREE,
  INDEX `idx02`(`time`, `open_status`) USING BTREE,
  INDEX `idx03`(`ideal_time`, `issue`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '腾讯分分彩开奖结果';

-- ----------------------------
-- Table structure for xjplhc_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `xjplhc_lottery_sg`;
CREATE TABLE `xjplhc_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '第几期',
  `number` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '开奖号码',
  `time` datetime(0) DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` datetime(0) DEFAULT NULL COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `status`(`open_status`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '新加坡六合彩的开奖结果';

-- ----------------------------
-- Table structure for xjssc_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `xjssc_lottery_sg`;
CREATE TABLE `xjssc_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '日期',
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `wan` int(1) DEFAULT NULL COMMENT '万位',
  `qian` int(1) DEFAULT NULL COMMENT '千位',
  `bai` int(1) DEFAULT NULL COMMENT '百位',
  `shi` int(1) DEFAULT NULL COMMENT '十位',
  `ge` int(1) DEFAULT NULL COMMENT '个位',
  `cpk_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '彩票控开奖结果',
  `kcw_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '开彩网开奖结果',
  `time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  `ideal_date` datetime(0) DEFAULT NULL COMMENT '官方开奖时间',
  `actual_date` datetime(0) DEFAULT NULL COMMENT '实际开奖时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `date`(`date`) USING BTREE,
  INDEX `status`(`open_status`, `issue`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`, `open_status`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '新疆时时彩开奖结果';

-- ----------------------------
-- Table structure for xyft_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `xyft_lottery_sg`;
CREATE TABLE `xyft_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '期号',
  `number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '开奖号码',
  `cpk_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '彩票控开奖结果',
  `kcw_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '开彩网开奖结果',
  `time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `status`(`open_status`) USING BTREE,
  INDEX `ideal_time`(`ideal_time`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '幸运飞艇的开奖结果';


-- ----------------------------
-- Table structure for bonus
-- ----------------------------
DROP TABLE IF EXISTS `bonus`;
CREATE TABLE `bonus`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category_id` int(11) NOT NULL COMMENT '分类id',
  `play_id` int(11) NOT NULL COMMENT '玩法id',
  `max_bonus` decimal(10, 2) NOT NULL COMMENT '最大投注限制',
  `sort` int(11) NOT NULL COMMENT '排序',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '红利设置表';

-- ----------------------------
-- Table structure for bonus_category
-- ----------------------------
DROP TABLE IF EXISTS `bonus_category`;
CREATE TABLE `bonus_category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '红利分类表';

-- ----------------------------
-- Table structure for kill_config
-- ----------------------------
DROP TABLE IF EXISTS `kill_config`;
CREATE TABLE `kill_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '彩种名称',
  `lottery_id` int(6) NOT NULL COMMENT '彩种id',
  `platfom` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '配置杀号平台(ALL,CPT,HKC,XGC,XYE,LHT,LLB)',
  `ratio` double(5, 2) NOT NULL COMMENT '杀号比例',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx01_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '帖子';

-- ----------------------------
-- Table structure for ky_bet_order
-- ----------------------------
DROP TABLE IF EXISTS `ky_bet_order`;
CREATE TABLE `ky_bet_order`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `game_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '游戏局号列表',
  `account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '玩家帐号列表',
  `server_id` int(8) DEFAULT NULL COMMENT '房间 ID',
  `kind_id` int(8) DEFAULT NULL COMMENT '游戏 ID',
  `table_id` int(8) DEFAULT NULL COMMENT '桌子号',
  `chair_id` int(8) DEFAULT NULL COMMENT '椅子号',
  `user_count` int(8) DEFAULT NULL COMMENT '玩家数量',
  `card_value` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '手牌公共牌',
  `cell_score` decimal(10, 2) DEFAULT NULL COMMENT '有效下注',
  `all_bet` decimal(10, 2) DEFAULT NULL COMMENT '总下注',
  `profit` decimal(10, 2) DEFAULT NULL COMMENT '盈利',
  `revenue` decimal(10, 2) DEFAULT NULL COMMENT '抽水',
  `game_start_time` datetime(0) DEFAULT NULL COMMENT '游戏开始时间',
  `game_end_time` datetime(0) DEFAULT NULL COMMENT '游戏结束时间',
  `channel_id` int(8) DEFAULT NULL COMMENT '渠道 ID',
  `line_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '游戏结果对应玩家所属站点',
  `is_handle` int(1) NOT NULL DEFAULT 0 COMMENT '打码量：0未处理，1已处理',
  `kind_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '游戏名称',
  `server_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '房间名称',
  `user_id` int(12) DEFAULT NULL COMMENT '用户ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `game_id_index` (`game_id`),
  INDEX `account_index`(`account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '开元订单表';

-- ----------------------------
-- Table structure for ky_kind
-- ----------------------------
DROP TABLE IF EXISTS `ky_kind`;
CREATE TABLE `ky_kind`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `kind_id` int(20) DEFAULT NULL COMMENT 'kindID',
  `kind_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '游戏名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_id`(`kind_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '开元游戏';

-- ----------------------------
-- Table structure for ky_server
-- ----------------------------
DROP TABLE IF EXISTS `ky_server`;
CREATE TABLE `ky_server`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `kind_id` int(20) DEFAULT NULL,
  `server_id` int(20) DEFAULT NULL,
  `server_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '房间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_id`(`server_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '开元房间';

-- ----------------------------
-- Table structure for circle_post
-- ----------------------------
DROP TABLE IF EXISTS `circle_post`;
CREATE TABLE `circle_post`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `account` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '发帖人',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '内容',
  `image` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `circle_postcol` tinyint(4) DEFAULT NULL,
  `me_has_praise` tinyint(4) DEFAULT NULL,
  `channel` int(3) DEFAULT 0 COMMENT '马甲包频道(0为原有圈子的数据,其它数字为马甲包的数据)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx01_create_time`(`create_time`) USING BTREE,
  INDEX `idx02_channel`(`channel`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '帖子';

-- ----------------------------
-- Table structure for circle_post_comments
-- ----------------------------
DROP TABLE IF EXISTS `circle_post_comments`;
CREATE TABLE `circle_post_comments`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `post_id` int(11) NOT NULL COMMENT '帖子的id',
  `account` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '评论人',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '评论内容',
  `reply_id` int(11) DEFAULT NULL COMMENT '回复id(自联本表id)',
  `reply_account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '回复对象(自联本表account)',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `IDX_postId`(`post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '帖子评论表';

-- ----------------------------
-- Table structure for circle_post_focus
-- ----------------------------
DROP TABLE IF EXISTS `circle_post_focus`;
CREATE TABLE `circle_post_focus`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_uid` int(11) NOT NULL,
  `to_uid` int(11) NOT NULL,
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_f_t_id`(`from_uid`, `to_uid`) USING BTREE,
  INDEX `idx01_to_uid`(`to_uid`, `is_delete`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '圈子关注';

-- ----------------------------
-- Table structure for circle_post_message_record
-- ----------------------------
DROP TABLE IF EXISTS `circle_post_message_record`;
CREATE TABLE `circle_post_message_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `type` int(1) NOT NULL COMMENT '1:我的关注2:我的回复',
  `opt_time` datetime(0) NOT NULL COMMENT '最后操作时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `IDX_uid_type`(`user_id`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '新消息时间记录';

-- ----------------------------
-- Table structure for circle_post_praise_record
-- ----------------------------
DROP TABLE IF EXISTS `circle_post_praise_record`;
CREATE TABLE `circle_post_praise_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `post_id` int(11) NOT NULL COMMENT '帖子id',
  `user_id` int(11) NOT NULL,
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx01_post_id`(`post_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '帖子点赞记录';

-- ----------------------------
-- Table structure for circle_post_report
-- ----------------------------
DROP TABLE IF EXISTS `circle_post_report`;
CREATE TABLE `circle_post_report`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_account` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '举报人',
  `to_account` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '被举报人',
  `type_id` int(11) NOT NULL COMMENT '举报类型id',
  `proof_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '证据图片',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '说明',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '发帖举报';

-- ----------------------------
-- Table structure for circle_post_report_type
-- ----------------------------
DROP TABLE IF EXISTS `circle_post_report_type`;
CREATE TABLE `circle_post_report_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `sort` int(11) NOT NULL DEFAULT 1 COMMENT '排序',
  `operater` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '添加人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '帖子举报类型';

-- ----------------------------
-- Table structure for circle_post_shield
-- ----------------------------
DROP TABLE IF EXISTS `circle_post_shield`;
CREATE TABLE `circle_post_shield`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_uid` int(11) NOT NULL,
  `to_uid` int(11) NOT NULL,
  `on_off` int(1) DEFAULT 1 COMMENT '开关状态：1打开2关闭',
  `update_time` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_f_t_id`(`from_uid`, `to_uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '屏蔽用户';

-- ----------------------------
-- Table structure for circle_rules
-- ----------------------------
DROP TABLE IF EXISTS `circle_rules`;
CREATE TABLE `circle_rules`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '内容',
  `operater` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '操作人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '圈规';


-- ----------------------------
-- Table structure for ad_basic
-- ----------------------------
DROP TABLE IF EXISTS `ad_basic`;
CREATE TABLE `ad_basic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '标题',
  `start_time` varchar(20) COLLATE utf8mb4_bin NOT NULL COMMENT '有效开始时间',
  `end_time` varchar(20) COLLATE utf8mb4_bin NOT NULL COMMENT '有效结束时间',
  `hide` int(1) NOT NULL DEFAULT '1' COMMENT '自动隐藏：0，否；1，是',
  `close` int(1) NOT NULL DEFAULT '0' COMMENT '是否关闭：0，否；1，是',
  `sort` int(11) NOT NULL COMMENT '排序值',
  `publish` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '发布系统',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='广告基本信息';

-- ----------------------------
-- Table structure for ad_photo
-- ----------------------------
DROP TABLE IF EXISTS `ad_photo`;
CREATE TABLE `ad_photo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(1) NOT NULL COMMENT '系统类型：1--android,2--ios,3--web',
  `site_id` int(11) NOT NULL COMMENT '广告位置id',
  `basic_id` int(11) NOT NULL COMMENT '广告基础信息表id',
  `photo` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '图片路径',
  `url` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '跳转地址',
  `target_id` int(11) DEFAULT NULL COMMENT '跳转ID',
  `target_type` int(1) DEFAULT NULL COMMENT '-1，使用原有的跳转url，0，前端原生界面跳转，1,跳转ID活动',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='广告图片信息';

-- ----------------------------
-- Table structure for ad_site
-- ----------------------------
DROP TABLE IF EXISTS `ad_site`;
CREATE TABLE `ad_site` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `size` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '规格',
  `type` int(1) NOT NULL COMMENT '系统类型：1android,2ios,3web',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='广告位置';


-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `share_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '分享网址',
  `two_code` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '二维码',
  `android_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '安卓下载包',
  `ios_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'IOS下载包',
  `about_us` text COLLATE utf8mb4_bin COMMENT '关于我们',
  `service_contract` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '服务协议',
  `service_qq1` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '客服qq1',
  `service_qq2` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '客服qq2',
  `h5_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'h5地址',
  `app_api` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'app-api地址(运维管理)',
  `web_api` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'web-api地址(运维管理)',
  `fp_api` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '封盘api(运维管理)',
  `chat_websocket` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '聊天websocket(运维管理)',
  `sg_websocket` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖websocket(运维管理)',
  `direct_kj` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '直播开奖(运维管理)',
  `file_upload` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '文件上传(运维管理)',
  `h5url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'h5地址(运维管理)',
  `download_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '下载地址(运维管理)',
  `ios_download_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'ios下载地址(运维管理)',
  `android_download_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'android下载地址(运维管理)',
  `pay_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付地址(运维管理)',
  `bbsweb_interface` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'bbsweb接口(运维管理)',
  `bbsapp_interface` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'bbsapp接口(运维管理)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='APP管理';


-- ----------------------------
-- Table structure for app_vest_bag
-- ----------------------------
DROP TABLE IF EXISTS `app_vest_bag`;
CREATE TABLE `app_vest_bag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `share_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '分享网址',
  `two_code` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '二维码',
  `android_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '安卓下载包',
  `ios_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'IOS下载包',
  `about_us` text COLLATE utf8mb4_bin COMMENT '关于我们',
  `service_contract` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '服务协议',
  `service_qq1` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '客服qq1',
  `service_qq2` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '客服qq2',
  `h5_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'h5地址',
  `app_api` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'app-api地址(运维管理)',
  `web_api` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'web-api地址(运维管理)',
  `fp_api` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '封盘api(运维管理)',
  `chat_websocket` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '聊天websocket(运维管理)',
  `sg_websocket` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖websocket(运维管理)',
  `direct_kj` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '直播开奖(运维管理)',
  `file_upload` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '文件上传(运维管理)',
  `h5url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'h5地址(运维管理)',
  `download_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '下载地址(运维管理)',
  `ios_download_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'ios下载地址(运维管理)',
  `android_download_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'android下载地址(运维管理)',
  `pay_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '支付地址(运维管理)',
  `bbsweb_interface` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'bbsweb接口(运维管理)',
  `bbsapp_interface` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'bbsapp接口(运维管理)',
  `chat_url` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '聊天服务器地址(运维管理)',
  `chat_type` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '聊天平台类型(运维管理)',
  `plat_form` int(11) DEFAULT NULL COMMENT '平台类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='ios马甲包所需要的所有平台信息';


-- ----------------------------
-- Table structure for cp_activity_data
-- ----------------------------
DROP TABLE IF EXISTS `cp_activity_data`;
CREATE TABLE `cp_activity_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `act_id` int(11) DEFAULT NULL COMMENT '活动编号',
  `act_title` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '活动标题',
  `meb_nickname` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户昵称',
  `meb_id` int(11) DEFAULT NULL COMMENT '会员id',
  `receive_amount` decimal(10,2) DEFAULT NULL COMMENT '领取奖金',
  `receive_time` datetime DEFAULT NULL COMMENT '领取时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ACT_ID_NORMAL_INDEX` (`act_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='彩票活动(红包)记录表';


-- ----------------------------
-- Table structure for cp_activity_info
-- ----------------------------
DROP TABLE IF EXISTS `cp_activity_info`;
CREATE TABLE `cp_activity_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `act_out_banner` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '活动表层横幅',
  `act_in_banner` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '活动内页横幅',
  `act_start_time` datetime DEFAULT NULL COMMENT '活动开始时间',
  `act_end_time` datetime DEFAULT NULL COMMENT '活动结束时间',
  `act_title` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '活动标题',
  `act_guide` varchar(400) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '活动引导',
  `act_min_amount` decimal(10,2) DEFAULT NULL COMMENT '活动最小金额（红包）',
  `act_max_amount` decimal(10,2) DEFAULT NULL COMMENT '活动最大金额（红包）',
  `act_min_sham_amount` decimal(10,2) DEFAULT NULL COMMENT '活动最小伪装金额',
  `act_max_sham_amount` decimal(10,2) DEFAULT NULL COMMENT '活动最大伪装金额',
  `act_type` int(1) DEFAULT NULL COMMENT '活动类型0其他，1红包',
  `act_status` int(1) DEFAULT '0' COMMENT '活动状态0开启，1关闭',
  `act_into_page` int(1) DEFAULT NULL COMMENT '进入页面',
  `is_popup` int(1) DEFAULT '0' COMMENT '是否弹出0是，1否',
  `act_receive_limit_bet_amount` decimal(10,2) DEFAULT NULL COMMENT '领取条件：打码量',
  `act_receive_limit_amount` decimal(10,2) DEFAULT NULL COMMENT '领取条件：充值额度',
  `is_today_charge_hundred` int(1) NOT NULL DEFAULT '0' COMMENT '领取条件当日充值过百',
  `start_time` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '启动时间',
  `stop_time` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '结束时间',
  `time_type` int(1) DEFAULT '0' COMMENT '时间类型(默认0为持续，1为定点)',
  `start_date` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '启动日期',
  `stop_date` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '结束日期',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='彩票活动配置表';


-- ----------------------------
-- Table structure for first_recharge_gift
-- ----------------------------
DROP TABLE IF EXISTS `first_recharge_gift`;
CREATE TABLE `first_recharge_gift` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_code` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单号',
  `user_id` int(11) DEFAULT NULL COMMENT '会员id',
  `account` varchar(40) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '会员账号',
  `topup_money` int(11) DEFAULT NULL COMMENT '充值金额',
  `gift_money` int(11) DEFAULT NULL COMMENT '赠送礼金',
  `got_money` int(11) DEFAULT NULL COMMENT '已领取金额',
  `status` int(1) DEFAULT '0' COMMENT '状态:0,未领完; 1,已领完',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='首充礼金管理';


-- ----------------------------
-- Table structure for first_recharge_gift_back
-- ----------------------------
DROP TABLE IF EXISTS `first_recharge_gift_back`;
CREATE TABLE `first_recharge_gift_back` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_code` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '订单号',
  `user_id` int(11) DEFAULT NULL COMMENT '会员id',
  `account` varchar(40) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '会员账号',
  `money` int(11) DEFAULT '0' COMMENT '金额(单位:分)',
  `bet_money` int(11) DEFAULT '0' COMMENT '投注金额(单位:分)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='首充礼金返还记录';



-- ----------------------------
-- Table structure for lhc_god_type
-- ----------------------------
DROP TABLE IF EXISTS `lhc_god_type`;
CREATE TABLE `lhc_god_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT '' COMMENT '分类名称',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_user` varchar(16) COLLATE utf8mb4_bin DEFAULT '' COMMENT '创建人',
  `update_user` varchar(16) COLLATE utf8mb4_bin DEFAULT '' COMMENT '最后更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


-- ----------------------------
-- Table structure for lhc_user
-- ----------------------------
DROP TABLE IF EXISTS `lhc_user`;
CREATE TABLE `lhc_user` (
  `id` int(11) NOT NULL COMMENT '主键',
  `account` varchar(40) COLLATE utf8mb4_bin NOT NULL COMMENT '账号',
  `heads` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '会员头像',
  `phone` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '手机号码',
  `nickname` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '昵称',
  `lhcxs_status` int(11) DEFAULT NULL COMMENT '心水推荐权限',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='六合彩用户表';


-- ----------------------------
-- Table structure for operate_sensitive_log
-- ----------------------------
DROP TABLE IF EXISTS `operate_sensitive_log`;
CREATE TABLE `operate_sensitive_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `account` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '用户帐号',
  `operate_ip` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '当前登录ip地址',
  `request_type` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '请求类型，GET，POST',
  `url` varchar(128) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '操作对应的url',
  `module` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '模块名字',
  `methods` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '方法名',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `response_time` bigint(11) NOT NULL DEFAULT '0' COMMENT '响应时间（毫秒）',
  `response_result` int(1) NOT NULL DEFAULT '0' COMMENT '响应结果，0：执行成功，1：执行失败',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='敏感操作日志';


-- ----------------------------
-- Table structure for return_lottery_set
-- ----------------------------
DROP TABLE IF EXISTS `return_lottery_set`;
CREATE TABLE `return_lottery_set` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `lottery_name` varchar(16) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '彩种名称',
  `lottery_category_id` int(12) DEFAULT NULL,
  `lottery_id` int(12) DEFAULT NULL COMMENT '彩种ID',
  `water_amout` int(20) DEFAULT NULL COMMENT '返水比例',
  `amount` decimal(20,3) DEFAULT NULL COMMENT '金额',
  `edit_user` varchar(16) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '修改人',
  `edit_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `index_category_lottery` (`lottery_category_id`,`lottery_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='购彩返水';


-- ----------------------------
-- Table structure for return_water_set
-- ----------------------------
DROP TABLE IF EXISTS `return_water_set`;
CREATE TABLE `return_water_set` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `type` int(3) DEFAULT NULL COMMENT '类型  1 购彩 2 开元 3 AG 4 电竞',
  `is_start` int(2) DEFAULT NULL COMMENT '是否开启',
  `edit_time` datetime DEFAULT NULL COMMENT '修改时间',
  `edit_user` varchar(16) DEFAULT NULL COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='返水设置';


-- ----------------------------
-- Table structure for return_third_set
-- ----------------------------
DROP TABLE IF EXISTS `return_third_set`;
CREATE TABLE `return_third_set` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `type` int(3) DEFAULT NULL COMMENT '类型  1开元  2 AG 3 电竞',
  `water_amout` int(20) DEFAULT NULL COMMENT '返水比例',
  `amount` decimal(20,3) DEFAULT NULL COMMENT '金额',
  `edit_user` varchar(16) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '修改人',
  `edit_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='返水设置';


-- ----------------------------
-- Table structure for es_bet_order
-- ----------------------------
DROP TABLE IF EXISTS `es_bet_order`;
CREATE TABLE `es_bet_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `es_order_id` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '电竞注单ID',
  `es_user_id` int(11) DEFAULT NULL COMMENT '电竞账号ID',
  `username` varchar(40) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户名',
  `total_amount` decimal(12,2) DEFAULT NULL COMMENT '总金额',
  `odds_value` decimal(12,2) DEFAULT NULL COMMENT '赔率',
  `can_win_amount` decimal(12,2) DEFAULT NULL COMMENT '可赢金额',
  `confirm_status` int(1) DEFAULT NULL COMMENT '结算状态',
  `win_lose_state` int(1) DEFAULT NULL COMMENT '输赢状态',
  `settlement_status` int(1) DEFAULT NULL COMMENT '结算状态',
  `settlement_amount` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '结算金额',
  `settlement_ratio` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '结算比例',
  `settlement_time` datetime DEFAULT NULL COMMENT '结算时间',
  `is_change` bit(1) DEFAULT NULL COMMENT '赛果是否变更',
  `is_handle` int(1) NOT NULL DEFAULT '0' COMMENT '打码量：0未处理，1已处理',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `es_order_id_unique` (`es_order_id`),
  KEY `idx_es_user_id` (`es_user_id`),
  KEY `idx_is_handle` (`is_handle`),
  KEY `idx_h_account_t` (`is_handle`,`username`,`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='电竞结算注单表';


-- ----------------------------
-- Table structure for gold_give_group
-- ----------------------------
DROP TABLE IF EXISTS `gold_give_group`;
CREATE TABLE `gold_give_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '名称',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='金币赠送分组';


DROP TABLE IF EXISTS `lhc_handicap`;
CREATE TABLE `lhc_handicap` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '期号',
  `startlotto_time` varchar(25) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖时间',
  `start_time` varchar(25) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '自动开盘时间',
  `end_time` varchar(25) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '自动封盘时间',
  `automation` int(1) DEFAULT NULL COMMENT '允许自动开盘: 0 不允许,1 允许',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk01_issue` (`issue`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='六合彩盘口';


-- Table structure for jsbjpks_lottery_sg
-- ----------------------------
DROP TABLE IF EXISTS `jsbjpks_lottery_sg`;
CREATE TABLE `jsbjpks_lottery_sg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码',
  `time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实际开奖时间',
  `ideal_time` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '官方开奖时间',
  `open_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：WAIT 等待开奖 | AUTO 自动开奖 | HANDLE 手动开奖',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE,
  INDEX `idx01`(`ideal_time`, `number`, `open_status`) USING BTREE,
  INDEX `idx_o_i`(`open_status`, `ideal_time`) USING BTREE,
  INDEX `idx_o_i_i`(`open_status`, `issue`, `ideal_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '北京PK10的开奖结果';


-- Table structure for god_plan
-- ----------------------------
DROP TABLE IF EXISTS `god_plan`;
CREATE TABLE `god_plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `god_id` int(11) NOT NULL COMMENT '大神Id',
  `head` varchar(128) CHARACTER SET utf8mb4 NOT NULL COMMENT '头像',
  `god_nick_name` varchar(20) COLLATE utf8mb4_bin NOT NULL COMMENT '大神昵称',
  `lottery_id` int(11) NOT NULL COMMENT '彩种Id',
  `play_tag_id` int(11) DEFAULT NULL COMMENT '玩法id',
  `plan_type` int(10) DEFAULT NULL COMMENT '计划类型(1,2,3)',
  `status` varchar(5) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '状态(0关闭，1开启)',
  `reward_amount` decimal(20,0) NOT NULL DEFAULT '0' COMMENT '大神打赏总金额',
  `win_count` int(11) NOT NULL DEFAULT '0' COMMENT '中奖数',
  `probability` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '总中奖率',
  `total_issue_count` int(11) NOT NULL DEFAULT '0' COMMENT '总推单期数',
  `order_num` int(2) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `play_show_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '显示名称',
  `play_name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '玩法名称',
  `plan_id` bigint(20) DEFAULT NULL COMMENT '计划Id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_god_plan_lottery_id_status` (`lottery_id`,`status`) COMMENT '彩种计划索引',
  KEY `idx_g_l_s` (`lottery_id`,`god_id`,`status`),
  KEY `idx_status` (`status`),
  KEY `idx_god_id` (`god_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


DROP TABLE IF EXISTS `tjssc_kill_number`;
CREATE TABLE `tjssc_kill_number`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '期号',
  `open_one` int(2) DEFAULT NULL COMMENT '第一球开奖结果',
  `ball_one` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第一球杀号（sin,sec,cos,cot,tan）',
  `open_two` int(2) DEFAULT NULL COMMENT '第二球开奖结果',
  `ball_two` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第二球杀号（sin,sec,cos,cot,tan）',
  `open_three` int(2) DEFAULT NULL COMMENT '第三球开奖结果',
  `ball_three` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第三球杀号（sin,sec,cos,cot,tan）',
  `open_four` int(2) DEFAULT NULL COMMENT '第四球开奖结果',
  `ball_four` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第四球杀号（sin,sec,cos,cot,tan）',
  `open_five` int(2) DEFAULT NULL COMMENT '第五球开奖结果',
  `ball_five` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第五球杀号',
  `time` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27995 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '天津时时公式杀号' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `tjssc_recommend`;
CREATE TABLE `tjssc_recommend`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `issue` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '期号',
  `open_number` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '开奖结果',
  `ball_one_number` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第一球推荐号码',
  `ball_one_single` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第一球推荐单双',
  `ball_one_size` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第一球推荐大小',
  `ball_two_number` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第二球推荐号码',
  `ball_two_single` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第二球推荐单双',
  `ball_two_size` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第二球推荐大小',
  `ball_three_number` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第三球推荐号码',
  `ball_three_single` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第三球推荐单双',
  `ball_three_size` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第三球推荐大小',
  `ball_four_number` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第四球推荐号码',
  `ball_four_single` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第四球推荐单双',
  `ball_four_size` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第四球推荐大小',
  `ball_five_number` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第五球推荐号码',
  `ball_five_single` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第五球推荐单双',
  `ball_five_size` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第五球推荐大小',
  `dragon_tiger` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '推荐龙虎',
  `create_time` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_issue`(`issue`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27682 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '天津时时彩免费推荐' ROW_FORMAT = Dynamic;

-- Table structure for god_plan_issue
-- ----------------------------
DROP TABLE IF EXISTS `god_plan_issue`;
CREATE TABLE `god_plan_issue` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `issue` varchar(16) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '期号',
  `god_id` int(11) DEFAULT NULL COMMENT '大神id（即god_plan_play中id）',
  `bet_number` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '投注号码',
  `lottery_id` int(11) DEFAULT NULL COMMENT '彩种Id',
  `play_tag_id` int(11) NOT NULL COMMENT '玩法Id',
  `setting_id` int(11) DEFAULT NULL COMMENT '玩法配置id',
  `play_name` varchar(16) COLLATE utf8mb4_bin NOT NULL COMMENT '玩法名称',
  `number` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '开奖号码',
  `status` int(1) NOT NULL COMMENT '状态（0预推，1待开奖，2已开奖）',
  `tb_status` varchar(10) COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：中奖 | 未中奖 | 等待开奖',
  `plan_type` int(1) DEFAULT '1' COMMENT '计划类型（1期，2期，3期）',
  `ideal_time` datetime DEFAULT NULL COMMENT '开奖时间',
  `bet_count` int(11) DEFAULT NULL COMMENT '投注注数',
  `single_money` decimal(20,2) DEFAULT NULL,
  `reward_amount` decimal(20,0) DEFAULT NULL COMMENT '大神打赏总金额',
  `recent_win_count` int(11) NOT NULL DEFAULT '0' COMMENT '中奖数',
  `play_show_name` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '显示名称',
  `plan_type_index` int(11) DEFAULT '0' COMMENT '计划索引',
  `plan_id` bigint(20) DEFAULT NULL COMMENT '计划Id',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_godId_lotteryId_issue` (`issue`,`god_id`,`lottery_id`,`is_delete`) USING BTREE COMMENT 'godId，玩法，期号唯一索引',
  KEY `idx_lotteryId_playName_issue` (`issue`,`lottery_id`,`play_name`) USING BTREE COMMENT '彩种，玩法，期号联合索引',
  KEY `idx_is_delete` (`is_delete`),
  KEY `idx_l_g_p_d` (`lottery_id`,`god_id`,`plan_id`,`is_delete`) USING BTREE,
  KEY `idx_p_d` (`plan_id`,`is_delete`),
  KEY `idx_l_g_d` (`lottery_id`,`god_id`,`is_delete`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='大神计划表';

-- Table structure for god_plan_lottery_category
-- ----------------------------
DROP TABLE IF EXISTS `god_plan_lottery_category`;
CREATE TABLE `god_plan_lottery_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `lottery_name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '彩种名称',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '父级id',
  `lottery_id` int(11) NOT NULL DEFAULT '0' COMMENT '彩种Id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_lottery_id` (`lottery_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- Table structure for god_plan_reward
-- ----------------------------
DROP TABLE IF EXISTS `god_plan_reward`;
CREATE TABLE `god_plan_reward` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) DEFAULT NULL COMMENT '打赏人ID',
  `user_name` varchar(40) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '计划昵称',
  `money` int(11) DEFAULT '0' COMMENT '金额',
  `god_plan_id` int(11) DEFAULT NULL COMMENT '大神Id',
  `lottery_id` int(11) DEFAULT NULL COMMENT '彩种Id',
  `play_tag_id` int(11) NOT NULL DEFAULT '0' COMMENT '玩法Id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user_id` int(11) DEFAULT NULL COMMENT '更新用户ID',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `god_name` varchar(16) COLLATE utf8mb4_bin NOT NULL COMMENT '大神计划名称',
  `lottery_name` varchar(16) COLLATE utf8mb4_bin NOT NULL COMMENT '彩种名称',
  `plan_id` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '计划planId',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='大神计划打赏记录';

-- Table structure for god_plan_series_play
-- ----------------------------
DROP TABLE IF EXISTS `god_plan_series_play`;
CREATE TABLE `god_plan_series_play` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '彩种系列Id',
  `name` varchar(40) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '系列名称',
  `lottery_id` int(11) DEFAULT NULL COMMENT '彩种Id',
  `show_play_name` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '显示玩法名称',
  `play_values` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '玩法值集合（逗号分隔）',
  `value_count` int(1) NOT NULL DEFAULT '1' COMMENT '值个数',
  `play_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '玩法名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='大神系列玩法表';

SET FOREIGN_KEY_CHECKS=1;


-- 上下分记录表
DROP TABLE IF EXISTS game_money_record;
CREATE TABLE game_money_record (
  id varchar(32) NOT NULL ,
  note varchar(32) DEFAULT NULL COMMENT '操作说明',
  platform varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '所属平台',
  user_id int(11) DEFAULT NULL COMMENT '用户id',
  money decimal(20,2) DEFAULT NULL COMMENT '操作金额',
  current_money decimal(20,2) DEFAULT NULL COMMENT '当前用户金额',
  order_no varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '订单号',
  type int(11) DEFAULT NULL COMMENT '游戏类型（1：ag；2：开元；3：电竞；4：ae；5：体育；6：足球）',
  operate int(11) DEFAULT NULL COMMENT '操作类型，1:上分；2：下分；',
  op_status varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '操作状态',
  create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx1 (platform,user_id,order_no,type,operate) USING BTREE,
  KEY idx2 (user_id,order_no,type,operate) USING BTREE,
  KEY idx3 (order_no,type,operate) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='第三方游戏上下分记录表';


-- 第三方游戏打码量记录
DROP TABLE IF EXISTS game_betamount_record;
CREATE TABLE `game_betamount_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `betAmount` decimal(20,2) DEFAULT NULL COMMENT '有效打码量',
  `user_id` bigint(32) DEFAULT NULL COMMENT '用户id',
  `beginTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '统计开始时间',
  `endTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '统计结束时间',
  `type` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '游戏类型',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx1` (`user_id`,`create_time`) USING BTREE,
  KEY `idx2` (`user_id`,`type`,`create_time`) USING BTREE,
  KEY `idx3` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='第三方游戏打码量记录';


-- 会员等级配置表
DROP TABLE IF EXISTS mem_level_config;
CREATE TABLE `mem_level_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '等级id',
  `level` varchar(32) DEFAULT NULL COMMENT '当前等级',
  `level_icon_url` varchar(128) DEFAULT NULL COMMENT '等级图标地址',
  `level_svga_url` varchar(128) DEFAULT NULL COMMENT '等级动画效果地址',
  `return_water` decimal(16,2) DEFAULT NULL COMMENT '返水比例',
  `recharge_amount` decimal(16,2) DEFAULT NULL COMMENT '所需充值金额',
  `platform_code` varchar(16) DEFAULT NULL COMMENT '归属平台',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `tp_reference_02` (`platform_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='会员等级配置表';


-- 财务管理手动加款表
drop table IF EXISTS finance_balance_adjustment;
CREATE TABLE `finance_balance_adjustment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` int DEFAULT NULL COMMENT '55手动入款  56手动出款',
  `accno` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '会员标识',
  `amount` decimal(20,0) DEFAULT NULL COMMENT '加扣款金额',
  `dama` bit(1) DEFAULT b'0' COMMENT '是否记打码量 0不计入，1计入',
  `dama_ratio` decimal(20,0) DEFAULT '1' COMMENT '打码倍率',
  `clean` bit(1) DEFAULT b'0' COMMENT '是否已经体现，0：未提，1已提',
  `orderno` varchar(64) DEFAULT NULL COMMENT '订单编号，记录关联的体现订单',
  `remark` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '加款原因',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `accno` (`accno`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='财务管理手动加款表';


-- 财务管理手动加码表
drop table IF EXISTS finance_dama_adjustment;
CREATE TABLE `finance_dama_adjustment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` int DEFAULT NULL COMMENT '加码:1,减码:-1',
  `accno` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '会员标识',
  `damaliang` decimal(20,0) DEFAULT NULL COMMENT '加扣打码量',
  `remark` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '加扣打码原因',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `accno` (`accno`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='财务管理手动加码表';


-- 版本1.0.1 变更sql
ALTER TABLE `sys_parameter` ADD COLUMN `sortby` int(11) NOT NULL DEFAULT 1 COMMENT '排序值,值越小越靠前' AFTER `sysparamvalue`;


drop table IF EXISTS mg_game;
CREATE TABLE `mg_game` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `game_code` varchar(255) DEFAULT NULL COMMENT '游戏编号',
  `game_name` varchar(255) DEFAULT NULL COMMENT '游戏名称',
  `lottery_tag` int(12) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='MG游戏字典表';



drop table IF EXISTS mg_bet_order;
CREATE TABLE `mg_bet_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `player_name` varchar(255) DEFAULT NULL COMMENT '玩家名称',
  `vendor_code` varchar(255) DEFAULT NULL COMMENT '产品代码',
  `game_code` varchar(255) DEFAULT NULL COMMENT '游戏代码',
  `parent_bet_id` varchar(255) DEFAULT NULL COMMENT '父注编号',
  `bet_id` varchar(255) DEFAULT NULL COMMENT '下注编号',
  `trans_type` varchar(255) DEFAULT NULL COMMENT '类型',
  `currency` varchar(255) DEFAULT NULL COMMENT '币种',
  `wallet_code` varchar(255) DEFAULT NULL COMMENT '钱包代码',
  `bet_amount` decimal(20,3) DEFAULT NULL COMMENT '下注金额',
  `win_amount` decimal(20,3) DEFAULT NULL COMMENT '派彩或退回金额',
  `traceId` varchar(255) DEFAULT NULL COMMENT '交易编号',
  `is_handle` int(1) DEFAULT '0' COMMENT '是否同步',
  `created_at` datetime DEFAULT NULL COMMENT '建立日期',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `game_name` varchar(255) DEFAULT NULL COMMENT '游戏名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_traceId` (`traceId`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='MG注单表';



drop table IF EXISTS db_bet_order;
CREATE TABLE `db_bet_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seq_no` bigint(20) DEFAULT NULL COMMENT '游戏序号',
  `player_id` varchar(25) DEFAULT NULL COMMENT '玩家账号',
  `game_date` datetime DEFAULT NULL COMMENT '游戏时间',
  `g_type` int(20) DEFAULT NULL COMMENT '游戏型态',
  `m_type` int(20) DEFAULT NULL COMMENT '机台类型',
  `room_type` int(20) DEFAULT NULL COMMENT '游戏区域',
  `currency` varchar(3) DEFAULT NULL COMMENT '货币',
  `bet` decimal(20,3) DEFAULT NULL COMMENT '押注金额',
  `win` decimal(20,3) DEFAULT NULL COMMENT '游戏赢分',
  `total` decimal(20,3) DEFAULT NULL COMMENT '总输赢',
  `denom` decimal(20,3) DEFAULT NULL COMMENT '投注面值',
  `before_balance` decimal(20,3) DEFAULT NULL COMMENT '进场金额',
  `after_balance` decimal(20,3) DEFAULT NULL COMMENT '离场金额',
  `last_modify_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `player_ip` varchar(128) DEFAULT NULL COMMENT '玩家登入 IP',
  `client_type` varchar(20) DEFAULT NULL COMMENT '玩家从网页或行动装置登入',
  `is_handle` int(1) DEFAULT '0',
  `game_name` varchar(32) DEFAULT NULL COMMENT '游戏名称',
  `member_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   PRIMARY KEY (`id`),
  UNIQUE KEY `index_seq_no` (`seq_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='JDB注单表';

drop table IF EXISTS db_game;
CREATE TABLE `db_game` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `m_type` int(20) DEFAULT NULL COMMENT '游戏mtype',
  `game_name` varchar(32) DEFAULT NULL COMMENT '游戏名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='JDB游戏字典表';


ALTER TABLE tra_paymentinfo  ADD `provider_id` bigint(20) DEFAULT NULL COMMENT '商户配置ID';

ALTER TABLE mem_baseinfo ADD remark varchar(100) COMMENT '备注';
ALTER TABLE mem_baseinfo ADD cg_nickname bit(1) DEFAULT b'0' COMMENT '0:未修改,1:已经修改过1次';
ALTER TABLE mem_baseinfo ADD COLUMN consume_amount decimal(20,3) NOT NULL DEFAULT '0.000' COMMENT '总消费(打码量)' AFTER withdrawal_amount;

alter table order_bet_record modify column `bet_amount` decimal(20,3) NOT NULL COMMENT '投注金额';
alter table order_bet_record modify column  `win_amount` decimal(20,3) NOT NULL DEFAULT '0.000' COMMENT '中奖金额';
alter table order_bet_record modify column  `back_amount` decimal(20,3) NOT NULL DEFAULT '0.000' COMMENT '返点金额';

alter table mem_baseinfo modify column `wait_amount` decimal(20,3) NOT NULL DEFAULT '0.000' COMMENT '待开奖金额（元）';
alter table mem_baseinfo modify column `withdrawal_remainder` decimal(20,3) NOT NULL DEFAULT '0.000' COMMENT '可提现余额';
alter table mem_baseinfo modify column `bet_amount` decimal(20,3) NOT NULL DEFAULT '0.000' COMMENT '累计投注  (元)';
alter table mem_baseinfo modify column `pay_amount` decimal(20,3) NOT NULL DEFAULT '0.000' COMMENT '累计充值（元）';
alter table mem_baseinfo modify column `withdrawal_amount` decimal(20,3) NOT NULL DEFAULT '0.000' COMMENT '累计提现（元)';
alter table mem_baseinfo modify column `no_withdrawal_amount` decimal(20,3) NOT NULL DEFAULT '0.000' COMMENT '不可提现金额(元)';

alter table tra_applycash modify column  `apycgold` decimal(16,3) DEFAULT NULL COMMENT '申请总金额';
alter table tra_applycash modify column  `apycamt` decimal(16,3) DEFAULT NULL COMMENT '打款金额';
alter table tra_applycash modify column `damaliang` decimal(16,3) DEFAULT '0.000' COMMENT '打码量';
alter table tra_applycash modify column `xingzhengfei` decimal(16,3) DEFAULT '0.000' COMMENT '行政费';
alter table tra_applycash modify column `bet_amount` decimal(16,3) DEFAULT '0.000' COMMENT '投注金额充值金额';
alter table tra_applycash modify column `no_withdrawal_amount` decimal(16,3) DEFAULT '0.000' COMMENT '当前所需打码量';

alter table tra_orderinfom modify column `oldamt` decimal(16,3) DEFAULT NULL COMMENT '订单原价';
alter table tra_orderinfom modify column `sumamt` decimal(16,3) DEFAULT NULL COMMENT '订单总金额';
alter table tra_orderinfom modify column `realamt` decimal(16,3) DEFAULT NULL COMMENT '实付金额';

alter table tra_paymentinfo modify column  `payamt` decimal(16,3) DEFAULT NULL COMMENT '支付金额';
alter table tra_rechargemeal modify column `realamt` decimal(16,3) DEFAULT NULL COMMENT '充值金额';
alter table tra_rechargemeal modify column `rechargegold` decimal(16,3) DEFAULT NULL COMMENT '充值播币数';
alter table tra_rechargemeal modify column `givegold` decimal(16,3) DEFAULT NULL COMMENT '赠送播币数';
alter table tra_rechargemeal modify column `givepercent` decimal(16,3) DEFAULT NULL COMMENT '赠送率  三位小数';

alter table ae_bet_order modify column `bet` decimal(20,3) DEFAULT NULL COMMENT '有效下注';
alter table ae_bet_order modify column `allbet` decimal(20,3) DEFAULT NULL COMMENT '总下注';
alter table ae_bet_order modify column `profit` decimal(20,3) DEFAULT NULL COMMENT '输赢';
alter table ae_bet_order modify column `revenue` decimal(20,3) DEFAULT NULL COMMENT '抽水';

alter table ag_bet_order modify column `net_amount` decimal(11,3) DEFAULT NULL COMMENT '玩家输赢额度';
alter table ag_bet_order modify column `bet_amount` decimal(11,3) DEFAULT NULL COMMENT '投注金额';
alter table ag_bet_order modify column `valid_bet_amount` decimal(11,3) DEFAULT NULL COMMENT '有效投注额度';
alter table ag_bet_order modify column `before_credit` decimal(11,3) DEFAULT NULL COMMENT '玩家下注前的剩余额\r\n度';
alter table ag_bet_order modify column `bet_amount_bonus` decimal(11,3) DEFAULT NULL COMMENT ' Jackpot 下注额度';
alter table ag_bet_order modify column `net_amount_bonus` decimal(11,3) DEFAULT NULL COMMENT 'Jackpot 派彩';

ALTER TABLE tra_orderinfom ADD cleanid bigint(20) DEFAULT 0 COMMENT '代理结算id,大于0，表示结算过，可追溯被何处结算';
alter table tra_agentclearing modify column `commission` decimal(5,2) DEFAULT NULL COMMENT '返佣比(%)';

-- v1.2.0新增
alter table sys_feedback modify column `feedimgs` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '反馈图片 多张以“,”分隔';


alter table tra_orderinfom add index idx_t_d_r_t (ordertype, is_delete, roomid, create_time);
alter table tra_orderinfom add index idx_d (is_delete);
alter table tra_orderinfom add index idx_r_o (roomid, ordertype);

alter table inf_sysnotice add index idx_d_w_t_s_e (is_delete, work_status, `type`, expirydates, expirydatee);
alter table sys_app_info add index idx_d_n_t (is_delete, isnew, app_type);

alter table order_bet_record add index idx_u_r (user_id, room_id);

alter table inf_sysremindinfo add index  `index_num` (`recipienter`,`issee`,`is_delete`,`rmtype`) USING BTREE;


ALTER TABLE `live`.`mem_login`
MODIFY COLUMN `clintipadd` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录ip地址' AFTER `loginnum`;

alter table circle_post modify column `image` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图片 多张以“,”分隔';




-- v.1.3.0版本sql
-- 用户表
ALTER TABLE mem_baseinfo ADD unique_id varchar(8) NOT NULL DEFAULT 0 COMMENT '会员ID' AFTER `memid`;
ALTER TABLE mem_baseinfo ADD last_login_dev varchar(32) DEFAULT NULL COMMENT '登录设备' AFTER `clintipadd`;
ALTER TABLE mem_baseinfo ADD register_dev varchar(32) DEFAULT NULL COMMENT '注册设备' AFTER `clintipadd`;
ALTER TABLE mem_baseinfo ADD register_ip varchar(64) DEFAULT NULL COMMENT '注册ip' AFTER `clintipadd`;

ALTER TABLE mem_baseinfo ADD withdrawal_num int(11) NOT NULL DEFAULT 0 COMMENT '提现总次数' AFTER `withdrawal_amount`;
ALTER TABLE mem_baseinfo ADD withdrawal_first decimal(20,3) NOT NULL DEFAULT '0.000' COMMENT '首次提现金额' AFTER `withdrawal_amount`;
ALTER TABLE mem_baseinfo ADD withdrawal_max decimal(20,3) NOT NULL DEFAULT '0.000' COMMENT '最大提现金额' AFTER `withdrawal_amount`;

ALTER TABLE mem_baseinfo ADD pay_num int(11) NOT NULL DEFAULT 0 COMMENT '充值总次数' AFTER `pay_amount`;
ALTER TABLE mem_baseinfo ADD pay_first decimal(20,3) NOT NULL DEFAULT '0.000' COMMENT '首次充值金额' AFTER `pay_amount`;
ALTER TABLE mem_baseinfo ADD pay_max decimal(20,3) NOT NULL DEFAULT '0.000' COMMENT '最大充值金额' AFTER `pay_amount`;

-- 等级表_锁定
ALTER TABLE mem_level ADD locked bit(1)  NOT NULL DEFAULT b'0' COMMENT '0:未锁定,1:锁定' AFTER `is_delete`;

-- 系统用户沉余字段(bd_user) 生产环境
ALTER TABLE bd_user ADD `acclogin` varchar(40) NOT NULL COMMENT '系统账号(登录用)';
ALTER TABLE bd_user ADD `password` varchar(64) DEFAULT NULL COMMENT '登陆密码';
ALTER TABLE bd_user ADD `passwordmd5` varchar(64) NOT NULL COMMENT '登录密码MD5';
ALTER TABLE bd_user ADD `loginnum` int(11) DEFAULT NULL COMMENT '登录总次数';
ALTER TABLE bd_user ADD `accstatus` decimal(1,0) NOT NULL COMMENT '账号状态 1正常 9禁止登陆 ';
ALTER TABLE bd_user ADD `clintipadd` varchar(128) DEFAULT NULL COMMENT '登录ip地址';
ALTER TABLE bd_user ADD `lastlogindate` datetime DEFAULT NULL COMMENT '最后登录时间';

-- 提现申请表(tra_applycash) 生产环境
ALTER TABLE tra_applycash ADD `ordertype` decimal(2,0) DEFAULT NULL COMMENT '提现类型，12投注分成，13礼物分成';

-- 修改表（fivelhc_lottery_sg）五分六合彩赛果期数为字符串 生产环境
alter table fivelhc_lottery_sg modify column issue varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号';

-- 主播结算收入明细表(tra_anchor) 生产环境
ALTER TABLE tra_anchor ADD `onlinedate` datetime DEFAULT NULL COMMENT '上线时间';
ALTER TABLE tra_anchor ADD `offlinedate` datetime DEFAULT NULL COMMENT '下线时间';


drop table IF EXISTS user_unique_code;
CREATE TABLE `user_unique_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(8) COLLATE utf8mb4_bin NOT NULL COMMENT '唯一随机码',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '状态(0:未使用,1:已使用)',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_code` (`code`) USING BTREE,
  KEY `idx_code` (`code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户唯一随机码';

drop table IF EXISTS user_invite_code;
CREATE TABLE `user_invite_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(6) COLLATE utf8mb4_bin NOT NULL COMMENT '唯一随机码',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '状态(0:未使用,1:已使用)',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_code` (`code`) USING BTREE,
  KEY `idx_code` (`code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户邀请唯一随机码';

-- 历史数据处理完再加唯一索引，无历史数据则不必处理
-- ALTER TABLE mem_baseinfo ADD UNIQUE INDEX `uk_recomcode`(`recomcode`) USING BTREE;
-- ALTER TABLE mem_baseinfo ADD UNIQUE INDEX `uk_unique_id`(`unique_id`) USING BTREE;


alter table bd_bannerpicinfo modify column  `linktype` int(1) DEFAULT NULL COMMENT '链接跳转类型 1 文本,2 图片,3链接,4 参数';

ALTER TABLE bd_bannerpicinfo ADD `title` varchar(100) DEFAULT NULL COMMENT '标题';

ALTER TABLE bd_bannerpicinfo ADD `path_url` varchar(128) DEFAULT NULL COMMENT '图片路径';


ALTER TABLE bd_bannerpicinfo ADD  `within_link` varchar(128) DEFAULT NULL COMMENT '内部链接';

-- 代理报表
ALTER TABLE tra_agentclearing ADD daily_new_users int(11) NOT NULL DEFAULT 0 COMMENT '本日下级人数' AFTER `buttonnote`;
ALTER TABLE tra_agentclearing ADD daily_pay_users int(11) NOT NULL DEFAULT 0 COMMENT '本日首充人数' AFTER `buttonnote`;
ALTER TABLE tra_agentclearing ADD daily_withdrawal decimal(16,3) NOT NULL DEFAULT '0.000' COMMENT '本日提现总金额' AFTER `buttonnote`;

-- 代理跳转url
ALTER TABLE mem_baseinfo ADD proxy_url varchar(80) DEFAULT NULL COMMENT '代理跳转url';

-- 意见反馈增加字段
ALTER TABLE sys_feedback ADD unique_id varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '会员ID' after `accno`;

-- 提现打款凭证长度增加至1000
Alter table tra_orderinfom modify column `payimg` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付凭证截图 多张以“，”分隔';
Alter table tra_applycash ADD column `payimg` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付凭证截图 多张以“，”分隔';

-- 支付信息 支付标示二维码(页面)增加至2000
Alter table tra_paymentinfo modify column `paycodeurl` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付标示二维码(页面)';



ALTER TABLE `sys_payprovider`
MODIFY COLUMN `accountno` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户ID  多个以英文逗号分隔' AFTER `paygateway`,
MODIFY COLUMN `secretcode` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户秘钥' AFTER `accountno`,
MODIFY COLUMN `pubsecret` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户公钥' AFTER `secretcode`,
MODIFY COLUMN `prisecret` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户私钥' AFTER `pubsecret`;

drop table IF EXISTS exp_fundstatement;
CREATE TABLE `exp_fundstatement`
(
    `fundid`        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资金报表id',
    `rechargeamt`   decimal(16, 3)                                               DEFAULT NULL COMMENT '充值总金额',
    `firstrecharge` bigint(20)                                                   DEFAULT NULL COMMENT '首充人数',
    `lotamt`        decimal(16, 3)                                               DEFAULT NULL COMMENT '彩票总投注金额',
    `lotawardamt`   decimal(16, 3)                                               DEFAULT NULL COMMENT '彩票总中奖金额',
    `gameamt`       decimal(16, 3)                                               DEFAULT NULL COMMENT '游戏总投注金额',
    `gameawardamt`  decimal(16, 3)                                               DEFAULT NULL COMMENT '游戏总中奖金额',
    `giftamt`       decimal(16, 3)                                               DEFAULT NULL COMMENT '直播盈利',
    `giftsumamt`    decimal(16, 3)                                               DEFAULT NULL COMMENT '直播总收入',
    `profitamt`     decimal(16, 3)                                               DEFAULT NULL COMMENT '站点总盈利(彩票+棋牌+礼物分成)',
    `platformamt`   decimal(16, 3)                                               DEFAULT NULL COMMENT '平台总抽成',
    `operateamt`    decimal(16, 3)                                               DEFAULT NULL COMMENT '站点维护费',
    `payamt`        decimal(16, 3)                                               DEFAULT NULL COMMENT '应缴平台账款',
    `paymentamt`    decimal(16, 3)                                               DEFAULT NULL COMMENT '出款总额',
    `is_delete`     bit(1)     NOT NULL                                          DEFAULT b'0' COMMENT '是否删除',
    `create_user`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
    `create_time`   timestamp  NOT NULL                                          DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_user`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '最后修改人',
    `update_time`   timestamp  NOT NULL                                          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`fundid`) USING BTREE,
    KEY `exp_fundstatememt_createdate` (`create_time`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='资金报表';


  CREATE TABLE ag_fish_bet_order (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  data_type varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '场景',
  hsr_id varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '项目编号',
  trade_no varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '交易编号',
  platform_type varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '平台类型',
  scene_id varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '场景号',
  player_name varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '账户名称',
  type int(11) DEFAULT NULL COMMENT '转账类别',
  scene_start_time datetime DEFAULT NULL COMMENT '场景开始时间',
  scene_end_time datetime DEFAULT NULL COMMENT '场景结束时间',
  room_id varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '房间号',
  roombet varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '房间倍率',
  cost varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '投注额度',
  earn varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '派彩',
  jack_pot_comm varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '场景彩池投注',
  transfer_amount decimal(20,2) DEFAULT NULL COMMENT '转账额度',
  previous_amount decimal(20,2) DEFAULT NULL COMMENT '转账前额度',
  current_amount decimal(20,2) DEFAULT NULL COMMENT '当前额度',
  currency varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT ' 货币类型',
  exchange_rate varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '汇率',
  ip varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '玩家 IP',
  flag int(11) DEFAULT NULL COMMENT '结算状态 (0=成功)',
  creation_time datetime DEFAULT NULL COMMENT '纪录时间',
  game_name varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '游戏名称',
  is_hald int(11) DEFAULT '0' COMMENT '是否同步',
  game_code varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '游戏类型',
  device_type varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '设备类型',
  member_id bigint(20) DEFAULT NULL COMMENT '用户ID',
  create_time timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY index_trade_no (trade_no) USING BTREE,
  KEY index_m_r_t_p_c (trade_no,player_name,room_id,creation_time,member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='AG 捕鱼王注单表';


-- 代充模块

ALTER TABLE `mem_login`
MODIFY COLUMN `logintype` decimal(2, 0) NULL DEFAULT NULL COMMENT '账户类型  普通会员1      主播2   家族长3 代充人4 运营后台管理员8    第三方登录7   服务注册中心管理员9  聚合站点后台管理员10' AFTER `acclogin`;

CREATE TABLE `mem_repayuser` (
  `repaymemid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '代充人id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '会员accno',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '代充人昵称',
  `qq` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'QQ',
  `webchat` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信',
  `alipay` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付宝账号',
  `mobileno` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
  `memgold` decimal(16,3) DEFAULT NULL COMMENT '代充余额',
  `onlinedates` datetime DEFAULT NULL COMMENT '营业时间起',
  `onlinedatee` datetime DEFAULT NULL COMMENT '营业时间止',
  `repaynums` bigint(20) DEFAULT NULL COMMENT '代充次数',
  `discountrag` decimal(5,2) DEFAULT NULL COMMENT '充值优惠(%)   ',
  `status` decimal(1,0) DEFAULT NULL COMMENT '启用状态0启用9未启用',
  `is_delete` bit(1) DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`repaymemid`) USING BTREE,
  KEY `mem_repayuser_union1` (`onlinedates`,`onlinedatee`) USING BTREE,
  KEY `mem_repayuser_accno` (`accno`) USING BTREE,
  KEY `mem_repayuser_nickname` (`nickname`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='代充人信息表';


CREATE TABLE `sys_repayaccount` (
  `bankid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '银行账户id',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '帐号名称',
  `bankname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '银行名称标识符 如ICBC',
  `accountno` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '银行卡账号',
  `bankaddress` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '开户行 如 某某支行',
  `accountname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收款人姓名',
  `minamt` decimal(16,3) DEFAULT NULL COMMENT '单笔最低金额',
  `maxamt` decimal(16,3) DEFAULT NULL COMMENT '单笔入账最高金额',
  `stopamt` decimal(16,3) DEFAULT NULL COMMENT '停用次卡上限金额',
  `status` decimal(1,0) DEFAULT NULL COMMENT '启用状态: 0启用 9停用',
  `is_delete` bit(1) DEFAULT b'0' COMMENT '删除标志 0或空未删除 9已删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`bankid`) USING BTREE,
  KEY `sys_repayaccount_union1` (`minamt`,`maxamt`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='代充银行卡表';

CREATE TABLE `tra_repayorder` (
  `reorderid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '代充订单id',
  `orderid` bigint(20) DEFAULT NULL COMMENT '订单id',
  `refaccno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '代充人accno',
  `refaccount` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '代充人帐号',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '充值人accno',
  `memaccount` bigint(20) DEFAULT NULL COMMENT '充值人会员ID  (8位数字=1位站点标识+7位memid)',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '充值人昵称',
  `realamt` decimal(16,3) DEFAULT NULL COMMENT '入款金额',
  `orderstatus` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '订单状态 ord01新订单 ord04待付款 ord08已付款  ord09用户取消 ord11已退款 ord99已过期   ',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注说明',
  `is_delete` bit(1) DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`reorderid`) USING BTREE,
  KEY `tra_repayorder_fk1` (`orderid`) USING BTREE,
  KEY `tra_repayorder_accno` (`accno`) USING BTREE,
  KEY `tra_repayorder_memaccount` (`memaccount`) USING BTREE,
  KEY `tra_repayorder_refaccno` (`refaccno`) USING BTREE,
  KEY `tra_repayorder_nickname` (`nickname`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='代充订单表';

CREATE TABLE `tra_artrepayorder` (
  `artorderid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '人工存提id',
  `orderid` bigint(20) DEFAULT NULL COMMENT '订单id',
  `logintype` decimal(2,0) DEFAULT NULL COMMENT '账户类型  代充人4 ',
  `ordertype` decimal(1,0) DEFAULT NULL COMMENT '存提类型  0 存入 1提出',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
  `memaccount` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '帐号ID mem_baseinfo 的 unique_id',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '会员识别号',
  `optamt` decimal(16,3) DEFAULT NULL COMMENT '存提金额 #.###',
  `giftamt` decimal(16,3) DEFAULT NULL COMMENT '优惠金额  #.###',
  `auditper` decimal(16,2) DEFAULT NULL COMMENT '打码倍数(常态性稽核）##.##   ',
  `note` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注说明',
  `is_delete` bit(1) DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`artorderid`) USING BTREE,
  KEY `tra_artrepayorder_fk1` (`orderid`) USING BTREE,
  KEY `tra_artrepayorder_memaccount` (`memaccount`) USING BTREE,
  KEY `tra_artrepayorder_nickname` (`nickname`) USING BTREE,
  KEY `tra_artrepayorder_accno` (`accno`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1846 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='代充存提订单表';

CREATE TABLE `tra_orderaccount` (
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '会员标识号',
  `bankid` bigint(20) DEFAULT NULL COMMENT '银行账户id',
  `orderid` bigint(20) DEFAULT NULL COMMENT '订单id',
    `is_delete` bit(1) DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后修改人',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  KEY `tra_orderaccount_fk1` (`orderid`) USING BTREE,
  KEY `tra_orderaccount_accno` (`accno`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='代充人入款订单银行卡';

ALTER TABLE `tra_orderinfom`
MODIFY COLUMN `ordertype` decimal(2, 0) NULL DEFAULT NULL COMMENT '订单类型 1在线支付 2线下支付 3在线提现 4线下提现 5彩票购彩 6彩票兑奖 7棋牌上分 8棋牌下分 9其他收入(发帖/推荐)  10其他支出(打赏)  11代理结算收入 14 代充人入款 15 代充人给会员充值\r\n' AFTER `tpaysetid`;
-- 代充模块结束

ALTER TABLE es_bet_order ADD `user_id` int(12) DEFAULT NULL COMMENT '用户ID';

ALTER TABLE mem_login ADD INDEX idx_accno(accno);
ALTER TABLE mem_relationship ADD INDEX idx_refaccno(refaccno);
ALTER TABLE mem_relationship ADD INDEX idx_accno(accno);
ALTER TABLE mem_relationship ADD INDEX idx_memname(memname);


-- 代充存提订单类型
ALTER TABLE `tra_orderinfom`
MODIFY COLUMN `ordertype` decimal(2, 0) NULL DEFAULT NULL COMMENT '订单类型 1在线支付 2线下支付 3在线提现 4线下提现 5彩票购彩 6彩票兑奖 7棋牌上分 8棋牌下分 9其他收入(发帖/推荐)  10其他支出(打赏)  11代理结算收入 14 代充人入款 15代充人给会员充值 16代充存入 17代充提出\r\n' AFTER `tpaysetid`;

ALTER TABLE `tra_repayorder`
MODIFY COLUMN `memaccount` varchar(8) NULL DEFAULT NULL COMMENT '充值人UNIQUE_ID' AFTER `accno`;

/*代理--本日新增首充金额*/
ALTER TABLE tra_agentclearing ADD daily_pay_total decimal(16,3) NOT NULL DEFAULT '0.000' COMMENT '本日新增首充金额' AFTER `daily_pay_users`;

/*订单联合索引*/
alter table tra_orderinfom add index idx_ac_ot_os (accno, ordertype, orderstatus);

-- v.1.3.2版本sql
CREATE TABLE `sys_sensitive_word` (
  `id` int NOT NULL AUTO_INCREMENT,
  `word` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '敏感词',
  `status` decimal(1,0) DEFAULT '0' COMMENT '敏感词启用状态0启用9未启用',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='敏感词';

ALTER TABLE `mem_level` MODIFY COLUMN `levellog` varchar(500) NULL DEFAULT NULL COMMENT '等级log' AFTER `nextlevscore`;

-- 机器人设置表
CREATE TABLE `bas_robotset`  (
  `robot_setid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '机器人设置id',
  `robot_nums` bigint(20) DEFAULT NULL COMMENT '机器人数量',
  `gift_bet_frequency` bigint(20) DEFAULT NULL COMMENT '送礼/下注 频率(分钟次数)',
  `stay_time` bigint(20) DEFAULT NULL COMMENT '房间停留时间（分钟数）',
  `gift_status` decimal(1, 0) DEFAULT NULL COMMENT '是否启用送礼 0启用9未启用',
  `bet_status` decimal(1, 0) DEFAULT NULL COMMENT '是否启用下注 0启用9未启用',
  `status` decimal(1, 0) DEFAULT NULL COMMENT '机器人启用 0启用9未启用',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`robot_setid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '机器人设置' ROW_FORMAT = Dynamic;

-- 机器人投注中奖初始化模板表
CREATE TABLE `order_robot_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `order_id` int(11) NOT NULL COMMENT '投注单id',
  `cate_id` int(11) NOT NULL COMMENT '彩种类别id',
  `lottery_id` int(11) NOT NULL COMMENT '彩种id',
  `play_id` int(11) NOT NULL COMMENT '玩法id',
  `setting_id` int(11) NOT NULL COMMENT '玩法配置id',
  `play_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '玩法名称',
  `issue` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '购买的期号',
  `order_sn` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '订单号',
  `bet_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '投注号码',
  `bet_count` int(11) NOT NULL COMMENT '投注总注数',
  `bet_amount` decimal(20, 2) NOT NULL COMMENT '投注金额',
  `win_amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '中奖金额',
  `back_amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '返点金额',
  `is_roboter` int(11) NOT NULL DEFAULT 0 COMMENT '是否机器人注单, 0是 1否',
  `tb_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT 'WAIT' COMMENT '状态：中奖:WIN | 未中奖:NO_WIN | 等待开奖:WAIT | 和:HE | 撤单:BACK',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `win_count` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '中奖注数',
  `is_push` int(1) DEFAULT 0 COMMENT '是否推单 0 否 1 是',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '来源：Android | IOS | WEB',
  `familyid` bigint(20) DEFAULT NULL COMMENT '直播间购彩对应的家族id',
  `room_id` bigint(20) DEFAULT NULL COMMENT '直播房间id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx01_play_id`(`play_id`, `lottery_id`, `tb_status`) USING BTREE,
  INDEX `god_order_id`(`is_roboter`, `tb_status`) USING BTREE,
  INDEX `idx02_create_time`(`create_time`) USING BTREE,
  INDEX `idx04_sort1`(`user_id`, `bet_amount`, `create_time`) USING BTREE,
  INDEX `idx05_sort2`(`user_id`, `create_time`) USING BTREE,
  INDEX `idx06_sort3`(`user_id`, `win_amount`, `create_time`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE,
  INDEX `idx07_lottery_id`(`lottery_id`, `tb_status`) USING BTREE,
  INDEX `idx_bet_amount`(`bet_amount`) USING BTREE,
  INDEX `idx_win_amount`(`win_amount`) USING BTREE,
  INDEX `idx_tb_status`(`tb_status`) USING BTREE,
  INDEX `idx_setting_id`(`setting_id`) USING BTREE,
  INDEX `idx_is_push`(`is_push`) USING BTREE,
  INDEX `idx_update_time`(`update_time`) USING BTREE,
  INDEX `idx_cate_id`(`cate_id`) USING BTREE,
  INDEX `idx_order_sn`(`order_sn`) USING BTREE,
  INDEX `idx_issue`(`issue`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58368 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '机器人投注中奖模板记录表' ROW_FORMAT = Dynamic;

ALTER TABLE tra_agentclearing MODIFY COLUMN `refids` text NULL DEFAULT NULL COMMENT '关联订单id 多条以逗号分隔' AFTER `cleantype`;


-- 1.3.2 试玩订单
ALTER TABLE order_bet_record  ADD `type` int(11) NOT NULL DEFAULT '0' COMMENT '0:普通订单 1:试玩订单';
ALTER TABLE order_bet_record  ADD `trial_id` int(11) DEFAULT NULL COMMENT '试玩房间';

ALTER TABLE `lottery_favorite`
ADD COLUMN `sort` int(11) COMMENT '排序' AFTER `lottery_id`,
MODIFY COLUMN `update_at` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间' AFTER `sort`,
ADD COLUMN `update_user` varchar(64) COMMENT '操作用户' AFTER `update_at`;

create unique index index_battle_id on ae_bet_order(battle_id);

ALTER TABLE `live`.`god_plan`
ADD INDEX `idx_plan_id`(`plan_id`) USING BTREE,
ADD INDEX `idx_plan_type`(`plan_type`) USING BTREE,
ADD INDEX `idx_create_time`(`create_time`) USING BTREE,
ADD INDEX `idx_play_name`(`play_name`) USING BTREE;

ALTER TABLE `live`.`god_plan_issue`
ADD INDEX `idx_god_id`(`god_id`) USING BTREE,
ADD INDEX `idx_create_time`(`create_time`) USING BTREE,
ADD INDEX `idx_update_time`(`update_time`) USING BTREE,
ADD INDEX `idx_ideal_time`(`ideal_time`) USING BTREE,
ADD INDEX `idx_tb_status`(`tb_status`) USING BTREE,
ADD INDEX `idx_status`(`status`) USING BTREE,
ADD INDEX `idx_number`(`number`) USING BTREE;

ALTER TABLE `live`.`god_plan_series_play`
ADD INDEX `idx_lottery_id`(`lottery_id`),
ADD INDEX `idx_name`(`name`);

ALTER TABLE `live`.`god_plan_reward`
ADD INDEX `idx_plan_id`(`plan_id`) USING BTREE,
ADD INDEX `idx_deleted`(`is_delete`) USING BTREE,
ADD INDEX `idx_create_time`(`create_time`) USING BTREE,
ADD INDEX `idx_lottery_id`(`lottery_id`) USING BTREE,
ADD INDEX `idx_god_plan_id`(`god_plan_id`) USING BTREE,
ADD INDEX `idx_play_tag_id`(`play_tag_id`) USING BTREE;

ALTER TABLE `live`.`order_bet_record`
DROP INDEX `idx_issue`,
ADD INDEX `idx_issue`(`issue`, `lottery_id`, `tb_status`, `play_id`) USING BTREE;

ALTER TABLE `live`.`order_bet_record`
ADD INDEX `idx_p_s_d_o`(`play_id`, `tb_status`, `is_delete`, `order_id`) USING BTREE;

-- mem_level add index
ALTER TABLE `live`.`mem_level`
DROP INDEX `tp_reference_02`,
ADD INDEX `tp_reference_02`(`accno`, `locked`, `is_delete`) USING BTREE,
ADD INDEX `idx_delete`(`is_delete`) USING BTREE,
ADD INDEX `idx_locked`(`locked`) USING BTREE,
ADD INDEX `idx_level`(`memlevel`) USING BTREE,
ADD INDEX `idx_score`(`memscore`) USING BTREE;

-- 3.4.0 版本sql
ALTER TABLE `live`.`lottery`
MODIFY COLUMN `icon` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '彩票图标' AFTER `name`,
MODIFY COLUMN `sort` int(6) DEFAULT 0 COMMENT '排序' AFTER `min_odds`,
ADD COLUMN `is_live_lottery` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否是直播间彩种(true:是; false:否)' AFTER `is_delete`,
ADD COLUMN `is_live_show` bit(1) NOT NULL DEFAULT b'0' COMMENT '直播间是否显示该彩种(true:是; false:否)' AFTER `is_live_lottery`,
ADD COLUMN `update_user` varchar(50) DEFAULT '' COMMENT '操作用户' AFTER `end_time`;

ALTER TABLE `live`.`finance_balance_adjustment`
ADD COLUMN `is_level`  bit(1) DEFAULT NULL DEFAULT b'0' COMMENT '是否计入升级经验(true:是; false:否)' AFTER `dama_ratio`;

-- ----------------------------
-- Table structure for sys_chess_balance
-- ----------------------------
DROP TABLE IF EXISTS `sys_chess_balance`;
CREATE TABLE `sys_chess_balance`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `chess_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '棋牌代码',
  `chess_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '棋牌名称',
  `chess_balance` decimal(16, 3) NOT NULL DEFAULT 0.000 COMMENT '棋牌余额',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '第三方棋牌余额' ROW_FORMAT = Dynamic;

-- add v3.4.0 新增红包相关表
CREATE TABLE `red_level_packet`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '等级红包id',
  `level_min` bigint(20) NULL DEFAULT NULL COMMENT '最小等级',
  `level_max` bigint(20) NULL DEFAULT NULL COMMENT '最大等级',
  `reward_min` decimal(16, 3) NULL DEFAULT NULL COMMENT '最小奖金额  ',
  `reward_max` decimal(16, 3) NULL DEFAULT NULL COMMENT '最大奖金额',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标志 0或空未删除 9已删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `red_levelpackets_union`(`level_min`, `level_max`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员等级红包' ROW_FORMAT = DYNAMIC;


CREATE TABLE `red_order_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '红包订单id',
  `orderid` bigint(20) NOT NULL COMMENT '订单id',
  `red_packet_id` bigint(20) NOT NULL COMMENT '红包id',
  `unique_id` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '会员ID',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员标识号',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '呢称',
  `packet_amt` decimal(16, 3) NULL DEFAULT NULL COMMENT '红包金额',
  `packet_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '红包名称(来源)',
  `auditfree` decimal(1, 0) NULL DEFAULT NULL COMMENT '是否计算稽核费   0是 9否',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标志 0或空未删除 9已删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `red_orderinfo_fk1`(`orderid`) USING BTREE,
  INDEX `red_orderinfo_fk2`(`red_packet_id`) USING BTREE,
  INDEX `red_orderinfo_accno`(`accno`) USING BTREE,
  INDEX `red_orderinfo_uniqueid`(`unique_id`) USING BTREE,
  INDEX `red_orderinfo_redpacketid`(`red_packet_id`) USING BTREE,
  INDEX `red_orderinfo_nickname`(`nickname`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '红包订单信息' ROW_FORMAT = DYNAMIC;

CREATE TABLE `red_packet`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '红包id',
  `packet_seat_id` bigint(20) NOT NULL COMMENT '红包位置id',
  `packet_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '红包名称',
  `start_date` timestamp(0) NULL DEFAULT NULL COMMENT '红包活动开放日期起',
  `end_date` timestamp(0) NULL DEFAULT NULL COMMENT '红包活动开放日期止',
  `start_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '抢红包时间起  HH:mm:ss',
  `end_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '抢红包时间止 HH:mm:ss',
  `total_days` int(5) NULL DEFAULT NULL COMMENT '红包总天数',
  `day_amt` decimal(16, 3) NULL DEFAULT NULL COMMENT '每日红包最大总金额',
  `pay_amt` decimal(16, 3) NULL DEFAULT NULL COMMENT '已抢总金额',
  `rob_day_nums` bigint(20) NULL DEFAULT NULL COMMENT '每人每天可抢次数',
  `rob_pay_nums` bigint(20) NULL DEFAULT NULL COMMENT '已抢总次数',
  `auditfree` decimal(1, 0) NOT NULL COMMENT '是否计算稽核费   0不打码 1打码1倍',
  `status` decimal(1, 0) NOT NULL COMMENT '启用状态0启用 9停用',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标志 0或空未删除 9已删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `red_packets_fk1`(`packet_seat_id`) USING BTREE,
  INDEX `red_packets_union1`(`start_date`, `end_date`) USING BTREE,
  INDEX `red_packets_union2`(`start_time`, `end_time`) USING BTREE,
  INDEX `red_packets_packetname`(`packet_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '平台红包' ROW_FORMAT = DYNAMIC;

CREATE TABLE `red_packet_seat`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '红包位置id',
  `seat_name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '红包位置名称',
  `seat_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '红包位置代码',
  `seat_des` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '红包位置描述',
  `status` decimal(1, 0) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '启用状态0启用9未启用',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标志 0或空未删除 9已删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `red_packetseat_seatcode`(`seat_code`) USING BTREE,
  INDEX `red_packetseat_seatname`(`seat_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '红包位置管理' ROW_FORMAT = DYNAMIC;

CREATE TABLE `red_recharge_packet`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会员充值红包id',
  `recharge_min` decimal(16, 3) NULL DEFAULT NULL COMMENT '最小充值金额',
  `recharge_max` decimal(16, 3) NULL DEFAULT NULL COMMENT '最大充值金额',
  `rob_nums` bigint(20) NULL DEFAULT NULL COMMENT '红包次数',
  `is_delete` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标志 0或空未删除 9已删除',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `red_rechargepackets_union1`(`recharge_min`, `recharge_max`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员充值红包' ROW_FORMAT = DYNAMIC;

ALTER TABLE `live`.`lottery`
ADD INDEX `idx_is_live_lottery`(`is_live_lottery`) USING BTREE,
ADD INDEX `idx_is_live_show`(`is_live_show`) USING BTREE;

-- v3.5.0 版本sql
CREATE TABLE `lottery_sg_stats`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lottery_id` int(11) NOT NULL COMMENT '彩种id',
  `category_id` int(11) NOT NULL COMMENT '彩种类别id',
  `issue` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '期号',
  `number` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'WAIT' COMMENT '开奖号码',
  `bet_count` int(11) NOT NULL DEFAULT 0 COMMENT '投注单数',
  `bet_amount` decimal(20, 3) NOT NULL DEFAULT 0.000 COMMENT '当期投注总额',
  `win_amount` decimal(20, 3) NOT NULL DEFAULT 0.000 COMMENT '当期中奖总额',
  `he_amount` decimal(20, 3) NOT NULL DEFAULT 0.000 COMMENT '当期打和总额',
  `back_amount` decimal(20, 3) DEFAULT 0.000 COMMENT '当期撤单总额',
  `profit_amount` decimal(20, 3) NOT NULL DEFAULT 0.000 COMMENT '当期盈利总额',
  `lottery_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '彩种名称',
  `category_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '彩种分类名称',
  `open_time` datetime(0) NOT NULL COMMENT '开奖时间',
  `create_time` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_l_i`(`lottery_id`, `issue`) USING BTREE,
  INDEX `idx_c_l_i`(`category_id`, `lottery_id`, `issue`) USING BTREE,
  INDEX `idx_issue`(`issue`) USING BTREE,
  INDEX `idx_ctime`(`create_time`) USING BTREE,
  INDEX `idx_otime`(`open_time`) USING BTREE,
  INDEX `idx_utime`(`update_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '赛果期数统计表';

-- 3.5 支付设定
ALTER TABLE sys_payset ADD allow_charge_nums int(11) DEFAULT NULL COMMENT '会员单日出款最大次数' after `minchargeamt`;






CREATE TABLE `sys_for_payprovider` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '支付商id',
  `provider` varchar(100) NOT NULL COMMENT '代付商名称',
  `providercode` varchar(100) DEFAULT NULL COMMENT '商戶code',
  `paydns` varchar(100) DEFAULT NULL COMMENT '代付域名',
  `backurl` varchar(100) DEFAULT NULL COMMENT '余额地址',
  `allowips` varchar(200) DEFAULT NULL COMMENT '回调IP 多个逗号隔开',
  `torderurl` varchar(100) DEFAULT NULL COMMENT '代付订单查询地址',
  `paygateway` varchar(100) DEFAULT NULL COMMENT '支付网关',
  `accountno` varchar(200) DEFAULT NULL COMMENT '商戶ID',
  `secretcode` varchar(100) DEFAULT NULL COMMENT '商戶秘钥',
  `pubsecret` varchar(200) DEFAULT NULL COMMENT '商戶公钥',
  `prisecret` varchar(200) DEFAULT NULL COMMENT '商戶私钥',
  `serversecret` varchar(200) DEFAULT NULL COMMENT '伺服器公钥',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '啟用狀態: 0啟用 9停用',
  `is_delete` int(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_user` varchar(32) DEFAULT '' COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` varchar(32) DEFAULT '' COMMENT '最后修改人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代付支付商設置';



CREATE TABLE `tra_for_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(64) DEFAULT NULL COMMENT '订单',
  `merchant_no` varchar(64) DEFAULT NULL COMMENT '代付商单号',
  `unique_id` varchar(64) DEFAULT NULL COMMENT '用户ID ',
  `momey` decimal(20,2) DEFAULT NULL COMMENT '提现金额',
  `pay_for_name` varchar(64) DEFAULT NULL,
  `pay_for_id` bigint(20) DEFAULT NULL COMMENT '代付商ID',
  `status` int(1) DEFAULT '0' COMMENT '代付状态（0代付中 1 成功 2 失败）',
  `message` text COMMENT '返回信息',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '回调时间',
  `withdraw_time` datetime DEFAULT NULL COMMENT '提现时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代付订单表';



ALTER TABLE tra_orderinfom ADD is_replace int(1) DEFAULT '0' COMMENT '是否代付（0 正常1代付）' after `paynote`;

DROP TABLE IF EXISTS `mem_active`;
CREATE TABLE `mem_active`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '活跃ID',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识',
  `login_date` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录日期',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录ip',
  `login_dev` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录设备',
  `is_delete` bit(1) NULL DEFAULT b'0',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_accno`(`accno`) USING BTREE,
  INDEX `index_login_date`(`login_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户每日活跃统计' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mem_active_retention
-- ----------------------------
DROP TABLE IF EXISTS `mem_active_retention`;
CREATE TABLE `mem_active_retention`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员标识',
  `recomcode` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '推荐码',
  `statistics_date` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '统计日期',
  `register_num` int(11) NULL DEFAULT 0 COMMENT '注册数量',
  `one_num` int(11) NULL DEFAULT 0 COMMENT '一天留存',
  `three_num` int(11) NULL DEFAULT 0 COMMENT '三天留存',
  `seven_num` int(11) NULL DEFAULT 0 COMMENT '七天留存',
  `fifteen_num` int(11) NULL DEFAULT 0 COMMENT '十五天留存',
  `thirty_num` int(11) NULL DEFAULT 0 COMMENT '三十天留存',
  `is_delete` bit(1) NULL DEFAULT b'0',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后修改人',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_accno`(`accno`) USING BTREE,
  INDEX `idx_recomcode`(`recomcode`) USING BTREE,
  INDEX `idx_date`(`statistics_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户留存' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mem_agent_statistics
-- ----------------------------
DROP TABLE IF EXISTS `mem_agent_statistics`;
CREATE TABLE `mem_agent_statistics`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `accno` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员标识',
  `recomcode` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '推荐码',
  `statistics_date` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '统计日期',
  `register_num` int(11) NOT NULL DEFAULT 0 COMMENT '注册数量',
  `active_num` int(11) NOT NULL DEFAULT 0 COMMENT '活跃人数',
  `recharge_num` int(11) NOT NULL DEFAULT 0 COMMENT '充值人数',
  `consume_num` int(11) NOT NULL DEFAULT 0 COMMENT '消费人数',
  `recharge_amount` decimal(16, 3) NOT NULL DEFAULT 0.000 COMMENT '充值金额',
  `discount_amount` decimal(16, 3) NOT NULL DEFAULT 0.000 COMMENT '活动优惠金额',
  `withdraw_amount` decimal(16, 3) NOT NULL DEFAULT 0.000 COMMENT '提现金额',
  `gift_amount` decimal(16, 3) NOT NULL DEFAULT 0.000 COMMENT '礼物赠送金额',
  `bet_num` int(11) NOT NULL DEFAULT 0 COMMENT '投注人数(棋牌+彩票)',
  `bet_amount` decimal(16, 3) NOT NULL DEFAULT 0.000 COMMENT '投注金额',
  `bet_win_amount` decimal(13, 3) NOT NULL DEFAULT 0.000 COMMENT '投注中奖金额',
  `is_delete` bit(1) NOT NULL DEFAULT b'0',
  `create_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '最后修改人',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_accno`(`accno`) USING BTREE,
  INDEX `idx_recomcode`(`recomcode`) USING BTREE,
  INDEX `idx_date`(`statistics_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户代理统计' ROW_FORMAT = Dynamic;


