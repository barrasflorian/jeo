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

import jeo.common.math.Statistics;

public class NumberTable<T extends Number>
	extends Table<T>
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = -6780581322570406940L;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructs a table of numbers of class {@code c} of {@code m} rows and
	 * {@code n} columns.
	 * <p>
	 * @param c the class of the numbers
	 * @param m the number of rows
	 * @param n the number of columns
	 */
	public NumberTable(final Class<T> c, final int m, final int n)
	{
		super(c, m, n);
	}


	////////////////////////////////////////////////////////////////////////////
	// NUMBER TABLE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the array of means that are computed for each row.
	 * <p>
	 * @return the array of means that are computed for each row
	 */
	public Double[] getRowMeans()
	{
		final Double[] means = new Double[m];
		for (int i = 0; i < m; ++i)
		{
			means[i] = Statistics.<T>getMean(getRow(i));
		}
		return means;
	}

	/**
	 * Returns the array of means that are computed for each column.
	 * <p>
	 * @return the array of means that are computed for each column
	 */
	public Double[] getColumnMeans()
	{
		final Double[] means = new Double[n];
		for (int i = 0; i < n; ++i)
		{
			means[i] = Statistics.<T>getMean(getColumn(i));
		}
		return means;
	}
}
