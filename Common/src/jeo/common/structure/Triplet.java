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

import java.io.Serializable;

public class Triplet<T1, T2, T3>
	implements Serializable
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 5604616394618891051L;
	private T1 first;
	private T2 second;
	private T3 third;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public Triplet()
	{
	}

	public Triplet(final T1 first, final T2 second, final T3 third)
	{
		this.first = first;
		this.second = second;
		this.third = third;
	}


	////////////////////////////////////////////////////////////////////////////
	// PAIR
	////////////////////////////////////////////////////////////////////////////

	/**
	 * @return the first element
	 */
	public T1 getFirst()
	{
		return first;
	}

	/**
	 * @param first the first element to set
	 */
	public void setFirst(final T1 first)
	{
		this.first = first;
	}

	/**
	 * @return the second element
	 */
	public T2 getSecond()
	{
		return second;
	}

	/**
	 * @param second the second element to set
	 */
	public void setSecond(final T2 second)
	{
		this.second = second;
	}

	/**
	 * @return the third element
	 */
	public T3 getThird()
	{
		return third;
	}

	/**
	 * @param third the third element to set
	 */
	public void setThird(final T3 third)
	{
		this.third = third;
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * @return a {@link String} representation of {@code this}
	 */
	@Override
	public String toString()
	{
		return "(" + first + ", " + second + ", " + third + ")";
	}
}
