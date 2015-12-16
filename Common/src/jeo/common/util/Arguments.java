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
package jeo.common.util;

import java.util.Collection;

public class Arguments
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private Arguments()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// COMMON
	////////////////////////////////////////////////////////////////////////////

	public static String expectedButFound(final Object expected, final Object found)
	{
		return "('" + expected + "' expected but '" + found + "' found)";
	}

	public static String atLeastExpectedButFound(final Object expected, final Object found)
	{
		return "(at least '" + expected + "' expected but '" + found + "' found)";
	}

	public static String atMostExpectedButFound(final Object expected, final Object found)
	{
		return "(at most '" + expected + "' expected but '" + found + "' found)";
	}

	public static String isNotEqualTo(final Object a, final Object b)
	{
		return "('" + a + "' is not equal to '" + b + "')";
	}


	////////////////////////////////////////////////////////////////////////////
	// CHECK OBJECT
	////////////////////////////////////////////////////////////////////////////

	public static <T> T requireNonNull(final T object, final String string)
	{
		if (object == null)
		{
			throw new NullPointerException(string);
		}
		return object;
	}

	public static <T> T requireNonNull(final T object)
	{
		return Arguments.<T>requireNonNull(object, "Specified object is null");
	}

	public static <T> void requireEquals(final T a, final T b)
	{
		if (requireNonNull(a) != requireNonNull(b))
		{
			throw new IllegalArgumentException(a + " is not equal to " + b);
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// CHECK STRING
	////////////////////////////////////////////////////////////////////////////

	public static String requireNonEmpty(final String string)
	{
		if (Strings.isEmpty(requireNonNull(string)))
		{
			throw new IllegalArgumentException("Specified string is empty");
		}
		return string;
	}


	////////////////////////////////////////////////////////////////////////////
	// CHECK ARRAY
	////////////////////////////////////////////////////////////////////////////

	public static <T> T[] requireNonEmpty(final T[] array)
	{
		if (requireNonNull(array).length == 0)
		{
			throw new IllegalArgumentException("Specified array is empty");
		}
		return array;
	}

	public static <T> T[] requireMinimumSize(final T[] array, final int minimumSize)
	{
		if (requireNonNull(array).length < minimumSize)
		{
			throw new IllegalArgumentException("Specified array has a size " + array.length + " inferior to " + minimumSize);
		}
		return array;
	}

	public static <T> void requireSameSize(final T[] a, final T[] b)
	{
		requireSameSize(requireNonNull(a).length, requireNonNull(b).length);
	}

	public static void requireSameSize(final int a, final int b)
	{
		if (a != b)
		{
			throw new IllegalArgumentException("Specified arrays do not have the same size " + isNotEqualTo(a, b));
		}
	}

	public static <T> T[] requireMaximumSize(final T[] array, final int maximumSize)
	{
		if (requireNonNull(array).length > maximumSize)
		{
			throw new IllegalArgumentException("Specified array has a size " + array.length + " superior to " + maximumSize);
		}
		return array;
	}


	////////////////////////////////////////////////////////////////////////////
	// CHECK COLLECTION
	////////////////////////////////////////////////////////////////////////////

	public static <T extends Collection<?>> T requireNonEmpty(final T collection)
	{
		if (requireNonNull(collection).isEmpty())
		{
			throw new IllegalArgumentException("Specified collection is empty");
		}
		return collection;
	}

	public static <T extends Collection<?>> T requireMinimumSize(final T collection, final int minimumSize)
	{
		if (requireNonNull(collection).size() < minimumSize)
		{
			throw new IllegalArgumentException("Specified collection has a size " + collection.size() + " inferior to " + minimumSize);
		}
		return collection;
	}

	public static <T extends Collection<?>> void requireSameSize(final T a, final T b)
	{
		if (requireNonNull(a).size() != requireNonNull(b).size())
		{
			throw new IllegalArgumentException("Specified collections do not have the same size " + isNotEqualTo(a.size(), b.size()));
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// CHECK NUMBER
	////////////////////////////////////////////////////////////////////////////

	public static <T extends Number> T requireGreaterThan(final T number, final Number lowerBound)
	{
		if (requireNonNull(number).doubleValue() <= lowerBound.doubleValue())
		{
			throw new IllegalArgumentException("Specified number is lower or equal to " + lowerBound);
		}
		return number;
	}

	public static <T extends Number> T requireGreaterOrEqualTo(final T number, final Number lowerBound)
	{
		if (requireNonNull(number).doubleValue() < lowerBound.doubleValue())
		{
			throw new IllegalArgumentException("Specified number is lower than " + lowerBound);
		}
		return number;
	}

	public static <T extends Number> T requireLessThan(final T number, final Number upperBound)
	{
		if (number.doubleValue() >= upperBound.doubleValue())
		{
			throw new IllegalArgumentException("Specified number " + number + " is greater or equal to " + upperBound);
		}
		return number;
	}

	public static <T extends Number> T requireLessOrEqualTo(final T number, final Number lowerBound)
	{
		if (number.doubleValue() > lowerBound.doubleValue())
		{
			throw new IllegalArgumentException("Specified number is greater than " + lowerBound);
		}
		return number;
	}

	public static <T extends Number> T requireNegative(final T number)
	{
		if (requireNonNull(number).doubleValue() >= 0.)
		{
			throw new IllegalArgumentException("Specified number is zero or positive");
		}
		return number;
	}

	public static <T extends Number> T requireNonNegative(final T number)
	{
		if (requireNonNull(number).doubleValue() < 0.)
		{
			throw new IllegalArgumentException("Specified number is negative");
		}
		return number;
	}

	public static <T extends Number> T requireNonZero(final T number)
	{
		if (requireNonNull(number).doubleValue() == 0.)
		{
			throw new IllegalArgumentException("Specified number is zero");
		}
		return number;
	}

	public static <T extends Number> T requirePositive(final T number)
	{
		if (requireNonNull(number).doubleValue() <= 0.)
		{
			throw new IllegalArgumentException("Specified number is zero or negative");
		}
		return number;
	}

	public static <T extends Number> T requireNonPositive(final T number)
	{
		if (requireNonNull(number).doubleValue() > 0.)
		{
			throw new IllegalArgumentException("Specified number is positive");
		}
		return number;
	}


	////////////////////////////////////////////////////////////////////////////
	// COMPARABLE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns {@code true} if the arguments are equal to each other, or
	 * {@code false} otherwise.
	 * <p>
	 * @param a an object
	 * @param b an object to be compared with {@code a} for equality
	 * <p>
	 * @return {@code true} if the arguments are equal to each other, or
	 *         {@code false} otherwise
	 */
	public static boolean equals(final Object a, final Object b)
	{
		return (a == b) || ((a != null) && a.equals(b));
	}
}
