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

public abstract class ComparableNumber
	extends Number
	implements IComparable<ComparableNumber>
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = -7608182412833955401L;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	protected ComparableNumber()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// COMPARABLE NUMBER
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Compares this number with the specified comparable number for order.
	 * Returns a negative integer, zero, or a positive integer as this number is
	 * less than, equal to, or greater than the specified comparable number.
	 * <p>
	 * @param anotherComparableNumber another {@link ComparableNumber} to be
	 *                                compared with {@code this} for order
	 * <p>
	 * @return a negative integer, zero, or a positive integer as {@code this}
	 *         is less than, equal to, or greater than
	 *         {@code anotherComparableNumber}
	 * <p>
	 * @throws NullPointerException if the specified comparable number is null
	 */
	public abstract int compareTo(final ComparableNumber anotherComparableNumber);

	/**
	 *
	 * @param anotherComparableNumber another {@link ComparableNumber} to be
	 *                                compared with {@code this} for equality
	 * <p>
	 * @return {@code true} if {@code this} is equal to
	 *         {@code anotherComparableNumber}, {@code false} otherwise
	 * <p>
	 * @throws NullPointerException if the specified comparable number is null
	 */
	public boolean equals(final ComparableNumber anotherComparableNumber)
	{
		return compareTo(anotherComparableNumber) == 0;
	}

	/**
	 *
	 * @param anotherComparableNumber another {@link ComparableNumber} to be
	 *                                compared
	 * <p>
	 * @return {@code true} if {@code this} is less than
	 *         {@code anotherComparableNumber}, {@code false} otherwise
	 * <p>
	 * @throws NullPointerException if the specified comparable number is null
	 */
	public boolean isLessThan(final ComparableNumber anotherComparableNumber)
	{
		return compareTo(anotherComparableNumber) < 0;
	}

	/**
	 *
	 * @param anotherComparableNumber another {@link ComparableNumber} to be
	 *                                compared
	 * <p>
	 * @return {@code true} if {@code this} is less or equal to
	 *         {@code anotherComparableNumber}, {@code false} otherwise
	 * <p>
	 * @throws NullPointerException if the specified comparable number is null
	 */
	public boolean isLessOrEqualTo(final ComparableNumber anotherComparableNumber)
	{
		return compareTo(anotherComparableNumber) <= 0;
	}

	/**
	 *
	 * @param anotherComparableNumber another {@link ComparableNumber} to be
	 *                                compared
	 * <p>
	 * @return {@code true} if {@code this} is greater than
	 *         {@code anotherComparableNumber}, {@code false} otherwise
	 * <p>
	 * @throws NullPointerException if the specified comparable number is null
	 */
	public boolean isGreaterThan(final ComparableNumber anotherComparableNumber)
	{
		return compareTo(anotherComparableNumber) > 0;
	}

	/**
	 *
	 * @param anotherComparableNumber another {@link ComparableNumber} to be
	 *                                compared
	 * <p>
	 * @return {@code true} if {@code this} is greater or equal to
	 *         {@code anotherComparableNumber}, {@code false} otherwise
	 * <p>
	 * @throws NullPointerException if the specified comparable number is null
	 */
	public boolean isGreaterOrEqualTo(final ComparableNumber anotherComparableNumber)
	{
		return compareTo(anotherComparableNumber) >= 0;
	}

	/**
	 * Returns the smaller of {@code this} and {@code anotherComparableNumber}.
	 * If they have the same value, the result is {@code this}.
	 * <p>
	 * @param anotherComparableNumber another {@link ComparableNumber} to be
	 *                                compared
	 * <p>
	 * @return the smaller of {@code this} and {@code anotherComparableNumber}
	 * <p>
	 * @throws NullPointerException if the specified comparable number is null
	 */
	public ComparableNumber getMin(final ComparableNumber anotherComparableNumber)
	{
		return isLessOrEqualTo(anotherComparableNumber) ? this : anotherComparableNumber;
	}

	/**
	 * Returns the larger of {@code this} and {@code anotherComparableNumber}.
	 * If they have the same value, the result is {@code this}.
	 * <p>
	 * @param anotherComparableNumber another {@link ComparableNumber} to be
	 *                                compared
	 * <p>
	 * @return the larger of {@code this} and {@code anotherComparableNumber}
	 * <p>
	 * @throws NullPointerException if the specified comparable number is null
	 */
	public ComparableNumber getMax(final ComparableNumber anotherComparableNumber)
	{
		return isGreaterOrEqualTo(anotherComparableNumber) ? this : anotherComparableNumber;
	}


	////////////////////////////////////////////////////////////////////////////
	// NUMBER
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Compares this number with the specified number for order. Returns a
	 * negative integer, zero, or a positive integer as this number is less
	 * than, equal to, or greater than the specified number.
	 * <p>
	 * @param anotherNumber another {@link Number} to be compared with
	 *                      {@code this} for order
	 * <p>
	 * @return a negative integer, zero, or a positive integer as {@code this}
	 *         is less than, equal to, or greater than {@code anotherNumber}
	 * <p>
	 * @throws NullPointerException if the specified number is null
	 */
	public int compareTo(final Number anotherNumber)
	{
		return Numbers.compareTo(this, anotherNumber);
	}

	/**
	 *
	 * @param anotherNumber another {@link Number} to be compared with
	 *                      {@code this} for equality
	 * <p>
	 * @return {@code true} if {@code this} is equal to {@code anotherNumber},
	 *         {@code false} otherwise
	 * <p>
	 * @throws NullPointerException if the specified number is null
	 */
	public boolean equals(final Number anotherNumber)
	{
		return compareTo(anotherNumber) == 0;
	}

	/**
	 *
	 * @param anotherNumber another {@link Number} to be compared
	 * <p>
	 * @return {@code true} if {@code this} is less than {@code anotherNumber},
	 *         {@code false} otherwise
	 * <p>
	 * @throws NullPointerException if the specified number is null
	 */
	public boolean isLessThan(final Number anotherNumber)
	{
		return compareTo(anotherNumber) < 0;
	}

	/**
	 *
	 * @param anotherNumber another {@link Number} to be compared
	 * <p>
	 * @return {@code true} if {@code this} is less or equal to
	 *         {@code anotherNumber}, {@code false} otherwise
	 * <p>
	 * @throws NullPointerException if the specified number is null
	 */
	public boolean isLessOrEqualTo(final Number anotherNumber)
	{
		return compareTo(anotherNumber) <= 0;
	}

	/**
	 *
	 * @param anotherNumber another {@link Number} to be compared
	 * <p>
	 * @return {@code true} if {@code this} is greater than
	 *         {@code anotherNumber}, {@code false} otherwise
	 * <p>
	 * @throws NullPointerException if the specified number is null
	 */
	public boolean isGreaterThan(final Number anotherNumber)
	{
		return compareTo(anotherNumber) > 0;
	}

	/**
	 *
	 * @param anotherNumber another {@link Number} to be compared
	 * <p>
	 * @return {@code true} if {@code this} is greater or equal to
	 *         {@code anotherNumber}, {@code false} otherwise
	 * <p>
	 * @throws NullPointerException if the specified number is null
	 */
	public boolean isGreaterOrEqualTo(final Number anotherNumber)
	{
		return compareTo(anotherNumber) >= 0;
	}

	/**
	 * Returns the smaller of {@code this} and {@code anotherNumber}. If they
	 * have the same value, the result is {@code this}.
	 * <p>
	 * @param anotherNumber another {@link Number} to be compared
	 * <p>
	 * @return the smaller of {@code this} and {@code anotherNumber}
	 * <p>
	 * @throws NullPointerException if the specified number is null
	 */
	public Number getMin(final Number anotherNumber)
	{
		return isLessOrEqualTo(anotherNumber) ? this : anotherNumber;
	}

	/**
	 * Returns the larger of {@code this} and {@code anotherNumber}. If they
	 * have the same value, the result is {@code this}.
	 * <p>
	 * @param anotherNumber another {@link Number} to be compared
	 * <p>
	 * @return the larger of {@code this} and {@code anotherNumber}
	 * <p>
	 * @throws NullPointerException if the specified number is null
	 */
	public Number getMax(final Number anotherNumber)
	{
		return isGreaterOrEqualTo(anotherNumber) ? this : anotherNumber;
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean equals(final Object object)
	{
		if ((object == null) || !(object instanceof ComparableNumber))
		{
			return false;
		}
		else
		{
			return compareTo((ComparableNumber) object) == 0;
		}
	}

	@Override
	public abstract int hashCode();
}
