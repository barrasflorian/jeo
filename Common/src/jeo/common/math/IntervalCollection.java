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

import java.util.Collection;

public class IntervalCollection<T extends Comparable<T>>
	implements IGroup<T>
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	private Collection<Interval<T>> intervals;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public IntervalCollection()
	{
	}

	/**
	 *
	 * @param intervals the {@link Collection} of {@link Interval}
	 */
	public IntervalCollection(final Collection<Interval<T>> intervals)
	{
		this.intervals = intervals;
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
		for (final Interval<T> interval : intervals)
		{
			if (interval.isInside(value))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 * @return {@code true} if {@code this} is empty, {@code false} otherwise
	 */
	public boolean isEmpty()
	{
		return intervals.isEmpty();
	}

	/**
	 *
	 * @return {@code true} if {@code this} is correct, {@code false} otherwise
	 */
	public boolean isCorrect()
	{
		for (final Interval<T> interval : intervals)
		{
			if (!interval.isCorrect())
			{
				return false;
			}
		}
		return !isEmpty();
	}

	/**
	 * @return the {@link Collection}of {@link Interval}
	 */
	public Collection<Interval<T>> getIntervals()
	{
		return intervals;
	}

	/**
	 * @param intervals the {@link Collection} of {@link Interval}
	 */
	public void setIntervals(final Collection<Interval<T>> intervals)
	{
		this.intervals = intervals;
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public String toString()
	{
		return String.valueOf(intervals);
	}
}
