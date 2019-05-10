package com.efuture.portal.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 生成唯一序号19位，格式：32位二进制时间(LONG型) + 20位二进制序号(0~999999循环的整数) + 3位十进制整数(机器号)
 */
public class UniqueID {
	private static UniqueID instance;

	/** 序号。范围（0-999999） */
	private static Integer number = new Integer(0);

	/** 机器号 */
	private String hostId = "";

	/**
	 * 获取ID
	 * 
	 * @return BigDecimal 唯一ID
	 */
	public long getId() {
		synchronized (UniqueID.class) {
			return createId();
		}
	}

	/**
	 * 获取一组ID
	 * 
	 * @param number
	 *            ID个数
	 * @return List<String> 唯一ID
	 */
	public List<Long> getIdArray(int number) {
		List<Long> retArray = new ArrayList<Long>();
		synchronized (UniqueID.class) {
			for (int i = 0; i < number; i++) {
				retArray.add(createId());
			}
		}

		return retArray;
	}

	/**
	 * 创建ID
	 * 
	 * @return BigDecimal ID
	 */
	private long createId() {
		long uid = 0;
		Date dt = new Date();

		// 最大ID(7643726453097023999)
		// dt.setYear(300);
		// dt.setMonth(11);
		// dt.setDate(31);
		// dt.setHours(23);
		// dt.setMinutes(59);
		// dt.setSeconds(59);
		// number = 999999;
		// hostId = "999";

		// 时间去掉毫秒(32位二进制整数,可表示到2200/12/31 23:59:59年)
		uid = dt.getTime() / 1000;
		uid <<= 20;

		// 加上序号(20位二进制整数,最大可表示整数999999)
		uid += number;
		number = (number + 1) % 1000000;

		// 加上机器号(3位10进制,最大整数999)
		return Long.parseLong(String.valueOf(uid) + hostId);
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public static long getUniqueID() {
		if (instance == null) {
			instance = new UniqueID();
			instance.setHostId("200");
		}

		return instance.getId();
	}

}
