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

public interface IComparable<T extends Comparable<T>>
	extends Comparable<T>
{
	/**
	 * Compares this object with the specified object for order. Returns a
	 * negative integer, zero, or a positive integer as this object is less
	 * than, equal to, or greater than the specified object.
	 * <p>
	 * @param anotherComparable the object to be compared
	 * <p>
	 * @return a negative integer, zero, or a positive integer as this object is
	 *         less than, equal to, or greater than the specified object
	 * <p>
	 * @throws NullPointerException if the specified object is null
	 * @throws ClassCastException   if the specified object's type prevents it
	 *                              from being compared to this object
	 */
	public int compareTo(final T anotherComparable);

	/**
	 *
	 * @param anotherComparable the object to be compared
	 * <p>
	 * @return {@code true} if {@code this} is equal to
	 *         {@code anotherComparable}, {@code false} otherwise
	 */
	public boolean equals(final T anotherComparable);

	/**
	 *
	 * @param anotherComparable the object to be compared
	 * <p>
	 * @return {@code true} if {@code this} is less than
	 * <p>
	 * {@code anotherComparable}, {@code false} otherwise
	 */
	public boolean isLessThan(final T anotherComparable);

	/**
	 *
	 * @param anotherComparable the object to be compared
	 * <p>
	 * @return {@code true} if {@code this} is less or equal to
	 * <p>
	 * {@code anotherComparable}, {@code false} otherwise
	 */
	public boolean isLessOrEqualTo(final T anotherComparable);

	/**
	 *
	 * @param anotherComparable the object to be compared
	 * <p>
	 * @return {@code true} if {@code this} is greater than
	 * <p>
	 * {@code anotherComparable}, {@code false} otherwise
	 */
	public boolean isGreaterThan(final T anotherComparable);

	/**
	 *
	 * @param anotherComparable the object to be compared
	 * <p>
	 * @return {@code true} if {@code this} is greater or equal to
	 * <p>
	 * {@code anotherComparable}, {@code false} otherwise
	 */
	public boolean isGreaterOrEqualTo(final T anotherComparable);

	/**
	 * Returns the smaller of two {@link Comparable} values. If they have the
	 * same value, the result is {@code this}.
	 * <p>
	 * @param anotherComparable the object to be compared
	 * <p>
	 * @return the smaller of {@code this} and {@code anotherComparable}
	 */
	public T getMin(final T anotherComparable);

	/**
	 * Returns the larger of two {@link Comparable} values. If they have the
	 * same value, the result is {@code this}.
	 * <p>
	 * @param anotherComparable the object to be compared
	 * <p>
	 * @return the larger of {@code this} and {@code anotherComparable}
	 */
	public T getMax(final T anotherComparable);
}
