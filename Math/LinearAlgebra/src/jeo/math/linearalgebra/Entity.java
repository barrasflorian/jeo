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

import java.io.Serializable;

public interface Entity
	extends Serializable
{
	////////////////////////////////////////////////////////////////////////////
	// GETTER(S)
	////////////////////////////////////////////////////////////////////////////

	public String getName();


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
	public Entity plus(Entity entity);

	/**
	 * Returns the subtraction of {@code entity} from {@code this}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return {@code this} - {@code entity}
	 */
	public Entity minus(Entity entity);

	/**
	 * Returns the multiplication of {@code this} by {@code entity}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return {@code this} * {@code entity}
	 */
	public Entity times(Entity entity);

	/**
	 * Returns the division of {@code this} by {@code entity}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return {@code this} / {@code entity}
	 */
	public Entity division(Entity entity);

	/**
	 * Returns the value of {@code this} raised to the power of {@code entity}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return {@code this} ^ {@code entity}
	 */
	public Entity power(Entity entity);

	/**
	 * Returns the solution of {@code this}*X={@code entity}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return the solution of {@code this}*X={@code entity}
	 */
	public Entity solution(Entity entity);

	/**
	 * Returns the identity of {@code this}.
	 * <p>
	 * @return eye({@code this})
	 */
	public Entity identity();

	/**
	 * Returns the inverse of {@code this}.
	 * <p>
	 * @return inv({@code this})
	 */
	public Entity inverse();

	/**
	 * Returns the transpose of {@code this}.
	 * <p>
	 * @return {@code this}'
	 */
	public Entity transpose();

	/**
	 * Print {@code this}.
	 */
	public void print();


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public String toString();
}
