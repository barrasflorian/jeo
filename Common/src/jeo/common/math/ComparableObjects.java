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

import java.util.Comparator;

public class ComparableObjects
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private ComparableObjects()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// COMPARABLE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns {@code 0} if the arguments are identical, or
	 * {@code c.compare(a, b)} otherwise.
	 * <p>
	 * @param <T> the type of the objects to be compared
	 * @param a   an {@link Object}
	 * @param b   another {@link Object} to be compared with {@code a} for order
	 * @param c   the {@link Comparator} to compare the first two arguments
	 * <p>
	 * @return {@code 0} if the arguments are identical, or
	 *         {@code c.compare(a, b)} otherwise
	 * <p>
	 * @throws NullPointerException if {@code a} or {@code b} is {@code null}
	 */
	public static <T> int compare(final T a, final T b, final Comparator<? super T> c)
	{
		return a == b ? 0 : c.compare(a, b);
	}

	/**
	 * Returns {@code true} if the arguments are equal to each other, or
	 * {@code false} otherwise.
	 * <p>
	 * @param <T> the type of the objects to be compared
	 * @param a   an {@link Object}
	 * @param b   another {@link Object} to be compared with {@code a} for
	 *            equality
	 * <p>
	 * @return {@code true} if the arguments are equal to each other, or
	 *         {@code false} otherwise
	 */
	public static <T extends Comparable<T>> boolean equals(final T a, final T b)
	{
		return (a == b) || ((a != null) && (a.compareTo(b) == 0));
	}

	/**
	 *
	 * @param <T> the type of the objects to be compared
	 * @param a   an {@link Object}
	 * @param b   another {@link Object} to be compared with {@code a}
	 * <p>
	 * @return {@code true} if {@code a} is less than {@code b}, {@code false}
	 *         otherwise
	 * <p>
	 * @throws NullPointerException if {@code a} or {@code b} is {@code null}
	 */
	public static <T extends Comparable<T>> boolean isLessThan(final T a, final T b)
	{
		return a.compareTo(b) < 0;
	}

	/**
	 *
	 * @param <T> the type of the objects to be compared
	 * @param a   an {@link Object}
	 * @param b   another {@link Object} to be compared with {@code a}
	 * <p>
	 * @return {@code true} if {@code a} is less or equal to {@code b},
	 *         {@code false} otherwise
	 * <p>
	 * @throws NullPointerException if {@code a} or {@code b} is {@code null}
	 */
	public static <T extends Comparable<T>> boolean isLessOrEqualTo(final T a, final T b)
	{
		return a.compareTo(b) <= 0;
	}

	/**
	 *
	 * @param <T> the type of the objects to be compared
	 * @param a   an {@link Object}
	 * @param b   another {@link Object} to be compared with {@code a}
	 * <p>
	 * @return {@code true} if {@code a} is greater than {@code b},
	 *         {@code false} otherwise
	 * <p>
	 * @throws NullPointerException if {@code a} or {@code b} is {@code null}
	 */
	public static <T extends Comparable<T>> boolean isGreaterThan(final T a, final T b)
	{
		return a.compareTo(b) > 0;
	}

	/**
	 *
	 * @param <T> the type of the objects to be compared
	 * @param a   an {@link Object}
	 * @param b   another {@link Object} to be compared with {@code a}
	 * <p>
	 * @return {@code true} if {@code a} is greater or equal to {@code b},
	 *         {@code false} otherwise
	 * <p>
	 * @throws NullPointerException if {@code a} or {@code b} is {@code null}
	 */
	public static <T extends Comparable<T>> boolean isGreaterOrEqualTo(final T a, final T b)
	{
		return a.compareTo(b) >= 0;
	}

	/**
	 * Returns the smaller of the two specified comparable objects. If they have
	 * the same value, the result is {@code a}.
	 * <p>
	 * @param <T> the type of the objects to be compared
	 * @param a   an {@link Object}
	 * @param b   another {@link Object} to be compared with {@code a}
	 * <p>
	 * @return the smaller of {@code a} and {@code b}
	 * <p>
	 * @throws NullPointerException if {@code a} or {@code b} is {@code null}
	 */
	public static <T extends Comparable<T>> T getMin(final T a, final T b)
	{
		return ComparableObjects.<T>isLessOrEqualTo(a, b) ? a : b;
	}

	/**
	 * Returns the larger of the two specified comparable objects. If they have
	 * the same value, the result is {@code a}.
	 * <p>
	 * @param <T> the type of the objects to be compared
	 * @param a   an {@link Object}
	 * @param b   another {@link Object} to be compared with {@code a}
	 * <p>
	 * @return the larger of {@code a} and {@code b}
	 * <p>
	 * @throws NullPointerException if {@code a} or {@code b} is {@code null}
	 */
	public static <T extends Comparable<T>> T getMax(final T a, final T b)
	{
		return ComparableObjects.<T>isGreaterOrEqualTo(a, b) ? a : b;
	}
}
