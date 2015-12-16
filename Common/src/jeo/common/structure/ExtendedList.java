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
package jeo.common.structure;

import java.util.ArrayList;
import java.util.Collection;

import jeo.common.math.WholeNumbers;
import jeo.common.model.ICopyable;
import jeo.common.util.Arguments;
import jeo.common.util.Collections;
import jeo.common.util.Strings;

public class ExtendedList<T>
	extends ArrayList<T>
	implements ICopyable<ExtendedList<T>>
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = -3140869118150579669L;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructs an empty list with an initial capacity of ten.
	 */
	public ExtendedList()
	{
		super(Collections.DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * Constructs an empty list with the specified initial capacity.
	 * <p>
	 * @param initialCapacity the initial capacity of the list
	 * <p>
	 * @throws IllegalArgumentException if {@code initialCapacity} is negative
	 */
	public ExtendedList(final int initialCapacity)
	{
		super(initialCapacity);
	}

	/**
	 * Constructs a list containing the elements of the specified collection, in
	 * the order they are returned by the collection's iterator.
	 * <p>
	 * @param c the collection whose elements are to be placed into this list
	 * <p>
	 * @throws NullPointerException if {@code c} is null
	 */
	public ExtendedList(final Collection<? extends T> c)
	{
		super(c);
	}


	////////////////////////////////////////////////////////////////////////////
	// ICOPYABLE
	////////////////////////////////////////////////////////////////////////////

	public ExtendedList<T> copy()
	{
		return new ExtendedList<T>(this);
	}


	////////////////////////////////////////////////////////////////////////////
	// EXTENDED LIST
	////////////////////////////////////////////////////////////////////////////

	public T getMiddleElement()
	{
		Arguments.requireNonEmpty(this);
		return get(WholeNumbers.getMiddle(size()));
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public Object clone()
	{
		return super.clone();
	}

	/**
	 * Returns a {@link String} representation of {@code this}.
	 * <p>
	 * @return a {@link String} representation of {@code this}
	 */
	@Override
	public String toString()
	{
		return Strings.<T>collectionToString(this);
	}
}
