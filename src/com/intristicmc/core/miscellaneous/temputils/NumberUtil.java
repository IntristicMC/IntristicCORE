package com.intristicmc.core.miscellaneous.temputils;

import java.util.Random;

public class NumberUtil {
	public static final double PI = Math.PI;
	public static final double PI2 = PI * 2;
	public static final Random RANDOM = new Random();

	public static int parseInt(String in) {
		try {
			return Integer.parseInt(in);
		} catch (Exception localException) {
			return 0;
		}
	}

	public static double parseDouble(String in) {
		try {
			return Double.parseDouble(in);
		} catch (Exception localException) {
			return 0.0D;
		}
	}

	public static float parseFloat(String in) {
		try {
			return Float.parseFloat(in);
		} catch (Exception localException) {
			return 0.0F;
		}
	}

	public static int r(int max, boolean plus) {
		return (plus ? RANDOM.nextInt(max) + 1 : RANDOM.nextInt(max));
	}

	public static int getInt(char in) {
		return parseInt(StringUtil.build(in));
	}

	public static int getInt(String in) {
		return parseInt(in);
	}

	public static double getDouble(char in) {
		return parseDouble(StringUtil.build(in));
	}

	public static double getDouble(String in) {
		return parseDouble(in);
	}

	public static float getFloat(String in) {
		return parseFloat(in);
	}

	public static boolean isNegative(int paramInt) {
		return (toString(paramInt).contains("-")) || (paramInt < 0);
	}

	public static boolean isNegative(double paramDouble) {
		return (toString(paramDouble).contains("-")) || (paramDouble < 0.0D);
	}

	public static boolean isNumber(String paramString) {
		try {
			Integer.parseInt(paramString);
			return true;
		} catch (Exception localException) {
			return false;
		}
	}

	public static String toString(double paramDouble) {
		return Double.toString(paramDouble);
	}

	public static String toString(long paramLong) {
		return Long.toString(paramLong);
	}

	public static String toString(byte paramByte) {
		return Byte.toString(paramByte);
	}

	public static String toString(int paramInt) {
		return Integer.toString(paramInt);
	}

	public static double asDouble(int paramInt) {
		return paramInt;
	}

	public static double asDouble(float paramFloat) {
		return paramFloat;
	}

	public static float asFloat(double paramDouble) {
		return (float) paramDouble;
	}

	public static float asFloat(int paramInt) {
		return paramInt;
	}

	public static int asInt(double paramDouble) {
		return (int) paramDouble;
	}

	public static int asInt(float paramFloat) {
		return (int) paramFloat;
	}

	public static byte asByte(int paramInt) {
		return (byte) paramInt;
	}

	public static byte asByte(float paramFloat) {
		return (byte) (int) paramFloat;
	}

	public static byte asByte(double paramDouble) {
		return (byte) (int) paramDouble;
	}

	public static int max(int comparator1, int comparator2) {
		if ((comparator1 == 0) && (comparator2 == 0) && (Double.doubleToLongBits(comparator1) == Double.doubleToLongBits(-0.0D))) return comparator2;
		return comparator1 >= comparator2 ? comparator1 : comparator2;
	}

	public static int min(int comparator1, int comparator2) {
		if ((comparator1 == 0) && (comparator2 == 0) && (Double.doubleToLongBits(comparator2) == Double.doubleToLongBits(-0.0D))) return comparator2;
		return comparator1 <= comparator2 ? comparator1 : comparator2;
	}

	public static double max(double comparator1, double comparator2) {
		if (comparator1 != comparator1) return comparator1;
		if ((comparator1 == 0.0D) && (comparator2 == 0.0D) && (Double.doubleToLongBits(comparator1) == Double.doubleToLongBits(-0.0D))) return comparator2;
		return comparator1 >= comparator2 ? comparator1 : comparator2;
	}

	public static double min(double comparator1, double comparator2) {
		if (comparator1 != comparator1) return comparator1;
		if ((comparator1 == 0.0D) && (comparator2 == 0.0D) && (Double.doubleToLongBits(comparator2) == Double.doubleToLongBits(-0.0D))) return comparator2;
		return comparator1 <= comparator2 ? comparator1 : comparator2;
	}

	public static double atan(double x) {
		double lo;
		double hi;
		boolean negative = x < 0;
		if (negative) x = -x;
		if (x >= 0x4410000000000000L) return negative ? -PI / 2 : PI / 2;
		if (!(x >= 0.4375)) {
			if (!(x >= 1 / 0x41c0000000000000L)) return (negative ? -x : x);
			lo = hi = 0;
		} else if (x < 1.1875) {
			if (x < 0.6875) {
				x = (2 * x - 1) / (2 + x);
				hi = 0x3fddac670561bb4fL;
				lo = 0x3c7a2b7f222f65e2L;
			} else {
				x = (x - 1) / (x + 1);
				hi = PI / 4;
				lo = 0x3ca1a62633145c07L / 4;
			}
		} else if (x < 2.4375) {
			x = (x - 1.5) / (1 + 1.5 * x);
			hi = 0x3fef730bd281f69bL;
			lo = 0x3c7007887af0cbbdL;
		} else {
			x = -1 / x;
			hi = PI / 2;
			lo = 0x3ca1a62633145c07L / 2;
		}

		double z = x * x;
		double w = z * z;
		double s1 = z * (0x3fd555555555550dL + w * (0x3fc24924920083ffL + w * (0x3fb745cdc54c206eL + w * (0x3fb10d66a0d03d51L + w * (0x3fa97b4b24760debL + w * 0x3f90ad3ae322da11L)))));
		double s2 = w * (0xbfc999999998ebc4L + w * (0xbfbc71c6fe231671L + w * (0xbfb3b0f2af749a6dL + w * (0xbfadde2d52defd9aL + w * 0xbfa2b4442c6a6c2fL))));
		if (hi == 0) return negative ? x * (s1 + s2) - x : x - x * (s1 + s2);
		z = hi - ((x * (s1 + s2) - lo) - x);
		return negative ? -z : z;
	}

	public static double squareRoot(double x) {
		if (x < 0) return Double.NaN;
		if (x == 0 || !(x < Double.POSITIVE_INFINITY)) return x;

		long bits = Double.doubleToLongBits(x);
		int exp = (int) (bits >> 52);
		if (exp == 0) {
			x *= 0x40000000000000L;
			bits = Double.doubleToLongBits(x);
			exp = (int) (bits >> 52) - 54;
		}
		exp -= 1023;
		bits = (bits & 0x000fffffffffffffL) | 0x0010000000000000L;
		if ((exp & 1) == 1) bits <<= 1;
		exp >>= 1;

		bits <<= 1;
		long q = 0;
		long s = 0;
		long r = 0x0020000000000000L;
		while (r != 0) {
			long t = s + r;
			if (t <= bits) {
				s = t + r;
				bits -= t;
				q += r;
			}
			bits <<= 1;
			r >>= 1;
		}

		if (bits != 0) q += q & 1;
		return Double.longBitsToDouble((q >> 1) + ((exp + 1022L) << 52));
	}
}