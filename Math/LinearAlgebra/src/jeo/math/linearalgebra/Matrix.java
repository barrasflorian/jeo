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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jeo.common.io.IOManager;
import jeo.common.structure.ExtendedList;
import jeo.common.util.Arguments;
import jeo.common.util.Bits;
import jeo.common.util.Formats;
import jeo.common.util.Strings;

/**
 * Extension of the Java Matrix class (JAMA).
 * <p>
 * This extension provides the possibilities to multiply Matrices with Scalars
 * and to parse Matrices from an input String.
 * <p>
 * The Java Matrix Class provides the fundamental operations of numerical linear
 * algebra. Various constructors create Matrices from two dimensional arrays of
 * double precision floating point numbers. Various "gets" and "sets" provide
 * access to submatrices and matrix elements. Several methods implement basic
 * matrix arithmetic, including matrix addition and multiplication, matrix
 * norms, and element-by-element array operations. Methods for reading and
 * printing matrices are also included. All the operations in this version of
 * the Matrix Class involve real matrices. Complex matrices may be handled in a
 * future version.
 * <p>
 * Five fundamental matrix decompositions, which consist of pairs or triples of
 * matrices, permutation vectors, and the like, produce results in five
 * decomposition classes. These decompositions are accessed by the Matrix class
 * to compute solutions of simultaneous linear equations, determinants, inverses
 * and other matrix functions. The five decompositions are:
 * <p>
 * <UL>
 * <LI>Cholesky Decomposition of symmetric, positive definite matrices.
 * <LI>LU Decomposition of rectangular matrices.
 * <LI>QR Decomposition of rectangular matrices.
 * <LI>Singular Value Decomposition of rectangular matrices.
 * <LI>Eigenvalue Decomposition of both symmetric and nonsymmetric square
 * matrices.
 * </UL>
 * <DL>
 * <DT><B>Example of use:</B></DT>
 * <p>
 * <DD>Solve a linear system A x = b and compute the residual norm, ||b - A x||.
 * <p>
 * </DD>
 * </DL>
 * <p>
 * @author JAMA, http://math.nist.gov/javanumerics/jama/
 * @version 1.0.3
 */
public class Matrix
	implements Entity, Cloneable
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 4509782084822552230L;
	/**
	 * The simple name of this class.
	 */
	private final String name = getClass().getSimpleName();
	/**
	 * The components.
	 */
	private final double[][] components;
	/**
	 * The row and column dimensions.
	 */
	private final int m, n;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructs a square {@link Matrix} of zeros with the specified number of
	 * rows and columns.
	 * <p>
	 * @param size the number of rows and columns
	 */
	public Matrix(final int size)
	{
		// Set the numbers of rows and columns
		m = size;
		n = size;
		// Set the components
		components = new double[m][n];
	}

	/**
	 * Constructs a {@link Matrix} of zeros with the specified numbers of rows
	 * and columns.
	 * <p>
	 * @param m the number of rows
	 * @param n the number of columns
	 */
	public Matrix(final int m, final int n)
	{
		// Set the numbers of rows and columns
		this.m = m;
		this.n = n;
		// Set the components
		components = new double[m][n];
	}

	/**
	 * Constructs a constant {@link Matrix} with the specified numbers of rows
	 * and columns.
	 * <p>
	 * @param m     the number of rows
	 * @param n     the number of columns
	 * @param value the value of the components of {@code this}
	 */
	public Matrix(final int m, final int n, final double value)
	{
		// Set the numbers of rows and columns
		this.m = m;
		this.n = n;
		// Set the components
		components = new double[m][n];
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				components[i][j] = value;
			}
		}
	}

	/**
	 * Constructs a {@link Matrix} with the specified numbers of rows and the
	 * specified components in a 1D array.
	 * <p>
	 * @param m          the number of rows
	 * @param components the components in a 1D array
	 * <p>
	 * @throws IllegalArgumentException if the length of {@code components} is
	 *                                  not a multiple of {@code m}
	 */
	public Matrix(final int m, final double[] components)
	{
		final int length = components.length;
		// Set the numbers of rows and columns
		this.m = m;
		n = m != 0 ? length / m : 0;
		// Check the length of the specified array
		if ((m * n) != length)
		{
			throw new IllegalArgumentException("Specified array has a length " + length + " that is not a multiple of " + m);
		}
		// Set the components
		this.components = new double[m][n];
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				this.components[i][j] = components[i + (j * m)];
			}
		}
	}

	/**
	 * Constructs a {@link Matrix} with the specified numbers of rows and
	 * columns and the specified components in a 2D array.
	 * <p>
	 * @param m          the number of rows
	 * @param n          the number of columns
	 * @param components the components in a 2D array
	 */
	public Matrix(final int m, final int n, final double[][] components)
	{
		// Set the numbers of rows and columns
		this.m = m;
		this.n = n;
		// Set the components
		this.components = components;
	}

	/**
	 * Constructs a {@link Matrix} with the specified components in a 2D array.
	 * <p>
	 * @param components the components in a 2D array
	 * <p>
	 * @throws IllegalArgumentException if the rows of {@code components} have
	 *                                  not the same length
	 */
	public Matrix(final double[][] components)
	{
		// Set the numbers of rows and columns
		m = components.length;
		n = components[0].length;
		// Check the row lengths of the specified array
		for (int i = 0; i < m; ++i)
		{
			if (components[i].length != n)
			{
				throw new IllegalArgumentException("Specified array has different row lengths");
			}
		}
		// Set the components
		this.components = new double[m][n];
		for (int i = 0; i < m; ++i)
		{
			System.arraycopy(components[i], 0, this.components[i], 0, n);
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// COPYABLE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns a deep copy of {@code this}.
	 * <p>
	 * @return a deep copy of {@code this}
	 */
	public Matrix copy()
	{
		final Matrix matrix = new Matrix(m, n);
		final double[][] matrixComponents = matrix.getComponents();
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				matrixComponents[i][j] = components[i][j];
			}
		}
		return matrix;
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S)
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
	 * Returns the components of {@code this}.
	 * <p>
	 * @return the components of {@code this}
	 */
	public double[][] getComponents()
	{
		return components;
	}

	/**
	 * Returns a copy of the components of {@code this}.
	 * <p>
	 * @return a copy of the components of {@code this}
	 */
	public double[][] getComponentsCopy()
	{
		final double[][] copy = new double[m][n];
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				copy[i][j] = components[i][j];
			}
		}
		return copy;
	}

	/**
	 * Returns the component with the specified row and column indexes.
	 * <p>
	 * @param i the row index of the component to be gotten
	 * @param j the column index of the component to be gotten
	 * <p>
	 * @return the component of {@code this} with the specified row and column
	 *         indexes
	 * <p>
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public double get(final int i, final int j)
	{
		return components[i][j];
	}

	/**
	 * Returns the row dimension of {@code this}.
	 * <p>
	 * @return the row dimension of {@code this}
	 */
	public int getRowDimension()
	{
		return m;
	}

	/**
	 * Returns the column dimension of {@code this}.
	 * <p>
	 * @return the column dimension of {@code this}
	 */
	public int getColumnDimension()
	{
		return n;
	}

	/**
	 * Returns the dimension of {@code this}.
	 * <p>
	 * @return the dimension of {@code this}
	 */
	public String getDimension()
	{
		return m + "x" + n;
	}

	/**
	 * Returns the submatrix {@code this}({@code rowStart}:{@code rowEnd},
	 * {@code columnStart}:{@code columnEnd}).
	 * <p>
	 * @param rowStart    the initial row index (inclusive)
	 * @param rowEnd      the final row index (exclusive)
	 * @param columnStart the initial column index (inclusive)
	 * @param columnEnd   the final column index (exclusive)
	 * <p>
	 * @return the submatrix {@code this}({@code rowStart}:{@code rowEnd},
	 *         {@code columnStart}:{@code columnEnd})
	 * <p>
	 * @throws ArrayIndexOutOfBoundsException if the specified submatrix indexes
	 *                                        are out of bounds
	 */
	public Matrix getMatrix(final int rowStart, final int rowEnd, final int columnStart, final int columnEnd)
	{
		// Create the submatrix
		final Matrix matrix = new Matrix(rowEnd - rowStart, columnEnd - columnStart);
		final double[][] matrixComponents = matrix.getComponents();
		try
		{
			for (int i = rowStart; i < rowEnd; ++i)
			{
				for (int j = columnStart; j < columnEnd; ++j)
				{
					matrixComponents[i - rowStart][j - columnStart] = components[i][j];
				}
			}
		}
		catch (final ArrayIndexOutOfBoundsException ex)
		{
			throw new ArrayIndexOutOfBoundsException("Specified submatrix indexes are out of bounds");
		}
		return matrix;
	}

	/**
	 * Returns the submatrix {@code this}({@code rowIndexes}(:),
	 * {@code columnIndexes}(:)).
	 * <p>
	 * @param rowIndexes    an array of row indexes
	 * @param columnIndexes an array of column indexes
	 * <p>
	 * @return the submatrix {@code this}({@code rowIndexes}(:),
	 *         {@code columnIndexes}(:))
	 * <p>
	 * @throws ArrayIndexOutOfBoundsException if the specified submatrix indexes
	 *                                        are out of bounds
	 */
	public Matrix getMatrix(final int[] rowIndexes, final int[] columnIndexes)
	{
		final int nRows = rowIndexes.length;
		final int nColumns = columnIndexes.length;
		// Create the submatrix
		final Matrix matrix = new Matrix(nRows, nColumns);
		final double[][] matrixComponents = matrix.getComponents();
		try
		{
			for (int i = 0; i < nRows; ++i)
			{
				for (int j = 0; j < nColumns; ++j)
				{
					matrixComponents[i][j] = components[rowIndexes[i]][columnIndexes[j]];
				}
			}
		}
		catch (final ArrayIndexOutOfBoundsException ex)
		{
			throw new ArrayIndexOutOfBoundsException("Specified submatrix indexes are out of bounds");
		}
		return matrix;
	}

	/**
	 * Returns the submatrix {@code this}({@code rowIndexes}(:),
	 * {@code columnStart}:{@code columnEnd})
	 * <p>
	 * @param rowIndexes  an array of row indexes
	 * @param columnStart the initial column index (inclusive)
	 * @param columnEnd   the final column index (exclusive)
	 * <p>
	 * @return the submatrix {@code this}({@code rowIndexes}(:),
	 *         {@code columnStart}:{@code columnEnd})
	 * <p>
	 * @throws ArrayIndexOutOfBoundsException if the specified submatrix indexes
	 *                                        are out of bounds
	 */
	public Matrix getMatrix(final int[] rowIndexes, final int columnStart, final int columnEnd)
	{
		final int nRows = rowIndexes.length;
		// Create the submatrix
		final Matrix matrix = new Matrix(nRows, columnEnd - columnStart);
		final double[][] B = matrix.getComponents();
		try
		{
			for (int i = 0; i < nRows; ++i)
			{
				for (int j = columnStart; j < columnEnd; ++j)
				{
					B[i][j - columnStart] = components[rowIndexes[i]][j];
				}
			}
		}
		catch (final ArrayIndexOutOfBoundsException ex)
		{
			throw new ArrayIndexOutOfBoundsException("Specified submatrix indexes are out of bounds");
		}
		return matrix;
	}

	/**
	 * Returns the submatrix {@code this}({@code rowStart}:{@code rowEnd},
	 * {@code columnIndexes}(:)).
	 * <p>
	 * @param rowStart      the initial row index (inclusive)
	 * @param rowEnd        the final row index (exclusive)
	 * @param columnIndexes an array of column indexes
	 * <p>
	 * @return {@code this}({@code rowStart}:{@code rowEnd},
	 *         {@code columnIndexes}(:))
	 * <p>
	 * @throws ArrayIndexOutOfBoundsException if the specified submatrix indexes
	 *                                        are out of bounds
	 */
	public Matrix getMatrix(final int rowStart, final int rowEnd, final int[] columnIndexes)
	{
		final int nColumns = columnIndexes.length;
		// Create the submatrix
		final Matrix matrix = new Matrix(rowEnd - rowStart, nColumns);
		final double[][] matrixComponents = matrix.getComponents();
		try
		{
			for (int i = rowStart; i < rowEnd; ++i)
			{
				for (int j = 0; j < nColumns; ++j)
				{
					matrixComponents[i - rowStart][j] = components[i][columnIndexes[j]];
				}
			}
		}
		catch (final ArrayIndexOutOfBoundsException ex)
		{
			throw new ArrayIndexOutOfBoundsException("Specified submatrix indexes are out of bounds");
		}
		return matrix;
	}


	////////////////////////////////////////////////////////////////////////////
	// SETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Sets the component with the specified row and column indexes.
	 * <p>
	 * @param i     the row index of the component to be set
	 * @param j     the column index of the component to be set
	 * @param value the value to set
	 * <p>
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public void set(final int i, final int j, final double value)
	{
		components[i][j] = value;
	}

	/**
	 * Returns the submatrix {@code this}({@code rowStart}:{@code rowEnd},
	 * {@code columnStart}:{@code columnEnd}).
	 * <p>
	 * @param rowStart    the initial row index (inclusive)
	 * @param rowEnd      the final row index (exclusive)
	 * @param columnStart the initial column index (inclusive)
	 * @param columnEnd   the final column index (exclusive)
	 * @param matrix      the {@link Matrix} to set
	 * <p>
	 * @throws ArrayIndexOutOfBoundsException if the specified submatrix indexes
	 *                                        are out of bounds
	 */
	public void setMatrix(final int rowStart, final int rowEnd, final int columnStart, final int columnEnd, final Matrix matrix)
	{
		try
		{
			for (int i = rowStart; i < rowEnd; ++i)
			{
				for (int j = columnStart; j < columnEnd; ++j)
				{
					components[i][j] = matrix.get(i - rowStart, j - columnStart);
				}
			}
		}
		catch (final ArrayIndexOutOfBoundsException ex)
		{
			throw new ArrayIndexOutOfBoundsException("Specified submatrix indexes are out of bounds");
		}
	}

	/**
	 * Sets the submatrix {@code this}({@code rowIndexes}(:),
	 * {@code columnIndexes}(:)).
	 * <p>
	 * @param rowIndexes    an array of row indexes
	 * @param columnIndexes an array of column indexes
	 * @param matrix        the {@link Matrix} to set
	 * <p>
	 * @throws ArrayIndexOutOfBoundsException if the specified submatrix indexes
	 *                                        are out of bounds
	 */
	public void setMatrix(final int[] rowIndexes, final int[] columnIndexes, final Matrix matrix)
	{
		final int nRows = rowIndexes.length;
		final int nColumns = columnIndexes.length;
		try
		{
			for (int i = 0; i < nRows; ++i)
			{
				for (int j = 0; j < nColumns; ++j)
				{
					components[rowIndexes[i]][columnIndexes[j]] = matrix.get(i, j);
				}
			}
		}
		catch (final ArrayIndexOutOfBoundsException ex)
		{
			throw new ArrayIndexOutOfBoundsException("Specified submatrix indexes are out of bounds");
		}
	}

	/**
	 * Sets the submatrix {@code this}({@code rowIndexes}(:),
	 * {@code columnStart}:{@code columnEnd})
	 * <p>
	 * @param rowIndexes  an array of row indexes
	 * @param columnStart the initial column index (inclusive)
	 * @param columnEnd   the final column index (exclusive)
	 * @param matrix      the {@link Matrix} to set
	 * <p>
	 * @throws ArrayIndexOutOfBoundsException if the specified submatrix indexes
	 *                                        are out of bounds
	 */
	public void setMatrix(final int[] rowIndexes, final int columnStart, final int columnEnd, final Matrix matrix)
	{
		final int nRows = rowIndexes.length;
		try
		{
			for (int i = 0; i < nRows; ++i)
			{
				for (int j = columnStart; j < columnEnd; ++j)
				{
					components[rowIndexes[i]][j] = matrix.get(i, j - columnStart);
				}
			}
		}
		catch (final ArrayIndexOutOfBoundsException ex)
		{
			throw new ArrayIndexOutOfBoundsException("Specified submatrix indexes are out of bounds");
		}
	}

	/**
	 * Sets the submatrix {@code this}({@code rowStart}:{@code rowEnd},
	 * {@code columnIndexes}(:)).
	 * <p>
	 * @param rowStart      the initial row index (inclusive)
	 * @param rowEnd        the final row index (exclusive)
	 * @param columnIndexes an array of column indexes
	 * @param matrix        the {@link Matrix} to set
	 * <p>
	 * @throws ArrayIndexOutOfBoundsException if the specified submatrix indexes
	 *                                        are out of bounds
	 */
	public void setMatrix(final int rowStart, final int rowEnd, final int[] columnIndexes, final Matrix matrix)
	{
		final int nColumns = columnIndexes.length;
		try
		{
			for (int i = rowStart; i < rowEnd; ++i)
			{
				for (int j = 0; j < nColumns; ++j)
				{
					components[i][columnIndexes[j]] = matrix.get(i - rowStart, j);
				}
			}
		}
		catch (final ArrayIndexOutOfBoundsException ex)
		{
			throw new ArrayIndexOutOfBoundsException("Specified submatrix indexes are out of bounds");
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// OPERATION(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the transpose of {@code this}.
	 * <p>
	 * @return {@code this}'
	 */
	public Matrix transpose()
	{
		final Matrix result = new Matrix(n, m);
		final double[][] resultComponents = result.getComponents();
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				resultComponents[j][i] = components[i][j];
			}
		}
		return result;
	}

	/**
	 * Returns the pivot of {@code this} whose row indexes are
	 * {@code pivotIndexes}.
	 * <p>
	 * @param pivotIndexes an array of row indexes
	 * <p>
	 * @return the pivot of {@code this} whose row indexes are
	 *         {@code pivotIndexes}
	 * <p>
	 * @throws ArrayIndexOutOfBoundsException if the specified pivot indexes are
	 *                                        out of bounds
	 */
	public Matrix pivot(final int[] pivotIndexes)
	{
		return getMatrix(pivotIndexes, 0, n);
	}

	/**
	 * Returns the unpivot of {@code this} whose row indexes are
	 * {@code pivotIndexes}.
	 * <p>
	 * @param pivotIndexes an array of row indexes
	 * <p>
	 * @return the unpivot of {@code this} whose row indexes are
	 *         {@code pivotIndexes}
	 * <p>
	 * @throws ArrayIndexOutOfBoundsException if the specified pivot indexes are
	 *                                        out of bounds
	 */
	public Matrix unpivot(final int[] pivotIndexes)
	{
		final int length = pivotIndexes.length;
		final int[] unpivotIndexes = new int[length];
		for (int i = 0; i < length; ++i)
		{
			unpivotIndexes[pivotIndexes[i]] = i;
		}
		return getMatrix(unpivotIndexes, 0, n);
	}

	/**
	 * Returns the one norm of {@code this}.
	 * <p>
	 * @return the maximum absolute column sum of {@code this}
	 */
	public double norm1()
	{
		double f = 0;
		for (int j = 0; j < n; ++j)
		{
			double s = 0;
			for (int i = 0; i < m; ++i)
			{
				s += Math.abs(components[i][j]);
			}
			f = Math.max(f, s);
		}
		return f;
	}

	/**
	 * Returns the two norm of {@code this}.
	 * <p>
	 * @return the maximum singular value of {@code this}
	 */
	public double norm2()
	{
		return new SingularValueDecomposition(this).norm2();
	}

	/**
	 * Returns the infinity norm of {@code this}.
	 * <p>
	 * @return the maximum absolute row sum of {@code this}
	 */
	public double normInf()
	{
		double f = 0;
		for (int i = 0; i < m; ++i)
		{
			double s = 0;
			for (int j = 0; j < n; ++j)
			{
				s += Math.abs(components[i][j]);
			}
			f = Math.max(f, s);
		}
		return f;
	}

	/**
	 * Returns the Frobenius norm of {@code this}.
	 * <p>
	 * @return the square root of the sum of the squares of all the components
	 */
	public double normF()
	{
		double f = 0;
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				f = Norms.getEuclideanNorm(f, components[i][j]);
			}
		}
		return f;
	}

	/**
	 * Returns the result of unary minus {@code this}.
	 * <p>
	 * @return -{@code this}
	 */
	public Matrix uminus()
	{
		final Matrix matrix = new Matrix(m, n);
		final double[][] resultComponents = matrix.getComponents();
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				resultComponents[i][j] = -components[i][j];
			}
		}
		return matrix;
	}

	/**
	 * Returns the addition of {@code entity} to {@code this}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return {@code this} + {@code entity}
	 */
	public Entity plus(final Entity entity)
	{
		if (entity instanceof Matrix)
		{
			return plus((Matrix) entity);
		}
		else
		{
			throw new IllegalArgumentException("Cannot add a " + entity.getName() + " to a " + name);
		}
	}

	/**
	 * Returns the addition of {@code other} to {@code this}.
	 * <p>
	 * @param other another {@link Matrix}
	 * <p>
	 * @return {@code this} + {@code other}
	 */
	public Matrix plus(final Matrix other)
	{
		checkMatrixDimensions(other);
		final Matrix result = new Matrix(m, n);
		final double[][] resultComponents = result.getComponents();
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				resultComponents[i][j] = components[i][j] + other.components[i][j];
			}
		}
		return result;
	}

	/**
	 * Adds {@code other} to {@code this}.
	 * <p>
	 * @param other another {@link Matrix}
	 * <p>
	 * @return {@code this} += {@code other}
	 */
	public Matrix add(final Matrix other)
	{
		checkMatrixDimensions(other);
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				components[i][j] += other.components[i][j];
			}
		}
		return this;
	}

	/**
	 * Returns the subtraction of {@code entity} from {@code this}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return {@code this} - {@code entity}
	 */
	public Entity minus(final Entity entity)
	{
		if (entity instanceof Matrix)
		{
			return minus((Matrix) entity);
		}
		else
		{
			throw new IllegalArgumentException("Cannot subtract a " + entity.getName() + " from a " + name);
		}
	}

	/**
	 * Returns the subtraction of {@code other} from {@code this}.
	 * <p>
	 * @param other another {@link Matrix}
	 * <p>
	 * @return {@code this} - {@code other}
	 */
	public Matrix minus(final Matrix other)
	{
		checkMatrixDimensions(other);
		final Matrix result = new Matrix(m, n);
		final double[][] resultComponents = result.getComponents();
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				resultComponents[i][j] = components[i][j] - other.components[i][j];
			}
		}
		return result;
	}

	/**
	 * Subtracts {@code other} from {@code this}.
	 * <p>
	 * @param other another {@link Matrix}
	 * <p>
	 * @return {@code this} -= {@code other}
	 */
	public Matrix subtract(final Matrix other)
	{
		checkMatrixDimensions(other);
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				components[i][j] -= other.components[i][j];
			}
		}
		return this;
	}

	/**
	 * Returns the element-by-element multiplication of {@code this} by
	 * {@code other}.
	 * <p>
	 * @param other another {@link Matrix}
	 * <p>
	 * @return {@code this} .* {@code other}
	 */
	public Matrix arrayTimes(final Matrix other)
	{
		checkMatrixDimensions(other);
		final Matrix result = new Matrix(m, n);
		final double[][] resultComponents = result.getComponents();
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				resultComponents[i][j] = components[i][j] * other.components[i][j];
			}
		}
		return result;
	}

	/**
	 * Multiplies {@code this} by {@code other} element-by-element.
	 * <p>
	 * @param other another {@link Matrix}
	 * <p>
	 * @return {@code this} .*= {@code other}
	 */
	public Matrix arrayMultiply(final Matrix other)
	{
		checkMatrixDimensions(other);
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				components[i][j] *= other.components[i][j];
			}
		}
		return this;
	}

	/**
	 * Returns the element-by-element division of {@code this} by {@code other}.
	 * <p>
	 * @param other another {@link Matrix}
	 * <p>
	 * @return {@code this} ./ {@code other}
	 */
	public Matrix arrayRightDivision(final Matrix other)
	{
		checkMatrixDimensions(other);
		final Matrix result = new Matrix(m, n);
		final double[][] resultComponents = result.getComponents();
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				resultComponents[i][j] = components[i][j] / other.components[i][j];
			}
		}
		return result;
	}

	/**
	 * Divides {@code this} by {@code other} element-by-element.
	 * <p>
	 * @param other another {@link Matrix}
	 * <p>
	 * @return {@code this} ./= {@code other}
	 */
	public Matrix arrayRightDivide(final Matrix other)
	{
		checkMatrixDimensions(other);
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				components[i][j] /= other.components[i][j];
			}
		}
		return this;
	}

	/**
	 * Returns the element-by-element division of {@code other} by {@code this}.
	 * <p>
	 * @param other another {@link Matrix}
	 * <p>
	 * @return {@code other} ./ {@code this}
	 */
	public Matrix arrayLeftDivision(final Matrix other)
	{
		checkMatrixDimensions(other);
		final Matrix result = new Matrix(m, n);
		final double[][] resultComponents = result.getComponents();
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				resultComponents[i][j] = other.components[i][j] / components[i][j];
			}
		}
		return result;
	}

	/**
	 * Divides {@code other} by {@code this} element-by-element.
	 * <p>
	 * @param other another {@link Matrix}
	 * <p>
	 * @return {@code this} = {@code other} ./ {@code this}
	 */
	public Matrix arrayLeftDivide(final Matrix other)
	{
		checkMatrixDimensions(other);
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				components[i][j] = other.components[i][j] / components[i][j];
			}
		}
		return this;
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
			return times(((Scalar) entity).getValue());
		}
		else if (entity instanceof Matrix)
		{
			return times((Matrix) entity);
		}
		else
		{
			throw new IllegalArgumentException("Cannot multiply a " + name + " by a " + entity.getName());
		}
	}

	/**
	 * Returns the multiplication of {@code this} by the specified scalar.
	 * <p>
	 * @param scalar the scalar
	 * <p>
	 * @return {@code this} * {@code scalar}
	 */
	public Matrix times(final double scalar)
	{
		final Matrix result = new Matrix(m, n);
		final double[][] resultComponents = result.getComponents();
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				resultComponents[i][j] = components[i][j] * scalar;
			}
		}
		return result;
	}

	/**
	 * Multiplies {@code this} by the specified scalar.
	 * <p>
	 * @param scalar the scalar
	 * <p>
	 * @return {@code this} *= {@code scalar}
	 */
	public Matrix multiply(final double scalar)
	{
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				components[i][j] *= scalar;
			}
		}
		return this;
	}

	/**
	 * Returns the multiplication of {@code this} by the specified
	 * {@link Matrix}.
	 * <p>
	 * @param other another {@link Matrix}
	 * <p>
	 * @return {@code this} * {@code other}
	 * <p>
	 * @throws IllegalArgumentException if the inner dimensions of the matrices
	 *                                  do not agree
	 */
	public Matrix times(final Matrix other)
	{
		if (other.m != n)
		{
			throw new IllegalArgumentException("Inner dimensions of the matrices do not agree");
		}
		final Matrix result = new Matrix(m, other.n);
		final double[][] resultComponents = result.getComponents();
		final double[] col = new double[n];
		for (int j = 0; j < other.n; ++j)
		{
			for (int k = 0; k < n; ++k)
			{
				col[k] = other.components[k][j];
			}
			for (int i = 0; i < m; ++i)
			{
				final double[] row = components[i];
				double sum = 0;
				for (int k = 0; k < n; ++k)
				{
					sum += row[k] * col[k];
				}
				resultComponents[i][j] = sum;
			}
		}
		return result;
	}

	/**
	 * Returns the division of {@code this} by {@code entity}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return {@code this} / {@code entity}
	 */
	public Entity division(final Entity entity)
	{
		if (entity instanceof Scalar)
		{
			return times(1. / ((Scalar) entity).getValue());
		}
		else if (entity instanceof Matrix)
		{
			return arrayRightDivision((Matrix) entity);
		}
		else
		{
			throw new IllegalArgumentException("Cannot divide a " + name + " by a " + entity.getName());
		}
	}

	/**
	 * Returns the value of {@code this} raised to the power of {@code entity}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return {@code this} ^ {@code entity}
	 */
	public Entity power(final Entity entity)
	{
		throw new IllegalArgumentException("Cannot raise a " + name + " to the power of a " + entity.getName());
	}


	////////////////////////////////////////////////////////////////////////////
	// DECOMPOSITIONS
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the LU decomposition of {@code this}.
	 * <p>
	 * @return the LU decomposition of {@code this}
	 * <p>
	 * @see LUDecomposition
	 */
	public LUDecomposition lu()
	{
		return new LUDecomposition(this);
	}

	/**
	 * Returns the QR decomposition of {@code this}.
	 * <p>
	 * @return the QR decomposition of {@code this}
	 * <p>
	 * @see QRDecomposition
	 */
	public QRDecomposition qr()
	{
		return new QRDecomposition(this);
	}

	/**
	 * Returns the Cholesky decomposition of {@code this}.
	 * <p>
	 * @return the Cholesky decomposition of {@code this}
	 * <p>
	 * @see CholeskyDecomposition
	 */
	public CholeskyDecomposition chol()
	{
		return new CholeskyDecomposition(this);
	}

	/**
	 * Returns the singular value decomposition of {@code this}.
	 * <p>
	 * @return the singular value decomposition of {@code this}
	 * <p>
	 * @see SingularValueDecomposition
	 */
	public SingularValueDecomposition svd()
	{
		return new SingularValueDecomposition(this);
	}

	/**
	 * Returns the eigenvalue decomposition of {@code this}.
	 * <p>
	 * @return the eigenvalue decomposition of {@code this}
	 * <p>
	 * @see EigenvalueDecomposition
	 */
	public EigenvalueDecomposition eig()
	{
		return new EigenvalueDecomposition(this);
	}


	////////////////////////////////////////////////////////////////////////////
	// SOLVER
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the solution of {@code this}*X={@code entity}.
	 * <p>
	 * @param entity the entity
	 * <p>
	 * @return the solution of {@code this}*X={@code entity}
	 */
	public Entity solution(final Entity entity)
	{
		if (entity instanceof Matrix)
		{
			return solve((Matrix) entity);
		}
		else
		{
			throw new IllegalArgumentException("Cannot find a solution if A is a " + name + " and B is a " + entity.getName());
		}
	}

	/**
	 * Solves A*X = B.
	 * <p>
	 * @param B the right hand side of the equation
	 * <p>
	 * @return the solution if {@code this} is square, the least squares
	 *         solution otherwise
	 */
	public Matrix solve(final Matrix B)
	{
		return m == n ? new LUDecomposition(this).solve(B) : new QRDecomposition(this).solve(B);
	}

	/**
	 * Solves X*A = B, which is also A'*X' = B'.
	 * <p>
	 * @param B the right hand side of the equation
	 * <p>
	 * @return the solution if {@code this} is square, the least squares
	 *         solution otherwise
	 */
	public Matrix solveTranspose(final Matrix B)
	{
		return transpose().solve(B.transpose());
	}

	/**
	 * Returns the inverse of {@code this} if {@code this} is square, the
	 * pseudoinverse otherwise.
	 * <p>
	 * @return the inverse of {@code this} if {@code this} is square, the
	 *         pseudoinverse otherwise
	 */
	public Matrix inverse()
	{
		return solve(identity(m, m));
	}

	/**
	 * Returns the determinant of {@code this}.
	 * <p>
	 * @return the determinant of {@code this}.
	 */
	public double det()
	{
		return new LUDecomposition(this).det();
	}

	/**
	 * Returns the rank of {@code this}.
	 * <p>
	 * @return the effective numerical rank, obtained from SVD.
	 */
	public int rank()
	{
		return new SingularValueDecomposition(this).rank();
	}

	/**
	 * Returns the condition of {@code this} (2 norm).
	 * <p>
	 * @return the ratio of the largest singular value to the smallest singular
	 *         value.
	 */
	public double cond()
	{
		return new SingularValueDecomposition(this).cond();
	}

	/**
	 * Returns the trace of {@code this}.
	 * <p>
	 * @return the sum of the diagonal components of {@code this}
	 */
	public double trace()
	{
		double t = 0;
		for (int i = 0; i < Math.min(m, n); ++i)
		{
			t += components[i][i];
		}
		return t;
	}


	////////////////////////////////////////////////////////////////////////////
	// GENERATION
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generates a random {@link Matrix} with the specified numbers of rows and
	 * columns.
	 * <p>
	 * @param m the number of rows
	 * @param n the number of columns
	 * <p>
	 * @return a random {@link Matrix} with the specified numbers of rows and
	 *         columns
	 */
	public static Matrix random(final int m, final int n)
	{
		final Matrix A = new Matrix(m, n);
		final double[][] X = A.getComponents();
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				X[i][j] = Math.random();
			}
		}
		return A;
	}

	/**
	 * Generates the identity {@link Matrix} with this numbers of rows and
	 * columns.
	 * <p>
	 * @return the identity {@link Matrix} with this numbers of rows and columns
	 */
	public Matrix identity()
	{
		return identity(m, m);
	}

	/**
	 * Generates the identity {@link Matrix} with the specified number of rows
	 * and columns.
	 * <p>
	 * @param size the number of rows and columns
	 * <p>
	 * @return the identity {@link Matrix} with the specified number of rows and
	 *         columns
	 */
	public static Matrix identity(final int size)
	{
		final Matrix matrix = new Matrix(size, size);
		final double[][] matrixComponents = matrix.getComponents();
		for (int i = 0; i < size; ++i)
		{
			for (int j = 0; j < size; ++j)
			{
				matrixComponents[i][j] = i == j ? 1. : 0.;
			}
		}
		return matrix;
	}

	/**
	 * Generates the identity {@link Matrix} with the specified numbers of rows
	 * and columns.
	 * <p>
	 * @param m the number of rows
	 * @param n the number of columns
	 * <p>
	 * @return the identity {@link Matrix} with the specified numbers of rows
	 *         and columns
	 */
	public static Matrix identity(final int m, final int n)
	{
		final Matrix matrix = new Matrix(m, n);
		final double[][] matrixComponents = matrix.getComponents();
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				matrixComponents[i][j] = i == j ? 1. : 0.;
			}
		}
		return matrix;
	}


	////////////////////////////////////////////////////////////////////////////
	// READ & WRITE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Print {@code this}.
	 */
	public void print()
	{
		print(Formats.MIN_NUMBER_SIZE);
	}

	/**
	 * Print the {@link Matrix} with {@link IOManager}. Line the elements up in
	 * columns and right justify within columns of width characters.
	 * <p>
	 * @param columnWidth the width for each column
	 */
	public void print(final int columnWidth)
	{
		IOManager.printLine(toString(columnWidth, true), false);
	}

	/**
	 * Print the {@link Matrix} to the specified output stream. Line the
	 * elements up in columns and right justify within columns of width
	 * characters.
	 * <p>
	 * @param output      the output stream
	 * @param columnWidth the width for each column
	 */
	public void print(final PrintWriter output, final int columnWidth)
	{
		output.println(toString(columnWidth, true));
	}

	/**
	 * Returns {@code true} if the specified input {@link String} contains the
	 * definition of a {@link Matrix}, {@code false} otherwise.
	 * <p>
	 * @param input the input {@link String}
	 * <p>
	 * @return {@code true} if the specified input {@link String} contains the
	 *         definition of a {@link Matrix}, {@code false} otherwise
	 */
	public static boolean isMatrix(final String input)
	{
		final List<Character> delimiters = new ArrayList<Character>(Arrays.asList('[', ']'));
		final List<Integer> indexes = Strings.getAllIndexes(input.trim(), delimiters);
		if (indexes.size() == 2)
		{
			final int from = indexes.get(0);
			final int to = indexes.get(1);
			if ((from < to) && (input.charAt(from) == delimiters.get(0)) && (input.charAt(to) == delimiters.get(1)))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the {@link Matrix} encoded in the specified input {@link String},
	 * or {@code null} if the the specified input {@link String} does not
	 * contain any {@link Matrix}.
	 * <p>
	 * @param input the input {@link String}
	 * <p>
	 * @return the {@link Matrix} encoded in the specified input {@link String},
	 *         or {@code null} if the the specified input {@link String} does
	 *         not contain any {@link Matrix}
	 */
	public static Matrix parse(final String input)
	{
		try
		{
			final List<Character> delimiters = new ArrayList<Character>(Arrays.asList('[', ']'));
			final List<Integer> indexes = Strings.getAllIndexes(input.trim(), delimiters);
			if (indexes.size() == 2)
			{
				final int from = indexes.get(0);
				final int to = indexes.get(1);
				if ((from < to) && (input.charAt(from) == delimiters.get(0)) && (input.charAt(to) == delimiters.get(1)))
				{
					final String content = input.substring(from + 1, to);
					// Get the rows
					final List<String> rows = Strings.split(content, ';');
					// Count the number of rows
					final int m = rows.size();
					// Count the number of columns
					String row = rows.get(0);
					final Character[] rowSeparators = new Character[2];
					rowSeparators[0] = ' ';
					rowSeparators[1] = ',';
					final int n = Strings.splitInside(row, rowSeparators).size();
					// Create the components of the matrix
					final double[][] components = new double[m][n];
					List<String> rowComponents;
					// Fill the matrix
					for (int i = 0; i < m; ++i)
					{
						// Get the current row
						row = rows.get(i);
						// Get the components of this row
						rowComponents = Strings.split(row, rowSeparators);
						// Store the components
						for (int j = 0; j < n; ++j)
						{
							components[i][j] = Double.valueOf(rowComponents.get(j));
						}
					}
					return new Matrix(m, n, components);
				}
			}
			else
			{
				final int size = indexes.size();
				if (size > 2)
				{
					throw new ParseException("Too many square brackets " + Arguments.expectedButFound(2, size), indexes.get(2));
				}
				else
				{
					throw new ParseException("Not enough square brackets " + Arguments.expectedButFound(2, size), 0);
				}
			}
		}
		catch (final NumberFormatException ex)
		{
			IOManager.printError(ex);
		}
		catch (final ParseException ex)
		{
			IOManager.printError(ex);
		}
		return null;
	}

	/**
	 * Read a {@link Matrix} from a stream. The format is the same as for the
	 * print method, so printed matrices can be read back in (provided they were
	 * printed using US Locale). Elements are separated by whitespace, all the
	 * elements for each row appear on a single line, the last row is followed
	 * by a blank line.
	 * <p>
	 * @param input the input stream
	 * <p>
	 * @return the {@link Matrix} read from the specified input stream
	 * <p>
	 * @throws java.io.IOException if there is a problem with parsing the
	 *                             {@link Matrix}
	 */
	public static Matrix read(final BufferedReader input)
		throws IOException
	{
		final StreamTokenizer tokenizer = new StreamTokenizer(input);
		// Although StreamTokenizer will parse numbers, it doesn't recognize
		// scientific notation (E or D); however, Double.valueOf does.
		// The strategy here is to disable StreamTokenizer's number parsing.
		// We'll only get whitespace delimited words, EOL's and EOF's.
		// These words should all be numbers, for Double.valueOf to parse.
		tokenizer.resetSyntax();
		tokenizer.wordChars(0, 255);
		tokenizer.whitespaceChars(0, ' ');
		tokenizer.eolIsSignificant(true);
		// Scan the first row of the matrix and get the number of columns
		final List<Double> list = new ExtendedList<Double>();
		// - Ignore initial empty lines
		while (tokenizer.nextToken() == StreamTokenizer.TT_EOL)
		{
		}
		if (tokenizer.ttype == StreamTokenizer.TT_EOF)
		{
			throw new IOException("Unexpected EOF on Matrix read");
		}
		// - Scan the first row
		do
		{
			list.add(Double.valueOf(tokenizer.sval));
		}
		while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);
		// - Get the number of columns
		final int n = list.size();
		// - Store the elements of the first row
		double[] row = new double[n];
		for (int j = 0; j < n; ++j)
		{
			row[j] = list.get(j).doubleValue();
		}
		// Store the rows of the matrix
		final List<double[]> rowList = new ExtendedList<double[]>();
		// - Store the first row
		rowList.add(row);
		// - Store the remaining rows of the matrix
		while (tokenizer.nextToken() == StreamTokenizer.TT_WORD)
		{
			// While non-empty lines
			rowList.add(row = new double[n]);
			int j = 0;
			do
			{
				if (j >= n)
				{
					throw new IOException("Row " + rowList.size() + " is too long");
				}
				row[j++] = Double.valueOf(tokenizer.sval).doubleValue();
				IOManager.printLine(Double.valueOf(tokenizer.sval).doubleValue(), false);
			}
			while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);
			if (j < n)
			{
				throw new IOException("Row " + rowList.size() + " is too short");
			}
		}
		// Get the number of rows
		final int m = rowList.size();
		// Copy the rows of the matrix
		final double[][] rows = new double[m][];
		rowList.toArray(rows);
		// Create the matrix with the rows
		return new Matrix(rows);
	}


	////////////////////////////////////////////////////////////////////////////
	// VERIFICATION
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Checks the size of the specified {@link Matrix} (check if size(
	 * {@code this}) == size({@code other})).
	 * <p>
	 * @param other another {@link Matrix}
	 * <p>
	 * @throws IllegalArgumentException if the dimensions of the matrices do not
	 *                                  agree
	 */
	private void checkMatrixDimensions(final Matrix other)
	{
		MatrixArguments.requireSameDimension(this, other);
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
		if (other instanceof Matrix)
		{
			final Matrix matrix = (Matrix) other;
			if ((matrix.getRowDimension() == m) && (matrix.getColumnDimension() == n))
			{
				for (int i = 0; i < m; ++i)
				{
					for (int j = 0; j < n; ++j)
					{
						if (get(i, j) != matrix.get(i, j))
						{
							return false;
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Bits.generateHashCode(Bits.hash(serialVersionUID), Arrays.deepHashCode(components));
	}

	@Override
	public String toString()
	{
		return toString(Formats.MIN_NUMBER_SIZE, false);
	}

	public String toString(final int columnWidth, final boolean multiLines)
	{
		final StringBuilder builder;
		if (!multiLines)
		{
			builder = new StringBuilder(2 + m + (m * n * (Formats.NUMBER_SIZE + 1)));
			builder.append("[");
		}
		else
		{
			builder = new StringBuilder(m + (m * n * (Formats.NUMBER_SIZE + 1)));
		}
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				final String formattedComponent = Formats.format(components[i][j]);
				final int padding = Math.max(1, columnWidth - formattedComponent.length());
				for (int k = 0; k < padding; ++k)
				{
					builder.append(' ');
				}
				builder.append(formattedComponent);
			}
			if (i < (m - 1))
			{
				if (multiLines)
				{
					builder.append("\n");
				}
				else
				{
					builder.append(";");
				}
			}
		}
		if (!multiLines)
		{
			builder.append("]");
		}
		return builder.toString();
	}
}
