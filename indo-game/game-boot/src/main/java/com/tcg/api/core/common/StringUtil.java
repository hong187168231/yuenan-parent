package com.tcg.api.core.common;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

public class StringUtil {

	public static char CAPITAL_A = 'A';
	public static char ONE = '1';
	public static int RANGE = 'Z' - '0';
	public static int GAP = 7;

	public static char[] CLEAR_TEXT = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f', 'h', 'i', 'k', 'l', 'r', 's', 't', 'u', 'v', 'x' };
	public static char[] DIGITAL_TEXT = new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

	private static Pattern EMAIL_PATTERN = Pattern
			.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

	public static boolean isEqual(String s1, String s2) {
		if (s1 == null || s2 == null) {
			return false;
		}
		return s1.equals(s2);
	}

	public static boolean isEmpty(String input) {
		return input == null || "".equals(input);
	}

	public static boolean isTrimEmpty(String input) {
		return input == null || "".equals(input.trim());
	}

	public static boolean validate(String input, int min, int max) {
		if (isEmpty(input)) {
			return false;
		}
		return input.length() >= min && input.length() <= max;
	}

	public static boolean validate(String input, int max) {
		return validate(input, 0, max);
	}

	public static String randomString(int length) {
		Random r = new Random(System.currentTimeMillis());
		String token = Long.toString(Math.abs(r.nextLong()), 36);
		return token.substring(0, length);
	}

	public static String randomText(int length) {
		Random r = new Random(System.currentTimeMillis());
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int index = Math.abs(r.nextInt()) % RANGE;
			if (index > 9 && index < 17) {
				index += GAP;
			}
			buffer.append((char) (index + ONE));
		}
		return buffer.toString();
	}

	public static String clearText(int length) {
		Random r = new Random(System.currentTimeMillis());
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buffer.append(CLEAR_TEXT[Math.abs(r.nextInt()) % CLEAR_TEXT.length]);
		}
		return buffer.toString();
	}

	public static String digitalText(int length) {
		Random r = new Random(System.currentTimeMillis());
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buffer.append(DIGITAL_TEXT[Math.abs(r.nextInt()) % DIGITAL_TEXT.length]);
		}
		return buffer.toString();
	}

	public static String randomCRCText(int length) {
		String randomText = randomText(length - 1);
		CRC32 crc = new CRC32();
		crc.update(randomText.getBytes());
		// return randomText;
		return randomText + (char) (crc.getValue() % 26 + CAPITAL_A);
	}

	public static boolean allEmpty(String... values) {
		if (values == null) {
			return true;
		}
		for (String v : values) {
			if (!isEmpty(v)) {
				return false;
			}
		}
		return true;
	}

	public static String concat(String delimiter, String... args) {
		if (args == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		for (String s : args) {
			if (s == null) {
				continue;
			}
			sb.append(s);
			if (delimiter != null) {
				sb.append(delimiter);
			}
		}
		return sb.toString();
	}

	public static String maskSensitive(String str) {
		if (StringUtil.isEmpty(str)) {
			return str;
		}
		return str.charAt(0) + "**********";
	}

	/*
	 * If parameter string lenght is greater than 4, the string will be masked.
	 * Otherwise, will return original String. This function will return string by
	 * replacing middle four characters to four '*'.
	 * 
	 */
	public static String maskSensitive2(String str) {
		if (StringUtil.isEmpty(str)) {
			return str;
		}
		if (str.length() > 4) {
			int quotStr = str.length() / 2;
			String beginStr = str.substring(0, quotStr - 2);
			String endStr = str.substring(quotStr + 2);
			return beginStr + "****" + endStr;
		}
		return str;
	}

	/**
	 * If parameter string length is greater than 4, the string will be masked.
	 * Otherwise, will return original String. This function will return string by
	 * keeping the first 2 and last 2 characters, the rest of the chars will be
	 * masked with four '*'
	 * 
	 * @param str
	 * @return
	 */
	public static String maskSensitive3(String str) {
		if (StringUtil.isEmpty(str)) {
			return str;
		}
		if (str.length() > 4) {
			String beginStr = str.substring(0, 2);
			String endStr = str.substring(str.length() - 2);
			return beginStr + "****" + endStr;
		}
		return str;
	}

	/**
	 * Show only the last 4 characters. The rest of the characters will be mask with
	 * ****.
	 * 
	 * @param str
	 * @return
	 */
	public static String maskSensitive4(String str) {
		if (StringUtil.isEmpty(str)) {
			return str;
		}
		return "***************" + str.substring(str.length() - 4);
	}

	public static String showLast(String str, int lastToShow, int stars) {
		if (str == null) {
			return null;
		}
		if (lastToShow < 0 || stars < 0) {
			throw new IllegalArgumentException("lastToShow < 0 || stars < 0");
		}
		String lastX = null;
		if (str.length() <= lastToShow) {
			lastX = str;
		} else {
			lastX = str.substring(str.length() - lastToShow);
		}
		return new StringBuilder().append(getMask(stars)).append(lastX).toString();
	}

	public static String showFirst(String str, int firstToShow, int stars) {
		if (str == null) {
			return null;
		}
		if (firstToShow < 0 || stars < 0) {
			throw new IllegalArgumentException("lastToShow < 0 || stars < 0");
		}
		String firstX = str.substring(0, firstToShow);
		return new StringBuilder().append(firstX).append(getMask(stars)).toString();
	}

	public static String maskLast(String str, int stars) {
		if (str == null) {
			return null;
		}
		if (stars < 0) {
			throw new IllegalArgumentException("stars < 0");
		}
		if (str.length() <= stars) {
			return getMask(stars);
		}
		return showFirst(str, str.length() - stars, stars);
	}

	private static String getMask(int stars) {
		if (stars <= 0) {
			return "";
		}
		char[] c = new char[stars];
		for (int i = 0; i < c.length; i++) {
			c[i] = '*';
		}
		return new String(c);
	}

	public static String maskEmail(String email, int firstToShow, int stars) {
		if (email == null) {
			return null;
		}
		String[] t = email.split("@");
		StringBuilder r = new StringBuilder(StringUtil.showFirst(t[0], firstToShow, stars));
		if (t.length > 1) {
			r.append("@").append(t[1]);
		}
		return r.toString();
	}

	public static String maskEmail2(String email) {
		String[] memEmail = email.split("@");
		String masked;
		if (memEmail[0].length() <= 3) {
			masked = memEmail[0].substring(0, 1);
		} else {
			masked = memEmail[0].substring(0, 1) + "*" + memEmail[0].substring(memEmail[0].length() - 2);
		}
		if (memEmail.length >= 2) {
			return masked + "@" + memEmail[memEmail.length - 1];
		}
		return masked;
	}

	public static boolean checkEmail(String email) {
		if (isEmpty(email)) {
			return false;
		}
		Matcher matcher = EMAIL_PATTERN.matcher(email);
		return matcher.matches();
	}

	public static String mask(String input, int showLength) {
		if (isEmpty(input) || input.length() < showLength) {
			return input;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < input.length() - showLength; i++) {
			buffer.append("*");
		}
		buffer.append(input.substring(input.length() - showLength));
		return buffer.toString();
	}

	public static String nullOrNonEmpty(String input) {
		if (isEmpty(input) || "undefined".equals(input)) {
			return null;
		}
		return input;
	}

	public static String replaceNullToSpace(Object value) {
		if (value == null) {
			return " ";
		}
		return value.toString();
	}

	public static String[] stringToArray(String str, String delim) {
		if (str == null || delim == null) {
			return null;
		}
		String[] result = str.split(delim);
		for (int i = 0; i < result.length; i++) {
			result[i] = result[i].trim();
		}
		return result;
	}

}
