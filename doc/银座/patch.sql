CREATE TABLE `billtaxreport` (
  `entid` varchar(10) NOT NULL,
  `shopid` varchar(10) NOT NULL,
  `sdate` date NOT NULL COMMENT '交易日期',
  `taxitemid` varchar(30) NOT NULL,
  `taxRate` decimal(5,4) NOT NULL COMMENT '税率',
  `qty` decimal(12,2) NOT NULL COMMENT '数量',
  `amt` decimal(14,2) NOT NULL COMMENT '总金额(含税)',
  `oldAmt` decimal(14,2) DEFAULT NULL,
  `usedAmt` decimal(14,2) DEFAULT NULL COMMENT '已开票金额(含税)',
  `sumPayAmt` decimal(16,2) DEFAULT NULL,
  `price` decimal(14,5) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`entid`,`shopid`,`sdate`,`taxitemid`,taxRate),
  KEY `idx_billtaxreport_01` (`shopid`,`sdate`),
  KEY `idx_billtaxreport_02` (`taxitemid`)
)  COMMENT='小票分类销售统计';

drop table billtax;
CREATE TABLE `billtax` (
  `entid` varchar(10) NOT NULL,
  `taxitemid` varchar(30) NOT NULL,
  `taxitemname` varchar(100) NOT NULL COMMENT '税目名称',
  PRIMARY KEY (`entid`,`taxitemid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into billtax(entid,taxitemid) values('A00001002','1030105010400000000');
insert into billtax(entid,taxitemid) values('A00001002','1070225000000000000');
insert into billtax(entid,taxitemid) values('A00001002','1030102010100000000');
insert into billtax(entid,taxitemid) values('A00001002','1030101010000000000');
insert into billtax(entid,taxitemid) values('A00001002','1030101990000000000');
insert into billtax(entid,taxitemid) values('A00001002','1010101990000000000');
insert into billtax(entid,taxitemid) values('A00001002','1030299000000000000');
insert into billtax(entid,taxitemid) values('A00001002','1010303020100000000');
insert into billtax(entid,taxitemid) values('A00001002','1030204010000000000');
insert into billtax(entid,taxitemid) values('A00001002','1030111070000000000');
insert into billtax(entid,taxitemid) values('A00001002','1030205070000000000');
insert into billtax(entid,taxitemid) values('A00001002','1010115039900000000');
insert into billtax(entid,taxitemid) values('A00001002','1030201020000000000');
insert into billtax(entid,taxitemid) values('A00001002','1030201010000000000');
insert into billtax(entid,taxitemid) values('A00001002','1030107010100000000');
insert into billtax(entid,taxitemid) values('A00001002','1030109010000000000');
insert into billtax(entid,taxitemid) values('A00001002','1010404000000000000');
insert into billtax(entid,taxitemid) values('A00001002','1030203030000000000');
insert into billtax(entid,taxitemid) values('A00001002','1010115019900000000');
insert into billtax(entid,taxitemid) values('A00001002','1030206040000000000');
insert into billtax(entid,taxitemid) values('A00001002','1030308020000000000');
insert into billtax(entid,taxitemid) values('A00001002','1010116010000000000');
insert into billtax(entid,taxitemid) values('A00001002','1030308010000000000');
insert into billtax(entid,taxitemid) values('A00001002','1030302000000000000');
insert into billtax(entid,taxitemid) values('A00001002','1030303020000000000');
insert into billtax(entid,taxitemid) values('A00001002','1030305000000000000');
insert into billtax(entid,taxitemid) values('A00001002','1030402010000000000');
insert into billtax(entid,taxitemid) values('A00001002','1030402020000000000');
insert into billtax(entid,taxitemid) values('A00001002','1070223030000000000');
insert into billtax(entid,taxitemid) values('A00001002','1070222010000000000');
insert into billtax(entid,taxitemid) values('A00001002','1070224010000000000');
insert into billtax(entid,taxitemid) values('A00001002','1060105040000000000');
insert into billtax(entid,taxitemid) values('A00001002','1070222020000000000');
insert into billtax(entid,taxitemid) values('A00001002','1040105070000000000');
insert into billtax(entid,taxitemid) values('A00001002','1040105990000000000');
insert into billtax(entid,taxitemid) values('A00001002','1090417050000000000');
insert into billtax(entid,taxitemid) values('A00001002','1090417990000000000');
insert into billtax(entid,taxitemid) values('A00001002','1090418050000000000');
insert into billtax(entid,taxitemid) values('A00001002','1090417010000000000');
insert into billtax(entid,taxitemid) values('A00001002','1090417030000000000');
insert into billtax(entid,taxitemid) values('A00001002','1090419010000000000');
insert into billtax(entid,taxitemid) values('A00001002','1080407040000000000');
insert into billtax(entid,taxitemid) values('A00001002','1040201990000000000');
insert into billtax(entid,taxitemid) values('A00001002','1040201230000000000');
insert into billtax(entid,taxitemid) values('A00001002','1040204010000000000');
insert into billtax(entid,taxitemid) values('A00001002','1040204990000000000');

update billtax a join taxitem b on b.taxitemid=a.taxitemid set a.taxitemname=b.taxitemname;

drop table billtaxgoodsname;

CREATE TABLE `billtaxgoodsname` (
  `entid` varchar(10) NOT NULL,
  `taxitemid` varchar(30) NOT NULL,
  `taxRate` decimal(5,4) NOT NULL COMMENT '税率',
  `goodsName` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`entid`,`taxitemid`,`goodsName`,taxRate)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030105010400000000',0.1,'花生油');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1070225000000000000',0.1,'香油');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030102010100000000',0.1,'大米');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030101010000000000',0.1,'面粉');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030101990000000000',0.1,'面粉');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1010101990000000000',0.1,'面粉');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030299000000000000',0.16,'有机面粉');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1010303020100000000',0.1,'鸡蛋');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030204010000000000',0.1,'牛奶');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030204010000000000',0.16,'牛奶');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030204010000000000',0.16,'饮料');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030111070000000000',0.16,'干果');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030205070000000000',0.16,'干果');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1010115039900000000',0.1,'干果');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030201020000000000',0.16,'面包');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030201020000000000',0.16,'月饼');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030201020000000000',0.16,'月蛋糕');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030201010000000000',0.16,'月饼');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030201010000000000',0.16,'蛋糕');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030107010100000000',0.1,'冷鲜肉');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030107010100000000',0.1,'猪肉');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030109010000000000',0.1,'海鲜');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1010404000000000000',0.1,'海鲜');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030203030000000000',0.16,'熟食');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030203030000000000',0.16,'粽子');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1010115019900000000',0.1,'水果');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030206040000000000',0.16,'调料');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030308020000000000',0.16,'茶叶');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1010116010000000000',0.1,'茶叶');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030308010000000000',0.16,'茶叶');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030302000000000000',0.16,'白酒');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030303020000000000',0.16,'啤酒');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030305000000000000',0.16,'红酒');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030402010000000000',0.16,'烟');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1030402020000000000',0.16,'烟');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1070223030000000000',0.16,'洗发水');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1070222010000000000',0.16,'香皂');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1070224010000000000',0.16,'牙膏');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1060105040000000000',0.16,'卷纸');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1060105040000000000',0.16,'抽纸');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1070222020000000000',0.16,'洗衣液');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1040105070000000000',0.16,'床上用品');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1040105990000000000',0.16,'床上用品');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1090417050000000000',0.16,'食品加工机');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1090417990000000000',0.16,'破壁机');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1090417990000000000',0.16,'榨汁机');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1090417990000000000',0.16,'料理机');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1090418050000000000',0.16,'吸尘器');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1090418050000000000',0.16,'空气净化器');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1090418050000000000',0.16,'加湿器');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1090417010000000000',0.16,'电饼档');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1090417010000000000',0.16,'豆浆机');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1090417010000000000',0.16,'电饭煲');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1090417030000000000',0.16,'电水壶');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1090417030000000000',0.16,'咖啡机');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1090419010000000000',0.16,'吹风机');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1080407040000000000',0.16,'剃须刀');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1040201990000000000',0.16,'服装');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1040201990000000000',0.16,'工装');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1040201990000000000',0.16,'衬衫');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1040201990000000000',0.16,'裤子');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1040201990000000000',0.16,'套装');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1040201230000000000',0.16,'运动服装');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1040204010000000000',0.16,'鞋');
insert into billtaxgoodsname(entid,taxitemid,taxRate,goodsName) values('A00001002','1040204990000000000',0.16,'运动鞋');

