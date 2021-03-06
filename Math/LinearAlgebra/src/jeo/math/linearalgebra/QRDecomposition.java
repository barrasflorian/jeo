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
 * QR Decomposition.
 * <p>
 * For an m-by-n matrix A with m >= n, the QR decomposition is an m-by-n
 * orthogonal matrix Q and an n-by-n upper triangular matrix R so that A = Q*R.
 * <p>
 * The QR decompostion always exists, even if the matrix does not have full
 * rank, so the constructor will never fail. The primary use of the QR
 * decomposition is in the least squares solution of nonsquare systems of
 * simultaneous linear equations. This will fail if isFullRank() returns false.
 * <p>
 * @author JAMA, http://math.nist.gov/javanumerics/jama/
 * @version 1.0.3
 */
public class QRDecomposition
	implements Serializable
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = -1664880821965605890L;
	/**
	 * Array for internal storage of decomposition.
	 * <p>
	 * @serial internal array storage
	 */
	private final double[][] QR;
	/**
	 * Array for internal storage of diagonal of {@code R}.
	 * <p>
	 * @serial diagonal of {@code R}
	 */
	private final double[] Rdiag;
	/**
	 * Row and column dimensions.
	 * <p>
	 * @serial column dimension
	 * @serial row dimension
	 */
	private final int m, n;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * QR Decomposition, computed by Householder reflections.
	 * <p>
	 * Note: structure to access {@code R} and the Householder vectors and
	 * computes {@code Q}.
	 * <p>
	 * @param A a rectangular {@link Matrix}
	 */
	public QRDecomposition(final Matrix A)
	{
		// Initialize
		QR = A.getComponentsCopy();
		m = A.getRowDimension();
		n = A.getColumnDimension();
		Rdiag = new double[n];
		// Main loop
		for (int k = 0; k < n; k++)
		{
			// Compute 2-norm of k-th column without under/overflow
			double nrm = 0;
			for (int i = k; i < m; i++)
			{
				nrm = Norms.getEuclideanNorm(nrm, QR[i][k]);
			}
			if (nrm != 0.)
			{
				// Form k-th Householder vector
				if (QR[k][k] < 0)
				{
					nrm = -nrm;
				}
				for (int i = k; i < m; i++)
				{
					QR[i][k] /= nrm;
				}
				QR[k][k] += 1.;
				// Apply transformation to remaining columns
				for (int j = k + 1; j < n; j++)
				{
					double s = 0.;
					for (int i = k; i < m; i++)
					{
						s += QR[i][k] * QR[i][j];
					}
					s = -s / QR[k][k];
					for (int i = k; i < m; i++)
					{
						QR[i][j] += s * QR[i][k];
					}
				}
			}
			Rdiag[k] = -nrm;
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Is {@code A} full rank?
	 * <p>
	 * @return {@code true} if {@code R} (and hence {@code A}) has full rank
	 */
	public boolean isFullRank()
	{
		for (int j = 0; j < n; j++)
		{
			if (Rdiag[j] == 0)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the Householder vectors.
	 * <p>
	 * @return the lower trapezoidal matrix whose columns define the reflections
	 */
	public Matrix getH()
	{
		final Matrix X = new Matrix(m, n);
		final double[][] H = X.getComponents();
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				if (i >= j)
				{
					H[i][j] = QR[i][j];
				}
				else
				{
					H[i][j] = 0.;
				}
			}
		}
		return X;
	}

	/**
	 * Return the upper triangular factor {@code R}.
	 * <p>
	 * @return the upper triangular factor {@code R}
	 */
	public Matrix getR()
	{
		final Matrix X = new Matrix(n, n);
		final double[][] R = X.getComponents();
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < n; j++)
			{
				if (i < j)
				{
					R[i][j] = QR[i][j];
				}
				else if (i == j)
				{
					R[i][j] = Rdiag[i];
				}
				else
				{
					R[i][j] = 0.;
				}
			}
		}
		return X;
	}

	/**
	 * Generates and returns the (economy-sized) orthogonal factor {@code Q}.
	 * <p>
	 * @return the (economy-sized) orthogonal factor {@code Q}
	 */
	public Matrix getQ()
	{
		final Matrix X = new Matrix(m, n);
		final double[][] Q = X.getComponents();
		for (int k = n - 1; k >= 0; --k)
		{
			for (int i = 0; i < m; i++)
			{
				Q[i][k] = 0.;
			}
			Q[k][k] = 1.;
			for (int j = k; j < n; j++)
			{
				if (QR[k][k] != 0)
				{
					double s = 0.;
					for (int i = k; i < m; i++)
					{
						s += QR[i][k] * Q[i][j];
					}
					s = -s / QR[k][k];
					for (int i = k; i < m; i++)
					{
						Q[i][j] += s * QR[i][k];
					}
				}
			}
		}
		return X;
	}


	////////////////////////////////////////////////////////////////////////////
	// SOLVER
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the least squares solution of {@code A*X = B}.
	 * <p>
	 * @param B a {@link Matrix} with as many rows as {@code A} and any number
	 *          of columns
	 * <p>
	 * @return {@code X} that minimizes the two norm of {@code Q*R*X-B}
	 * <p>
	 * @throws IllegalArgumentException if the matrix row dimensions do not
	 *                                  agree
	 * @throws RuntimeException         if {@code A} is rank deficient
	 */
	public Matrix solve(final Matrix B)
	{
		MatrixArguments.requireSameRowDimension(m, B.getRowDimension());
		if (!isFullRank())
		{
			throw new RuntimeException("This matrix is rank deficient");
		}
		// Copy right hand side
		final int nx = B.getColumnDimension();
		final double[][] X = B.getComponentsCopy();
		// Compute Y = Q'*B
		for (int k = 0; k < n; k++)
		{
			for (int j = 0; j < nx; j++)
			{
				double s = 0.;
				for (int i = k; i < m; i++)
				{
					s += QR[i][k] * X[i][j];
				}
				s = -s / QR[k][k];
				for (int i = k; i < m; i++)
				{
					X[i][j] += s * QR[i][k];
				}
			}
		}
		// Solve R*X = Y
		for (int k = n - 1; k >= 0; --k)
		{
			for (int j = 0; j < nx; j++)
			{
				X[k][j] /= Rdiag[k];
			}
			for (int i = 0; i < k; i++)
			{
				for (int j = 0; j < nx; j++)
				{
					X[i][j] -= X[k][j] * QR[i][k];
				}
			}
		}
		return new Matrix(n, nx, X).getMatrix(0, n, 0, nx);
	}
}
