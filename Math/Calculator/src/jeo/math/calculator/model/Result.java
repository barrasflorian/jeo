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

import jeo.common.io.Message;
import jeo.common.util.Strings;
import jeo.math.linearalgebra.Entity;

public class Result
{
	private final Entity solution;
	private final Message message;

	public Result(final Entity solution, final Message message)
	{
		this.solution = solution;
		this.message = message;
	}

	/**
	 * @return the solution
	 */
	public Entity getSolution()
	{
		return solution;
	}

	/**
	 * @return the message
	 */
	public Message getMessage()
	{
		return message;
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = Strings.createBuffer();
		builder.append(String.valueOf(solution));
		if (message != null)
		{
			builder.append(" (").append(message.getContent()).append(")");
		}
		return builder.toString();
	}
}
