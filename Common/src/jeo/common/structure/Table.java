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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import jeo.common.exception.InvalidOperationException;
import jeo.common.util.Arguments;
import jeo.common.util.Arrays;
import jeo.common.util.Strings;

public class Table<T>
	implements Iterable<T[]>, Serializable
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 3948171231946222522L;
	/**
	 * The class of the elements.
	 */
	protected final Class<T> c;
	/**
	 * The number of rows.
	 */
	protected int m;
	/**
	 * The number of columns.
	 */
	protected int n;
	/**
	 * The table.
	 */
	protected T[][] table;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructs a table of elements of class {@code c} of {@code m} rows and
	 * {@code n} columns.
	 * <p>
	 * @param c the class of the elements
	 * @param m the number of rows
	 * @param n the number of columns
	 */
	public Table(final Class<T> c, final int m, final int n)
	{
		// Check the argument(s)
		Arguments.requirePositive(m);
		Arguments.requirePositive(n);
		// Set the attribute(s)
		this.c = c;
		this.m = m;
		this.n = n;
		this.table = createArray(m, n);
	}

	/**
	 * Constructs a table of elements of class {@code c} from the specified
	 * table.
	 * <p>
	 * @param c     the class of the elements
	 * @param table the content of the table
	 */
	public Table(final Class<T> c, final T[][] table)
	{
		// Set the attribute(s)
		this.c = c;
		this.table = table.clone();
		this.m = table.length;
		if (this.m > 0)
		{
			this.n = table[0].length;
		}
		else
		{
			this.n = 0;
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// TABLE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the element of the table that is indexed by {@code rowIndex} and
	 * {@code columnIndex}.
	 * <p>
	 * @param rowIndex    the row index of the element to be gotten
	 * @param columnIndex the column index of the element to be gotten
	 * <p>
	 * @return the element of the table that is indexed by {@code rowIndex} and
	 *         {@code columnIndex}
	 */
	public T get(final int rowIndex, final int columnIndex)
	{
		return table[rowIndex][columnIndex];
	}

	/**
	 * Sets the element of the table that is indexed by {@code rowIndex} and
	 * {@code columnIndex} to {@code element}.
	 * <p>
	 * @param rowIndex    the row index of the element to be set
	 * @param columnIndex the column index of the element to be set
	 * @param element     the element to set
	 */
	public void set(final int rowIndex, final int columnIndex, final T element)
	{
		table[rowIndex][columnIndex] = element;
	}

	/**
	 * Returns the number of rows.
	 * <p>
	 * @return the number of rows
	 */
	public int getRowSize()
	{
		return m;
	}

	/**
	 * Returns the row of the table that is indexed by {@code rowIndex}.
	 * <p>
	 * @param rowIndex the index of the row to be gotten
	 * <p>
	 * @return the row of the table that is indexed by {@code rowIndex}
	 */
	public T[] getRow(final int rowIndex)
	{
		// Check the argument(s)
		Arguments.requireNonNegative(rowIndex);
		Arguments.requireLessThan(rowIndex, m);
		// Get the corresponding row
		final T[] row = createArray(n);
		for (int i = 0; i < n; ++i)
		{
			row[i] = table[rowIndex][i];
		}
		return row;
	}

	/**
	 * Sets the row of the table that is indexed by {@code rowIndex} to
	 * {@code row}.
	 * <p>
	 * @param rowIndex the index of the row to be set
	 * @param row      the row to set
	 */
	public void setRow(final int rowIndex, final List<T> row)
	{
		setRow(rowIndex, row.toArray(createArray(n)));
	}

	/**
	 * Sets the row of the table that is indexed by {@code rowIndex} to
	 * {@code row}.
	 * <p>
	 * @param rowIndex the index of the row to be set
	 * @param row      the row to set
	 */
	public void setRow(final int rowIndex, final T[] row)
	{
		// Check the argument(s)
		// - rowIndex
		Arguments.requireNonNegative(rowIndex);
		Arguments.requireLessThan(rowIndex, m);
		// - row
		Arguments.requireNonEmpty(row);
		Arguments.requireMinimumSize(row, n);
		// Set the corresponding row
		for (int i = 0; i < n; ++i)
		{
			table[rowIndex][i] = row[i];
		}
	}

	/**
	 * Sets the row of the table that is indexed by {@code rowIndex} to
	 * {@code row}.
	 * <p>
	 * @param rowIndex the index of the row to be set
	 * @param row      the row to set
	 */
	public void setRow(final int rowIndex, final String[] row)
		throws ClassCastException, IllegalArgumentException
	{
		// Check the argument(s)
		// - rowIndex
		Arguments.requireNonNegative(rowIndex);
		Arguments.requireLessThan(rowIndex, m);
		// - row
		Arguments.requireNonEmpty(row);
		Arguments.requireMinimumSize(row, n);
		// Set the corresponding row
		for (int i = 0; i < n; ++i)
		{
			table[rowIndex][i] = c.cast(row[i]);
		}
	}

	/**
	 * Returns the number of columns.
	 * <p>
	 * @return the number of columns
	 */
	public int getColumnSize()
	{
		return n;
	}

	/**
	 * Returns the column of the table that is indexed by {@code columnIndex}.
	 * <p>
	 * @param columnIndex the index of the column to be gotten
	 * <p>
	 * @return the column of the table that is indexed by {@code columnIndex}
	 */
	public T[] getColumn(final int columnIndex)
	{
		// Check the argument(s)
		Arguments.requireNonNegative(columnIndex);
		Arguments.requireLessThan(columnIndex, n);
		// Get the corresponding column
		final T[] column = createArray(m);
		for (int i = 0; i < m; ++i)
		{
			column[i] = table[i][columnIndex];
		}
		return column;
	}

	/**
	 * Sets the column of the table that is indexed by {@code columnIndex} to
	 * {@code column}.
	 * <p>
	 * @param columnIndex the index of the column to be set
	 * @param column      the column to set
	 */
	public void setColumn(final int columnIndex, final List<T> column)
	{
		setColumn(columnIndex, column.toArray(createArray(m)));
	}

	/**
	 * Sets the column of the table that is indexed by {@code columnIndex} to
	 * {@code column}.
	 * <p>
	 * @param columnIndex the index of the column to be set
	 * @param column      the column to set
	 */
	public void setColumn(final int columnIndex, final T[] column)
	{
		// Check the argument(s)
		// - columnIndex
		Arguments.requireNonNegative(columnIndex);
		Arguments.requireLessThan(columnIndex, n);
		// - column
		Arguments.requireNonEmpty(column);
		Arguments.requireMinimumSize(column, m);
		// Set the corresponding column
		for (int i = 0; i < m; ++i)
		{
			table[i][columnIndex] = column[i];
		}
	}

	/**
	 * Returns a new instance of a one-dimensional array of type {@code T} and
	 * of size {@code size}.
	 * <p>
	 * @param size the size of the array to be instantiated
	 * <p>
	 * @return a new instance of a one-dimensional array of type {@code T} and
	 *         of size {@code size}
	 */
	protected T[] createArray(final int size)
	{
		return Arrays.<T>createArray(c, size);
	}

	/**
	 * Returns a new instance of a two-dimensional array of type {@code T}, of
	 * row size {@code rowSize} and of column size {@code columnSize}.
	 * <p>
	 * @param rowSize    the row size of the array to be instantiated
	 * @param columnSize the column size of the array to be instantiated
	 * <p>
	 * @return a new instance of a two-dimensional array of type {@code T}, of
	 *         row size {@code rowSize} and of column size {@code columnSize}
	 */
	protected T[][] createArray(final int rowSize, final int columnSize)
	{
		return Arrays.<T>createArray(c, rowSize, columnSize);
	}

	/**
	 * Resizes the rows and the columns of {@code this}.
	 */
	public void resize()
	{
		resizeRows();
		resizeColumns();
	}

	/**
	 * Resizes the rows of {@code this}.
	 */
	public void resizeRows()
	{
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				if (table[i][j] == null)
				{
					resize(i, n);
					return;
				}
			}
		}
	}

	/**
	 * Resizes the columns of {@code this}.
	 */
	public void resizeColumns()
	{
		for (int j = 0; j < n; ++j)
		{
			for (int i = 0; i < m; ++i)
			{
				if (table[i][j] == null)
				{
					resize(m, j);
					return;
				}
			}
		}
	}

	/**
	 * Resizes the rows to {@code rowSize} and the columns to {@code columnSize}
	 * of {@code this}.
	 * <p>
	 * @param rowSize    the new number of rows
	 * @param columnSize the new number of columns
	 */
	public void resize(final int rowSize, final int columnSize)
	{
		// Check the argument(s)
		Arguments.requirePositive(rowSize);
		Arguments.requirePositive(columnSize);
		// Resize
		final T[][] resizedTable = createArray(rowSize, columnSize);
		final int minRowSize = Math.min(rowSize, m);
		final int minColumnSize = Math.min(columnSize, n);
		for (int i = 0; i < minRowSize; ++i)
		{
			for (int j = 0; j < minColumnSize; ++j)
			{
				resizedTable[i][j] = table[i][j];
			}
		}
		table = resizedTable;
		m = rowSize;
		n = columnSize;
	}


	////////////////////////////////////////////////////////////////////////////
	// ITERABLE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns an {@link Iterator} over the rows of {@code this}.
	 * <p>
	 * @return an {@link Iterator} over the rows of {@code this}
	 */
	public Iterator<T[]> iterator()
	{
		return new TableIterator();
	}

	/**
	 * An {@link Iterator} over the rows of {@code this}.
	 */
	protected class TableIterator
		implements Iterator<T[]>
	{
		/**
		 * The index of the next row.
		 */
		protected int cursor = 0;

		public boolean hasNext()
		{
			return cursor != m;
		}

		public T[] next()
		{
			final int i = cursor;
			if (i >= m)
			{
				throw new NoSuchElementException();
			}
			cursor = i + 1;
			return table[i];
		}

		public void remove()
		{
			if (cursor > 0)
			{
				if ((m - 1) > 0)
				{
					final T[][] resizedTable = createArray(m - 1, n);
					final int rowIndex = cursor - 1;
					for (int i = 0; i < m; ++i)
					{
						if (i < rowIndex)
						{
							for (int j = 0; j < n; ++j)
							{
								resizedTable[i][j] = table[i][j];
							}
						}
						else if (i > rowIndex)
						{
							for (int j = 0; j < n; ++j)
							{
								resizedTable[i - 1][j] = table[i][j];
							}
						}
					}
					table = resizedTable;
					--m;
					--cursor;
				}
				else
				{
					throw new InvalidOperationException("The last row cannot be removed because the table cannot be empty");
				}
			}
		}
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
		final StringBuilder buffer = new StringBuilder(2 * m * n);
		for (int i = 0; i < m; ++i)
		{
			buffer.append(Strings.arrayToString(getRow(i), "\t")).append("\n");
		}
		return String.valueOf(buffer);
	}
}
