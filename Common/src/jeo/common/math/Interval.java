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

import jeo.common.structure.Pair;

public class Interval<T extends Comparable<T>>
	implements IGroup<T>
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	private T lowerBound = null, upperBound = null;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public Interval()
	{
	}

	/**
	 *
	 * @param lowerBound the lower bound
	 * @param upperBound the upper bound
	 */
	public Interval(final T lowerBound, final T upperBound)
	{
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	/**
	 *
	 * @param pair the lower and upper bounds
	 */
	public Interval(final Pair<T, T> pair)
	{
		this.lowerBound = pair.getFirst();
		this.upperBound = pair.getSecond();
	}


	////////////////////////////////////////////////////////////////////////////
	// GROUP
	////////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * @param value the value to be tested
	 * <p>
	 * @return {@code true} if {@code this} contains the value, {@code false}
	 *         otherwise
	 */
	public boolean isInside(final T value)
	{
		return (value.compareTo(lowerBound) >= 0) && (value.compareTo(upperBound) <= 0);
	}

	/**
	 *
	 * @return {@code true} if {@code this} is empty, {@code false} otherwise
	 */
	public boolean isEmpty()
	{
		return (lowerBound == null) || (upperBound == null);
	}

	/**
	 *
	 * @return {@code true} if {@code this} is correct, {@code false} otherwise
	 */
	public boolean isCorrect()
	{
		return !isEmpty() && (lowerBound.compareTo(upperBound) < 0);
	}


	////////////////////////////////////////////////////////////////////////////
	// BOUNDS
	////////////////////////////////////////////////////////////////////////////

	/**
	 * @return the lower bound
	 */
	public T getLowerBound()
	{
		return lowerBound;
	}

	/**
	 * @param lowerBound the lower bound
	 */
	public void setLowerBound(final T lowerBound)
	{
		this.lowerBound = lowerBound;
	}

	/**
	 * @return the upper bound
	 */
	public T getUpperBound()
	{
		return upperBound;
	}

	/**
	 * @param upperBound the upper bound
	 */
	public void setUpperBound(final T upperBound)
	{
		this.upperBound = upperBound;
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public String toString()
	{
		return "[" + lowerBound + ", " + upperBound + "]";
	}
}
