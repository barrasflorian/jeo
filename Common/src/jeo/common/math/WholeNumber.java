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

import jeo.common.util.Bits;

public class WholeNumber
	extends ComparableNumber
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = -7457031063663571871L;
	private Long value;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public WholeNumber(final long value)
	{
		this.value = value;
	}


	////////////////////////////////////////////////////////////////////////////
	// WHOLE NUMBER
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the middle of this value rounded to the lower {@code long} value
	 * <p>
	 * @return the middle of this value rounded to the lower {@code long} value
	 */
	public long getMiddle()
	{
		return WholeNumbers.getMiddle(value);
	}

	/**
	 * Returns the greatest common divisor (GCD) of {@code this} and
	 * {@code other}.
	 * <p>
	 * @param other an other whole number
	 * <p>
	 * @return the greatest common divisor (GCD) of {@code this} and
	 *         {@code other}
	 * <p>
	 * @throws InterruptedException
	 */
	public long getGCD(final WholeNumber other)
		throws InterruptedException
	{
		return WholeNumbers.getGCD(value, other.getValue());
	}

	/**
	 * Returns the least common multiple (LCM) of {@code this} and
	 * {@code other}.
	 * <p>
	 * @param other an other whole number
	 * <p>
	 * @return the least common multiple (LCM) of {@code this} and {@code other}
	 * <p>
	 * @throws InterruptedException
	 */
	public long getLCM(final WholeNumber other)
		throws InterruptedException
	{
		return WholeNumbers.getLCM(value, other.getValue());
	}


	////////////////////////////////////////////////////////////////////////////
	// NUMBER
	////////////////////////////////////////////////////////////////////////////

	@Override
	public int intValue()
	{
		return value.intValue();
	}

	@Override
	public long longValue()
	{
		return value.longValue();
	}

	@Override
	public float floatValue()
	{
		return value.floatValue();
	}

	@Override
	public double doubleValue()
	{
		return value.doubleValue();
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S) & SETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * @return the value
	 */
	public Long getValue()
	{
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(final Long value)
	{
		this.value = value;
	}


	////////////////////////////////////////////////////////////////////////////
	// COMPARABLE NUMBER
	////////////////////////////////////////////////////////////////////////////

	@Override
	public int compareTo(final ComparableNumber anotherComparableNumber)
	{
		if (anotherComparableNumber instanceof WholeNumber)
		{
			return value.compareTo(((WholeNumber) anotherComparableNumber).getValue());
		}
		else
		{
			return Numbers.compareTo(value, anotherComparableNumber);
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean equals(final Object object)
	{
		return super.equals(object);
	}

	@Override
	public int hashCode()
	{
		return Bits.hash(value);
	}

	@Override
	public String toString()
	{
		return String.valueOf(value);
	}
}
