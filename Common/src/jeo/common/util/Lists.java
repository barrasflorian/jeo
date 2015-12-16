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
package jeo.common.util;

import java.util.List;

import jeo.common.math.Numbers;
import jeo.common.structure.ExtendedList;

public class Lists
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private Lists()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// LISTS
	////////////////////////////////////////////////////////////////////////////

	public static <T extends Number> List<T> getMaxElements(final List<T> a, final List<T> b)
	{
		// Check the argument(s)
		Arguments.<List<T>>requireSameSize(a, b);
		// Get the size
		final int size = a.size();
		// For each index, get the max number
		final List<T> maxElements = new ExtendedList<T>(size);
		for (int i = 0; i < size; ++i)
		{
			maxElements.add(Numbers.<T>getMax(a.get(i), b.get(i)));
		}
		return maxElements;
	}

	public static <T extends Number> List<T> getMeans(final List<T> a, final List<T> b)
	{
		// Check the argument(s)
		Arguments.<List<T>>requireSameSize(a, b);
		// Get the size
		final int size = a.size();
		// For each index, get the max number
		final List<T> maxElements = new ExtendedList<T>(size);
		for (int i = 0; i < size; ++i)
		{
			maxElements.add(Numbers.<T>getMax(a.get(i), b.get(i)));
		}
		return maxElements;
	}
}
