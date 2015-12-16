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

public class WholeNumbers
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private WholeNumbers()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// STATIC
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the middle of the argument rounded to the lower {@code long}
	 * value.
	 * <p>
	 * @param number a {@code long} value
	 * <p>
	 * @return the middle of the argument rounded to the lower {@code long}
	 *         value
	 */
	public static long getMiddle(final long number)
	{
		return getMiddle(0, number);
	}

	/**
	 * Returns the middle of the argument rounded to the lower {@code int}
	 * value.
	 * <p>
	 * @param number a {@code int} value
	 * <p>
	 * @return the middle of the argument rounded to the lower {@code int} value
	 */
	public static int getMiddle(final int number)
	{
		return getMiddle(0, number);
	}

	/**
	 * Returns the middle of the arguments rounded to the lower {@code long}
	 * value.
	 * <p>
	 * @param lowerBound a lower bound
	 * @param upperBound an upper bound
	 * <p>
	 * @return the middle of the arguments rounded to the lower {@code long}
	 *         value
	 */
	public static long getMiddle(final long lowerBound, final long upperBound)
	{
		return lowerBound + (long) ((upperBound - lowerBound) / 2.);
	}

	/**
	 * Returns the middle of the arguments rounded to the lower {@code int}
	 * value.
	 * <p>
	 * @param lowerBound a lower bound
	 * @param upperBound an upper bound
	 * <p>
	 * @return the middle of the arguments rounded to the lower {@code int}
	 *         value
	 */
	public static int getMiddle(final int lowerBound, final int upperBound)
	{
		return lowerBound + (int) ((upperBound - lowerBound) / 2.);
	}

	/**
	 * Returns the greatest common divisor (GCD) of {@code a} and {@code b}.
	 * <p>
	 * @param a the first {@code long} number
	 * @param b the second {@code long} number
	 * <p>
	 * @return the greatest common divisor (GCD) of {@code a} and {@code b}
	 * <p>
	 * @throws InterruptedException
	 */
	public static long getGCD(final long a, final long b)
		throws InterruptedException
	{
		long va = a, vb = b;
		long temp;
		while ((va != 0) && (vb != 0))
		{
			temp = vb;
			vb = va % vb;
			va = temp;
		}
		return va != 0 ? va : vb;
	}

	/**
	 * Returns the the least common multiple (LCM) of {@code a} and {@code b}.
	 * <p>
	 * @param a the first {@code long} number
	 * @param b the second {@code long} number
	 * <p>
	 * @return the least common multiple (LCM) of {@code a} and {@code b}
	 * <p>
	 * @throws InterruptedException
	 */
	public static long getLCM(final long a, final long b)
		throws InterruptedException
	{
		final long gcd = getGCD(a, b);
		return gcd != 0 ? Math.abs(a * b) / gcd : 0;
	}
}
