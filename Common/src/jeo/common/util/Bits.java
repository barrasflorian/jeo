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

public class Bits
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTANT(S)
	////////////////////////////////////////////////////////////////////////////

	public static final int INTEGER_BITS_NUMBER = 32;
	public static final int HALF_INTEGER_BITS_NUMBER = INTEGER_BITS_NUMBER / 2;
	public static final long LONG_BITS_NUMBER = 64L;
	public static final long HALF_LONG_BITS_NUMBER = LONG_BITS_NUMBER / 2L;
	public static final NumberGenerator NUMBER_GENERATOR = new NumberGenerator();


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private Bits()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// BITS
	////////////////////////////////////////////////////////////////////////////

	public static long rotateLeft(final long bits)
	{
		return rotateLeft(bits, HALF_LONG_BITS_NUMBER);
	}

	public static long rotateLeft(final long bits, final long k)
	{
		return (bits >>> k) | (bits << (LONG_BITS_NUMBER - k));
	}

	public static int rotateLeft(final int bits)
	{
		return rotateLeft(bits, HALF_INTEGER_BITS_NUMBER);
	}

	public static int rotateLeft(final int bits, final int k)
	{
		return (bits >>> k) | (bits << (INTEGER_BITS_NUMBER - k));
	}

	public static long rotateRight(final long bits)
	{
		return rotateRight(bits, HALF_LONG_BITS_NUMBER);
	}

	public static long rotateRight(final long bits, final long k)
	{
		return (bits << k) | (bits >>> (LONG_BITS_NUMBER - k));
	}

	public static int rotateRight(final int bits)
	{
		return rotateRight(bits, HALF_INTEGER_BITS_NUMBER);
	}

	public static int rotateRight(final int bits, final int k)
	{
		return (bits << k) | (bits >>> (INTEGER_BITS_NUMBER - k));
	}


	////////////////////////////////////////////////////////////////////////////
	// HASH CODE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns a hash code from the specified integers.
	 * <p>
	 * @param a an {@code int} value
	 * @param b an {@code int} value
	 * <p>
	 * @return a hash code from the specified integers
	 */
	public static int generateHashCode(final int a, final int b)
	{
		return Bits.rotateLeft(a) ^ b;
	}

	/**
	 * Returns a hash code from the specified integers.
	 * <p>
	 * @param a an {@code int} value
	 * @param b an {@code int} value
	 * @param c an {@code int} value
	 * <p>
	 * @return a hash code from the specified integers
	 */
	public static int generateHashCode(final int a, final int b, final int c)
	{
		return Bits.rotateRight(generateHashCode(a, b)) ^ c;
	}

	public static <T> int hash(final T object)
	{
		return object == null ? NUMBER_GENERATOR.generateRandomInt() : object.hashCode();
	}
}
