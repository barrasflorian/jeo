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

import jeo.common.util.Arguments;

public class MatrixArguments
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private MatrixArguments()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// MATRIX ARGUMENTS
	////////////////////////////////////////////////////////////////////////////

	public static void requireSameInnerDimension(final int aColumnDimension, final int bRowDimension)
	{
		if (aColumnDimension != bRowDimension)
		{
			throw new IllegalArgumentException("Specified matrix has wrong row dimension +" + Arguments.expectedButFound(aColumnDimension, bRowDimension));
		}
	}

	public static void requireSameRowDimension(final int aRowDimension, final int bRowDimension)
	{
		if (aRowDimension != bRowDimension)
		{
			throw new IllegalArgumentException("Specified matrix has wrong row dimension " + Arguments.expectedButFound(aRowDimension, bRowDimension));
		}
	}

	public static void requireSameColumnDimension(final int aColumnDimension, final int bColumnDimension)
	{
		if (aColumnDimension != bColumnDimension)
		{
			throw new IllegalArgumentException("Specified matrix has wrong column dimension " + Arguments.expectedButFound(aColumnDimension, bColumnDimension));
		}
	}

	public static void requireSameDimension(final Matrix A, final Matrix B)
	{
		Arguments.requireNonNull(A);
		Arguments.requireNonNull(B);
		if ((A.getRowDimension() != B.getRowDimension()) || (A.getColumnDimension() != B.getColumnDimension()))
		{
			throw new IllegalArgumentException("Specified matrix has wrong dimension " + Arguments.expectedButFound(A.getDimension(), B.getDimension()));
		}
	}
}
