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

public class Integers
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private Integers()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// GENERATION(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns an {@code int} array of size {@code size} containing the sequence
	 * of integers starting with zero and spaced by one.
	 * <p>
	 * @param size the size of the array to be generated
	 * <p>
	 * @return an {@code int} array of size {@code size} containing the sequence
	 *         of integers starting with zero and spaced by one
	 */
	public static int[] generateSequence(final int size)
	{
		return generateSequence(size, 0, 1);
	}

	/**
	 * Returns an {@code int} array of size {@code size} containing the sequence
	 * of integers starting with {@code start} and spaced by one.
	 * <p>
	 * @param size  the size of the array to be generated
	 * @param start the first value of the array to be generated
	 * <p>
	 * @return an {@code int} array of size {@code size} containing the sequence
	 *         of integers starting with {@code start} and spaced by one
	 */
	public static int[] generateSequence(final int size, final int start)
	{
		return generateSequence(size, start, 1);
	}

	/**
	 * Returns an {@code int} array of size {@code size} containing the sequence
	 * of integers starting with {@code start} and spaced by {@code step}.
	 * <p>
	 * @param size  the size of the array to be generated
	 * @param start the first value of the array to be generated
	 * @param step  the interval between the values of the array to be generated
	 * <p>
	 * @return an {@code int} array of size {@code size} containing the sequence
	 *         of integers starting with {@code start} and spaced by
	 *         {@code step}
	 */
	public static int[] generateSequence(final int size, final int start, final int step)
	{
		final int[] array = new int[size];
		int value = start;
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
	 * Returns an {@link Integer} array from the specified {@code int} array.
	 * <p>
	 * @param array an array of {@code int}
	 * <p>
	 * @return an {@link Integer} array from the specified {@code int} array
	 */
	public static Integer[] arrayToObjectArray(final int[] array)
	{
		final int size = array.length;
		final Integer[] objectArray = new Integer[size];
		for (int i = 0; i < size; ++i)
		{
			objectArray[i] = array[i];
		}
		return objectArray;
	}

	/**
	 * Returns an {@code int} array from the specified {@link Integer} array.
	 * <p>
	 * @param objectArray an array of {@link Integer}
	 * <p>
	 * @return an {@code int} array from the specified {@link Integer} array
	 */
	public static int[] objectArrayToArray(final Integer[] objectArray)
	{
		final int size = objectArray.length;
		final int[] array = new int[size];
		for (int i = 0; i < size; ++i)
		{
			array[i] = objectArray[i];
		}
		return array;
	}

	/**
	 * Returns an {@link Integer} array list from the specified {@code int}
	 * array.
	 * <p>
	 * @param array an array of {@code int}
	 * <p>
	 * @return an {@link Integer} array list from the specified {@code int}
	 *         array
	 */
	public static ExtendedList<Integer> arrayToExtendedList(final int[] array)
	{
		final ExtendedList<Integer> arrayList = new ExtendedList<Integer>();
		for (final int element : array)
		{
			arrayList.add(element);
		}
		return arrayList;
	}

	/**
	 * Returns an {@code int} array from the specified {@link Number}
	 * collection.
	 * <p>
	 * @param <T>        the type of {@link Number}
	 * @param collection a collection of {@link Number}
	 * <p>
	 * @return an {@code int} array from the specified {@link Number} collection
	 */
	public static <T extends Number> int[] collectionToArray(final Collection<T> collection)
	{
		final int size = collection.size();
		final int[] array = new int[size];
		int i = 0;
		for (final T element : collection)
		{
			array[i] = element.intValue();
			++i;
		}
		return array;
	}
}
