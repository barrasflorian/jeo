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
 * Cholesky Decomposition.
 * <p>
 * For a symmetric, positive definite matrix A, the Cholesky decomposition is an
 * lower triangular matrix L so that A = L*L'.
 * <p>
 * If the matrix is not symmetric or positive definite, the constructor returns
 * a partial decomposition and sets an internal flag that may be queried by the
 * isSPD() method.
 * <p>
 * @author JAMA, http://math.nist.gov/javanumerics/jama/
 * @version 1.0.3
 */
public class CholeskyDecomposition
	implements Serializable
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 5777013572034989644L;
	/**
	 * Row and column dimension (square matrix).
	 * <p>
	 * @serial matrix dimension
	 */
	private final int n;
	/**
	 * Array for internal storage of decomposition.
	 * <p>
	 * @serial internal array storage
	 */
	private final double[][] L;
	/**
	 * Symmetric and positive definite flag.
	 * <p>
	 * @serial is symmetric and positive definite flag
	 */
	private boolean isspd;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructs the Cholesky decomposition for the symmetric and positive
	 * definite matrix {@code A}.
	 * <p>
	 * Note: structure to access {@code L} and {@code isspd}.
	 * <p>
	 * @param A a symmetric and positive definite {@link Matrix}
	 */
	public CholeskyDecomposition(final Matrix A)
	{
		// Initialize
		final double[][] components = A.getComponents();
		n = A.getRowDimension();
		isspd = A.getColumnDimension() == n;
		L = new double[n][n];
		// Main loop
		for (int j = 0; j < n; ++j)
		{
			final double[] Lrowj = L[j];
			double d = 0.;
			for (int k = 0; k < j; ++k)
			{
				final double[] Lrowk = L[k];
				double s = 0.;
				for (int i = 0; i < k; ++i)
				{
					s += Lrowk[i] * Lrowj[i];
				}
				Lrowj[k] = s = (components[j][k] - s) / L[k][k];
				d += s * s;
				isspd &= components[k][j] == components[j][k];
			}
			d = components[j][j] - d;
			isspd &= d > 0.;
			L[j][j] = Math.sqrt(Math.max(d, 0.));
			for (int k = j + 1; k < n; ++k)
			{
				L[j][k] = 0.;
			}
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Is {@code A} symmetric and positive definite?
	 * <p>
	 * @return {@code true} if {@code A} is symmetric and positive definite,
	 *         {@code false} otherwise
	 */
	public boolean isSPD()
	{
		return isspd;
	}

	/**
	 * Returns the triangular factor {@code L}.
	 * <p>
	 * @return the triangular factor {@code L}
	 */
	public Matrix getL()
	{
		return new Matrix(n, n, L);
	}


	////////////////////////////////////////////////////////////////////////////
	// SOLVER
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Solves {@code A*X = B}.
	 * <p>
	 * @param B a {@link Matrix} with as many rows as {@code A} and any number
	 *          of columns
	 * <p>
	 * @return {@code X} so that {@code L*L'*X = B}
	 * <p>
	 * @throws IllegalArgumentException if the inner dimensions do not agree
	 * @throws RuntimeException         if {@code A} is not symmetric positive
	 *                                  definite
	 */
	public Matrix solve(final Matrix B)
	{
		MatrixArguments.requireSameInnerDimension(n, B.getRowDimension());
		if (!isspd)
		{
			throw new RuntimeException("This matrix is not symmetric positive definite");
		}
		// Copy right hand side
		final double[][] X = B.getComponentsCopy();
		final int nx = B.getColumnDimension();
		// Solve L*Y = B
		for (int k = 0; k < n; ++k)
		{
			for (int j = 0; j < nx; ++j)
			{
				for (int i = 0; i < k; ++i)
				{
					X[k][j] -= X[i][j] * L[k][i];
				}
				X[k][j] /= L[k][k];
			}
		}
		// Solve L'*X = Y
		for (int k = n - 1; k >= 0; --k)
		{
			for (int j = 0; j < nx; ++j)
			{
				for (int i = k + 1; i < n; ++i)
				{
					X[k][j] -= X[i][j] * L[i][k];
				}
				X[k][j] /= L[k][k];
			}
		}
		return new Matrix(n, nx, X);
	}
}
