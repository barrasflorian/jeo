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

import jeo.math.linearalgebra.Entity;

public abstract class Element
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	private Element parent;
	private String expression;
	private Entity entity;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public Element(final Element parent, final String expression)
	{
		this.parent = parent;
		this.expression = expression;
	}

	public Element(final Element parent, final String expression, final Entity entity)
	{
		this.parent = parent;
		this.expression = expression;
		this.entity = entity;
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S) & SETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * @return the parent
	 */
	public Element getParent()
	{
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(final Element parent)
	{
		this.parent = parent;
	}

	/**
	 * @return the expression
	 */
	public String getExpression()
	{
		return expression;
	}

	/**
	 * @param expression the expression to set
	 */
	public void setExpression(final String expression)
	{
		this.expression = expression;
	}

	/**
	 * @return the entity
	 */
	public Entity getEntity()
	{
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(final Entity entity)
	{
		this.entity = entity;
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public String toString()
	{
		return entity.toString();
	}


	////////////////////////////////////////////////////////////////////////////
	// ENUM(S)
	////////////////////////////////////////////////////////////////////////////

	public enum Type
	{
		NUMBER,
		ADDITION,
		SUBTRACTION,
		MULTIPLICATION,
		DIVISION,
		POWER,
		SOLUTION,
		FACTORIAL,
		TRANSPOSE,
		INVERSE,
		LPARENTHESIS,
		RPARENTHESIS
	}
}
