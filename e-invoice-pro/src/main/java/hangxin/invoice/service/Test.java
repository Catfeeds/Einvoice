package hangxin.invoice.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.invoice.bean.db.InvoiceSaleDetail;
import com.invoice.bean.db.Invque;
import com.invoice.bean.db.Taxinfo;
import com.invoice.rtn.data.RtnData;

import hangxin.invoice.bean.HangXinRtInvoiceBean;
import sun.misc.BASE64Decoder;

public class Test {
	static String nsrsbh = "15000119780514458045";
	// String jrdm ="aabbccddeeff";
	// String serviceUrl =
	// "http://221.234.42.183:8082/zzs_kpfw_ARM9/webservice/eInvWS/15000119780514458045?wsdl";
	// //发票开具
	// String serviceUrl
	// ="http://221.234.42.183:8082/zzs_kpfw_ARM9/fpkj/15000119780514458045";
	// //发票开具
	// String serviceUrl
	// ="http://ei-test.51fapiao.cn:28888/fpqz/webservice/eInvWS?wsdl";//发票签章
	// String serviceUrl
	// ="http://221.234.42.183:8082/zzs_kpfw_ARM9/webservice/eInvWS/15000119780514458045?wsdl";//发票查询

	// webservice方法名：fpcyInterface

	HangXinServiceImpl service = new HangXinServiceImpl(nsrsbh);

	public static void main(String[] args) {
		
		String sheetid ="shopsyji12323721";
		//尝试根据位数截取
		String shopid=sheetid.substring(0, 4);
		String syjid=sheetid.substring(4, 8);
		String billno=sheetid.substring(8);
		
		// TODO Auto-generated method stub
		Test test = new Test();
		
		RtnData rtn = new RtnData();
		test.openinvoice(rtn); //发票开具
//		test.openQzinvoice(rtn);//发票签章
//		// test.findInvoiceBean(rtn);//发票查询
		System.out.println(rtn.getMessage());
		
		HangXinGenerateBean bean = new HangXinGenerateBean(nsrsbh);
		String s = "";
		try {
			String a = null;
			s = "a";
			a = bean.encode(s);
			System.err.println(a);
//			a = bean.decode(s);
//			System.out.println(a);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 发票查询
	public void findInvoiceBean(RtnData rtn) {
		Invque invque = new Invque();

		List<InvoiceSaleDetail> detailList = new ArrayList<InvoiceSaleDetail>();
		InvoiceSaleDetail detail = new InvoiceSaleDetail();
		Taxinfo taxinfo = new Taxinfo();

		invque.setIqseqno("hbhxhbhx1707281449072");

		service.findInvoiceBean(invque, taxinfo, detailList, rtn);
	}

	/**
	 * 开具发票
	 * **/
	public void openinvoice(RtnData rtn) {

		// HashMap map = new HashMap();
		Invque invque = new Invque();

		List<InvoiceSaleDetail> detailList = new ArrayList<InvoiceSaleDetail>();
		InvoiceSaleDetail detail = new InvoiceSaleDetail();
		InvoiceSaleDetail detail1 = new InvoiceSaleDetail();

		Taxinfo taxinfo = new Taxinfo();
		taxinfo.setItfeqpttype("0");
		taxinfo.setItfskpbh("661565676559");
		// taxinfo.setItfskpkl("88888888");
		// taxinfo.setItfkeypwd("12345678");
		taxinfo.setTaxadd("石家庄");
		taxinfo.setTaxbank("建行  3334455666");
		taxinfo.setItfbbh("12.0");
		// taxinfo.setItfkpd("kpyuan002");

		detail.setGoodsid("1060301020100000000");
		detail.setGoodsname("测试中类税目");
		detail.setTaxitemid("1060301020100000000");
		detail.setRowno(2);
		detail.setAmount(19.74);
		detail.setTaxfee(3.36);
		detail.setTaxrate(0.17);
		detail.setIsinvoice("Y");

		detail1.setGoodsid("1060301020100000000");
		detail1.setGoodsname("测试中类税目");
		detail1.setTaxitemid("1060301020100000000");
		detail1.setRowno(1);
		detail1.setAmount(-1.71);
		detail1.setTaxfee(-0.29);
		detail1.setTaxrate(0.17);
		detail1.setIsinvoice("Y");

		invque.setIqseqno("hbhxhbhx1707281449072");
		// invque.setIqtaxzdh("kpyuan002");
		invque.setIqtotje(18.03);
		invque.setIqtotse(3.07);
		invque.setIqgmfname("北京富基融通科技有限公司");
		invque.setIqgmftax("91110108718774697G");
		invque.setIqgmfadd("北京市海淀区中关村南大街8号甲8号乙8号威地科技大厦805室51650988");
		invque.setIqgmfbank("招商银行北京分行东方广场支行862580489510001");
		invque.setIqadmin("张三");
		// invque.setIqyfpdm("150003521055");
		// invque.setIqyfphm("82520088");
		invque.setIqtype(0);
		invque.setZsfs("0");
		invque.setIqfplxdm("026");

		detailList.add(detail);
		detailList.add(detail1);
		// map.put("invque", invque);
		// map.put("taxinfo", taxinfo);
		// map.put("detailList", detailList);

		// BaiWangServiceImpl service = new
		// BaiWangServiceImpl(serviceUrl,nsrsbh,jrdm);
		service.openinvoice(invque, taxinfo, detailList, rtn);
	}

	/**
	 * 发票签章
	 * **/
	public void openQzinvoice(RtnData rtn) {

		// HashMap map = new HashMap();
		Invque invque = new Invque();

		List<InvoiceSaleDetail> detailList = new ArrayList<InvoiceSaleDetail>();
		InvoiceSaleDetail detail = new InvoiceSaleDetail();

		Taxinfo taxinfo = new Taxinfo();
		taxinfo.setItfeqpttype("0");
		taxinfo.setItfskpbh("661701097384");
		taxinfo.setItfskpkl("88888888");
		taxinfo.setItfkeypwd("12345678");
		taxinfo.setTaxadd("石家庄");
		taxinfo.setTaxbank("建行  3334455666");
		taxinfo.setTaxname("北京富基融通科技有限公司");
		taxinfo.setItfbbh("12.0");
		// taxinfo.setItfkpd("kpyuan002");
		/*
		 * detail.setGoodsid("1060301020100000000");
		 * detail.setGoodsname("测试中类税目");
		 * detail.setTaxitemid("1060301020100000000"); detail.setRowno(2);
		 * detail.setAmount(19.74); detail.setTaxfee(3.36);
		 * detail.setTaxrate(0.17);
		 * 
		 * detail1.setGoodsid("1060301020100000000");
		 * detail1.setGoodsname("测试中类税目");
		 * detail1.setTaxitemid("1060301020100000000"); detail1.setRowno(1);
		 * detail1.setAmount(-1.71); detail1.setTaxfee(-0.29);
		 * detail1.setTaxrate(0.17);
		 */

		detail.setGoodsid("1060301020100000000");
		detail.setGoodsname("测试中类税目");
		detail.setTaxitemid("1060301020100000000");
		detail.setRowno(0);
		detail.setAmount(100.00);
		detail.setTaxfee(17.00);
		detail.setTaxrate(0.17);
		detail.setIsinvoice("Y");

		invque.setIqseqno("hbhxhbhx1707281449059");
		invque.setIqtaxzdh("kpyuan002");
		invque.setIqtotje(100.00);
		invque.setIqtotse(17.00);
		invque.setIqgmfname("北京富基融通科技有限公司");
		invque.setIqgmftax("91110108718774697G");
		invque.setIqgmfadd("北京市海淀区中关村南大街8号甲8号乙8号威地科技大厦805室51650988");
		invque.setIqgmfbank("招商银行北京分行东方广场支行862580489510001");
		invque.setIqadmin("张三");
		// invque.setIqyfpdm("150003533340");
		// invque.setIqyfphm("35681534");
		invque.setRtfpdm("150003533340");
		invque.setRtfphm("35681559");
		invque.setRtkprq("20170903200431");
		invque.setRtjym("82327328960875516151");
		invque.setRtskm("3+7*015</*9>+2*-50104*1<<*1-9+7*3*0206**//7*16+0*6+-198*<98+9<5*>*7850/<6>5/54*/1<+81+885*7-821+*--*5811*9-2");
		invque.setRtewm("Qk3CAwAAAAAAAD4AAAAoAAAASwAAAEsAAAABAAEAAAAAAIQDAAAAAAAAAAAAAAAAAAACAAAAAAAA///////////////////gAAAAAz8AAD/M/DAgAAAAAz8AAD/M/DAgAAA/8/AzMz8AzPPgAAA/8/AzMz8AzPPgAAAwMwADDDwD8zPgAAAwMwADDDwD8zPgAAAwMw8M8wAzMA8gAAAwMw8M8wAzMA8gAAAwM88D8PM/AD/gAAAwM88D8PM/AD/gAAA/8wwwPzA/PzzgAAA/8wwwPzA/PzzgAAAAAz/8D8//Mw8gAAAAAz/8D8//Mw8gAAD//wwwA/zAPzAgAAD//wwwA/zAPzAgAAAPwAzMP8wDAAPgAAAPwAzMP8wDAAPgAADMPz//M8888MwgAADMPz//M8888MwgAAAzAPDAPD8DPw/gAAAzAPDAPD8DPw/gAAAzzwwDAw/M8z8gAAAzzwwDAw/M8z8gAAD/8P8M8wMMMDMgAAD/8P8M8wMMMDMgAAD/zMw/MMAPzPAgAAD/zMw/MMAPzPAgAAD/APDPzwDPAAzgAAD/APDPzwDPAAzgAADz/Mz//Pw/z8/gAADz/Mz//Pw/z8/gAADMMA/zz8A8/w/gAADMMA/zz8A8/w/gAAAMzADzzPAzzzzgAAAMzADzzPAzzzzgAADDMA/MAM888AMgAADDMA/MAM888AMgAADAPMDPPAMAAPAgAADAPMDPPAMAAPAgAADDMDD8AD/DDMMgAADDMDD8AD/DDMMgAAAAPM/8wAMz8M8gAAAAPM/8wAMz8M8gAADzwMwDwP/P8/PgAADzwMwDwP/P8/PgAAAMz8zA8DP88wMgAAAMz8zA8DP88wMgAADMMMDPAz/MMDMgAADMMMDPAz/MMDMgAAA8zD/M/P/M/PDgAAA8zD/M/P/M/PDgAADA88PAw/A/D/zgAADA88PAw/A/D/zgAAA8DwMwD8wzz8PgAAA8DwMwD8wzz8PgAAAwwPDzPD8/88wgAAAwwPDzPD8/88wgAAD//wzzAzD8///gAAD//wzzAzD8///gAAAAAzMzMzMzMAAgAAAAAzMzMzMzMAAgAAA/8/MMPMMD8/8gAAA/8/MMPMMD8/8gAAAwM/wPzDP88wMgAAAwM/wPzDP88wMgAAAwMwM8ADzMMwMgAAAwMwM8ADzMMwMgAAAwM8wz/z/D8wMgAAAwM8wz/z/D8wMgAAA/8wMzDDDDM/8gAAA/8wMzDDDDM/8gAAAAAwPA8w/z8AAgAAAAAwPA8w/z8AAgAAA=");
		invque.setIqtype(0);
		invque.setZsfs("0");
		invque.setIqfplxdm("026");
		invque.setIqtel("13722890816");
		invque.setIqemail("157432381@qq.com");
		invque.setIqmemo("0.00");

		detailList.add(detail);
		// detailList.add(detail1);
		// map.put("invque", invque);
		// map.put("taxinfo", taxinfo);
		// map.put("detailList", detailList);

		// BaiWangServiceImpl service = new
		// BaiWangServiceImpl(serviceUrl,nsrsbh,jrdm);
		service.openQzinvoice(invque, taxinfo, detailList, rtn);
	}
}
