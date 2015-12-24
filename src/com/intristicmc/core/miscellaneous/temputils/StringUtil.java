package com.intristicmc.core.miscellaneous.temputils;

import java.util.List;
import java.util.regex.Pattern;

public class StringUtil {
	public static String join(String... messageParts) {
		String message = "";

		for (String string : messageParts)
			message += string;

		return message;
	}

	public static String join(List<String> list, String seperator) {
		if (list == null || list.isEmpty()) return "";

		String first = list.get(0);
		StringBuilder builder = new StringBuilder();

		builder.append(first);
		list.remove(0);

		for (String string : list) {
			builder.append(seperator).append(string);
		}

		return builder.toString();
	}

	public static String fill(char c, int amount) {
		String message = "";

		for (int i = 0; i <= amount; i++)
			message += build(c);

		return message;
	}

	public static boolean canBeChar(String in) {
		return in.length() == 1;
	}

	public static boolean equals(String compare, String compare2) {
		return compare.equalsIgnoreCase(compare2);
	}

	public static char toChar(String in) {
		if (!canBeChar(in)) return '?';
		else return in.toCharArray()[0];
	}

	public static String build(char[] c) {
		return new String(c);
	}

	public static String build(byte[] b) {
		return new String(b);
	}

	public static String build(byte[] b, String charSet) {
		try {
			return new String(b, charSet);
		} catch (Exception localException) {
		}

		return new String(b);
	}

	public static String trim(String string) {
		return trim(string, false);
	}

	public static String trim(String string, boolean doubles) {
		if (doubles) {
			while (string.contains("  "))
				string = string.replace("  ", " ");
		}

		return string.trim();
	}

	public static String lowercase(String string) {
		return string.toLowerCase();
	}

	public static String uppercase(String string, boolean[] params) {
		if ((params.length >= 1 && params[0])) return string.toUpperCase();
		else if (params.length >= 2 && !params[0]) {
			if (params.length >= 3) {
				if (params[2]) {
					StringBuilder builder = new StringBuilder();

					for (String word : string.split(" ")) {
						char[] wordChars = word.toCharArray();
						wordChars[0] = Character.toUpperCase(wordChars[0]);

						builder.append(build(wordChars)).append(" ");
					}

					return builder.toString().trim();
				} else {
					char[] stringChars = string.toCharArray();
					stringChars[0] = Character.toUpperCase(stringChars[0]);

					return build(stringChars);
				}
			}
			if (params[1]) {
				char[] stringChars = string.toCharArray();
				stringChars[0] = Character.toUpperCase(stringChars[0]);

				return build(stringChars);
			} else return string;
		} else if (params == null || params.length <= 0) {
			return string;
		} else {
			return string;
		}
	}

	public static boolean matches(String string, String regex) {
		if (string == null || regex == null) return false;
		Pattern compiledPattern = Pattern.compile(regex);
		return compiledPattern.matcher(string).matches();
	}

	public static String build(char c) {
		return build(Character.toString(c).toCharArray());
	}
}