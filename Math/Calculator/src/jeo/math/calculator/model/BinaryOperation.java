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
package jeo.math.calculator.model;

public class BinaryOperation
	extends Element
{
	private Type type;
	private Element left;
	private Element right;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public BinaryOperation(final Element parent, final String expression, final Type type, final Element left, final Element right)
	{
		super(parent, expression);
		this.type = type;
		this.left = left;
		this.right = right;
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S) & SETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * @return the type
	 */
	public Type getType()
	{
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(final Type type)
	{
		this.type = type;
	}

	/**
	 * @return the left
	 */
	public Element getLeft()
	{
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(final Element left)
	{
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public Element getRight()
	{
		return right;
	}

	/**
	 * @param right the right to set
	 */
	public void setRight(final Element right)
	{
		this.right = right;
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public String toString()
	{
		return left.toString() + " " + type.toString() + " " + right.toString();
	}
}
