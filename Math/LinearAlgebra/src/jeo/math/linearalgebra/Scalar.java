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
package jeo.math.linearalgebra;

import java.io.PrintWriter;

import jeo.common.io.IOManager;
import jeo.common.math.ComparableNumber;
import jeo.common.math.Numbers;
import jeo.common.util.Bits;
import jeo.common.util.Formats;

public class Scalar
	extends ComparableNumber
	implements Entity, Cloneable
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 704990444751846872L;
	/**
	 * The name of this class.
	 */
	private final String name = getClass().getSimpleName();
	/*
	 * The value.
	 */
	private Double value;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public Scalar(final Double value)
	{
		this.value = value;
	}


	////////////////////////////////////////////////////////////////////////////
	// COPYABLE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns a deep copy of {@code this}.
	 * <p>
	 * @return a deep copy of {@code this}
	 */
	public Scalar copy()
	{
		return new Scalar(value);
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S) & SETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the name of {@code this}.
	 * <p>
	 * @return the name of {@code this}
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the value
	 */
	public Double getValue()
	{
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(final Double value)
	{
		this.value = value;
	}


	////////////////////////////////////////////////////////////////////////////
	// OPERATION(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the addition of {@code entity} to {@code this}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return {@code this} + {@code entity}
	 */
	public Scalar plus(final Entity entity)
	{
		if (entity instanceof Scalar)
		{
			final Scalar scalar = (Scalar) entity;
			return new Scalar(value + scalar.getValue());
		}
		else
		{
			throw new IllegalArgumentException("Cannot add a " + entity.getClass().getSimpleName() + " to a scalar");
		}
	}

	/**
	 * Returns the subtraction of {@code entity} from {@code this}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return {@code this} - {@code entity}
	 */
	public Scalar minus(final Entity entity)
	{
		if (entity instanceof Scalar)
		{
			final Scalar scalar = (Scalar) entity;
			return new Scalar(value - scalar.getValue());
		}
		else
		{
			throw new IllegalArgumentException("Cannot subtract a " + entity.getClass().getSimpleName() + " from a scalar");
		}
	}

	/**
	 * Returns the multiplication of {@code this} by {@code entity}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return {@code this} * {@code entity}
	 */
	public Entity times(final Entity entity)
	{
		if (entity instanceof Scalar)
		{
			final Scalar scalar = (Scalar) entity;
			return new Scalar(value * scalar.getValue());
		}
		else if (entity instanceof Matrix)
		{
			final Matrix matrix = (Matrix) entity;
			return matrix.times(value);
		}
		else
		{
			throw new IllegalArgumentException("Cannot multiply a scalar by a " + entity.getClass().getSimpleName());
		}
	}

	/**
	 * Returns the division of {@code this} by {@code entity}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return {@code this} / {@code entity}
	 */
	public Scalar division(final Entity entity)
	{
		if (entity instanceof Scalar)
		{
			return new Scalar(value / ((Scalar) entity).getValue());
		}
		else
		{
			throw new IllegalArgumentException("Cannot divide a scalar by a " + entity.getClass().getSimpleName());
		}
	}

	/**
	 * Returns the value of {@code this} raised to the power of {@code entity}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return {@code this} ^ {@code entity}
	 */
	public Scalar power(final Entity entity)
	{
		if (entity instanceof Scalar)
		{
			return new Scalar(Math.pow(value, ((Scalar) entity).getValue()));
		}
		else
		{
			throw new IllegalArgumentException("Cannot raise a scalar to the power of " + entity.getClass().getSimpleName());
		}
	}

	/**
	 * Generates the identity {@link Scalar}.
	 * <p>
	 * @return the identity {@link Scalar}
	 */
	public Scalar identity()
	{
		return new Scalar(1.);
	}

	/**
	 * Returns the inverse of {@code this}.
	 * <p>
	 * @return inv({@code this})
	 */
	public Scalar inverse()
	{
		return new Scalar(1. / value);
	}

	/**
	 * Returns the solution of {@code this}*X={@code entity}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return the solution of {@code this}*X={@code entity}
	 */
	public Entity solution(final Entity entity)
	{
		if (entity instanceof Scalar)
		{
			final Scalar B = (Scalar) entity;
			if (equals(0))
			{
				if (B.equals(0))
				{
					throw new ArithmeticException("Cannot find a unique solution if A and B are equal to zero");
				}
				else
				{
					throw new ArithmeticException("Cannot find a solution if A is equal to zero and B is not equal to zero");
				}
			}
			else
			{
				return new Scalar(B.getValue() / value);
			}
		}
		else
		{
			throw new IllegalArgumentException("Cannot find a solution if A is a " + getClass().getSimpleName() + " and B is a " + entity.getClass().getSimpleName());
		}
	}

	/**
	 * Returns the transpose of {@code this}.
	 * <p>
	 * @return {@code this}'
	 */
	public Scalar transpose()
	{
		return this;
	}

	/**
	 * Returns the value of {@code this} raised to the power of {@code entity}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return {@code this} * {@code entity}
	 */
	public Entity factorial(final Entity entity)
	{
		if (entity instanceof Scalar)
		{
			return new Scalar(Math.pow(value, ((Scalar) entity).getValue()));
		}
		else
		{
			throw new IllegalArgumentException("Cannot raise a scalar to the power of " + entity.getClass().getSimpleName());
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// PRINT
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Print {@code this}.
	 */
	public void print()
	{
		print(Formats.MIN_NUMBER_SIZE);
	}

	/**
	 * Print the {@link Scalar} with {@link IOManager}.
	 * <p>
	 * @param width the characters width
	 */
	public void print(final int width)
	{
		IOManager.printLine(toString(width), false);
	}

	/**
	 * Print the {@link Scalar} to the specified output stream.
	 * <p>
	 * @param output the output stream
	 * @param width  the characters width
	 */
	public void print(final PrintWriter output, final int width)
	{
		output.println(toString(width));
	}


	////////////////////////////////////////////////////////////////////////////
	// NUMBER
	////////////////////////////////////////////////////////////////////////////

	@Override
	public int compareTo(final ComparableNumber anotherComparableNumber)
	{
		if (anotherComparableNumber instanceof Scalar)
		{
			return value.compareTo(((Scalar) anotherComparableNumber).getValue());
		}
		else
		{
			return Numbers.compareTo(value, anotherComparableNumber);
		}
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
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public Object clone()
		throws CloneNotSupportedException
	{
		return super.clone();
	}

	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (other instanceof Scalar)
		{
			return value.equals(((Scalar) other).getValue());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Bits.generateHashCode(Bits.hash(serialVersionUID), Bits.hash(value));
	}

	@Override
	public String toString()
	{
		return toString(Formats.MIN_NUMBER_SIZE);
	}

	public String toString(final int minWidth)
	{
		final StringBuilder builder = new StringBuilder(Formats.NUMBER_SIZE);
		final String formattedValue = Formats.format(value);
		final int padding = Math.max(0, minWidth - formattedValue.length());
		for (int k = 0; k < padding; ++k)
		{
			builder.append(' ');
		}
		builder.append(formattedValue);
		return builder.toString();
	}
}
