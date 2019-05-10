ALTER TABLE `catetax`
ADD COLUMN `taxpre`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '是否享受税收优惠政策：0 不享受，1 享受' AFTER `taxitemid`,
ADD COLUMN `taxprecon`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠政策类型' AFTER `taxpre`,
ADD COLUMN `zerotax`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '零税率标识：空 非零税率，0：出口退税，1：免税，2：不征收，3：普通零税率' AFTER `taxprecon`;

ALTER TABLE `catetax`
MODIFY COLUMN `taxrate`  decimal(6,2) NOT NULL COMMENT '税率' AFTER `catename`;

