/*
 * The MIT License
 *
 * Copyright 2013-2015 Florian Barras.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jeo.common.math;

import java.math.BigDecimal;

public class Numbers
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private Numbers()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// COMPARABLE NUMBER
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Compares the specified numbers for order. Returns a negative integer,
	 * zero, or a positive integer as {@code a} is less than, equal to, or
	 * greater than {@code b}.
	 * <p>
	 * @param a a {@link Number}
	 * @param b another {@link Number} to be compared with {@code a} for order
	 * <p>
	 * @return a negative integer, zero, or a positive integer as {@code a} is
	 *         less than, equal to, or greater than {@code b}
	 * <p>
	 * @throws NullPointerException if a specified number is null
	 */
	public static int compareTo(final Number a, final Number b)
	{
		if ((a instanceof BigDecimal) || (b instanceof BigDecimal))
		{
			return convertToBigDecimal(a).compareTo(convertToBigDecimal(b));
		}
		else
		{
			return Double.valueOf(a.doubleValue()).compareTo(b.doubleValue());
		}
	}

	/**
	 * Converts a {@link Number} to a {@link BigDecimal}.
	 * <p>
	 * @param number a {@link Number}
	 * <p>
	 * @return a {@link BigDecimal}
	 */
	public static BigDecimal convertToBigDecimal(final Number number)
	{
		if (number instanceof BigDecimal)
		{
			return (BigDecimal) number;
		}
		else
		{
			return new BigDecimal(String.valueOf(number));
		}
	}

	/**
	 *
	 * @param a a {@link Number}
	 * @param b another {@link Number} to be compared with {@code a} for
	 *          equality
	 * <p>
	 * @return {@code true} if {@code a} is equal to {@code b}, {@code false}
	 *         otherwise
	 * <p>
	 * @throws NullPointerException if a specified number is null
	 */
	public static boolean equals(final Number a, final Number b)
	{
		return compareTo(a, b) == 0;
	}

	/**
	 *
	 * @param a a {@link Number}
	 * @param b another {@link Number} to be compared with {@code a}
	 * <p>
	 * @return {@code true} if {@code a} is less than {@code b}, {@code false}
	 *         otherwise
	 * <p>
	 * @throws NullPointerException if a specified number is null
	 */
	public static boolean isLessThan(final Number a, final Number b)
	{
		return compareTo(a, b) < 0;
	}

	/**
	 *
	 * @param a a {@link Number}
	 * @param b another {@link Number} to be compared with {@code a}
	 * <p>
	 * @return {@code true} if {@code a} is less or equal to {@code b},
	 *         {@code false} otherwise
	 * <p>
	 * @throws NullPointerException if a specified number is null
	 */
	public static boolean isLessOrEqualTo(final Number a, final Number b)
	{
		return compareTo(a, b) <= 0;
	}

	/**
	 *
	 * @param a a {@link Number}
	 * @param b another {@link Number} to be compared with {@code a}
	 * <p>
	 * @return {@code true} if {@code a} is greater than {@code b},
	 *         {@code false} otherwise
	 * <p>
	 * @throws NullPointerException if a specified number is null
	 */
	public static boolean isGreaterThan(final Number a, final Number b)
	{
		return compareTo(a, b) > 0;
	}

	/**
	 *
	 * @param a a {@link Number}
	 * @param b another {@link Number} to be compared with {@code a}
	 * <p>
	 * @return {@code true} if {@code a} is greater or equal to {@code b},
	 *         {@code false} otherwise
	 * <p>
	 * @throws NullPointerException if a specified number is null
	 */
	public static boolean isGreaterOrEqualTo(final Number a, final Number b)
	{
		return compareTo(a, b) >= 0;
	}

	/**
	 * Returns the smaller of {@code a} and {@code b}. If they have the same
	 * value, the result is {@code a}.
	 * <p>
	 * @param <T> the type of the numbers to be compared
	 * @param a   a {@link Number}
	 * @param b   another {@link Number} to be compared with {@code a}
	 * <p>
	 * @return the smaller of {@code a} and {@code b}
	 * <p>
	 * @throws NullPointerException if a specified number is null
	 */
	public static <T extends Number> T getMin(final T a, final T b)
	{
		return compareTo(a, b) <= 0 ? a : b;
	}

	/**
	 * Returns the larger of {@code a} and {@code b}. If they have the same
	 * value, the result is {@code a}.
	 * <p>
	 * @param <T> the type of the numbers to be compared
	 * @param a   a {@link Number}
	 * @param b   another {@link Number} to be compared with {@code a}
	 * <p>
	 * @return the larger of {@code a} and {@code b}
	 * <p>
	 * @throws NullPointerException if a specified number is null
	 */
	public static <T extends Number> T getMax(final T a, final T b)
	{
		return compareTo(a, b) >= 0 ? a : b;
	}
}
