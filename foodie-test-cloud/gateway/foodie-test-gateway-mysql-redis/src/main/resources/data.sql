DROP TABLE IF EXISTS `bob_gateway_route_info`;
CREATE TABLE `bob_gateway_route_info` (
  `id` int(20) NOT NULL,
  `service_id` varchar(100) NOT NULL COMMENT '服务id',
  `uri` varchar(100) NOT NULL COMMENT '转发地址',
  `predicates` varchar(200) NOT NULL COMMENT '访问路径',
  `filters` varchar(100) NOT NULL COMMENT '过滤条件',
  `order` varchar(2) NOT NULL DEFAULT '0' COMMENT '顺序',
  `create_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remarks` varchar(255) NOT NULL COMMENT '备注',
  `del_flag` varchar(255) NOT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bob_gateway_route_info
-- ----------------------------
INSERT INTO `bob_gateway_route_info` VALUES ('1', 'userService1', 'bob-userservice', '/userService/**', '1', '0', '2020-07-15 10:46:39', '2020-07-15 10:46:39', '测试用户服务访问', '0');