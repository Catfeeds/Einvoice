drop VIEW v_wholesale;
drop VIEW v_wholesaleitem;
drop VIEW v_wholesalepay;



CREATE VIEW v_wholesale AS SELECT
	sheetid AS sheetid,
	refsheetid AS refsheetid,
	shopid AS shopid,
	flag AS STATUS,
	checkdate AS sdate,
	checker AS editor
FROM
	dbusrdms.wholesale;

CREATE OR REPLACE VIEW V_WHOLESALEITEM AS
SELECT
  a.sheetid AS sheetid,
  a.serialid AS rowno,
  a.categoryid AS categoryid,
  a.pknumber AS unit,
  a.goodsid AS itemid,
  a.cgname AS itemname,
  a.qty AS qty,
  a.pricevalue AS amt,
  b.saletaxrate/100 AS taxrate
FROM
  dbusrdms.wholesaleitem a
left join dbusrdms.goods b on a.goodsid=b.goodsid;


CREATE VIEW v_wholesalepay AS SELECT
	sheetid AS sheetid,
	paytype AS payid,
	value AS amt
FROM
	dbusrdms.wholesalepay;


	