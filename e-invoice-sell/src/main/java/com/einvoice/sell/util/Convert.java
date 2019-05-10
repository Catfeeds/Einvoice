package com.einvoice.sell.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.CharacterIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.util.StringUtils;

/**
 * 通用数据类型转换函数
 * 
 * @author ZJL
 * 
 */
public class Convert {
	/**
	 * Hexadecimal characters corresponding to each half byte value.
	 */
	private static final char[] HexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	/**
	 * The BASE64 encoding standard's 6-bit alphabet, from RFC 1521, plus the
	 * padding character at the end.
	 */
	private static final char[] Base64Chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '+', '/', '=' };

	/**
	 * Converts a long integer to an unsigned hexadecimal String. Treats the
	 * integer as an unsigned 64 bit value and left-pads with the pad character
	 * of the caller's choice.
	 * 
	 * @param value
	 *            The long integer to convert to a hexadecimal string.
	 * @param len
	 *            The total padded length of the string. If the number is larger
	 *            than the padded length, then this length of the string will be
	 *            the length of the number.
	 * @param pad
	 *            The character to use for padding.
	 * @return Unsigned hexadecimal numeric string representing the specified
	 *         value.
	 */
	public static final String LongToHex(long value, int len, char pad) {
		StringBuffer sb = new StringBuffer(Long.toHexString(value));
		int npad = len - sb.length();
		while (npad-- > 0)
			sb.insert(0, pad);
		return new String(sb);
	}

	/**
	 * Converts an arbitrary array of bytes to ASCII hexadecimal string form,
	 * with two hex characters corresponding to each byte. The length of the
	 * resultant string in characters will be twice the length of the specified
	 * array of bytes.
	 * 
	 * @param bytes
	 *            The array of bytes to convert to ASCII hex form.
	 * @return An ASCII hexadecimal numeric string representing the specified
	 *         array of bytes.
	 */
	public static final String BytesToHex(byte[] bytes) {
		if (bytes == null)
			return "";
		if (bytes.length == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		int i;
		for (i = 0; i < bytes.length; i++) {
			sb.append(HexChars[(bytes[i] >> 4) & 0xf]);
			sb.append(HexChars[bytes[i] & 0xf]);
		}
		return new String(sb);
	}

	/**
	 * 字符串替换(不区分大小写)
	 * 
	 * @param sourcestr
	 *            原字符串
	 * @param oldstr
	 *            旧的字符串
	 * @param newstr
	 *            替换的字符串
	 * @return
	 */
	public static String Replace(String sourcestr, String oldstr, String newstr) {
		return StringHelper.Replace(sourcestr, oldstr, newstr);
	}

	/**
	 * 字符串替换(不区分大小写)
	 * 
	 * @param sourcestr
	 *            原字符串
	 * @param oldstr
	 *            旧的字符串
	 * @param newstr
	 *            替换的字符串
	 * @return
	 */
	public static String replace(String sourcestr, String oldstr, String newstr) {
		return StringHelper.Replace(sourcestr, oldstr, newstr);
	}

	/**
	 * 替换字符串
	 * 
	 * @param strExpression
	 * @param strSearch
	 * @param strReplace
	 * @param intMode
	 *            0=区分大小写,1=不区分大小写
	 * @return
	 */
	public static String Replace(String strExpression, String strSearch, String strReplace, int intMode) {
		return StringHelper.Replace(strExpression, strSearch, strReplace, intMode);
	}

	/**
	 * 替换 Json的特殊字符
	 * 
	 * @param str
	 *            要处理的字符
	 * @return
	 */
	public static String formatJson(String str) {
		String s = str;
		s = Replace(s, "\"", "\\\"");
		s = Replace(s, "\\", "\\\\");
		s = Replace(s, "/", "\\/");

		return s;
	}

	/**
	 * Hex String to Byte Array
	 * 
	 * @param s
	 * @return
	 */
	public static final byte[] HexToBytes(String s) {
		if (s.equals(""))
			return null;
		byte[] arr = new byte[s.length() / 2];
		for (int start = 0; start < s.length(); start += 2) {
			String thisByte = s.substring(start, start + 2);
			arr[start / 2] = Byte.parseByte(thisByte, 16);
		}
		return arr;
	}

	/**
	 * Performs RFC1521 style Base64 encoding of arbitrary binary data. The
	 * output is a java String containing the Base64 characters representing the
	 * binary data. Be aware that this string is in Unicode form, and should be
	 * converted to UTF8 with the usual java conversion routines before it is
	 * sent over a network. The output string is guaranteed to only contain
	 * characters that are a single byte in UTF8 format. Also be aware that this
	 * routine leaves it to the caller to break the string into 70 byte lines as
	 * per RFC1521.
	 * 
	 * @param bytes
	 *            The array of bytes to convert to Base64 encoding.
	 * @return An string containing the specified bytes in Base64 encoded form.
	 */
	public static final String BytesToBase64String(byte[] bytes) {
		return BytesToBase64String(bytes, Base64Chars);
	}

	/**
	 * Performs encoding of arbitrary binary data based on a 6 bit alphabet. The
	 * output is a java String containing the encoded characters representing
	 * the binary data. Be aware that this string is in Unicode form, and should
	 * be converted to UTF8 with the usual java conversion routines before it is
	 * sent over a network. The alphabet passed in via <code>chars</code> is
	 * used without further checks, it's the callers responsibility to set it to
	 * something meaningful.
	 * 
	 * @param bytes
	 *            The array of bytes to convert to Base64 encoding.
	 * @param chars
	 *            The alphabet used in encoding. Must contain exactly 65
	 *            characters: A 6 bit alphabet plus one padding char at position
	 *            65.
	 * @return An string containing the specified bytes in Base64 encoded form.
	 */
	public static final String BytesToBase64String(byte[] bytes, char[] chars) {
		if (bytes == null)
			return "";
		StringBuffer sb = new StringBuffer();
		int len = bytes.length, i = 0, ival;
		while (len >= 3) {
			ival = ((int) bytes[i++] + 256) & 0xff;
			ival <<= 8;
			ival += ((int) bytes[i++] + 256) & 0xff;
			ival <<= 8;
			ival += ((int) bytes[i++] + 256) & 0xff;
			len -= 3;
			sb.append(chars[(ival >> 18) & 63]);
			sb.append(chars[(ival >> 12) & 63]);
			sb.append(chars[(ival >> 6) & 63]);
			sb.append(chars[ival & 63]);
		}
		switch (len) {
		case 0: // No pads needed.
			break;
		case 1: // Two more output bytes and two pads.
			ival = ((int) bytes[i++] + 256) & 0xff;
			ival <<= 16;
			sb.append(chars[(ival >> 18) & 63]);
			sb.append(chars[(ival >> 12) & 63]);
			sb.append(chars[64]);
			sb.append(chars[64]);
			break;
		case 2: // Three more output bytes and one pad.
			ival = ((int) bytes[i++] + 256) & 0xff;
			ival <<= 8;
			ival += ((int) bytes[i] + 256) & 0xff;
			ival <<= 8;
			sb.append(chars[(ival >> 18) & 63]);
			sb.append(chars[(ival >> 12) & 63]);
			sb.append(chars[(ival >> 6) & 63]);
			sb.append(chars[64]);
			break;
		}
		return new String(sb);
	}

	/**
	 * Performs RFC1521 style Base64 decoding of Base64 encoded data. The output
	 * is a byte array containing the decoded binary data. The input is expected
	 * to be a normal Unicode String object.
	 * 
	 * @param s
	 *            The Base64 encoded string to decode into binary data.
	 * @return An array of bytes containing the decoded data.
	 */
	public static final byte[] Base64StringToBytes(String s) {
		if (s.equals("") || s == null)
			return null;
		try {
			StringCharacterIterator iter = new StringCharacterIterator(s);
			ByteArrayOutputStream bytestr = new ByteArrayOutputStream();
			DataOutputStream outstr = new DataOutputStream(bytestr);
			char c;
			int d, i, group;
			int[] bgroup = new int[4];
			decode: for (i = 0, group = 0, c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
				switch (c) {
				case 'A':
					d = 0;
					break;
				case 'B':
					d = 1;
					break;
				case 'C':
					d = 2;
					break;
				case 'D':
					d = 3;
					break;
				case 'E':
					d = 4;
					break;
				case 'F':
					d = 5;
					break;
				case 'G':
					d = 6;
					break;
				case 'H':
					d = 7;
					break;
				case 'I':
					d = 8;
					break;
				case 'J':
					d = 9;
					break;
				case 'K':
					d = 10;
					break;
				case 'L':
					d = 11;
					break;
				case 'M':
					d = 12;
					break;
				case 'N':
					d = 13;
					break;
				case 'O':
					d = 14;
					break;
				case 'P':
					d = 15;
					break;
				case 'Q':
					d = 16;
					break;
				case 'R':
					d = 17;
					break;
				case 'S':
					d = 18;
					break;
				case 'T':
					d = 19;
					break;
				case 'U':
					d = 20;
					break;
				case 'V':
					d = 21;
					break;
				case 'W':
					d = 22;
					break;
				case 'X':
					d = 23;
					break;
				case 'Y':
					d = 24;
					break;
				case 'Z':
					d = 25;
					break;
				case 'a':
					d = 26;
					break;
				case 'b':
					d = 27;
					break;
				case 'c':
					d = 28;
					break;
				case 'd':
					d = 29;
					break;
				case 'e':
					d = 30;
					break;
				case 'f':
					d = 31;
					break;
				case 'g':
					d = 32;
					break;
				case 'h':
					d = 33;
					break;
				case 'i':
					d = 34;
					break;
				case 'j':
					d = 35;
					break;
				case 'k':
					d = 36;
					break;
				case 'l':
					d = 37;
					break;
				case 'm':
					d = 38;
					break;
				case 'n':
					d = 39;
					break;
				case 'o':
					d = 40;
					break;
				case 'p':
					d = 41;
					break;
				case 'q':
					d = 42;
					break;
				case 'r':
					d = 43;
					break;
				case 's':
					d = 44;
					break;
				case 't':
					d = 45;
					break;
				case 'u':
					d = 46;
					break;
				case 'v':
					d = 47;
					break;
				case 'w':
					d = 48;
					break;
				case 'x':
					d = 49;
					break;
				case 'y':
					d = 50;
					break;
				case 'z':
					d = 51;
					break;
				case '0':
					d = 52;
					break;
				case '1':
					d = 53;
					break;
				case '2':
					d = 54;
					break;
				case '3':
					d = 55;
					break;
				case '4':
					d = 56;
					break;
				case '5':
					d = 57;
					break;
				case '6':
					d = 58;
					break;
				case '7':
					d = 59;
					break;
				case '8':
					d = 60;
					break;
				case '9':
					d = 61;
					break;
				case '+':
					d = 62;
					break;
				case '/':
					d = 63;
					break;
				default:
					// Any character not in Base64 alphabet is treated
					// as end of data. This includes the '=' (pad) char.
					break decode; // Skip illegal characters.
				}
				bgroup[i++] = d;
				if (i >= 4) {
					i = 0;
					group = ((bgroup[0] & 63) << 18) + ((bgroup[1] & 63) << 12) + ((bgroup[2] & 63) << 6)
							+ (bgroup[3] & 63);
					outstr.writeByte(((group >> 16) & 255));
					outstr.writeByte(((group >> 8) & 255));
					outstr.writeByte(group & 255);
				}
			}
			// Handle the case of remaining characters and
			// pad handling. If input is not a multiple of 4
			// in length, then '=' pads are assumed.
			switch (i) {
			case 2:
				// One output byte from two input bytes.
				group = ((bgroup[0] & 63) << 18) + ((bgroup[1] & 63) << 12);
				outstr.writeByte(((group >> 16) & 255));
				break;
			case 3:
				// Two output bytes from three input bytes.
				group = ((bgroup[0] & 63) << 18) + ((bgroup[1] & 63) << 12) + ((bgroup[2] & 63) << 6);
				outstr.writeByte(((group >> 16) & 255));
				outstr.writeByte(((group >> 8) & 255));
				break;
			default:
				// Any other case, including correct 0, is treated as
				// end of data.
				break;
			}
			outstr.flush();
			return bytestr.toByteArray();
		} catch (IOException e) {
		} // Won't happen. Return null if it does.
		return null;
	}

	/**
	 * Int32转换为String
	 * 
	 * @param value
	 * @return
	 */
	public static String Int32ToString(int value) {
		return Integer.toString(value);
	}

	/**
	 * String 转换为Int32
	 * 
	 * @param value
	 * @return
	 */
	public static int StringToInt32(String value) {
		return Integer.parseInt(value);
	}

	/**
	 * Short类型转换为String
	 * 
	 * @param value
	 * @return
	 */
	public static String Int16ToString(short value) {
		return Short.toString(value);
	}

	/**
	 * String转换为Short
	 * 
	 * @param value
	 * @return
	 */
	public static Short StringToInt16(String value) {
		return Short.parseShort(value);
	}

	/**
	 * Long转换为String
	 * 
	 * @param value
	 * @return
	 */
	public static String Int64ToString(long value) {
		return Long.toString(value);
	}

	/**
	 * String转换为Long
	 * 
	 * @param value
	 * @return
	 */
	public static long StringToInt64(String value) {
		return Long.parseLong(value);
	}

	/**
	 * Double转换为String
	 * 
	 * @param value
	 * @return
	 */
	public static String DoubleToString(double value) {
		return Double.toString(value);
	}

	/**
	 * String转换为Double
	 * 
	 * @param value
	 * @return
	 */
	public static double StringToDouble(String value) {
		return Double.parseDouble(value);
	}

	/**
	 * Float转换为String
	 * 
	 * @param value
	 * @return
	 */
	public static String FloatToString(float value) {
		return Float.toString(value);
	}

	/**
	 * String转换为Float
	 * 
	 * @param value
	 * @return
	 */
	public static float StringToFloat(String value) {
		return Float.parseFloat(value);
	}

	/**
	 * String转换为BigDecimal
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal StringToDecimal(String value) {
		if (value == null || value.equals(""))
			value = "0";
		return new BigDecimal(value);
	}

	/**
	 * Double转换为BigDecimal
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal DoubleToDecimal(double value) {
		return new BigDecimal(value);
	}

	/**
	 * BigDecimal转换为String
	 * 
	 * @param value
	 * @return
	 */
	public static String DecimalToString(BigDecimal value) {
		return value.toString();
	}

	/**
	 * AscII代码转换为Char
	 * 
	 * @param code
	 * @return
	 */
	public static Character AscIIToChar(int code) {
		Character aChar = new Character((char) code);
		return aChar;
	}

	/**
	 * Char转换为Int
	 * 
	 * @param ch
	 * @return
	 */
	public static Integer CharToInt(char ch) {
		int i = (int) ch;
		return i;
	}

	/**
	 * Boolean转换为Int
	 * 
	 * @param value
	 * @return
	 */
	public static Integer BooleanToInt(boolean value) {
		int i = (value) ? 1 : 0;
		return i;
	}

	/**
	 * Int转换为Boolean
	 * 
	 * @param value
	 * @return
	 */
	public static Boolean IntToBoolean(int value) {
		boolean i = (value != 0);
		return i;
	}

	/**
	 * 通用Object转换为Int32用
	 * 
	 * @param value
	 * @return
	 */
	public static Integer ToInt32(Object value) {
		if (value instanceof Integer) {
			int value2 = ((Integer) value).intValue();
			return value2;
		} else if (value instanceof Short) {
			Short s = (Short) value;
			return s.intValue();
		} else if (value instanceof String) {
			String s = (String) value;
			return StringToInt32(s);
		} else if (value instanceof StringBuffer) {
			StringBuffer s = (StringBuffer) value;
			return StringToInt32(s.toString());
		} else if (value instanceof Double) {
			double d = ((Double) value).doubleValue();
			int value2 = (int) d;
			return value2;
		} else if (value instanceof Float) {
			float f = ((Float) value).floatValue();
			int value2 = (int) f;
			return value2;
		} else if (value instanceof Long) {
			long l = ((Long) value).longValue();
			int value2 = (int) l;
			return value2;
		} else if (value instanceof Boolean) {
			boolean b = ((Boolean) value).booleanValue();
			return BooleanToInt(b);
		} else if (value instanceof Date) {
			// Date d = (Date) value;
			return 0;
		} else if (value instanceof BigDecimal) {
			BigDecimal d = (BigDecimal) value;
			int value2 = d.toBigInteger().intValue();
			return value2;
		} else if (value instanceof BigInteger) {
			BigInteger d = (BigInteger) value;
			int value2 = d.intValue();
			return value2;
		}
		return 0;
	}

	/**
	 * 通用Object转换为Short
	 * 
	 * @param value
	 * @return
	 */
	public static Short ToInt16(Object value) {
		if (value instanceof Integer) {
			int value2 = ((Integer) value).intValue();
			return (short) value2;
		} else if (value instanceof Short) {
			Short s = (Short) value;
			return s;
		} else if (value instanceof String) {
			String s = (String) value;
			return StringToInt16(s);
		} else if (value instanceof StringBuffer) {
			StringBuffer s = (StringBuffer) value;
			return StringToInt16(s.toString());
		} else if (value instanceof Double) {
			double d = ((Double) value).doubleValue();
			int value2 = (int) d;
			return (short) value2;
		} else if (value instanceof Float) {
			float f = ((Float) value).floatValue();
			int value2 = (int) f;
			return (short) value2;
		} else if (value instanceof Long) {
			long l = ((Long) value).longValue();
			int value2 = (int) l;
			return (short) value2;
		} else if (value instanceof Boolean) {
			boolean b = ((Boolean) value).booleanValue();
			int i = (b) ? 1 : 0;
			return (short) i;
		} else if (value instanceof Date) {
			// Date d = (Date) value;
			return (short) 0;
		} else if (value instanceof BigDecimal) {
			BigDecimal d = (BigDecimal) value;
			int value2 = d.toBigInteger().intValue();
			return (short) value2;
		}
		return 0;
	}

	/**
	 * 通用Object转换为Long
	 * 
	 * @param value
	 * @return
	 */
	public static Long ToInt64(Object value) {
		if (value instanceof Integer) {
			int value2 = ((Integer) value).intValue();
			return (long) value2;
		} else if (value instanceof Short) {
			Short s = (Short) value;
			return (long) s;
		} else if (value instanceof String) {
			String s = (String) value;
			return StringToInt64(s);
		} else if (value instanceof StringBuffer) {
			StringBuffer s = (StringBuffer) value;
			return StringToInt64(s.toString());
		} else if (value instanceof Double) {
			double d = ((Double) value).doubleValue();
			int value2 = (int) d;
			return (long) value2;
		} else if (value instanceof Float) {
			float f = ((Float) value).floatValue();
			int value2 = (int) f;
			return (long) value2;
		} else if (value instanceof Long) {
			long l = ((Long) value).longValue();
			return l;
		} else if (value instanceof Boolean) {
			boolean b = ((Boolean) value).booleanValue();
			int i = (b) ? 1 : 0;
			return (long) i;
		} else if (value instanceof Date) {
			// Date d = (Date) value;
			return (long) 0;
		} else if (value instanceof BigDecimal) {
			BigDecimal d = (BigDecimal) value;
			long value2 = d.toBigInteger().longValue();
			return value2;
		}
		return (long) 0;
	}

	/**
	 * 通用Object转换为String
	 * 
	 * @param value
	 * @return
	 * @throws @throws
	 *             Throwable
	 */
	public static String ToString(Object value) {
		if (value instanceof Integer) {
			int value2 = ((Integer) value).intValue();
			return Int32ToString(value2);
		} else if (value instanceof Short) {
			Short s = (Short) value;
			return Int16ToString(s);
		} else if (value instanceof Character) {
			Character s = (Character) value;
			return s.toString();

		} else if (value instanceof String) {
			String s = (String) value;
			return s;
		} else if (value instanceof StringBuffer) {
			StringBuffer s = (StringBuffer) value;
			return s.toString();
		} else if (value instanceof Double) {
			double d = ((Double) value).doubleValue();
			String str = DoubleToString(d);
			if (str.endsWith(".0"))
				str = str.replaceAll("\\.0", "");
			if (str.endsWith(".00"))
				str = str.replaceAll("\\.00", "");
			return str;
		} else if (value instanceof Float) {
			float f = ((Float) value).floatValue();
			String str = FloatToString(f);
			if (str.endsWith(".0"))
				str = str.replaceAll("\\.0", "");
			if (str.endsWith(".00"))
				str = str.replaceAll("\\.00", "");
			return str;
		} else if (value instanceof Long) {
			long l = ((Long) value).longValue();
			return Int64ToString(l);
		} else if (value instanceof java.sql.Blob) {
			java.sql.Blob blob = (java.sql.Blob) value;
			if (blob == null)
				return "";
			byte[] data = Convert.BlobToBytes(blob);
			/*
			 * try { return new String(data,"UTF-8"); } catch
			 * (UnsupportedEncodingException e) { e.printStackTrace(); }
			 */
			return ByteUtils.logBytes(data);
			// return Convert.BytesToHex(data);
		} else if (value instanceof java.sql.Clob) {
			java.sql.Clob clob = (java.sql.Clob) value;
			if (clob == null)
				return "";
			return Convert.ClobToString(clob);
		} else if (value instanceof Boolean) {
			boolean b = ((Boolean) value).booleanValue();
			return b ? "true" : "false";
		} else if (value instanceof java.util.Date) {
			java.util.Date d = (java.util.Date) value;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String str = formatter.format(d);
			return str;
		} else if (value instanceof java.sql.Timestamp) {
			java.sql.Timestamp d2 = (java.sql.Timestamp) value;
			java.util.Date d = Convert.ConvertToSQLDate(d2);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String str = formatter.format(d);
			return str;
		} else if (value instanceof Calendar) {
			Calendar d = (Calendar) value;
			java.util.Date d2 = d.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String str = formatter.format(d2);
			return str;

		} else if (value instanceof BigDecimal) {
			BigDecimal d = (BigDecimal) value;
			return d.toString();
		}
		return "";
	}

	public static String DateToStr(java.util.Date date, String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String str = formatter.format(date);
		return str;
	}

	public static String SQLDateToStr(java.sql.Timestamp date, String dateFormat) {
		java.util.Date date2 = Convert.ToDateTime(date);
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String str = formatter.format(date2);
		return str;
	}

	/**
	 * 通用Object转换为Double
	 * 
	 * @param value
	 * @return
	 */
	public static Double ToDouble(Object value) {
		if (value instanceof Integer) {
			int value2 = ((Integer) value).intValue();
			return StringToDouble(Int32ToString(value2));
		} else if (value instanceof Short) {
			Short s = (Short) value;
			return StringToDouble(Int16ToString(s));
		} else if (value instanceof String) {
			String s = (String) value;
			return StringToDouble(s);
		} else if (value instanceof StringBuffer) {
			StringBuffer s = (StringBuffer) value;
			return StringToDouble(s.toString());
		} else if (value instanceof Double) {
			double d = ((Double) value).doubleValue();
			return d;
		} else if (value instanceof Float) {
			float f = ((Float) value).floatValue();
			return StringToDouble(FloatToString(f));
		} else if (value instanceof Long) {
			long l = ((Long) value).longValue();
			return StringToDouble(Int64ToString(l));
		} else if (value instanceof Boolean) {
			boolean b = ((Boolean) value).booleanValue();
			double d = b ? (double) 1 : (double) 0;
			return d;
		} else if (value instanceof Date) {
			Date d = (Date) value;
			return Convert.ToDouble(d.getTime());
		} else if (value instanceof BigDecimal) {
			BigDecimal d = (BigDecimal) value;
			double value2 = d.doubleValue();
			return value2;
		}
		return (double) 0;
	}

	/**
	 * 通用Object转换为Boolean
	 * 
	 * @param value
	 * @return
	 */
	public static Boolean ToBoolean(Object value) {
		if (value instanceof String) {
			String s = (String) value;
			if (s.equalsIgnoreCase("true"))
				return true;
			if (s.equalsIgnoreCase("t"))
				return true;
			if (s.equalsIgnoreCase("1"))
				return true;

			if (s.equalsIgnoreCase("false"))
				return false;
			if (s.equalsIgnoreCase("f"))
				return false;
			if (s.equalsIgnoreCase("0"))
				return false;

		}
		int iValue = Convert.ToInt32(value);
		return IntToBoolean(iValue);
	}

	/**
	 * 通用Object转换为Float
	 * 
	 * @param value
	 * @return
	 */
	public static Float ToFloat(Object value) {
		if (value instanceof Integer) {
			int value2 = ((Integer) value).intValue();
			return StringToFloat(Int32ToString(value2));
		} else if (value instanceof Short) {
			Short s = (Short) value;
			return StringToFloat(Int16ToString(s));
		} else if (value instanceof String) {
			String s = (String) value;
			return StringToFloat(s);
		} else if (value instanceof StringBuffer) {
			StringBuffer s = (StringBuffer) value;
			return StringToFloat(s.toString());
		} else if (value instanceof Double) {
			double d = ((Double) value).doubleValue();
			return StringToFloat(DoubleToString(d));
		} else if (value instanceof Float) {
			float f = ((Float) value).floatValue();
			return f;
		} else if (value instanceof Long) {
			long l = ((Long) value).longValue();
			return StringToFloat(Int64ToString(l));
		} else if (value instanceof Boolean) {
			boolean b = ((Boolean) value).booleanValue();
			float d = b ? (float) 1 : (float) 0;
			return d;
		} else if (value instanceof Date) {
			Date d = (Date) value;
			return Convert.ToFloat(d.getTime());
		} else if (value instanceof BigDecimal) {
			BigDecimal d = (BigDecimal) value;
			float value2 = d.floatValue();
			return value2;
		}
		return (float) 0;
	}

	/**
	 * 通用Object转换为BigDecimal
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal ToDecimal(Object value) {
		if (value instanceof Integer) {
			int value2 = ((Integer) value).intValue();
			return StringToDecimal(Int32ToString(value2));
		} else if (value instanceof Short) {
			Short s = (Short) value;
			return StringToDecimal(Int16ToString(s));
		} else if (value instanceof String) {
			String s = (String) value;
			return StringToDecimal(s);
		} else if (value instanceof StringBuffer) {
			StringBuffer s = (StringBuffer) value;
			return StringToDecimal(s.toString());
		} else if (value instanceof Double) {
			double d = ((Double) value).doubleValue();
			return StringToDecimal(DoubleToString(d));
		} else if (value instanceof Float) {
			float f = ((Float) value).floatValue();
			return StringToDecimal(FloatToString(f));
		} else if (value instanceof Long) {
			long l = ((Long) value).longValue();
			return StringToDecimal(Int64ToString(l));
		} else if (value instanceof Boolean) {
			boolean b = ((Boolean) value).booleanValue();
			BigDecimal d = b ? new BigDecimal(1) : new BigDecimal(0);
			return d;
		} else if (value instanceof Date) {
			Date d = (Date) value;
			return Convert.ToDecimal(d.getTime());
		} else if (value instanceof BigDecimal) {
			BigDecimal d = (BigDecimal) value;
			return d;
		}
		return new BigDecimal(0);
	}

	/**
	 * 通用Object转换为Date
	 * 
	 * @param value
	 * @return
	 */
	public static java.util.Date ToDateTime(Object value) {
		if (value instanceof java.util.Date) {
			java.util.Date d = (java.util.Date) value;
			return d;
		}
		if (value instanceof java.sql.Timestamp) {
			java.sql.Timestamp d = (java.sql.Timestamp) value;
			return new java.util.Date(d.getTime());
		}
		if (value instanceof String) {
			String d = Convert.ToString(value);
			return StringToDateTime(d);
		}
		if (value instanceof StringBuffer) {
			StringBuffer d = (StringBuffer) value;
			return StringToDateTime(d.toString());
		}
		if (value instanceof Long) {
			long lValue = Convert.ToInt64(value);
			java.util.Date d = new java.util.Date(lValue);
			return d;
		}
		if (value instanceof Integer) {
			long lValue = Convert.ToInt64(value);
			java.util.Date d = new java.util.Date(lValue);
			return d;
		}
		if (value instanceof Short) {
			long lValue = Convert.ToInt64(value);
			java.util.Date d = new java.util.Date(lValue);
			return d;
		}
		if (value instanceof Double) {
			long lValue = Convert.ToInt64(value);
			java.util.Date d = new java.util.Date(lValue);
			return d;
		}
		if (value instanceof Float) {
			long lValue = Convert.ToInt64(value);
			java.util.Date d = new java.util.Date(lValue);
			return d;
		}
		if (value instanceof BigDecimal) {
			long lValue = Convert.ToInt64(value);
			java.util.Date d = new java.util.Date(lValue);
			return d;
		}
		java.util.Calendar calender = java.util.Calendar.getInstance();
		return calender.getTime();

	}

	public static java.util.Date StringToDateTime(String value, String sFormat) {
		String DateTime = value;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		SimpleDateFormat s = new SimpleDateFormat(sFormat);

		java.util.Date date = new java.util.Date();
		try {
			date = s.parse(DateTime);
		} catch (ParseException e) {
		}
		calendar.setTime(date);
		return date;
	}

	public static java.util.Date StringToDateTime(String value) {
		if (value.equals("0000-00-00"))
			value = "1970-01-01";
		if (value.length() > 12)
			return StringToDateTime(value, "yyyy-MM-dd HH:mm:ss");
		return StringToDateTime(value, "yyyy-MM-dd");
	}

	/**
	 * 通用Object转换为Date
	 * 
	 * @param value
	 * @return
	 */
	public static java.sql.Timestamp ToSQLDateTime(Object value) {
		if (value instanceof java.util.Date) {
			java.util.Date d = (java.util.Date) value;
			return new java.sql.Timestamp(d.getTime());
		}
		if (value instanceof java.sql.Timestamp) {
			java.sql.Timestamp d = (java.sql.Timestamp) value;
			return d;
		}
		if (value instanceof String) {
			String d = Convert.ToString(value);
			if (d == null || d.equals("") || d.equals("0000-00-00")) {
				return new java.sql.Timestamp(1970, 1, 1, 0, 0, 0, 0);
			}
			java.util.Date d2 = StringToDateTime(d);
			return ConvertToSQLDate(d2);
		}
		if (value instanceof StringBuffer) {
			StringBuffer d = (StringBuffer) value;
			java.util.Date d2 = StringToDateTime(d.toString());
			return ConvertToSQLDate(d2);
		}
		if (value instanceof Long) {
			long lValue = Convert.ToInt64(value);
			java.sql.Timestamp d = new java.sql.Timestamp(lValue);
			return d;
		}
		if (value instanceof Integer) {
			long lValue = Convert.ToInt64(value);
			java.sql.Timestamp d = new java.sql.Timestamp(lValue);
			return d;
		}
		if (value instanceof Short) {
			long lValue = Convert.ToInt64(value);
			java.sql.Timestamp d = new java.sql.Timestamp(lValue);
			return d;
		}
		if (value instanceof Double) {
			long lValue = Convert.ToInt64(value);
			java.sql.Timestamp d = new java.sql.Timestamp(lValue);
			return d;
		}
		if (value instanceof Float) {
			long lValue = Convert.ToInt64(value);
			java.sql.Timestamp d = new java.sql.Timestamp(lValue);
			return d;
		}
		if (value instanceof BigDecimal) {
			long lValue = Convert.ToInt64(value);
			java.sql.Timestamp d = new java.sql.Timestamp(lValue);
			return d;
		}
		java.util.Calendar calender = java.util.Calendar.getInstance();
		java.util.Date date = calender.getTime();
		return new java.sql.Timestamp(date.getTime());

	}

	/**
	 * java.util.Date Convert to java.sql.Date
	 * 
	 * @param value
	 * @return
	 */
	public static java.sql.Timestamp ConvertToSQLDate(java.util.Date value) {
		return new java.sql.Timestamp(value.getTime());
	}

	/**
	 * 判断Object是否为Null
	 * 
	 * @param value
	 * @return
	 */
	public static Boolean IsNull(Object value) {
		if (value == null)
			return true;
		return false;
	}

	/**
	 * Blob Convert to Byte Array
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] BlobToBytes(java.sql.Blob value) {
		if (value == null)
			return null;
		byte[] data = null;
		try {
			data = value.getBytes(1, (int) value.length());
			return data;
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * CLOB大字段转换
	 * 
	 * @param cl
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static String ClobToString(Clob cl) {
		try {
			String reString = "";
			Reader is = cl.getCharacterStream();// 得到流
			BufferedReader br = new BufferedReader(is);
			String s = br.readLine();
			StringBuffer sb = new StringBuffer();
			// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
			while (s != null) {
				sb.append(s);
				s = br.readLine();
			}
			reString = sb.toString();
			return reString;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Byte Array Convert to Blob
	 * 
	 * @param value
	 * @return
	 */
	public static java.sql.Blob BytesToBlob(byte[] value) {
		try {
			java.sql.Blob blob = new SerialBlob(value);
			return blob;
		} catch (SerialException e) {
			return null;
		} catch (SQLException e) {
			return null;
		}
	}

	public static java.sql.Blob StringToBlob(String value) {
		try {
			byte[] b = StringToUTF8Bytes(value);
			java.sql.Blob blob = BytesToBlob(b);
			return blob;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * UTF8 Byte Array Convert to Unicode String
	 * 
	 * @param utf8
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String UTF8BytesToString(byte[] utf8) throws UnsupportedEncodingException {
		if (utf8 == null)
			return null;
		return new String(utf8, "UTF-8");
	}

	/**
	 * Unicode String Convert to UTF8 Byte Array
	 * 
	 * @param sourceStr
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] StringToUTF8Bytes(String sourceStr) throws UnsupportedEncodingException {
		if (sourceStr.equals("") || sourceStr == null)
			return null;
		byte[] utf8 = sourceStr.getBytes("UTF-8");
		return utf8;
	}

	/**
	 * GBK Byte Array Convert to Unicode String
	 * 
	 * @param utf8
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String GBKBytesToString(byte[] gbk) throws UnsupportedEncodingException {
		if (gbk == null)
			return null;
		return new String(gbk, "GBK");
	}

	/**
	 * GBK String Convert to UTF8 Byte Array
	 * 
	 * @param sourceStr
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] StringToGBKBytes(String sourceStr) throws UnsupportedEncodingException {
		if (sourceStr.equals("") || sourceStr == null)
			return null;
		byte[] gbk = sourceStr.getBytes("GBK");
		return gbk;
	}

	/**
	 * UTF8 String Convert to GBK String
	 * 
	 * @param sourceStr
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String UTF8ToGBK(String sourceStr) throws UnsupportedEncodingException {
		if (sourceStr.equals("") || sourceStr == null)
			return "";
		byte[] utf8 = sourceStr.getBytes("UTF-8");
		return new String(utf8, "GBK");
	}

	/**
	 * GBK String Convert to UTF8 String
	 * 
	 * @param sourceStr
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String GBKToUTF8(String sourceStr) throws UnsupportedEncodingException {
		if (sourceStr.equals("") || sourceStr == null)
			return "";
		byte[] utf8 = sourceStr.getBytes("GBK");
		return new String(utf8, "UTF-8");
	}

	/**
	 * Hex String to Integer
	 * 
	 * @param hexStr
	 * @return
	 */
	public static int HexToInt(String hexStr) {
		int i = Integer.valueOf(hexStr, 16).intValue();
		return i;
	}

	/**
	 * Get Date1 and Date2's Days
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int DaysDiff(Date date1, Date date2) {
		long diff = date2.getTime() - date1.getTime();
		if (diff < 0.0)
			diff = -diff;
		diff = diff / (1000 * 60 * 60 * 24);
		return Convert.ToInt32(diff);
	}

	/**
	 * 获取当天的日期
	 * 
	 * @return "YYYY-MM-DD"
	 */
	public static String getTodayString() {
		java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Date now = new java.util.Date();
		return sd.format(now);
	}

	/**
	 * 获取当前时间
	 * 
	 * @return HH:mm:ss
	 */
	public static String getNowTime() {
		java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat("HH:mm:ss");
		java.util.Date now = new java.util.Date();
		return sd.format(now);
	}

	/**
	 * 获取现在的时间
	 * 
	 * @return "YYYY-MM-DD HH:MM:SS"
	 */
	public static String getNowString() {
		java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date now = new java.util.Date();
		return sd.format(now);
	}

	/**
	 * 获取昨天日期
	 * 
	 * @return
	 */
	public static String getLastTodayString() {
		java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Date now = new java.util.Date();
		java.util.Date lastday = addDate(now, -1);
		return sd.format(lastday);
	}

	/**
	 * 获取明天日期
	 * 
	 * @return
	 */
	public static String getNextTodayString() {
		java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Date now = new java.util.Date();
		java.util.Date nextday = addDate(now, 1);
		return sd.format(nextday);
	}

	private static Calendar fromCal = Calendar.getInstance();

	/**
	 * 取得某天相加(减)後的那一天
	 * 
	 * @param date
	 *            原日期
	 * @param day
	 *            加天数
	 * @return 日期
	 */
	public static Date addDate(Date date, int day) {
		try {
			fromCal.setTime(date);
			fromCal.add(Calendar.DATE, day);

			// System.out.println("Date:"+dateFormat.format(fromCal.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return fromCal.getTime();
	}

	public static String formatTime(long timeLong) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(timeLong);
	}

	/**
	 * 转换字符串为Calendar类型
	 * 
	 * @param sDate
	 *            String 要转换的日期
	 * @param sDateTimeFormat
	 *            String 要转换的日期格式
	 * @return Calendar 返回指定格式的日期
	 */
	public static Calendar stringToCalendar(String sDate, String sDateTimeFormat) {
		Calendar cal = null;
		if (sDate != null && !sDate.equals("")) {
			try {
				cal = Calendar.getInstance();
				cal.setTime((new SimpleDateFormat(sDateTimeFormat)).parse(sDate));
			} catch (Exception exc) {
				cal = null;
			}
		}
		return cal;
	}

	/**
	 * 日期小时计算(加或者减)
	 * 
	 * @param sDate
	 *            日期对象,如:2007-09-25 01:12:28
	 * @param oper
	 *            oper 分钟, 加20小时如: +20, 减20小时如: -20
	 * @return 处理后的日期
	 */
	public static Calendar calHour(String sDate, int oper) {
		if (sDate != null) {
			try {
				Calendar cal = stringToCalendar(sDate, "yyyy-MM-dd HH:mm:ss");
				cal.add(Calendar.HOUR, oper);
				return cal;
			} catch (Exception exc) {
				return null;
			}
		}
		return null;
	}

	/**
	 * 日期小时计算(加或者减)
	 * 
	 * @param sDate
	 *            日期对象,如:2007-09-25 01:12:28
	 * @param oper
	 *            oper 分钟, 加20小时如: +20, 减20小时如: -20
	 * @return 处理后的日期
	 */
	public static Calendar calMinute(String sDate, int oper) {
		if (sDate != null) {
			try {
				Calendar cal = stringToCalendar(sDate, "yyyy-MM-dd HH:mm:ss");
				cal.add(Calendar.MINUTE, oper);
				return cal;
			} catch (Exception exc) {
				return null;
			}
		}
		return null;
	}

	/**
	 * 功能说明: 转换日期时间为指定的字符串格式
	 * 
	 * @author Snoopy Chen
	 * @param sourceObject
	 *            要转换的日期格式的对象,可以是日期字符串,也可以是日期类型. 要求日期字符串参数格式为:yyyy-MM-dd
	 *            HH:mm:ss
	 * @return 返回日期"字符串"格式:yyyy-MM-dd HH:mm:ss
	 */
	public static String formatDateTime(Object sourceObject) {
		if (sourceObject == null) {
			return "";
		} else if (sourceObject instanceof String) {
			SimpleDateFormat dF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dF.format(stringToDate((String) sourceObject));
		} else if (sourceObject instanceof java.util.Date) {
			SimpleDateFormat dF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dF.format((java.util.Date) sourceObject);
		} else if (sourceObject instanceof java.util.Calendar) {
			SimpleDateFormat dF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar t = (Calendar) sourceObject;
			return dF.format(t.getTime());
		}
		return "";
	}

	/**
	 * 转换字符串为Date类型
	 * 
	 * @param sDate
	 *            String
	 * @return Calendar 格式为:yyyy-MM-dd 或者:yyyy-MM-dd HH:mm
	 */
	public static Date stringToDate(String sDate) {
		if (sDate != null && !sDate.equals("")) {
			try {
				String sDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
				if (sDate.length() == 8)
					sDateTimeFormat = "HH:mm:ss";
				if (sDate.length() == 10)
					sDateTimeFormat = "yyyy-MM-dd";
				Date dt = (new SimpleDateFormat(sDateTimeFormat)).parse(sDate);

				return dt;
			} catch (Exception exc) {
				return null;
			}
		}
		return null;
	}

	public static Date stringToDate(String sDate, String sDateTimeFormat) {
		Date dt = null;
		if (sDate != null && !sDate.equals("")) {
			try {
				dt = new Date();
				if (sDate.length() == 5)
					sDateTimeFormat = "HH:mm";
				else if (sDate.length() == 8)
					sDateTimeFormat = "HH:mm:ss";
				else if (sDate.length() == 10)
					sDateTimeFormat = "yyyy-MM-dd";
				dt = (new SimpleDateFormat(sDateTimeFormat)).parse(sDate);
			} catch (Exception exc) {
				dt = null;
			}
		}
		return dt;
	}

	public static String dateFormat(Date date, String sDateTimeFormat) {
		String strdt = null;
		if (date != null) {
			try {
				java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(sDateTimeFormat);
				strdt = format.format(date);
			} catch (Exception exc) {
				return strdt;
			}
		}
		return strdt;
	}

	/**
	 * 获取当前时间(年-月-日)
	 * 
	 * @return String
	 * @author: ctfzh
	 */
	public static String getShortDate() {
		SimpleDateFormat dF = new SimpleDateFormat("yyyy-MM-dd");
		return dF.format(new Date());
	}

	/**
	 * 获取当前时间(年-月-日 时分秒)
	 * 
	 * @return String
	 * @author: wzh
	 */
	public static String getNowFullTime() {
		SimpleDateFormat dF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dF.format(new Date());
	}

	/**
	 * 获取当前时间(年-月-日)
	 * 
	 * @param pDate
	 *            Date
	 * @return String
	 * @author: ctfzh
	 */
	public static String getShortDate(Date pDate) {
		SimpleDateFormat dF = new SimpleDateFormat("yyyy-MM-dd");
		return dF.format(pDate);
	}

	/**
	 * 获取当前时间(年-月-日 时:分:秒)
	 * 
	 * @return String
	 */
	public static String getLongDate() {
		SimpleDateFormat dF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dF.format(new Date());
	}

	// 比较时间，unit是时间单位
	public static int compareTime(String time1, String time2, String unit) {
		if (time1 == null || time2 == null || time1.equals("") || time2.equals(""))
			return -1;

		try {
			java.util.Date dt1 = (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(time1);
			java.util.Date dt2 = (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(time2);

			long time1_L = dt1.getTime();
			long time2_L = dt2.getTime();
			if (time1_L == time2_L) {
				return 0;
			}
			long compareTime = time2_L - time1_L;

			if (unit.equals("dd")) {
				compareTime = compareTime / (24 * 3600 * 1000);
			} else if (unit.equals("hh")) {
				compareTime = compareTime / (3600 * 1000);
			} else if (unit.equals("mm")) {
				compareTime = compareTime / (60 * 1000);
			} else if (unit.equals("ss")) {
				compareTime = compareTime / (1000);
			} else {
				compareTime = -1;
			}

			int time = new Long(compareTime).intValue();
			return time;
		} catch (Exception e) {
			return -1;
		}
	}

	// 比较时间，unit是时间单位
	public static int compareTime(Date time1, Date time2, String unit) {
		if (time1 == null || time2 == null) {
			return -1;
		}

		try {
			long time1_L = time1.getTime();
			long time2_L = time2.getTime();
			if (time1_L == time2_L) {
				return 0;
			}
			long compareTime = time2_L - time1_L;

			if (unit.equals("dd")) {
				compareTime = compareTime / (24 * 3600 * 1000);
			} else if (unit.equals("hh")) {
				compareTime = compareTime / (3600 * 1000);
			} else if (unit.equals("mm")) {
				compareTime = compareTime / (60 * 1000);
			} else if (unit.equals("ss")) {
				compareTime = compareTime / (1000);
			} else {
				compareTime = -1;
			}

			int time = new Long(compareTime).intValue();
			return time;
		} catch (Exception e) {
			return -1;
		}

	}

	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	// 增加全角字符转半角字符和半角字符转全角字符方法
	// Convert.toHalfWidth(String Str)
	// Convert.toFullWidth(String Str)
	/**
	 * 全角转半角
	 * 
	 * @param Str
	 * @return
	 */
	public static String toHalfWidth(String Str) {
		char[] c = Str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 半角转全角
	 * 
	 * @param Str
	 * @return
	 */
	public static String toFullWidth(String Str) {
		char[] c = Str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == (char) 32) {
				c[i] = 12288;
				continue;
			}
			if (c[i] + 65248 > 65280 && c[i] + 65248 < 65375)
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}

	/**
	 * 基于正则表达式替换所有关键字 钱海兵 2011-12-03
	 * 
	 * @param operator
	 * @return
	 */
	public static String toRelationAll(String input) {
		// 使用正则表达式枚举所有关系运算符
		String replacement = "";
		if (!input.isEmpty()) {
			Pattern p = Pattern.compile("\\(.{2}\\)");
			Matcher m = p.matcher(input);
			StringBuffer sb = new StringBuffer();
			while (m.find()) {
				String operator = m.group().replaceAll("(\\()|(\\))", "");
				m.appendReplacement(sb, toRelation(operator, m.group()));
			}
			m.appendTail(sb);
			replacement = sb.toString();
		}
		return replacement;
	}

	/**
	 * 简化操作符转换 钱海兵 2011-12-02
	 * 
	 * @param operator
	 * @return
	 */
	public static String toRelation(String operator, String defaultOpt) {
		String relation = "";
		if ("LK".equalsIgnoreCase(operator)) {
			relation = "LIKE";
		} else if ("NK".equalsIgnoreCase(operator)) {
			relation = "NOT LIKE";
		} else if ("IN".equalsIgnoreCase(operator)) {
			relation = "IN";
		} else if ("NI".equalsIgnoreCase(operator)) {
			relation = "NOT IN";
		} else if ("EQ".equalsIgnoreCase(operator)) {
			relation = "=";
		} else if ("NE".equalsIgnoreCase(operator)) {
			relation = "<>";
		} else if ("GE".equalsIgnoreCase(operator)) {
			relation = ">=";
		} else if ("LE".equalsIgnoreCase(operator)) {
			relation = "<=";
		} else if ("GR".equalsIgnoreCase(operator)) {
			relation = ">";
		} else if ("LS".equalsIgnoreCase(operator)) {
			relation = "<";
		} else if ("BT".equalsIgnoreCase(operator)) {
			relation = "BETWEEN";
		} else if ("NB".equalsIgnoreCase(operator)) {
			relation = "NOT BETWEEN";
		} else if ("NL".equalsIgnoreCase(operator)) {
			relation = "IS NULL";
		} else if ("NN".equalsIgnoreCase(operator)) {
			relation = "IS NOT NULL";
		} else {
			relation = defaultOpt;
		}
		return relation;
	}

	public static String changeToBig(double value) {
		char[] hunit = { '拾', '佰', '仟' }; // 段内位置表示
		char[] vunit = { '万', '亿' }; // 段名表示
		char[] digit = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' }; // 数字表示
		long midVal = (long) (value * 100); // 转化成整形
		String valStr = String.valueOf(midVal); // 转化成字符串
		String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
		String rail = valStr.substring(valStr.length() - 2); // 取小数部分

		String prefix = ""; // 整数部分转化的结果
		String suffix = ""; // 小数部分转化的结果
		// 处理小数点后面的数
		if (rail.equals("00")) { // 如果小数部分为0
			suffix = "整";
		} else {
			suffix = digit[rail.charAt(0) - '0'] + "角" + digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
		}
		// 处理小数点前面的数
		char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
		boolean preZero = false; // 标志当前位的上一位是否为有效0位（如万位的0对千位无效）
		byte zeroSerNum = 0; // 连续出现0的次数
		for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
			int idx = (chDig.length - i - 1) % 4; // 取段内位置
			int vidx = (chDig.length - i - 1) / 4; // 取段位置
			if (chDig[i] == '0') { // 如果当前字符是0
				preZero = true;
				zeroSerNum++; // 连续0次数递增
				if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
					prefix += vunit[vidx - 1];
					preZero = false; // 不管上一位是否为0，置为无效0位
				}
			} else {
				zeroSerNum = 0; // 连续0次数清零
				if (preZero) { // 上一位为有效0位
					prefix += digit[0]; // 只有在这地方用到'零'
					preZero = false;
				}
				prefix += digit[chDig[i] - '0']; // 转化该数字表示
				if (idx > 0)
					prefix += hunit[idx - 1];
				if (idx == 0 && vidx > 0) {
					prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
				}
			}
		}

		if (prefix.length() > 0)
			prefix += '圆'; // 如果整数部分存在,则有圆的字样
		return prefix + suffix; // 返回正确表示
	}
	
	static public void dbChar(String dbChar,Map<String,Object> map) {
		if(!StringUtils.isEmpty(dbChar) && !"GBK".equalsIgnoreCase(dbChar) && !"UTF-8".equalsIgnoreCase(dbChar)){
			Set<String> keySet = map.keySet();
			for (String key : keySet) {
				Object v = map.get(key);
				if(v!=null && key.toLowerCase().contains("date")) {
					if(v instanceof java.sql.Timestamp) {
						String s = ((java.sql.Timestamp)v).toString();
						int idx = s.indexOf(".");
						if(idx>0) {
							map.put(key, s.substring(0,idx));
						}
					}
				}else {
					if(v!=null && v.toString().length()>0) {
						try {
							map.put(key, new String(v.toString().getBytes(dbChar), "GBK"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

}
