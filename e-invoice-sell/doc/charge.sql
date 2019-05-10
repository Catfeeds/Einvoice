CREATE OR replace VIEW v_charge AS 
SELECT
  a.billno AS sheetid,
  a.SIHMARKET AS shopid,
  a.BILLSTATUS AS STATUS,
  a.SIHHSJE AS amt,
  a.INPUTDATE AS sdate,
  a.INPUTER_NAME AS editor,
  b.SBFP AS taxtitle,
  b.TAXNO AS taxno,
  b.sbfpadd AS taxaddr,
  b.sbbank||b.sbaccount AS taxbank
FROM
  supinvoicehd a
  join supplierbase b on b.sbid=a.sihsupid;

CREATE OR REPLACE VIEW V_WHOLESALEITEM AS
select 
  seqno AS sheetid
  rownum AS rowno,
  0 AS categoryid,
  'По' AS unit,
  a.cccode AS itemid,
  b.cccname AS itemname,
  1 AS qty,
  a.YSAMOUNT AS amt,
  c.tdrate AS taxrate 
from supchargelist a
join codecharge b on b.cccode=a.cccode
join taxdefine c on c.tdcode=b.cctaxtype;


	