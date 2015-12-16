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

import java.math.BigInteger;
import java.util.Collection;

public class Maths
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTANT(S)
	////////////////////////////////////////////////////////////////////////////

	public static final double DEFAULT_EPSILON = 1E-16;
	public static final double DEFAULT_Z = 1.959964; // 95%
	public static final double SQUARE_ROOT_OF_TWO = Math.sqrt(2.);
	public static final double SQUARE_ROOT_OF_PI = Math.sqrt(Math.PI);
	public static final double DEGREE_TO_RADIAN = Math.PI / 180.;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private Maths()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// BASIC FUNCTIONS
	////////////////////////////////////////////////////////////////////////////

	public static double getDecimalPart(final double number)
	{
		return number - Math.floor(number);
	}

	public static int floorToInt(final double number)
	{
		return (int) Math.floor(number);
	}

	public static long floorToLong(final double number)
	{
		return (long) Math.floor(number);
	}

	public static int ceilToInt(final double number)
	{
		return (int) Math.ceil(number);
	}

	public static long ceilToLong(final double number)
	{
		return (long) Math.ceil(number);
	}

	public static int roundToInt(final double number)
	{
		return (int) Math.round(number);
	}

	public static long roundToLong(final double number)
	{
		return Math.round(number);
	}

	public static double gamma(final double z)
	{
		final double tmp1 = Math.sqrt((2 * Math.PI) / z);
		double tmp2 = z + (1. / ((12 * z) - (1. / (10 * z))));
		tmp2 = Math.pow(tmp2 / Math.E, z);
		return tmp1 * tmp2;
	}

	public static double factorial(final double n)
	{
		return roundToLong(n * gamma(n));
	}

	public static BigInteger factorial(final long n)
	{
		if (n == 0)
		{
			return BigInteger.ONE;
		}
		return BigInteger.valueOf(n).multiply(factorial(n - 1));
	}

	public static long getGCD(final long a, final long b)
	{
		long i = a, j = b, t;
		while (j != 0)
		{
			t = j;
			j = i % t;
			i = t;
		}
		return i;
	}

	public static long getLCM(final long a, final long b)
	{
		return (a * b) / getGCD(a, b);
	}


	////////////////////////////////////////////////////////////////////////////
	// ALGORITHMIC
	////////////////////////////////////////////////////////////////////////////

	public static long countPoints(final double min, final double max, final double increment)
	{
		return min <= max ? 1L + floorToLong((max - min) / increment) : 0L;
	}


	////////////////////////////////////////////////////////////////////////////
	// GEOMETRY
	////////////////////////////////////////////////////////////////////////////

	public static double cos(final double angle)
	{
		return Math.cos(angle * DEGREE_TO_RADIAN);
	}

	public static double sin(final double angle)
	{
		return Math.sin(angle * DEGREE_TO_RADIAN);
	}

	public static double haversin(final double angle)
	{
		return (1. - cos(angle)) / 2.;
	}


	////////////////////////////////////////////////////////////////////////////
	// SUM OF NUMBERS
	////////////////////////////////////////////////////////////////////////////

	public static <T extends Number> Double sum(final T[] array)
	{
		Double sum = 0.;
		for (final T element : array)
		{
			if (element != null)
			{
				sum += element.doubleValue();
			}
		}
		return sum;
	}

	public static <T extends Number> Double sumWithoutNaN(final T[] array)
	{
		Double sum = 0.;
		Double elementValue;
		for (final T element : array)
		{
			if (element != null)
			{
				elementValue = element.doubleValue();
				if (!elementValue.isNaN())
				{
					sum += elementValue;
				}
			}
		}
		return sum;
	}

	public static <T extends Number> Double sum(final Collection<T> collection)
	{
		Double sum = 0.;
		for (final T element : collection)
		{
			if (element != null)
			{
				sum += element.doubleValue();
			}
		}
		return sum;
	}

	public static <T extends Number> Double sumWithoutNaN(final Collection<T> collection)
	{
		Double sum = 0.;
		Double elementValue;
		for (final T element : collection)
		{
			if (element != null)
			{
				elementValue = element.doubleValue();
				if (!elementValue.isNaN())
				{
					sum += elementValue;
				}
			}
		}
		return sum;
	}

	public static <T extends Number> Double sumOfSquares(final T[] array, final double mean)
	{
		Double sum = 0.;
		for (final T element : array)
		{
			if (element != null)
			{
				sum += Math.pow(element.doubleValue() - mean, 2);
			}
		}
		return sum;
	}

	public static <T extends Number> Double sumOfSquaresWithoutNaN(final T[] array, final double mean)
	{
		Double sum = 0.;
		Double elementValue;
		for (final T element : array)
		{
			if (element != null)
			{
				elementValue = element.doubleValue();
				if (!elementValue.isNaN())
				{
					sum += Math.pow(elementValue - mean, 2);
				}
			}
		}
		return sum;
	}

	public static <T extends Number> Double sumOfSquares(final Collection<T> collection, final double mean)
	{
		Double sum = 0.;
		for (final T element : collection)
		{
			if (element != null)
			{
				sum += Math.pow(element.doubleValue() - mean, 2);
			}
		}
		return sum;
	}

	public static <T extends Number> Double sumOfSquaresWithoutNaN(final Collection<T> collection, final double mean)
	{
		Double sum = 0.;
		Double elementValue;
		for (final T element : collection)
		{
			if (element != null)
			{
				elementValue = element.doubleValue();
				if (!elementValue.isNaN())
				{
					sum += Math.pow(elementValue - mean, 2);
				}
			}
		}
		return sum;
	}


	////////////////////////////////////////////////////////////////////////////
	// GEOMETRY
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the approximate edge length of the inscribed square of the circle
	 * with the specified radius.
	 * <p>
	 * @param radius the radius of the circle
	 * <p>
	 * @return the approximate edge length of the inscribed square of the circle
	 *         with the specified radius
	 */
	public static double getInscribedSquare(final double radius)
	{
		return SQUARE_ROOT_OF_TWO * radius;
	}

	/**
	 * Returns the approximate edge length of the square that has the same area
	 * of the circle with the specified radius.
	 * <p>
	 * @param radius the radius of the circle
	 * <p>
	 * @return the approximate edge length of the square that has the same area
	 *         of the circle with the specified radius.
	 */
	public static double squareCircle(final double radius)
	{
		return SQUARE_ROOT_OF_PI * radius;
	}

	/**
	 * Returns the approximate edge length of the circumscribed square of the
	 * circle with the specified radius.
	 * <p>
	 * @param radius the radius of the circle
	 * <p>
	 * @return the approximate edge length of the circumscribed square of the
	 *         circle with the specified radius
	 */
	public static double getCircumscribedSquare(final double radius)
	{
		return 2. * radius;
	}
}
