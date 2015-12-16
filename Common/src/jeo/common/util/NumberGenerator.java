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

import java.util.Random;

public class NumberGenerator
{
	private final Random random;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public NumberGenerator()
	{
		random = new Random();
	}


	////////////////////////////////////////////////////////////////////////////
	// NUMBER GENERATOR
	////////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * @return the next pseudorandom, uniformly distributed {@code int} value
	 *         from this random number generator's sequence
	 */
	public int generateRandomInt()
	{
		return random.nextInt();
	}

	/**
	 *
	 * @param lowerBound the lower bound of the integer to be generated
	 * @param upperBound the upper bound of the integer to be generated
	 * <p>
	 * @return the next pseudorandom, uniformly distributed {@code int} value
	 *         between {@code lowerBound} (inclusive) and {@code upperBound}
	 *         (exclusive) from this random number generator's sequence
	 */
	public int generateRandomInt(final int lowerBound, final int upperBound)
	{
		return lowerBound + random.nextInt(upperBound - lowerBound);
	}

	/**
	 *
	 * @param lowerBound the lower bound of the integer to be generated
	 * @param upperBound the upper bound of the integer to be generated
	 * <p>
	 * @return the next pseudorandom, uniformly distributed {@code double} value
	 *         between {@code lowerBound} (inclusive) and {@code upperBound}
	 *         (exclusive) from this random number generator's sequence
	 */
	public double generateRandomDouble(final double lowerBound, final double upperBound)
	{
		return lowerBound + ((upperBound - lowerBound) * random.nextDouble());
	}
}
