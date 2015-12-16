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

/**
 * LU Decomposition.
 * <p>
 * For an m-by-n matrix A with m >= n, the LU decomposition is an m-by-n unit
 * lower triangular matrix L, an n-by-n upper triangular matrix U, and a
 * permutation vector piv of length m so that A(piv,:) = L*U. If m < n, then L
 * is m-by-m and U is m-by-n. <p>
 * The LU decompostion with pivoting always exists, even if the matrix is
 * singular, so the constructor will never fail. The primary use of the LU
 * decomposition is in the solution of square systems of simultaneous linear
 * equations. This will fail if isNonsingular() returns false.
 * <p>
 * @author JAMA, http://math.nist.gov/javanumerics/jama/
 * @version 1.0.3
 */
public class LUDecomposition
	implements Serializable
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 6634944314433288332L;
	/**
	 * Array for internal storage of decomposition.
	 * <p>
	 * @serial internal array storage
	 */
	private final double[][] LU;
	/**
	 * Internal storage of pivot vector.
	 * <p>
	 * @serial pivot vector
	 */
	private final int[] pivot;
	/**
	 * Row and column dimensions.
	 * <p>
	 * @serial column dimension
	 * @serial row dimension
	 */
	private final int m, n;
	/**
	 * Pivot sign.
	 * <p>
	 * @serial pivot sign
	 */
	private int pivotSign;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructs the LU Decomposition.
	 * <p>
	 * Note: structure to access {@code L}, {@code U} and {@code pivot}.
	 * <p>
	 * @param A a rectangular {@link Matrix}
	 */
	public LUDecomposition(final Matrix A)
	{
		// Use a "left-looking", dot-product, Crout/Doolittle algorithm
		LU = A.getComponentsCopy();
		m = A.getRowDimension();
		n = A.getColumnDimension();
		pivotSign = 1;
		pivot = new int[m];
		for (int i = 0; i < m; ++i)
		{
			pivot[i] = i;
		}
		double[] row;
		final double[] col = new double[m];
		// Outer loop
		for (int j = 0; j < n; ++j)
		{
			// Make a copy of the j-th column to localize references
			for (int i = 0; i < m; ++i)
			{
				col[i] = LU[i][j];
			}
			// Apply previous transformations
			for (int i = 0; i < m; ++i)
			{
				row = LU[i];
				// Most of the time is spent in the following dot product
				final int kmax = Math.min(i, j);
				double s = 0.;
				for (int k = 0; k < kmax; ++k)
				{
					s += row[k] * col[k];
				}
				row[j] = col[i] -= s;
			}
			// Find pivot and exchange if necessary
			int p = j;
			for (int i = j + 1; i < m; ++i)
			{
				if (Math.abs(col[i]) > Math.abs(col[p]))
				{
					p = i;
				}
			}
			if (p != j)
			{
				for (int k = 0; k < n; ++k)
				{
					final double t = LU[p][k];
					LU[p][k] = LU[j][k];
					LU[j][k] = t;
				}
				final int k = pivot[p];
				pivot[p] = pivot[j];
				pivot[j] = k;
				pivotSign = -pivotSign;
			}
			// Compute multipliers
			if ((j < m) & (LU[j][j] != 0.))
			{
				for (int i = j + 1; i < m; ++i)
				{
					LU[i][j] /= LU[j][j];
				}
			}
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Is {@code A} nonsingular?
	 * <p>
	 * @return {@code true} if U (and hence {@code A}) is nonsingular,
	 *         {@code false} otherwise
	 */
	public boolean isNonsingular()
	{
		for (int j = 0; j < n; ++j)
		{
			if (LU[j][j] == 0)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the lower triangular factor {@code L}.
	 * <p>
	 * @return the lower triangular factor {@code L}
	 */
	public Matrix getL()
	{
		final Matrix X = new Matrix(m, n);
		final double[][] L = X.getComponents();
		for (int i = 0; i < m; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				if (i > j)
				{
					L[i][j] = LU[i][j];
				}
				else if (i == j)
				{
					L[i][j] = 1.;
				}
				else
				{
					L[i][j] = 0.;
				}
			}
		}
		return X;
	}

	/**
	 * Returns the unpivoted lower triangular factor.
	 * <p>
	 * @return the unpivoted lower triangular factor
	 */
	public Matrix getUnpivotedL()
	{
		return getL().unpivot(getPivot());
	}

	/**
	 * Returns the upper triangular factor {@code U}.
	 * <p>
	 * @return the upper triangular factor {@code U}
	 */
	public Matrix getU()
	{
		final Matrix X = new Matrix(n, n);
		final double[][] U = X.getComponents();
		for (int i = 0; i < n; ++i)
		{
			for (int j = 0; j < n; ++j)
			{
				if (i <= j)
				{
					U[i][j] = LU[i][j];
				}
				else
				{
					U[i][j] = 0.;
				}
			}
		}
		return X;
	}

	/**
	 * Returns the pivot permutation vector {@code pivot}.
	 * <p>
	 * @return the pivot permutation vector {@code pivot}
	 */
	public int[] getPivot()
	{
		final int[] p = new int[m];
		System.arraycopy(pivot, 0, p, 0, m);
		return p;
	}

	/**
	 * Returns the pivot permutation vector as a one-dimensional double array.
	 * <p>
	 * @return the pivot permutation vector as a one-dimensional double array
	 */
	public double[] getDoublePivot()
	{
		final double[] vals = new double[m];
		for (int i = 0; i < m; ++i)
		{
			vals[i] = pivot[i];
		}
		return vals;
	}

	/**
	 * Returns the determinant of {@code A}.
	 * <p>
	 * @return the determinant of {@code A}
	 * <p>
	 * @throws IllegalArgumentException if {@code A} is not square
	 */
	public double det()
	{
		if (m != n)
		{
			throw new IllegalArgumentException("This matrix is not square");
		}
		double d = pivotSign;
		for (int j = 0; j < n; ++j)
		{
			d *= LU[j][j];
		}
		return d;
	}


	////////////////////////////////////////////////////////////////////////////
	// SOLVER
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the solution of A*X = B.
	 * <p>
	 * @param B a {@link Matrix} with as many rows as {@code A} and any number
	 *          of columns
	 * <p>
	 * @return {@code X} so that {@code L*U*X = B(pivot,:)}
	 * <p>
	 * @throws IllegalArgumentException if the matrix row dimensions do not
	 *                                  agree
	 * @throws RuntimeException         if {@code A} is singular
	 */
	public Matrix solve(final Matrix B)
	{
		MatrixArguments.requireSameRowDimension(m, B.getRowDimension());
		if (!isNonsingular())
		{
			throw new RuntimeException("This matrix is singular");
		}
		// Copy right hand side with pivoting
		final int nx = B.getColumnDimension();
		final Matrix X = B.getMatrix(pivot, 0, nx);
		final double[][] xComponents = X.getComponents();
		// Solve L*Y = B(pivot,:)
		for (int k = 0; k < n; ++k)
		{
			for (int i = k + 1; i < n; ++i)
			{
				for (int j = 0; j < nx; ++j)
				{
					xComponents[i][j] -= xComponents[k][j] * LU[i][k];
				}
			}
		}
		// Solve U*X = Y
		for (int k = n - 1; k >= 0; --k)
		{
			for (int j = 0; j < nx; ++j)
			{
				xComponents[k][j] /= LU[k][k];
			}
			for (int i = 0; i < k; ++i)
			{
				for (int j = 0; j < nx; ++j)
				{
					xComponents[i][j] -= xComponents[k][j] * LU[i][k];
				}
			}
		}
		return X;
	}
}
