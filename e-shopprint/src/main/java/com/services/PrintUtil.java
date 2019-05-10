package com.services;

import java.awt.Font;
import java.awt.FontMetrics;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class PrintUtil {

//	public static void main(String[] args) throws UnsupportedEncodingException {
//		System.setProperty("jna.encoding", "GBK");
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("tsc_name", "TSC TTP-244 Pro");
//		param.put("paper_width", "100");
//		param.put("paper_height", "150");
//		param.put("logistics_order_name", "万象物流包裹单");
//		param.put("logistics_name", "万象物流");
//		param.put("service_tel", "119");
//		param.put("home_url", "www.taobao.com.cn");
//		param.put("time1", "2015年10月11日");
//		param.put("time2", "2015年10月20日");
//		param.put("orderid", "123150917178547");
//		param.put("logisticsid", "123150917178547");
//		param.put("customid", "21282637");
//		param.put("customer_name", "王莉中7");
//		param.put("telphone", "135643342887");
//		param.put("money", "20.87");
//		param.put("address", "上海宝山区中环外球之间三泉路1495号/弄共康公寓小区76号/幢102室7");
//		// test2();
//		print(param);
//		// test5();
//		// test4();
//	}

	static void print(Map<String, Object> param)
			throws UnsupportedEncodingException {
		// TscLibDll.INSTANCE.about();
		TscLibDll.INSTANCE.openport(param.get("tsc_name") + "");
//		TscLibDll.INSTANCE.downloadpcx("D://KSK.PCX", "KSK.PCX");
		TscLibDll.INSTANCE.setup(param.get("paper_width") + "",
				param.get("paper_height") + "", "5", "8", "0", "0", "0");
		TscLibDll.INSTANCE.sendcommand("SET TEAR ON");
		TscLibDll.INSTANCE.clearbuffer();

		 commands(param);

//		TscLibDll.INSTANCE.sendcommand("PUTPCX 10,200,\"KSK.PCX\"");

		TscLibDll.INSTANCE.printlabel("1", "1");

		TscLibDll.INSTANCE.closeport();
	}

	private static void commands(Map<String, Object> param)
			throws UnsupportedEncodingException {

		
		TscLibDll.INSTANCE.sendcommand("BOX 0,0,800,1,5");

		TscLibDll.INSTANCE.sendcommand("BOX 16,32,233,33,5");

		TscLibDll.INSTANCE.windowsfont(266, 16, 40, 0, 0, 0, "Arial",
				param.get("logistics_order_name") + "");

		TscLibDll.INSTANCE.sendcommand("BOX 551,32,784,33,5");

		TscLibDll.INSTANCE.windowsfont(16, 60, 28, 0, 0, 0, "Arial",
				param.get("time1") + "");

		TscLibDll.INSTANCE.windowsfont(551, 60, 28, 0, 2, 0, "Arial",
				param.get("orderid") + "");

		TscLibDll.INSTANCE.sendcommand("BOX 16,96,64,288,2");

		TscLibDll.INSTANCE.windowsfont(24, 120, 36, 0, 0, 0, "Arial", "客");

		TscLibDll.INSTANCE.windowsfont(24, 156, 36, 0, 0, 0, "Arial", "户");

		TscLibDll.INSTANCE.windowsfont(24, 192, 36, 0, 0, 0, "Arial", "信");

		TscLibDll.INSTANCE.windowsfont(24, 228, 36, 0, 0, 0, "Arial", "息");

		TscLibDll.INSTANCE.sendcommand("BOX 16,288,64,480,2");

		TscLibDll.INSTANCE.windowsfont(24, 312, 36, 0, 0, 0, "Arial", "订");

		TscLibDll.INSTANCE.windowsfont(24, 348, 36, 0, 0, 0, "Arial", "购");

		TscLibDll.INSTANCE.windowsfont(24, 384, 36, 0, 0, 0, "Arial", "信");

		TscLibDll.INSTANCE.windowsfont(24, 420, 36, 0, 0, 0, "Arial", "息");

		TscLibDll.INSTANCE.sendcommand("BOX 64,96,784,160,2");

		TscLibDll.INSTANCE.windowsfont(72, 112, 26, 0, 0, 0, "Arial", "客户编号:");

		TscLibDll.INSTANCE.windowsfont(204, 112, 32, 0, 2, 0, "Arial",
				param.get("customid") + "");

		TscLibDll.INSTANCE.windowsfont(352, 112, 34, 0, 0, 0, "Arial",
				param.get("customer_name") + "");

		TscLibDll.INSTANCE.windowsfont(540, 112, 32, 0, 2, 0, "Arial",
				param.get("telphone") + "");
		int end = 784;
		int start = 64;
		int margin = 8;
		TscLibDll.INSTANCE.sendcommand("BOX " + start + ",160," + end
				+ ",288,2");

		String address = param.get("address") + "";

		printTextArea(address, (end - start), start + margin, 176, 32);

		TscLibDll.INSTANCE.sendcommand("BOX 64,288,504,480,2");

		TscLibDll.INSTANCE.sendcommand("BOX 504,288,784,384,2");

		TscLibDll.INSTANCE.sendcommand("BOX 504,384,784,480,2");

		TscLibDll.INSTANCE.windowsfont(80, 360, 48, 0, 0, 0, "Arial", "应收款：");

		TscLibDll.INSTANCE.windowsfont(240, 360, 48, 0, 0, 0, "Arial",
				param.get("money") + "");

		TscLibDll.INSTANCE.windowsfont(520, 304, 34, 0, 0, 0, "Arial", "客户签字");

		TscLibDll.INSTANCE.windowsfont(520, 400, 30, 0, 0, 0, "Arial", "时间：");

		TscLibDll.INSTANCE.sendcommand("BOX 16,480,784,720,2");

		TscLibDll.INSTANCE.sendcommand("BARCODE 32,560,\"128\",48,1,0,2,2,\""
				+ param.get("logisticsid") + "" + "\"");

		TscLibDll.INSTANCE.windowsfont(488, 560, 40, 0, 0, 0, "Arial", "物流中心");

		TscLibDll.INSTANCE.windowsfont(266, 16 + 720, 40, 0, 0, 0, "Arial",
				param.get("logistics_order_name") + "");

		TscLibDll.INSTANCE.windowsfont(784 - 25 * 8, 4 * 8 + 720, 28, 0, 0, 0,
				"Arial", param.get("time2") + "");

		TscLibDll.INSTANCE.sendcommand("BOX 16," + (720 + 8 * 8) + ",784,"
				+ ((150 - 2) * 8) + ",2");

		TscLibDll.INSTANCE.sendcommand("BOX 16," + ((150 - 2 - 15) * 8)
				+ ",784," + ((150 - 2 - 15) * 8 + 1) + ",2");

		TscLibDll.INSTANCE.windowsfont(50 * 8, (150 - 10) * 8, 28, 0, 0, 0,
				"Arial", param.get("logistics_name") + "");
		TscLibDll.INSTANCE.windowsfont(50 * 8, (150 - 10) * 8 + 28, 16, 0, 0,
				0, "Arial", param.get("home_url") + "");

		TscLibDll.INSTANCE.windowsfont(65 * 8, (150 - 15) * 8, 36, 0, 0, 0,
				"Arial", "客户服务热线");

		TscLibDll.INSTANCE.windowsfont(65 * 8, (150 - 15) * 8 + 36, 38, 0, 2,
				0, "Arial", param.get("service_tel") + "");

		TscLibDll.INSTANCE.sendcommand("BOX 64," + (720 + 8 * 8) + ",65,"
				+ ((150 - 2 - 15) * 8) + ",2");

		TscLibDll.INSTANCE.sendcommand("BOX 64," + (720 + 8 * 8) + ",65,"
				+ ((150 - 2 - 15) * 8) + ",2");

		TscLibDll.INSTANCE.sendcommand("BOX 16," + (924) + ",784," + (925)
				+ ",2");

		TscLibDll.INSTANCE.sendcommand("BOX 424," + (924) + ",425,"
				+ ((150 - 2 - 15) * 8) + ",2");

		TscLibDll.INSTANCE.sendcommand("BARCODE " + (424 + 5 * 8) + ","
				+ (924 + 3 * 8) + ",\"128\",48,1,0,2,2,\""
				+ param.get("logisticsid") + "" + "\"");

		TscLibDll.INSTANCE.sendcommand("BOX 64," + (924 - 12 * 8) + ",784,"
				+ (924 - 12 * 8 + 1) + ",2");

		TscLibDll.INSTANCE.windowsfont(64 + 2 * 8, (720 + 9 * 8), 24, 0, 0, 0,
				"Arial", "收件人  " + param.get("customer_name") + "");

		TscLibDll.INSTANCE.windowsfont((784 - 30 * 8), (720 + 9 * 8), 24, 0, 0,
				0, "Arial", "电话：");

		TscLibDll.INSTANCE.windowsfont((784 - 20 * 8), (720 + 9 * 8), 24, 0, 2,
				0, "Arial", param.get("telphone") + "");

		printTextArea(address, (end - start), start + margin, 924 - 10 * 8, 32);

		TscLibDll.INSTANCE.windowsfont(24, (924 - 16 * 8), 32, 0, 0, 0,
				"Arial", "客");

		TscLibDll.INSTANCE.windowsfont(24, (924 - 16 * 8) + 32, 32, 0, 0, 0,
				"Arial", "户");

		TscLibDll.INSTANCE.windowsfont(24, (924 - 16 * 8) + 32 * 2, 32, 0, 0,
				0, "Arial", "信");

		TscLibDll.INSTANCE.windowsfont(24, (924 - 16 * 8) + 32 * 3, 32, 0, 0,
				0, "Arial", "息");

		TscLibDll.INSTANCE
				.windowsfont(24, (924 + 8), 32, 0, 0, 0, "Arial", "订");

		TscLibDll.INSTANCE.windowsfont(24, (924 + 8) + 32, 32, 0, 0, 0,
				"Arial", "购");

		TscLibDll.INSTANCE.windowsfont(24, (924 + 8) + 32 * 2, 32, 0, 0, 0,
				"Arial", "信");

		TscLibDll.INSTANCE.windowsfont(24, (924 + 8) + 32 * 3, 32, 0, 0, 0,
				"Arial", "息");

		TscLibDll.INSTANCE.sendcommand("PUTPCX 100,100,\"ksk.pcx\"");
	}

	private static void printTextArea(String address, int textLength, int x,
			int y, int textHeight) {
		String text_ary[] = getTextPerLine(address, textLength);
		TscLibDll.INSTANCE.windowsfont(x, y, textHeight, 0, 0, 0, "Arial",
				text_ary[0]);
		if ("".equals(text_ary[1])) {
			return;
		}
		printTextArea(text_ary[1], textLength, x, y + textHeight, textHeight);
	}

	private static String[] getTextPerLine(String address, int textLength) {
		String[] str_ary = new String[2];
		int i = 0;
		for (; i < address.length(); i++) {
			if ((address.substring(0, i).length() * 28) > textLength) {
				break;
			}
		}
		str_ary[0] = address.substring(0, i);
		str_ary[1] = i == address.length() ? "" : address.substring(i);

		return str_ary;
	}

	private static Font f = new Font("Arial", Font.PLAIN, 27);
	private static JComponent t = new JLabel();
	private static FontMetrics fm = t.getFontMetrics(f);

	private static void test4() {
		int strWidth = fm.stringWidth("你好");
		int strHeight = fm.getHeight();
		System.out.println(strWidth);
		System.out.println(strHeight);
	}

	public interface TscLibDll extends Library {
		TscLibDll INSTANCE = (TscLibDll) Native.loadLibrary("TSCLIB",
				TscLibDll.class);

		int about();

		int openport(String pirnterName);

		int closeport();

		int sendcommand(String printerCommand);

		int setup(String width, String height, String speed, String density,
				String sensor, String vertical, String offset);

		int downloadpcx(String filename, String image_name);

		int barcode(String x, String y, String type, String height,
				String readable, String rotation, String narrow, String wide,
				String code);

		int printerfont(String x, String y, String fonttype, String rotation,
				String xmul, String ymul, String text);

		int clearbuffer();

		int printlabel(String set, String copy);

		int formfeed();

		int nobackfeed();

		int windowsfont(int x, int y, int fontheight, int rotation,
				int fontstyle, int fontunderline, String szFaceName,
				String content);
	}
}
