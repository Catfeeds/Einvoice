--1���������ӷ�Ʊ�û�dbusrdzfp ,  (dbusrdzfp/futuredzfp)
create user dbusrdzfp
  identified by "futuredzfp"
  default tablespace DATA_SPC
  temporary tablespace TEMP_SPC
  profile DEFAULT;

-- Grant/Revoke system privileges 
grant create session to dbusrdzfp;

--2������selectȨ�޸�dbusrdzfp�û�

grant select on dbusrposnew.salehead to dbusrdzfp;
grant select on dbusrposnew.salegoods to dbusrdzfp;
grant select on dbusrposnew.salepay to dbusrdzfp;

--3��������ͼ

---СƱͷ

CREATE OR REPLACE VIEW DBUSRDZFP.V_SELLHEAD AS
SELECT
BILLNO AS sheetid, --СƱΨһ��
MKT AS shopid, --���
DJLB AS STATUS, --״̬ ��������|�˻�
YBILLNO AS refsheetid, --�˻�СƱ��ԭʼƱΨһ��
SYJH AS syjid, --������iD
FPHM AS billno, --��ˮ��
RQSJ AS sdate, --��������
YSJE AS amt, --�ɽ����
SYYH AS editor
FROM dbusrposnew.salehead;

--СƱ��ϸ

CREATE OR REPLACE VIEW DBUSRDZFP.V_SELLDETAIL AS
SELECT
BILLNO AS sheetid, --СƱΨһ��
MKT AS shopid, --�ŵ�
ROWNO AS rowno, --�к�
CATID AS categoryid, --���С�ࣩ
UNITID AS unit, --��λ
CODE AS itemid, --��Ʒid
gbcname AS itemname, --����
SL AS qty, --����
ROUND((HJJE - HJZK), 2) AS amt , --���
GMPXSTAX AS taxrate --����˰��
FROM
DBUSRposnew.Salegoods,DBUSRPOS.goodsbase , DBUSRPOS.GOODSMFPRICE  WHERE CODE = gbid AND CODE = GMPGDID
AND GZ = gmpmfid;

--������ϸ

CREATE OR REPLACE VIEW DBUSRDZFP.V_SELLPAYMENT AS
SELECT
BILLNO AS sheetid, --СƱΨһ��
MKT AS shopid, --�ŵ�
PAYCODE AS payid, --֧����ʽ
JE AS amt --֧�����
FROM
DBUSRPOSNEW.SALEPAY
 Where FLAG = '1';
