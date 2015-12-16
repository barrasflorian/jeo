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

import java.util.Collection;

import jeo.common.structure.ExtendedList;

public class Doubles
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private Doubles()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// GENERATION(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns a {@code double} array of size {@code size} containing the
	 * sequence of real numbers starting with zero and spaced by one.
	 * <p>
	 * @param size the size of the array to be generated
	 * <p>
	 * @return a {@code double} array of size {@code size} containing the
	 *         sequence of real numbers starting with zero and spaced by one
	 */
	public static double[] generateSequence(final int size)
	{
		return generateSequence(size, 0.);
	}

	/**
	 * Returns a {@code double} array of size {@code size} containing the
	 * sequence of real numbers starting with {@code start} and spaced by one.
	 * <p>
	 * @param size  the size of the array to be generated
	 * @param start the first value of the array to be generated
	 * <p>
	 * @return a {@code double} array of size {@code size} containing the
	 *         sequence of real numbers starting with {@code start} and spaced
	 *         by one
	 */
	public static double[] generateSequence(final int size, final double start)
	{
		return generateSequence(size, start, 1.);
	}

	/**
	 * Returns a {@code double} array of size {@code size} containing the
	 * sequence of real numbers starting with {@code start} and spaced by
	 * {@code step}.
	 * <p>
	 * @param size  the size of the array to be generated
	 * @param start the first value of the array to be generated
	 * @param step  the interval between the values of the array to be generated
	 * <p>
	 * @return a {@code double} array of size {@code size} containing the
	 *         sequence of real numbers starting with {@code start} and spaced
	 *         by {@code step}
	 */
	public static double[] generateSequence(final int size, final double start, final double step)
	{
		final double[] array = new double[size];
		double value = start;
		for (int i = 0; i < size; ++i, value += step)
		{
			array[i] = value;
		}
		return array;
	}


	////////////////////////////////////////////////////////////////////////////
	// CONVERSION(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns a {@link Double} array from the specified {@code double} array.
	 * <p>
	 * @param array an array of {@code double}
	 * <p>
	 * @return a {@link Double} array from the specified {@code double} array
	 */
	public static Double[] arrayToObjectArray(final double[] array)
	{
		final int size = array.length;
		final Double[] objectArray = new Double[size];
		for (int i = 0; i < size; ++i)
		{
			objectArray[i] = array[i];
		}
		return objectArray;
	}

	/**
	 * Returns a {@code double} array from the specified {@link Double} array.
	 * <p>
	 * @param objectArray an array of {@link Double}
	 * <p>
	 * @return a {@code double} array from the specified {@link Double} array
	 */
	public static double[] objectArrayToArray(final Double[] objectArray)
	{
		final int size = objectArray.length;
		final double[] array = new double[size];
		for (int i = 0; i < size; ++i)
		{
			array[i] = objectArray[i];
		}
		return array;
	}

	/**
	 * Returns a {@link Double} array list from the specified {@code double}
	 * array.
	 * <p>
	 * @param array an array of {@code double}
	 * <p>
	 * @return a {@link Double} array list from the specified {@code double}
	 *         array
	 */
	public static ExtendedList<Double> arrayToExtendedList(final double[] array)
	{
		final ExtendedList<Double> arrayList = new ExtendedList<Double>();
		for (final double element : array)
		{
			arrayList.add(element);
		}
		return arrayList;
	}

	/**
	 * Returns a {@code double} array from the specified {@link Number}
	 * collection.
	 * <p>
	 * @param <T>        the type of {@link Number}
	 * @param collection a collection of {@link Number}
	 * <p>
	 * @return a {@code double} array from the specified {@link Number}
	 *         collection
	 */
	public static <T extends Number> double[] collectionToArray(final Collection<T> collection)
	{
		final int size = collection.size();
		final double[] array = new double[size];
		int i = 0;
		for (final T element : collection)
		{
			array[i] = element.doubleValue();
			++i;
		}
		return array;
	}
}
