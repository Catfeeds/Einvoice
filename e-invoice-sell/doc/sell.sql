--1、创建电子发票用户dbusrdzfp ,  (dbusrdzfp/futuredzfp)
create user dbusrdzfp
  identified by "futuredzfp"
  default tablespace DATA_SPC
  temporary tablespace TEMP_SPC
  profile DEFAULT;

-- Grant/Revoke system privileges 
grant create session to dbusrdzfp;

--2、赋予select权限给dbusrdzfp用户

grant select on dbusrposnew.salehead to dbusrdzfp;
grant select on dbusrposnew.salegoods to dbusrdzfp;
grant select on dbusrposnew.salepay to dbusrdzfp;

--3、创建视图

---小票头

CREATE OR REPLACE VIEW DBUSRDZFP.V_SELLHEAD AS
SELECT
BILLNO AS sheetid, --小票唯一号
MKT AS shopid, --店号
DJLB AS STATUS, --状态 区分销售|退货
YBILLNO AS refsheetid, --退货小票的原始票唯一号
SYJH AS syjid, --收银机iD
FPHM AS billno, --流水号
RQSJ AS sdate, --交易日期
YSJE AS amt, --成交金额
SYYH AS editor
FROM dbusrposnew.salehead;

--小票明细

CREATE OR REPLACE VIEW DBUSRDZFP.V_SELLDETAIL AS
SELECT
BILLNO AS sheetid, --小票唯一号
MKT AS shopid, --门店
ROWNO AS rowno, --行号
CATID AS categoryid, --类别（小类）
UNITID AS unit, --单位
CODE AS itemid, --商品id
gbcname AS itemname, --名称
SL AS qty, --数量
ROUND((HJJE - HJZK), 2) AS amt , --金额
GMPXSTAX AS taxrate --销项税率
FROM
DBUSRposnew.Salegoods,DBUSRPOS.goodsbase , DBUSRPOS.GOODSMFPRICE  WHERE CODE = gbid AND CODE = GMPGDID
AND GZ = gmpmfid;

--付款明细

CREATE OR REPLACE VIEW DBUSRDZFP.V_SELLPAYMENT AS
SELECT
BILLNO AS sheetid, --小票唯一号
MKT AS shopid, --门店
PAYCODE AS payid, --支付方式
JE AS amt --支付金额
FROM
DBUSRPOSNEW.SALEPAY
 Where FLAG = '1';
