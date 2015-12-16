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

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jeo.common.structure.ExtendedList;

public class Strings
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTANT(S)
	////////////////////////////////////////////////////////////////////////////

	public static final int DEFAULT_INITIAL_CAPACITY = 256;
	public static final String EMPTY = "";


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private Strings()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// GENERATION(S)
	////////////////////////////////////////////////////////////////////////////

	public static StringBuilder createBuffer()
	{
		return new StringBuilder(DEFAULT_INITIAL_CAPACITY);
	}

	public static String repeat(final String string, final int repeat)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNegative(repeat);
		// Initialize
		final StringBuilder buffer = new StringBuilder(repeat);
		// Process
		for (long i = 0; i < repeat; i++)
		{
			buffer.append(string);
		}
		return String.valueOf(buffer);
	}

	/**
	 * Returns a {@link String} line with the default length.
	 * <p>
	 * @return a {@link String} line with the default length
	 */
	public static String generateLine()
	{
		return generateLine(Formats.DEFAULT_LINE_LENGTH);
	}

	/**
	 * Returns a {@link String} line with the specified length.
	 * <p>
	 * @param length the length of the line to be generated
	 * <p>
	 * @return a {@link String} line with the specified length
	 */
	public static String generateLine(final int length)
	{
		return Strings.repeat("-", length);
	}


	////////////////////////////////////////////////////////////////////////////
	// SEARCHES
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the index of the first character of {@code string} that is in
	 * {@code characters}, or {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the array of characters to be searched
	 * <p>
	 * @return the index of the first character of {@code string} that is in
	 *         {@code characters}, or {@code -1} if there is no such occurrence
	 */
	public static int searchFirstCharacter(final String string, final Character[] characters)
	{
		return searchFirstCharacter(string, characters, 0);
	}

	/**
	 * Returns the index of the first character of {@code string} that is in
	 * {@code characters}, or {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the {@link Collection} of characters to be searched
	 * <p>
	 * @return the index of the first character of {@code string} that is in
	 *         {@code characters}, or {@code -1} if there is no such occurrence
	 */
	public static int searchFirstCharacter(final String string, final Collection<Character> characters)
	{
		return searchFirstCharacter(string, characters, 0);
	}

	/**
	 * Returns the index of the first character of {@code string} that is in
	 * {@code characters}, searching forward from {@code fromIndex}, or
	 * {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the array of characters to be searched
	 * @param fromIndex  the index to start the search from (inclusive)
	 * <p>
	 * @return the index of the first character of {@code string} that is in
	 *         {@code characters}, searching forward from {@code fromIndex}, or
	 *         {@code -1} if there is no such occurrence
	 */
	public static int searchFirstCharacter(final String string, final Character[] characters, final int fromIndex)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(characters);
		final int length = string.length();
		Arguments.requireNonNegative(fromIndex);
		Arguments.requireLessOrEqualTo(fromIndex, length);
		// Process
		for (int index = fromIndex; index < length; ++index)
		{
			if (contains(characters, string.charAt(index)))
			{
				return index;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the first character of {@code string} that is in
	 * {@code characters}, searching forward from {@code fromIndex}, or
	 * {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the {@link Collection} of characters to be searched
	 * @param fromIndex  the index to start the search from (inclusive)
	 * <p>
	 * @return the index of the first character of {@code string} that is in
	 *         {@code characters}, searching forward from {@code fromIndex}, or
	 *         {@code -1} if there is no such occurrence
	 */
	public static int searchFirstCharacter(final String string, final Collection<Character> characters, final int fromIndex)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(characters);
		final int length = string.length();
		Arguments.requireNonNegative(fromIndex);
		Arguments.requireLessOrEqualTo(fromIndex, length);
		// Process
		for (int index = fromIndex; index < length; ++index)
		{
			if (characters.contains(string.charAt(index)))
			{
				return index;
			}
		}
		return -1;
	}


	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the index of the last character of {@code string} that is in
	 * {@code characters}, or {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the array of characters to be searched
	 * <p>
	 * @return the index of the last character of {@code string} that is in
	 *         {@code characters}, or {@code -1} if there is no such occurrence
	 */
	public static int searchLastCharacter(final String string, final Character[] characters)
	{
		return searchLastCharacter(string, characters, string.length());
	}

	/**
	 * Returns the index of the last character of {@code string} that is in
	 * {@code characters}, or {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the {@link Collection} of characters to be searched
	 * <p>
	 * @return the index of the last character of {@code string} that is in
	 *         {@code characters}, or {@code -1} if there is no such occurrence
	 */
	public static int searchLastCharacter(final String string, final Collection<Character> characters)
	{
		return searchLastCharacter(string, characters, string.length());
	}

	/**
	 * Returns the index of the last character of {@code string} that is in
	 * {@code characters}, searching backward from {@code toIndex}, or
	 * {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the array of characters to be searched
	 * @param toIndex    the index to finish the search at (exclusive)
	 * <p>
	 * @return the index of the last character of {@code string} that is in
	 *         {@code characters}, searching backward from {@code toIndex}, or
	 *         {@code -1} if there is no such occurrence
	 */
	public static int searchLastCharacter(final String string, final Character[] characters, final int toIndex)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(characters);
		final int length = string.length();
		Arguments.requireNonNegative(toIndex);
		Arguments.requireLessOrEqualTo(toIndex, length);
		// Process
		for (int index = toIndex - 1; index >= 0; --index)
		{
			if (contains(characters, string.charAt(index)))
			{
				return index;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the last character of {@code string} that is in
	 * {@code characters}, searching backward from {@code toIndex}, or
	 * {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the {@link Collection} of characters to be searched
	 * @param toIndex    the index to finish the search at (exclusive)
	 * <p>
	 * @return the index of the last character of {@code string} that is in
	 *         {@code characters}, searching backward from {@code toIndex}, or
	 *         {@code -1} if there is no such occurrence
	 */
	public static int searchLastCharacter(final String string, final Collection<Character> characters, final int toIndex)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(characters);
		final int length = string.length();
		Arguments.requireNonNegative(toIndex);
		Arguments.requireLessOrEqualTo(toIndex, length);
		// Process
		for (int index = toIndex - 1; index >= 0; --index)
		{
			if (characters.contains(string.charAt(index)))
			{
				return index;
			}
		}
		return -1;
	}


	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the index of the first character of {@code string} that is not
	 * equal to {@code character}, or {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string    the input {@link String}
	 * @param character the character to be searched
	 * <p>
	 * @return the index of the first character of {@code string} that is not
	 *         equal to {@code character}, or {@code -1} if there is no such
	 *         occurrence
	 */
	public static int searchFirstCharacterNotEqualTo(final String string, final Character character)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(character);
		final int length = string.length();
		// Process
		for (int index = 0; index < length; ++index)
		{
			if (string.charAt(index) != character)
			{
				return index;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the first character of {@code string} that is not in
	 * {@code characters}, or {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the array of characters to be searched
	 * <p>
	 * @return the index of the first character of {@code string} that is not in
	 *         {@code characters}, or {@code -1} if there is no such occurrence
	 */
	public static int searchFirstCharacterNotIn(final String string, final Character[] characters)
	{
		return searchFirstCharacterNotIn(string, characters, 0);
	}

	/**
	 * Returns the index of the first character of {@code string} that is not in
	 * {@code characters}, or {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the {@link Collection} of characters to be searched
	 * <p>
	 * @return the index of the first character of {@code string} that is not in
	 *         {@code characters}, or {@code -1} if there is no such occurrence
	 */
	public static int searchFirstCharacterNotIn(final String string, final Collection<Character> characters)
	{
		return searchFirstCharacterNotIn(string, characters, 0);
	}

	/**
	 * Returns the index of the first character of {@code string} that is not in
	 * {@code characters}, searching forward from {@code fromIndex}, or
	 * {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the array of characters to be searched
	 * @param fromIndex  the index to start the search from (inclusive)
	 * <p>
	 * @return the index of the first character of {@code string} that is not in
	 *         {@code characters}, searching forward from {@code fromIndex}, or
	 *         {@code -1} if there is no such occurrence
	 */
	public static int searchFirstCharacterNotIn(final String string, final Character[] characters, final int fromIndex)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(characters);
		final int length = string.length();
		Arguments.requireNonNegative(fromIndex);
		Arguments.requireLessOrEqualTo(fromIndex, length);
		// Process
		for (int index = fromIndex; index < length; ++index)
		{
			if (!contains(characters, string.charAt(index)))
			{
				return index;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the first character of {@code string} that is not in
	 * {@code characters}, searching forward from {@code fromIndex}, or
	 * {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the {@link Collection} of characters to be searched
	 * @param fromIndex  the index to start the search from (inclusive)
	 * <p>
	 * @return the index of the first character of {@code string} that is not in
	 *         {@code characters}, searching forward from {@code fromIndex}, or
	 *         {@code -1} if there is no such occurrence
	 */
	public static int searchFirstCharacterNotIn(final String string, final Collection<Character> characters, final int fromIndex)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(characters);
		final int length = string.length();
		Arguments.requireNonNegative(fromIndex);
		Arguments.requireLessOrEqualTo(fromIndex, length);
		// Process
		for (int index = fromIndex; index < length; ++index)
		{
			if (!characters.contains(string.charAt(index)))
			{
				return index;
			}
		}
		return -1;
	}


	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the index of the last character of {@code string} that is not
	 * equal to {@code character}, or {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string    the input {@link String}
	 * @param character the character to be searched
	 * <p>
	 * @return the index of the last character of {@code string} that is not
	 *         equal to {@code character}, or {@code -1} if there is no such
	 *         occurrence
	 */
	public static int searchLastCharacterNotEqualTo(final String string, final Character character)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(character);
		final int length = string.length();
		// Process
		for (int index = length - 1; index >= 0; --index)
		{
			if (string.charAt(index) != character)
			{
				return index;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the last character of {@code string} that is not in
	 * {@code characters}, or {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the array of characters to be searched
	 * <p>
	 * @return the index of the last character of {@code string} that is not in
	 *         {@code characters}, or {@code -1} if there is no such occurrence
	 */
	public static int searchLastCharacterNotIn(final String string, final Character[] characters)
	{
		return searchLastCharacterNotIn(string, characters, string.length());
	}

	/**
	 * Returns the index of the last character of {@code string} that is not in
	 * {@code characters}, or {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the {@link Collection} of characters to be searched
	 * <p>
	 * @return the index of the last character of {@code string} that is not in
	 *         {@code characters}, or {@code -1} if there is no such occurrence
	 */
	public static int searchLastCharacterNotIn(final String string, final Collection<Character> characters)
	{
		return searchLastCharacterNotIn(string, characters, string.length());
	}

	/**
	 * Returns the index of the last character of {@code string} that is not in
	 * {@code characters}, searching backward from {@code toIndex}, or
	 * {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the array of characters to be searched
	 * @param toIndex    the index to finish the search at (exclusive)
	 * <p>
	 * @return the index of the last character of {@code string} that is not in
	 *         {@code characters}, searching backward from {@code toIndex}, or
	 *         {@code -1} if there is no such occurrence
	 */
	public static int searchLastCharacterNotIn(final String string, final Character[] characters, final int toIndex)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(characters);
		final int length = string.length();
		Arguments.requireNonNegative(toIndex);
		Arguments.requireLessOrEqualTo(toIndex, length);
		// Process
		for (int index = toIndex - 1; index >= 0; --index)
		{
			if (!contains(characters, string.charAt(index)))
			{
				return index;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the last character of {@code string} that is not in
	 * {@code characters}, searching backward from {@code toIndex}, or
	 * {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the {@link Collection} of characters to be searched
	 * @param toIndex    the index to finish the search at (exclusive)
	 * <p>
	 * @return the index of the last character of {@code string} that is not in
	 *         {@code characters}, searching backward from {@code toIndex}, or
	 *         {@code -1} if there is no such occurrence
	 */
	public static int searchLastCharacterNotIn(final String string, final Collection<Character> characters, final int toIndex)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(characters);
		final int length = string.length();
		Arguments.requireNonNegative(toIndex);
		Arguments.requireLessOrEqualTo(toIndex, length);
		// Process
		for (int index = toIndex - 1; index >= 0; --index)
		{
			if (!characters.contains(string.charAt(index)))
			{
				return index;
			}
		}
		return -1;
	}


	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns all the indexes of {@code character} in {@code string}.
	 * <p>
	 * @param string    the input {@link String}
	 * @param character the {@link Character} to be searched
	 * <p>
	 * @return all the indexes of {@code character} in {@code string}
	 */
	public static List<Integer> getAllIndexes(final String string, final Character character)
	{
		return getIndexesFrom(string, character, 0);
	}

	/**
	 * Returns all the indexes of {@code characters} in {@code string}.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the array of characters to be searched
	 * <p>
	 * @return all the indexes of {@code characters} in {@code string}
	 */
	public static List<Integer> getAllIndexes(final String string, final Character[] characters)
	{
		return getIndexesFrom(string, characters, 0);
	}

	/**
	 * Returns all the indexes of {@code characters} in {@code string}.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the {@link Collection} of characters to be searched
	 * <p>
	 * @return all the indexes of {@code characters} in {@code string}
	 */
	public static List<Integer> getAllIndexes(final String string, final Collection<Character> characters)
	{
		return getIndexesFrom(string, characters, 0);
	}


	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the indexes of {@code character} in {@code string}, searching
	 * forward from {@code fromIndex}, or {@code -1} if there is no such
	 * occurrence.
	 * <p>
	 * @param string    the input {@link String}
	 * @param character the character to be searched
	 * @param fromIndex the index to start the search from (inclusive)
	 * <p>
	 * @return the indexes of {@code character} in {@code string}, searching
	 *         forward from {@code fromIndex}, or {@code -1} if there is no such
	 *         occurrence
	 */
	public static List<Integer> getIndexesFrom(final String string, final Character character, final int fromIndex)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(character);
		final int length = string.length();
		Arguments.requireNonNegative(fromIndex);
		Arguments.requireLessOrEqualTo(fromIndex, length);
		// Initialize
		final List<Integer> indexes = new ExtendedList<Integer>();
		int index = string.indexOf(character);
		// Process
		while (index >= 0)
		{
			if (index >= fromIndex)
			{
				indexes.add(index);
			}
			index = string.indexOf(character, index + 1);
		}
		return indexes;
	}

	/**
	 * Returns the indexes of the characters of {@code string} that are in
	 * {@code characters}, searching forward from {@code fromIndex}, or
	 * {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the array of characters to be searched
	 * @param fromIndex  the index to start the search from (inclusive)
	 * <p>
	 * @return the indexes of the characters of {@code string} that are in
	 *         {@code characters}, searching forward from {@code fromIndex}, or
	 *         {@code -1} if there is no such occurrence
	 */
	public static List<Integer> getIndexesFrom(final String string, final Character[] characters, final int fromIndex)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(characters);
		final int length = string.length();
		Arguments.requireNonNegative(fromIndex);
		Arguments.requireLessOrEqualTo(fromIndex, length);
		// Initialize
		final List<Integer> indexes = new ExtendedList<Integer>();
		final char[] array = string.toCharArray();
		// Process
		for (int i = fromIndex; i < length; ++i)
		{
			if (contains(characters, array[i]))
			{
				indexes.add(i);
			}
		}
		return indexes;
	}

	/**
	 * Returns the indexes of the characters of {@code string} that are in
	 * {@code characters}, searching forward from {@code fromIndex}, or
	 * {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the {@link Collection} of characters to be searched
	 * @param fromIndex  the index to start the search from (inclusive)
	 * <p>
	 * @return the indexes of the characters of {@code string} that are in
	 *         {@code characters}, searching forward from {@code fromIndex}, or
	 *         {@code -1} if there is no such occurrence
	 */
	public static List<Integer> getIndexesFrom(final String string, final Collection<Character> characters, final int fromIndex)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(characters);
		final int length = string.length();
		Arguments.requireNonNegative(fromIndex);
		Arguments.requireLessOrEqualTo(fromIndex, length);
		// Initialize
		final List<Integer> indexes = new ExtendedList<Integer>();
		final char[] array = string.toCharArray();
		// Process
		for (int i = fromIndex; i < length; ++i)
		{
			if (characters.contains(array[i]))
			{
				indexes.add(i);
			}
		}
		return indexes;
	}


	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the indexes of {@code character} in {@code string}, searching
	 * forward to {@code toIndex}, or {@code -1} if there is no such occurrence.
	 * <p>
	 * @param string    the input {@link String}
	 * @param character the character to be searched
	 * @param toIndex   the index to finish the search at (exclusive)
	 * <p>
	 * @return the indexes of {@code character} in {@code string}, searching
	 *         forward to {@code toIndex}, or {@code -1} if there is no such
	 *         occurrence
	 */
	public static List<Integer> getIndexesTo(final String string, final Character character, final int toIndex)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(character);
		final int length = string.length();
		Arguments.requireNonNegative(toIndex);
		Arguments.requireLessOrEqualTo(toIndex, length);
		// Initialize
		final List<Integer> indexes = new ExtendedList<Integer>();
		int index = string.indexOf(character);
		// Process
		while (index < toIndex)
		{
			indexes.add(index);
			index = string.indexOf(character, index + 1);
		}
		return indexes;
	}

	/**
	 * Returns the indexes of the characters of {@code string} that are in
	 * {@code characters}, searching forward to {@code toIndex}, or {@code -1}
	 * if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the array of characters to be searched
	 * @param toIndex    the index to finish the search at (exclusive)
	 * <p>
	 * @return the indexes of the characters of {@code string} that are in
	 *         {@code characters}, searching forward to {@code toIndex}, or
	 *         {@code -1} if there is no such occurrence
	 */
	public static List<Integer> getIndexesTo(final String string, final Character[] characters, final int toIndex)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(characters);
		final int length = string.length();
		Arguments.requireNonNegative(toIndex);
		Arguments.requireLessOrEqualTo(toIndex, length);
		// Initialize
		final List<Integer> indexes = new ExtendedList<Integer>();
		final char[] array = string.toCharArray();
		// Process
		for (int index = 0; index < toIndex; ++index)
		{
			if (contains(characters, array[index]))
			{
				indexes.add(index);
			}
		}
		return indexes;
	}

	/**
	 * Returns the indexes of the characters of {@code string} that are in
	 * {@code characters}, searching forward to {@code toIndex}, or {@code -1}
	 * if there is no such occurrence.
	 * <p>
	 * @param string     the input {@link String}
	 * @param characters the {@link Collection} of characters to be searched
	 * @param toIndex    the index to finish the search at (exclusive)
	 * <p>
	 * @return the indexes of the characters of {@code string} that are in
	 *         {@code characters}, searching forward to {@code toIndex}, or
	 *         {@code -1} if there is no such occurrence
	 */
	public static List<Integer> getIndexesTo(final String string, final Collection<Character> characters, final int toIndex)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(characters);
		// Initialize
		final List<Integer> indexes = new ExtendedList<Integer>();
		final char[] array = string.toCharArray();
		final int length = string.length();
		Arguments.requireNonNegative(toIndex);
		Arguments.requireLessOrEqualTo(toIndex, length);
		// Process
		for (int index = 0; index < toIndex; ++index)
		{
			if (characters.contains(array[index]))
			{
				indexes.add(index);
			}
		}
		return indexes;
	}


	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the {@link List} of {@link String} computed by splitting
	 * {@code string} around {@code delimitingIndexes}.
	 * <p>
	 * @param string            the input {@link String}
	 * @param delimitingIndexes an array of delimiting indexes
	 * <p>
	 * @return the {@link List} of {@link String} computed by splitting
	 *         {@code string} around {@code delimitingIndexes}
	 */
	public static List<String> getTokens(final String string, final Integer[] delimitingIndexes)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(delimitingIndexes);
		// Initialize
		final List<String> tokens = new ExtendedList<String>();
		int lastIndex = 0;
		// Process
		for (final Integer index : delimitingIndexes)
		{
			if (lastIndex < index)
			{
				tokens.add(string.substring(lastIndex, index));
			}
			lastIndex = index + 1;
		}
		tokens.add(string.substring(lastIndex));
		return tokens;
	}

	/**
	 * Returns the {@link List} of {@link String} computed by splitting
	 * {@code string} around {@code delimitingIndexes}.
	 * <p>
	 * @param string            the input {@link String}
	 * @param delimitingIndexes the {@link Collection} of delimiting indexes
	 * <p>
	 * @return the {@link List} of {@link String} computed by splitting
	 *         {@code string} around {@code delimitingIndexes}
	 */
	public static List<String> getTokens(final String string, final Collection<Integer> delimitingIndexes)
	{
		// Check the argument(s)
		Arguments.requireNonNull(string);
		Arguments.requireNonNull(delimitingIndexes);
		// Initialize
		final List<String> tokens = new ExtendedList<String>();
		int lastIndex = 0;
		// Process
		for (final Integer index : delimitingIndexes)
		{
			if (lastIndex < index)
			{
				tokens.add(string.substring(lastIndex, index));
			}
			lastIndex = index + 1;
		}
		tokens.add(string.substring(lastIndex));
		return tokens;
	}


	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns {@code true} if {@code characters} contains {@code character}.
	 * <p>
	 * @param characters the input array of {@link Character}
	 * @param character  the character to be searched
	 * <p>
	 * @return {@code true} if {@code characters} contains {@code character}
	 */
	public static boolean contains(final Character[] characters, final Character character)
	{
		for (final Character c : characters)
		{
			if (character.equals(c))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns {@code true} if {@code string} is not {@code null} and empty,
	 * {@code false} otherwise.
	 * <p>
	 * @param string the input {@link String}
	 * <p>
	 * @return {@code true} if {@code string} is not {@code null} and empty,
	 *         {@code false} otherwise
	 */
	public static boolean isEmpty(final String string)
	{
		return (string != null) && (string.length() == 0);
	}

	/**
	 * Returns {@code true} if {@code string} is not {@code null} and not empty,
	 * {@code false} otherwise.
	 * <p>
	 * @param string the input {@link String}
	 * <p>
	 * @return {@code true} if {@code string} is not {@code null} and not empty,
	 *         {@code false} otherwise
	 */
	public static boolean isNotEmpty(final String string)
	{
		return (string != null) && (string.length() != 0);
	}


	////////////////////////////////////////////////////////////////////////////
	// OPERATION(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the {@link List} of {@link String} computed by splitting
	 * {@code string} around {@code delimitingCharacter}.
	 * <p>
	 * @param string              the input {@link String}
	 * @param delimitingCharacter the delimiting character
	 * <p>
	 * @return the {@link List} of {@link String} computed by splitting
	 *         {@code string} around {@code delimitingCharacter}
	 */
	public static List<String> split(final String string, final Character delimitingCharacter)
	{
		return getTokens(string, getAllIndexes(string, delimitingCharacter));
	}

	/**
	 * Returns the {@link List} of {@link String} computed by splitting
	 * {@code string} around {@code delimitingCharacter} (inside).
	 * <p>
	 * @param string              the input {@link String}
	 * @param delimitingCharacter the delimiting character
	 * <p>
	 * @return the {@link List} of {@link String} computed by splitting
	 *         {@code string} around {@code delimitingCharacter} (inside)
	 */
	public static List<String> splitInside(final String string, final Character delimitingCharacter)
	{
		return splitTo(string, delimitingCharacter, searchLastCharacterNotEqualTo(string, delimitingCharacter));
	}

	/**
	 * Returns the {@link List} of {@link String} computed by splitting
	 * {@code string} around {@code delimitingCharacter}.
	 * <p>
	 * @param string              the input {@link String}
	 * @param delimitingCharacter the delimiting character
	 * @param toIndex             the index to finish the search at (exclusive)
	 * <p>
	 * @return the {@link List} of {@link String} computed by splitting
	 *         {@code string} around {@code delimitingCharacter}
	 */
	public static List<String> splitTo(final String string, final Character delimitingCharacter, final int toIndex)
	{
		return getTokens(string, getIndexesTo(string, delimitingCharacter, toIndex));
	}


	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the {@link List} of {@link String} computed by splitting
	 * {@code string} around {@code delimitingCharacters}.
	 * <p>
	 * @param string               the input {@link String}
	 * @param delimitingCharacters the array of delimiting characters
	 * <p>
	 * @return the {@link List} of {@link String} computed by splitting
	 *         {@code string} around {@code delimitingCharacters}
	 */
	public static List<String> split(final String string, final Character[] delimitingCharacters)
	{
		return getTokens(string, getAllIndexes(string, delimitingCharacters));
	}

	/**
	 * Returns the {@link List} of {@link String} computed by splitting
	 * {@code string} around {@code delimitingCharacters} (inside).
	 * <p>
	 * @param string               the input {@link String}
	 * @param delimitingCharacters the array of delimiting characters
	 * <p>
	 * @return the {@link List} of {@link String} computed by splitting
	 *         {@code string} around {@code delimitingCharacters} (inside)
	 */
	public static List<String> splitInside(final String string, final Character[] delimitingCharacters)
	{
		return splitTo(string, delimitingCharacters, searchLastCharacterNotIn(string, delimitingCharacters));
	}

	/**
	 * Returns the {@link List} of {@link String} computed by splitting
	 * {@code string} around {@code delimitingCharacters} to {@code toIndex}.
	 * <p>
	 * @param string               the input {@link String}
	 * @param delimitingCharacters the array of delimiting characters
	 * @param toIndex              the index to finish the search at (exclusive)
	 * <p>
	 * @return the {@link List} of {@link String} computed by splitting
	 *         {@code string} around {@code delimitingCharacters} to
	 *         {@code toIndex}
	 */
	public static List<String> splitTo(final String string, final Character[] delimitingCharacters, final int toIndex)
	{
		return getTokens(string, getIndexesTo(string, delimitingCharacters, toIndex));
	}


	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the {@link List} of {@link String} computed by splitting
	 * {@code string} around {@code delimitingCharacters}.
	 * <p>
	 * @param string               the input {@link String}
	 * @param delimitingCharacters the {@link Collection} of delimiting
	 *                             characters
	 * <p>
	 * @return the {@link List} of {@link String} computed by splitting
	 *         {@code string} around {@code delimitingCharacters}
	 */
	public static List<String> split(final String string, final Collection<Character> delimitingCharacters)
	{
		return getTokens(string, getAllIndexes(string, delimitingCharacters));
	}

	/**
	 * Returns the {@link List} of {@link String} computed by splitting
	 * {@code string} around {@code delimitingCharacters} (inside).
	 * <p>
	 * @param string               the input {@link String}
	 * @param delimitingCharacters the {@link Collection} of delimiting
	 *                             characters
	 * <p>
	 * @return the {@link List} of {@link String} computed by splitting
	 *         {@code string} around {@code delimitingCharacters} (inside)
	 */
	public static List<String> splitInside(final String string, final Collection<Character> delimitingCharacters)
	{
		return splitTo(string, delimitingCharacters, searchLastCharacterNotIn(string, delimitingCharacters));
	}

	/**
	 * Returns the {@link List} of {@link String} computed by splitting
	 * {@code string} around {@code delimitingCharacters} to {@code toIndex}.
	 * <p>
	 * @param string               the input {@link String}
	 * @param delimitingCharacters the {@link Collection} of delimiting
	 *                             characters
	 * @param toIndex              the index to finish the search at (exclusive)
	 * <p>
	 * @return the {@link List} of {@link String} computed by splitting
	 *         {@code string} around {@code delimitingCharacters} to
	 *         {@code toIndex}
	 */
	public static List<String> splitTo(final String string, final Collection<Character> delimitingCharacters, final int toIndex)
	{
		return getTokens(string, getIndexesTo(string, delimitingCharacters, toIndex));
	}


	////////////////////////////////////////////////////////////////////////////
	// CONVERSION(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns {@code true} if {@code string} is numeric, {@code false}
	 * otherwise.
	 * <p>
	 * @param string a {@link String}
	 * <p>
	 * @return {@code true} if {@code string} is numeric, {@code false}
	 *         otherwise
	 */
	public static boolean isNumeric(final String string)
	{
		final NumberFormat formatter = NumberFormat.getInstance();
		final ParsePosition pos = new ParsePosition(0);
		formatter.parse(string, pos);
		return string.length() == pos.getIndex();
	}

	public static String toLowerCase(final String string)
	{
		return string.toLowerCase(Formats.DEFAULT_LOCALE);
	}

	public static String toUpperCase(final String string)
	{
		return string.toUpperCase(Formats.DEFAULT_LOCALE);
	}

	/**
	 * Returns the result of calling {@code toString} on {@code o} if {@code o}
	 * is not {@code null}, or {@code "null"} otherwise.
	 * <p>
	 * @param object an object
	 * <p>
	 * @return the result of calling {@code toString} on {@code o} if {@code o}
	 *         is not {@code null}, or {@code "null"} otherwise
	 */
	public static String toString(final Object object)
	{
		return String.valueOf(object);
	}

	/**
	 * Returns the result of calling {@code toString} on {@code o} if {@code o}
	 * is not {@code null}, or {@code nullDefault} otherwise.
	 * <p>
	 * @param object      an object
	 * @param nullDefault string to return if {@code o} is null
	 * <p>
	 * @return the result of calling {@code toString} on {@code o} if {@code o}
	 *         is not {@code null}, or {@code nullDefault} otherwise
	 */
	public static String toString(final Object object, final String nullDefault)
	{
		return object != null ? String.valueOf(object) : nullDefault;
	}

	/**
	 * Returns a {@link String} representation of {@code array}.
	 * <p>
	 * @param array an array of {@link Object}
	 * <p>
	 * @return a {@link String} representation of {@code array}
	 */
	public static String arrayToString(final Object[] array)
	{
		return "(" + arrayToString(array, ", ") + ")";
	}

	/**
	 * Returns a {@link String} representation of {@code array} using
	 * {@code separator}.
	 * <p>
	 * @param array     an array of {@link Object}
	 * @param separator a {@link String} separator
	 * <p>
	 * @return a {@link String} representation of {@code array}
	 */
	public static String arrayToString(final Object[] array, final String separator)
	{
		// Check the argument(s)
		Arguments.requireNonNull(array);
		// Initialize
		final int length = array.length;
		int i = 0;
		final StringBuilder buffer = createBuffer();
		// Process
		if (length > 0)
		{
			buffer.append(array[i]);
			++i;
			while (i < length)
			{
				buffer.append(separator).append(array[i]);
				++i;
			}
		}
		return String.valueOf(buffer);
	}

	/**
	 * Returns a {@link String} representation of {@code array}.
	 * <p>
	 * @param array an array of {@code double}
	 * <p>
	 * @return a {@link String} representation of {@code array}
	 */
	public static String doubleArrayToString(final double[] array)
	{
		return collectionToString(Doubles.arrayToExtendedList(array));
	}

	/**
	 * Returns a {@link String} representation of {@code array}.
	 * <p>
	 * @param array an array of {@code int}
	 * <p>
	 * @return a {@link String} representation of {@code array}
	 */
	public static String intArrayToString(final int[] array)
	{
		return collectionToString(Integers.arrayToExtendedList(array));
	}

	/**
	 * Returns a {@link String} representation of {@code collection}.
	 * <p>
	 * @param <T>        the type of the {@link Collection}
	 * @param collection a {@link Collection} of type {@code T}
	 * <p>
	 * @return a {@link String} representation of {@code collection}
	 */
	public static <T> String collectionToString(final Collection<T> collection)
	{
		return "(" + collectionToString(collection, ", ") + ")";
	}

	/**
	 * Returns a {@link String} representation of {@code collection} using
	 * {@code separator}.
	 * <p>
	 * @param <T>        the type of the {@link Collection}
	 * @param collection a {@link Collection}
	 * @param separator  a {@link String} separator
	 * <p>
	 * @return a {@link String} representation of {@code collection}
	 */
	public static <T> String collectionToString(final Collection<T> collection, final String separator)
	{
		// Check the argument(s)
		Arguments.requireNonNull(collection);
		// Initialize
		final StringBuilder buffer = createBuffer();
		final Iterator<T> iterator = collection.iterator();
		// Process
		if (iterator.hasNext())
		{
			buffer.append(iterator.next());
			while (iterator.hasNext())
			{
				buffer.append(separator).append(iterator.next());
			}
		}
		return String.valueOf(buffer);
	}
}
