/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 80026
Source Host           : localhost:3306
Source Database       : live

Target Server Type    : MYSQL
Target Server Version : 80026
File Encoding         : 65001

Date: 2021-08-27 14:55:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for mem_baseinfo
-- ----------------------------
DROP TABLE IF EXISTS `mem_baseinfo`;
CREATE TABLE `mem_baseinfo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `Identity_type` int DEFAULT '0' COMMENT '会员类型 0 普通会员 1 代理会员',
  `account` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户账号',
  `real_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '真实姓名',
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '昵称',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户密码',
  `security_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '安全码',
  `mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL,
  `qq` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'qq',
  `wechat` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微信',
  `invite_code` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '邀请码',
  `cash_rate` decimal(20,3) DEFAULT '0.000' COMMENT '提现费率',
  `rebate_status` int DEFAULT '0' COMMENT '第三方反水状态 0 停止 1正常',
  `below_odds_status` int DEFAULT '0' COMMENT '能否修改下级反水 0 不能 1 能',
  `growth_status` int DEFAULT '0' COMMENT '成长值和等级更新状态 0 正常 1 停止',
  `max_cash_num` int DEFAULT '0' COMMENT '当天最大提款次数',
  `pass_error_num` int DEFAULT '0' COMMENT '今日密码错误次数',
  `head_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '头像地址',
  `hierarchy_id` bigint DEFAULT NULL COMMENT '层级id',
  `level_id` bigint DEFAULT NULL COMMENT '等级id',
  `status` tinyint(1) DEFAULT '0' COMMENT '用户状态 0 正常 1 冻结',
  `point` int DEFAULT '0' COMMENT '用户积分',
  `source` int DEFAULT NULL COMMENT '注册来源 1 苹果 2 安卓 3 h5',
  `balance` bigint NOT NULL DEFAULT '1000000000' COMMENT '余额',
  `device_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '设备号',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '是否删除 0 未删除 1 删除',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of mem_baseinfo
-- ----------------------------
INSERT INTO `mem_baseinfo` VALUES ('2', null, 'puff', null, '泡芙', '49ba59abbe56e057', null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eorwiaJcRPxKMNHgov0HGBRA8JODQrhw67x61FGEFwic2E2UlhXSKmQ455jqT5RIPsZjmpkdia0pyZdA/132', null, null, '1', '0', null, '1000000000', null, null, '0', '2021-01-12 17:52:03', '2021-08-26 08:42:05', null);
INSERT INTO `mem_baseinfo` VALUES ('3', null, null, null, '非洲小白脸', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/PiajxSqBRaEIIs1glKcYOadLFibr2et98eXTADdicLUGrQqF8EtvicIu5e5TwOkuBAzIf8zEl0aYPJaDkfIHTOEWuQ/132', null, null, '1', '0', null, '1000000000', null, null, '0', '2021-01-12 17:52:06', null, null);
INSERT INTO `mem_baseinfo` VALUES ('4', null, null, null, '花花的世界', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLxWhtkFhVKpfXib0BibMaIzeOAVCGVScnR5ibsibdENiaibjvnfy7AxeSSCTbn9IBvqMe1iaJ6BWTxIjZtg/132', null, null, '1', '0', null, '999680200', null, null, '0', '2021-01-12 17:52:09', '2021-02-24 22:43:41', null);
INSERT INTO `mem_baseinfo` VALUES ('5', null, null, null, '微尘', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/vNZqQTZRAia4sz17MJeeXeqhzaBbIEzEXGvgwG4l1KQg2mQAb3eB1q9HLnVJUo4u8OSNSv1seuqHxNPKyYicb4Dw/132', null, null, '1', '0', null, '1000000000', null, null, '0', '2021-01-12 17:52:12', null, null);
INSERT INTO `mem_baseinfo` VALUES ('6', null, null, null, '大田', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIPhsrhOFictxk44ialrd4gZP6SyFmz4v2rHBZ3C72O0KsKQDTHlVBqtoSJ5uiaPAvD0t9F5VBjbruQw/132', null, null, '1', '0', null, '1000000000', null, null, '0', '2021-01-12 17:52:16', null, null);
INSERT INTO `mem_baseinfo` VALUES ('7', null, null, null, '看好路，向前走！', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/lZibiaShtph66QznR6yiarR7VsBkkjqGqPCwqDGD8WlaxnllcjG7SRiaX0DOujFXX8epAbyvFpHv03uI83xXFhdwZA/132', null, null, '1', '0', null, '1000000000', null, null, '0', null, null, null);
INSERT INTO `mem_baseinfo` VALUES ('8', null, null, null, 'CIAO！', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/0d5kVzsH20SUXzPjbgamFn7DraWURYE7GJX15rMSVVDCeHN3kKW3ZozlUichS7Ch5jXADocWYW3jzBTj24oZVKw/132', null, null, '1', '0', null, '1000000000', null, null, '0', null, null, null);
INSERT INTO `mem_baseinfo` VALUES ('10', null, null, null, '时光会咬人', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJyb4yl7JJCDKNX3yRwGjZ8fdXBTSVaW9cQIErvibmDR08m0vsrqWonxvRibrFxric0wqAKgVFa1IBlg/132', null, null, '1', '0', null, '1000000000', null, null, '0', '2021-01-30 17:22:18', null, null);
INSERT INTO `mem_baseinfo` VALUES ('12', null, null, null, 'I', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJGjiauanv1bnlJxKiaBQtalptWZUnCnE725cS8SjWoVAjLDuFLg3sKDfumhKuMs7NGHIc0gz8dNopQ/132', null, null, '1', '0', null, '1000000000', null, null, '0', '2021-02-01 19:45:57', '2021-02-01 19:45:57', null);
INSERT INTO `mem_baseinfo` VALUES ('14', null, null, null, '77777777', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83ercp0SnvuleWloRkX8y5pibLHtg2OoKGECJH7udBoAoicsO87ibjmsUMiaDgJAJ8ibaiavGv1aEQicle8lMA/132', null, null, '1', '0', null, '1000000000', null, null, '0', '2021-02-02 19:49:50', '2021-02-02 19:49:50', null);
INSERT INTO `mem_baseinfo` VALUES ('15', null, null, null, 'Max_Qiu', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIspnkuj3p0Ly4v6dIz5nClVLnNvIE5BVyd6ORaz6kLrwsxbicfqnG7ic4JpqWedpqk1lgx71QlHauQ/132', null, null, '1', '0', null, '1000000000', null, null, '0', '2021-02-04 12:08:16', '2021-02-04 12:08:16', null);
INSERT INTO `mem_baseinfo` VALUES ('16', null, null, null, '神经蛙', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q3auHgzwzM4Z6nX9KwiaJy2momCR0BLGnXF7ibI9WCJNTpqiaWDYS80624vRibsWr1muV2N8qM5wia0n5lSxOvttjNA/132', null, null, '1', '0', null, '1000000000', null, null, '0', '2021-02-16 15:48:07', '2021-02-16 15:48:07', null);
INSERT INTO `mem_baseinfo` VALUES ('17', null, null, null, '自渡.', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/jAiavgRK2sHGs29TfZOlfGibBEpkq5btJQcVib2OoOibDTrsbC3d1R2LEtyEN48Cx8pQBZE174k13ribJamUkrD1ctg/132', null, null, '1', '0', null, '1000000000', null, null, '0', '2021-02-17 13:11:54', '2021-02-17 13:11:54', null);
INSERT INTO `mem_baseinfo` VALUES ('18', null, null, null, 'lxm', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKCMRO8bKSzryP9QD8DqOHyaVP8ibK41qpviaqNpTN3IDibiapjqLibibIZS7LrQTfiaNV6YNhYn8vzqcviaw/132', null, null, '1', '0', null, '1000000000', null, null, '0', '2021-02-20 16:47:34', '2021-02-20 16:47:34', null);
INSERT INTO `mem_baseinfo` VALUES ('19', null, null, null, '林育挺', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/pHghIy3YR0f1pyWuENRiaqic03azQnbW6YtjyWrfl0bXZjF4J9UA5QPG9jXUe8BymtngqJ0zPwnS0VSPLIBBJEiaw/132', null, null, '1', '0', null, '1000000000', null, null, '0', '2021-02-20 22:12:13', '2021-02-20 22:12:13', null);
INSERT INTO `mem_baseinfo` VALUES ('20', null, null, null, '香蕉皮i', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/Z1BicLpfe2ygKc91pm1LhKdLKUtFPdyn4lSkVkA5Pn5iaI5lT3h4M4dFAanxGKEMfPIgOCZjxjiaIHLuqq9Fn5E0Q/132', null, null, '1', '0', null, '1000000000', null, null, '0', '2021-02-24 12:09:26', '2021-02-24 12:09:26', null);
INSERT INTO `mem_baseinfo` VALUES ('22', null, null, null, 'F', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/geyibXYyaoODjLy2aYP54WjUxYz71pwOMDrnRWBXibgh2gDr4hZuw5qiawic75oacEXYxRicykCRINube7MFd9ANicrw/132', null, null, '1', '0', null, '999060500', null, null, '0', '2021-03-22 21:37:51', '2021-03-22 21:37:51', null);
INSERT INTO `mem_baseinfo` VALUES ('23', null, null, null, '蓝动', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/9cfWYQjJMKsplTQQLJqR3A75j9Hib44jHF0vIEJqfHC2ttfg0GCiaSzQbSQVVxrgicAJallo3eB2qsGyE1Z4RNYCQ/132', null, null, '1', '0', null, '1000000000', null, null, '0', '2021-03-23 17:35:03', '2021-03-23 17:35:03', null);
INSERT INTO `mem_baseinfo` VALUES ('24', null, null, null, '路亚小生', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/tnib4ZCXWGOznmtyoHBL5BFicYZWICNyic0EyPWk70kr9IWzHSCVdIqFKN2o7BxyuYaDib0ogmfpuMTBgo3pOibPt9A/132', null, null, '1', '0', null, '1000000000', null, null, '0', '2021-03-24 12:06:23', '2021-03-24 12:06:23', null);
INSERT INTO `mem_baseinfo` VALUES ('25', null, null, null, 'Alan', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/QohQ9hnZnxF2mJOM1RywBPqToNVicDpeF8KdXrwmtYnRyoWaBHk0R25T1wxzleCJV3Un8iappa70yn8fJmgGAZnQ/132', null, null, '1', '0', null, '999380300', null, null, '0', '2021-03-29 15:57:24', '2021-03-29 15:57:24', null);
INSERT INTO `mem_baseinfo` VALUES ('39', null, null, null, '郝先瑞', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/J31cY2qVWviaOqhjPlr18VY5ic1SUvDESG1mQkicQfFugWibYe7VJIhYJBZYDBib0T4TJVhUOtiaW1TGkJRqIWd3K0dQ/132', null, null, '1', '0', null, '994999500', null, null, '0', '2021-06-10 01:55:41', '2021-06-10 02:04:31', null);
INSERT INTO `mem_baseinfo` VALUES ('40', null, null, null, '秋城', null, null, null, null, null, null, null, null, null, null, null, null, null, 'https://thirdwx.qlogo.cn/mmopen/vi_32/ajNVdqHZLLDfyM5iaYFwhzQ1Xv9zyA3bXDV42niazQlibiajdXba0YK4yAFFWIMY7vwfI1ny8Ej8pm0pmp7OkC2afg/132', null, null, '1', '0', null, '1000000000', null, null, '0', '2021-06-19 16:57:51', '2021-06-19 16:57:51', null);

-- ----------------------------
-- Table structure for mem_level
-- ----------------------------
DROP TABLE IF EXISTS `mem_level`;
CREATE TABLE `mem_level` (
  `id` int NOT NULL AUTO_INCREMENT,
  `level` int NOT NULL DEFAULT '0' COMMENT '会员等级',
  `hierarchy` int DEFAULT '0' COMMENT '会员层级',
  `need_integral` int DEFAULT '0' COMMENT '所需积分',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '等级名称（头衔）',
  `reward` int NOT NULL DEFAULT '0' COMMENT '晋级奖励',
  `skip_reward` int NOT NULL DEFAULT '0' COMMENT '跳级奖励',
  `valid_date` int NOT NULL DEFAULT '0' COMMENT '有效时间',
  `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '会员卡背景',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '会员图标',
  `create_time` datetime NOT NULL COMMENT '创建时间时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除.1=删除,0=未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户等级表';

-- ----------------------------
-- Records of mem_level
-- ----------------------------
INSERT INTO `mem_level` VALUES ('1', '1', null, null, '普通会员', '20', '0', '0', 'http://pic.dayouqiantu.cn/5c9ccca8cd632.jpg', 'http://pic.dayouqiantu.cn/5c9ccca8bc1e0.png', '2020-06-25 18:54:35', null, '1');
INSERT INTO `mem_level` VALUES ('2', '2', null, null, '青铜会员', '0', '0', '0', 'http://pic.dayouqiantu.cn/5c9ccca904016.jpg', 'http://pic.dayouqiantu.cn/5c9ccca8f0a30.png', '2020-06-25 18:54:35', null, '0');
INSERT INTO `mem_level` VALUES ('3', '3', null, null, '黄铜会员', '0', '0', '0', 'http://pic.dayouqiantu.cn/5c9ccca8b27f1.jpg', 'http://pic.dayouqiantu.cn/5c9ccca8e9365.png', '2020-06-25 18:54:35', null, '0');
INSERT INTO `mem_level` VALUES ('4', '4', null, null, '白银会员', '0', '0', '0', 'http://pic.dayouqiantu.cn/5c9ccca8d6ae1.jpg', 'http://pic.dayouqiantu.cn/5c9ccca8a27f0.png', '2020-06-25 18:54:35', null, '0');
INSERT INTO `mem_level` VALUES ('5', '5', null, null, '黄金会员', '0', '0', '0', 'http://pic.dayouqiantu.cn/5c9ccca8b27f1.jpg', 'http://pic.dayouqiantu.cn/5c9ccca8aa5b9.png', '2020-06-25 18:54:35', null, '0');
INSERT INTO `mem_level` VALUES ('6', '6', null, null, '钻石会员', '0', '0', '0', 'http://localhost:8000/file/pic/钻石-20200328094531898.jpg', 'http://pic.dayouqiantu.cn/5c9ccca90d2d3.png', '2020-06-25 18:54:35', null, '1');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '部门名称',
  `parent_id` int DEFAULT '0' COMMENT '父节点id',
  `tree_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '父节点id路径',
  `sort` int DEFAULT '0' COMMENT '显示顺序',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态：1-正常 0-禁用',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除状态：1-删除 0-未删除',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1', '运营技术', '0', '0', '1', '1', '0', null, null);
INSERT INTO `sys_dept` VALUES ('2', '研发部门', '1', '0,1', '1', '1', '0', '2021-06-03 00:33:56', '2021-06-03 00:33:56');
INSERT INTO `sys_dept` VALUES ('9', 'A', '2', '0,1,2', '1', '1', '0', null, null);
INSERT INTO `sys_dept` VALUES ('10', 'B', '9', '0,1,2,9', '1', '1', '0', null, null);
INSERT INTO `sys_dept` VALUES ('11', 'C', '10', '0,1,2,9,10', '1', '1', '0', null, null);
INSERT INTO `sys_dept` VALUES ('12', 'D', '11', '0,1,2,9,10,11', '1', '1', '0', null, null);
INSERT INTO `sys_dept` VALUES ('13', 'E', '12', '0,1,2,9,10,11,12', '1', '1', '0', null, null);
INSERT INTO `sys_dept` VALUES ('14', 'F', '13', '0,1,2,9,10,11,12,13', '1', '1', '0', null, null);
INSERT INTO `sys_dept` VALUES ('15', 'G', '14', '0,1,2,9,10,11,12,13,14', '1', '1', '0', null, null);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '类型名称',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '类型编码',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0-正常 ,1-停用）',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `type_code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='字典类型表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('1', '性别', 'gender', '1', '性别', '2019-12-06 19:03:32', '2021-02-08 14:58:01');
INSERT INTO `sys_dict` VALUES ('11', '授权方式', 'grant_type', '1', null, '2020-10-17 08:09:50', '2021-01-31 09:48:24');
INSERT INTO `sys_dict` VALUES ('24', '微服务列表', 'micro_service', '1', '设置URL权限标识使用', '2021-06-17 00:13:43', '2021-06-17 00:17:22');
INSERT INTO `sys_dict` VALUES ('25', '请求方式', 'request_method', '1', '设置URL权限标识使用', '2021-06-17 00:18:07', '2021-06-17 00:18:07');

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '字典项名称',
  `value` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '字典项值',
  `dict_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '字典编码',
  `sort` int DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0 停用 1正常）',
  `defaulted` tinyint(1) DEFAULT '0' COMMENT '是否默认（0否 1是）',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '备注',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='字典数据表';

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES ('1', '男', '1', 'gender', '1', '1', '0', '性别男', '2019-05-05 13:07:52', '2019-07-02 14:23:05');
INSERT INTO `sys_dict_item` VALUES ('2', '女', '2', 'gender', '2', '1', '0', '性别女', '2019-04-19 11:33:00', '2019-07-02 14:23:05');
INSERT INTO `sys_dict_item` VALUES ('5', '未知', '0', 'gender', '1', '1', '0', '', '2020-10-17 08:09:31', '2020-10-17 08:09:31');
INSERT INTO `sys_dict_item` VALUES ('6', '密码模式', 'password', 'grant_type', '1', '1', '0', '', '2020-10-17 09:11:52', '2021-01-31 09:48:18');
INSERT INTO `sys_dict_item` VALUES ('7', '授权码模式', 'authorization_code', 'grant_type', '1', '1', '0', '', '2020-10-17 09:12:15', '2020-12-14 10:11:00');
INSERT INTO `sys_dict_item` VALUES ('8', '客户端模式', 'client_credentials', 'grant_type', '1', '1', '0', '', '2020-10-17 09:12:36', '2020-12-14 10:11:00');
INSERT INTO `sys_dict_item` VALUES ('9', '刷新模式', 'refresh_token', 'grant_type', '1', '1', '0', '', '2020-10-17 09:12:57', '2021-01-08 17:33:12');
INSERT INTO `sys_dict_item` VALUES ('10', '简化模式', 'implicit', 'grant_type', '1', '1', '0', '', '2020-10-17 09:13:23', '2020-12-14 10:11:00');
INSERT INTO `sys_dict_item` VALUES ('38', '系统服务', 'youlai-admin', 'micro_service', '1', '1', '0', '', '2021-06-17 00:14:12', '2021-06-17 00:14:12');
INSERT INTO `sys_dict_item` VALUES ('39', '会员服务', 'youlai-ums', 'micro_service', '2', '1', '0', '', '2021-06-17 00:15:06', '2021-06-17 00:15:06');
INSERT INTO `sys_dict_item` VALUES ('40', '商品服务', 'youlai-pms', 'micro_service', '3', '1', '0', '', '2021-06-17 00:15:26', '2021-06-17 00:16:18');
INSERT INTO `sys_dict_item` VALUES ('41', '订单服务', 'youlai-oms', 'micro_service', '4', '1', '0', '', '2021-06-17 00:15:40', '2021-06-17 00:16:10');
INSERT INTO `sys_dict_item` VALUES ('42', '营销服务', 'youlai-sms', 'micro_service', '5', '1', '0', '', '2021-06-17 00:16:01', '2021-06-17 00:16:01');
INSERT INTO `sys_dict_item` VALUES ('43', '不限', '*', 'request_method', '1', '1', '0', '', '2021-06-17 00:18:34', '2021-06-17 00:18:34');
INSERT INTO `sys_dict_item` VALUES ('44', 'GET', 'GET', 'request_method', '2', '1', '0', '', '2021-06-17 00:18:55', '2021-06-17 00:18:55');
INSERT INTO `sys_dict_item` VALUES ('45', 'POST', 'POST', 'request_method', '3', '1', '0', '', '2021-06-17 00:19:06', '2021-06-17 00:19:06');
INSERT INTO `sys_dict_item` VALUES ('46', 'PUT', 'PUT', 'request_method', '4', '1', '0', '', '2021-06-17 00:19:17', '2021-06-17 00:19:17');
INSERT INTO `sys_dict_item` VALUES ('47', 'DELETE', 'DELETE', 'request_method', '5', '1', '0', '', '2021-06-17 00:19:30', '2021-06-17 00:19:30');
INSERT INTO `sys_dict_item` VALUES ('48', 'PATCH', 'PATCH', 'request_method', '6', '1', '0', '', '2021-06-17 00:19:42', '2021-06-17 00:19:42');

-- ----------------------------
-- Table structure for sys_file_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_info`;
CREATE TABLE `sys_file_info` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件md5',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_img` tinyint(1) NOT NULL,
  `content_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `size` int NOT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物理路径',
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `source` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_file_info
-- ----------------------------
INSERT INTO `sys_file_info` VALUES ('2c95b54f4d8356cf8ab40802f496df83', '头像.png', '1', 'image/png', '1290', 'http://pkqtmn0p1.bkt.clouddn.com/头像.png', 'http://pkqtmn0p1.bkt.clouddn.com/头像.png', 'QINIU', '2019-01-08 17:05:36', '2019-01-08 17:05:36');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '菜单名称',
  `parent_id` bigint DEFAULT NULL COMMENT '父菜单ID',
  `path` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '路由路径',
  `component` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '组件路径',
  `redirect` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '跳转路径',
  `icon` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '菜单图标',
  `sort` int DEFAULT '0' COMMENT '排序',
  `visible` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用 1-开启',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='菜单管理';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '系统管理', '0', '/admin', 'Layout', '', 'table', '1', '1', '2020-09-23 09:12:21', '2021-05-21 11:09:57');
INSERT INTO `sys_menu` VALUES ('2', '用户管理', '1', 'user', 'admin/user/index', '', 'user', '1', '1', null, '2021-06-06 23:36:28');
INSERT INTO `sys_menu` VALUES ('4', '菜单管理', '1', 'menu', 'admin/menu/index', null, 'tree-table', '3', '1', '2020-09-23 09:12:21', '2021-03-09 09:30:44');
INSERT INTO `sys_menu` VALUES ('5', '字典管理', '1', 'dict', 'admin/dict/index', null, 'education', '5', '1', '2020-09-23 09:12:21', '2021-03-09 09:30:53');
INSERT INTO `sys_menu` VALUES ('6', '部门管理', '1', 'dept', 'admin/dept/index', null, 'tree', '4', '1', '2020-09-23 09:12:21', '2021-03-09 09:30:50');
INSERT INTO `sys_menu` VALUES ('8', '客户端管理', '1', 'client', 'admin/client/index', null, 'tab', '6', '1', '2020-10-17 08:04:08', '2021-03-09 09:30:56');
INSERT INTO `sys_menu` VALUES ('10', '广告管理', '9', 'advert', 'sms/advert/index', null, 'documentation', '1', '1', '2020-10-24 15:25:15', '2021-02-01 19:26:21');
INSERT INTO `sys_menu` VALUES ('12', '商品列表', '11', 'goods', 'pms/goods/index', null, 'component', '1', '1', '2020-11-06 11:54:37', '2021-03-11 20:18:14');
INSERT INTO `sys_menu` VALUES ('14', '订单列表', '13', 'order', 'oms/order', '', 'component', '3', '1', '2020-10-31 10:50:23', '2021-03-25 19:52:05');
INSERT INTO `sys_menu` VALUES ('15', '会员管理', '0', '/ums', 'Layout', null, 'user', '4', '1', '2020-10-31 10:51:07', '2021-02-06 14:57:13');
INSERT INTO `sys_menu` VALUES ('16', '会员列表', '15', 'user', 'ums/user/index', null, 'peoples', '1', '1', '2020-10-31 10:51:43', '2021-03-02 10:41:56');
INSERT INTO `sys_menu` VALUES ('17', '品牌管理', '11', 'brand', 'pms/brand/index', null, 'component', '4', '1', '2020-09-23 09:12:21', '2021-02-01 19:25:06');
INSERT INTO `sys_menu` VALUES ('18', '商品分类', '11', 'category', 'pms/category/index', null, 'component', '3', '1', '2020-09-23 09:12:21', '2021-03-17 11:17:06');
INSERT INTO `sys_menu` VALUES ('22', '商品上架', '11', 'goods-detail', 'pms/goods/detail', '', 'component', '2', '1', null, '2021-02-19 18:43:23');
INSERT INTO `sys_menu` VALUES ('23', '角色管理', '1', 'role', 'admin/role/index', '', 'peoples', '3', '1', null, '2021-03-29 10:44:13');
INSERT INTO `sys_menu` VALUES ('74', '等级管理', '15', 'level', 'ums/level/index', '', 'swagger', '1', '1', '2021-08-19 16:22:44', '2021-08-19 16:22:44');

-- ----------------------------
-- Table structure for sys_oauth_client
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client`;
CREATE TABLE `sys_oauth_client` (
  `client_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `access_token_validity` int DEFAULT NULL,
  `refresh_token_validity` int DEFAULT NULL,
  `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sys_oauth_client
-- ----------------------------
INSERT INTO `sys_oauth_client` VALUES ('client', null, '123456', 'all', 'authorization_code,password,refresh_token,implicit', null, null, '3600', '7200', null, 'true');
INSERT INTO `sys_oauth_client` VALUES ('live-admin', '', '123456', 'all', 'password,client_credentials,refresh_token,authorization_code', '', '', '-1', '7200', null, 'true');
INSERT INTO `sys_oauth_client` VALUES ('live-app', null, '123456', 'all', 'authorization_code,password,refresh_token,implicit', null, null, '3600', '7200', null, 'true');
INSERT INTO `sys_oauth_client` VALUES ('live-weapp', '', '123456', 'all', 'authorization_code,password,refresh_token,implicit', null, null, '3600', '7200', null, 'true');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限名称',
  `menu_id` bigint DEFAULT NULL COMMENT '菜单模块ID\r\n',
  `url_perm` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'URL权限标识',
  `btn_perm` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '按钮权限标识',
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `id` (`id`,`name`) USING BTREE,
  KEY `id_2` (`id`,`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('1', '查看用户', '2', 'GET:/youlai-admin/api/v1/users/*', 'admin:user:view', '2021-02-02 14:16:07', '2021-06-16 22:25:24');
INSERT INTO `sys_permission` VALUES ('74', '编辑用户', '2', 'PUT:/youlai-admin/users/*', 'admin:user:edit', '2021-06-16 16:19:44', '2021-06-16 23:36:53');
INSERT INTO `sys_permission` VALUES ('75', '新增用户', '2', 'POST:/youlai-admin/api/v1/users', 'admin:user:add', '2021-06-16 23:36:37', '2021-06-16 23:37:03');
INSERT INTO `sys_permission` VALUES ('76', '删除用户', '2', 'DELETE:/youlai-admin/api/v1/users/*', 'admin:user:delete', '2021-06-16 23:43:54', '2021-06-16 23:43:54');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色编码',
  `sort` int DEFAULT NULL COMMENT '显示顺序',
  `status` tinyint(1) DEFAULT '1' COMMENT '角色状态（0正常 1停用）',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识  (0未删除 1已删除)',
  `gmt_create` datetime DEFAULT NULL COMMENT '更新时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级管理员', 'ROOT', '1', '1', '0', '2021-05-21 14:56:51', '2018-12-23 16:00:00');
INSERT INTO `sys_role` VALUES ('2', '系统管理员', 'ADMIN', '2', '1', '0', '2021-03-25 12:39:54', '2018-12-23 16:00:00');
INSERT INTO `sys_role` VALUES ('3', '游客', 'GUEST', '3', '1', '0', '2021-05-26 15:49:05', '2019-05-05 16:00:00');
INSERT INTO `sys_role` VALUES ('67', '测试', 'TEST', '1', '1', '0', '2021-06-05 01:30:40', '2021-06-05 01:30:40');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='角色和菜单关联表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('3', '12');
INSERT INTO `sys_role_menu` VALUES ('3', '1');
INSERT INTO `sys_role_menu` VALUES ('3', '11');
INSERT INTO `sys_role_menu` VALUES ('3', '2');
INSERT INTO `sys_role_menu` VALUES ('3', '6');
INSERT INTO `sys_role_menu` VALUES ('2', '15');
INSERT INTO `sys_role_menu` VALUES ('2', '16');
INSERT INTO `sys_role_menu` VALUES ('2', '23');
INSERT INTO `sys_role_menu` VALUES ('2', '2');
INSERT INTO `sys_role_menu` VALUES ('2', '4');
INSERT INTO `sys_role_menu` VALUES ('2', '6');
INSERT INTO `sys_role_menu` VALUES ('2', '8');
INSERT INTO `sys_role_menu` VALUES ('3', '4');
INSERT INTO `sys_role_menu` VALUES ('3', '5');
INSERT INTO `sys_role_menu` VALUES ('3', '10');
INSERT INTO `sys_role_menu` VALUES ('3', '14');
INSERT INTO `sys_role_menu` VALUES ('2', '5');
INSERT INTO `sys_role_menu` VALUES ('1', '25');
INSERT INTO `sys_role_menu` VALUES ('3', '8');
INSERT INTO `sys_role_menu` VALUES ('1', '23');
INSERT INTO `sys_role_menu` VALUES ('1', '2');
INSERT INTO `sys_role_menu` VALUES ('1', '10');
INSERT INTO `sys_role_menu` VALUES ('1', '9');
INSERT INTO `sys_role_menu` VALUES ('1', '11');
INSERT INTO `sys_role_menu` VALUES ('1', '12');
INSERT INTO `sys_role_menu` VALUES ('1', '17');
INSERT INTO `sys_role_menu` VALUES ('1', '18');
INSERT INTO `sys_role_menu` VALUES ('1', '22');
INSERT INTO `sys_role_menu` VALUES ('1', '4');
INSERT INTO `sys_role_menu` VALUES ('1', '41');
INSERT INTO `sys_role_menu` VALUES ('1', '5');
INSERT INTO `sys_role_menu` VALUES ('1', '6');
INSERT INTO `sys_role_menu` VALUES ('1', '8');
INSERT INTO `sys_role_menu` VALUES ('1', '1');
INSERT INTO `sys_role_menu` VALUES ('1', '13');
INSERT INTO `sys_role_menu` VALUES ('1', '14');
INSERT INTO `sys_role_menu` VALUES ('1', '15');
INSERT INTO `sys_role_menu` VALUES ('1', '16');
INSERT INTO `sys_role_menu` VALUES ('1', '26');
INSERT INTO `sys_role_menu` VALUES ('65', '2');
INSERT INTO `sys_role_menu` VALUES ('66', '2');
INSERT INTO `sys_role_menu` VALUES ('67', '23');
INSERT INTO `sys_role_menu` VALUES ('67', '5');
INSERT INTO `sys_role_menu` VALUES ('67', '8');
INSERT INTO `sys_role_menu` VALUES ('67', '41');
INSERT INTO `sys_role_menu` VALUES ('67', '1');
INSERT INTO `sys_role_menu` VALUES ('67', '2');
INSERT INTO `sys_role_menu` VALUES ('67', '4');
INSERT INTO `sys_role_menu` VALUES ('2', '1');
INSERT INTO `sys_role_menu` VALUES ('2', '74');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `role_id` bigint DEFAULT NULL COMMENT '角色id',
  `permission_id` bigint DEFAULT NULL COMMENT '资源id',
  KEY `role_id` (`role_id`) USING BTREE,
  KEY `permission_id` (`permission_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='角色权限表';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('2', '1');
INSERT INTO `sys_role_permission` VALUES ('67', '1');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '昵称',
  `gender` tinyint(1) DEFAULT '0' COMMENT '性别',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
  `dept_id` int DEFAULT NULL COMMENT '部门ID',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '用户头像',
  `mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '联系方式',
  `status` tinyint(1) DEFAULT '0' COMMENT '用户状态（0正常 1禁用）',
  `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户邮箱',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标识（0未删除 1已删除）',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `login_name` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'root', '超级管理员', '2', '$2a$10$GdoxtCdEM0LHvKpxCa5sgOacLQr0c2PslRHknI3FSHWMdV0hc4Z6.', '1', 'https://gitee.com/haoxr/image/raw/master/20210605215800.png', '17621590365', '1', '1490493387@qq.com', '0', '2021-02-10 12:27:30', '2021-06-06 23:36:51');
INSERT INTO `sys_user` VALUES ('2', 'admin', '系统管理员', '1', '$2a$10$yJSqqr6sTxNuYtA6EKcVUe2I4USFCzJ29sNcRrBvtAkSYcNg5ydQ6', '2', 'https://gitee.com/haoxr/image/raw/master/20210605215800.png', '17621210366', '1', '1490493387@qq.com', '0', '2019-10-10 13:41:22', '2021-06-06 23:41:35');
INSERT INTO `sys_user` VALUES ('3', 'test', '测试小用户', '1', '$2a$10$MPJkNw.hKT/fZOgwYP8q9eu/rFJJDsNov697AmdkHNJkpjIpVSw2q', '1', 'https://gitee.com/haoxr/image/raw/master/20210605215800.png', null, '1', null, '0', '2021-06-05 01:31:29', '2021-06-05 01:31:29');
INSERT INTO `sys_user` VALUES ('8', '123', null, '0', '$2a$10$wWCWmCEVQWd6mP5erjXtRez5LpwBqwAReN1A22BtxcwT8E1MbBpqa', '11', '', null, '1', null, '0', '2021-08-14 22:22:22', '2021-08-14 22:22:22');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户和角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1');
INSERT INTO `sys_user_role` VALUES ('2', '1');
INSERT INTO `sys_user_role` VALUES ('2', '2');
INSERT INTO `sys_user_role` VALUES ('2', '3');
INSERT INTO `sys_user_role` VALUES ('2', '58');
INSERT INTO `sys_user_role` VALUES ('2', '59');
INSERT INTO `sys_user_role` VALUES ('3', '67');
INSERT INTO `sys_user_role` VALUES ('5', '15');
INSERT INTO `sys_user_role` VALUES ('6', '3');
INSERT INTO `sys_user_role` VALUES ('6', '18');
INSERT INTO `sys_user_role` VALUES ('6', '67');
INSERT INTO `sys_user_role` VALUES ('7', '67');
INSERT INTO `sys_user_role` VALUES ('8', '3');
INSERT INTO `sys_user_role` VALUES ('8', '67');
INSERT INTO `sys_user_role` VALUES ('9', '2');
INSERT INTO `sys_user_role` VALUES ('10', '26');
INSERT INTO `sys_user_role` VALUES ('12', '2');
INSERT INTO `sys_user_role` VALUES ('13', '2');
INSERT INTO `sys_user_role` VALUES ('17', '1');
INSERT INTO `sys_user_role` VALUES ('19', '2');
INSERT INTO `sys_user_role` VALUES ('21', '3');
INSERT INTO `sys_user_role` VALUES ('21', '15');
INSERT INTO `sys_user_role` VALUES ('23', '15');
INSERT INTO `sys_user_role` VALUES ('24', '1');
INSERT INTO `sys_user_role` VALUES ('25', '14');
INSERT INTO `sys_user_role` VALUES ('25', '50');
INSERT INTO `sys_user_role` VALUES ('26', '50');
INSERT INTO `sys_user_role` VALUES ('31', '15');
INSERT INTO `sys_user_role` VALUES ('32', '1');
INSERT INTO `sys_user_role` VALUES ('32', '2');
INSERT INTO `sys_user_role` VALUES ('32', '3');
INSERT INTO `sys_user_role` VALUES ('32', '56');
INSERT INTO `sys_user_role` VALUES ('32', '57');
INSERT INTO `sys_user_role` VALUES ('32', '58');
INSERT INTO `sys_user_role` VALUES ('33', '2');
INSERT INTO `sys_user_role` VALUES ('36', '60');
INSERT INTO `sys_user_role` VALUES ('37', '1');
INSERT INTO `sys_user_role` VALUES ('38', '1');
INSERT INTO `sys_user_role` VALUES ('38', '2');
INSERT INTO `sys_user_role` VALUES ('39', '1');
INSERT INTO `sys_user_role` VALUES ('39', '2');
INSERT INTO `sys_user_role` VALUES ('40', '60');

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='AT transaction mode undo table';

-- ----------------------------
-- Records of undo_log
-- ----------------------------
