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
package jeo.common.io;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import jeo.common.structure.ExtendedList;
import jeo.common.structure.Table;
import jeo.common.util.Arguments;
import jeo.common.util.Strings;

public class FileParser
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private FileParser()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// CSV
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Parses the CSV file denoted by the specified path.
	 * <p>
	 * @param filePath the path of the file to be parsed
	 * <p>
	 * @return the content of the CSV file
	 */
	public static Table<String> parseCSV(final String filePath)
	{
		return parseFile(filePath, new ExtendedList<Character>(Arrays.asList(',', ';', '\t')), String.class);
	}

	/**
	 * Parses the CSV file denoted by the specified path.
	 * <p>
	 * @param <T>      the type of the result table
	 * @param filePath the path of the file to be parsed
	 * @param c        the class of the result table
	 * <p>
	 * @return the content of the CSV file
	 */
	public static <T> Table<T> parseCSV(final String filePath, final Class<T> c)
	{
		return parseFile(filePath, new ExtendedList<Character>(Arrays.asList(',', ';', '\t')), c);
	}


	////////////////////////////////////////////////////////////////////////////
	// FILE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Parses the file denoted by the specified path and returns a table
	 * containing its content.
	 * <p>
	 * @param <T>        the type of the result table
	 * @param filePath   the path of the file to be parsed
	 * @param separators the list of separators to be used
	 * @param c          the class of the result table
	 * <p>
	 * @return a table containing the content of the file
	 */
	public static <T> Table<T> parseFile(final String filePath, final List<Character> separators, final Class<T> c)
		throws ClassCastException
	{
		// Initialize the result
		Table<T> result = null;
		// Get the number of separators
		final int separatorsNumber = separators.size();
		// Get the content of the file
		final FileContent fileContent = FileHandler.read(filePath);
		final Scanner scanner = new Scanner(fileContent.getContent());
		// Parse the file and store the content in result
		if (scanner.hasNextLine())
		{
			// Look for the separator (if the file contains different separators, take the first one in the list)
			int occurrencesNumber, columnsNumber = 0, separatorIndex = -1;
			String currentLine = scanner.nextLine();
			boolean severalSeparators = false;
			for (int i = 0; i < separatorsNumber; ++i)
			{
				occurrencesNumber = Strings.getAllIndexes(currentLine, separators.get(i)).size();
				if (occurrencesNumber > 0)
				{
					if (columnsNumber == 0)
					{
						columnsNumber = occurrencesNumber;
						separatorIndex = i;
					}
					else
					{
						severalSeparators = true;
						break;
					}
				}
			}
			++columnsNumber;
			final String separator = String.valueOf(separators.get(separatorIndex));
			if (severalSeparators)
			{
				IOManager.printWarning("The file contains different separators ('" + separator + "' will be used)");
			}
			// Scan the file line per line and store the values in result
			result = new Table<T>(c, fileContent.getLinesNumber(), columnsNumber);
			int lineNumber = 0;
			result.setRow(lineNumber, currentLine.split(separator));
			++lineNumber;
			String[] values;
			while (scanner.hasNextLine())
			{
				currentLine = scanner.nextLine();
				values = currentLine.split(separator);
				if ((values == null) || (values.length == 0) || (values[0] == null) || Strings.EMPTY.equals(values[0]))
				{
					IOManager.printWarning("At line " + lineNumber + ", there is no element " + Arguments.expectedButFound(columnsNumber, 0));
				}
				else if (values.length < columnsNumber)
				{
					IOManager.printError("At line " + lineNumber + ", there are not enough elements " + Arguments.expectedButFound(columnsNumber, values.length));
				}
				else
				{
					if (values.length > columnsNumber)
					{
						IOManager.printWarning("At line " + lineNumber + ", there are too many elements " + Arguments.expectedButFound(columnsNumber, values.length));
					}
					result.setRow(lineNumber, values);
					++lineNumber;
				}
			}
			// Close the scanner
			scanner.close();
			// Resize the table if there are any blank rows or blank columns
			result.resize();
		}
		return result;
	}
}
