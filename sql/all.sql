-- 如果CMS数据库已经存在，则删除
drop database if exists my;

-- 创建数据库CMS
create database my;

-- 使用CMS数据库
use my;

CREATE TABLE `st_city` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `cityId` int(11) NOT NULL COMMENT '城市ID',
  `cityName` varchar(20) NOT NULL COMMENT '城市名称',
  `cityEname` varchar(20) NOT NULL COMMENT '城市拼音名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='城市信息表';

CREATE TABLE `st_district` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `districtId` varchar(20) NOT NULL COMMENT '行政区ID',
  `districtName` varchar(50) NOT NULL COMMENT '行政区名称',
  `cityId` varchar(20) NOT NULL COMMENT '城市ID',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='城市行政区信息表';


CREATE TABLE `st_area` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `areaId` varchar(20) NOT NULL COMMENT '商圈Id',
  `areaName` varchar(50) NOT NULL COMMENT '商圈名称',
  `cityId` varchar(20) NOT NULL COMMENT '城市ID',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='城市商区信息表';

