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

import jeo.common.io.Messages.IOType;
import jeo.common.io.Messages.SeverityLevel;
import jeo.common.util.Arguments;
import jeo.common.util.Formats;
import jeo.common.util.Strings;

public class IOManager
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * The flag for using the debug mode.
	 */
	public static boolean DEBUG_MODE = false;
	/**
	 * The flag for using the logs.
	 */
	public static boolean USE_LOGS = true;
	public static final int EXIT_SUCCESS = 0;
	public static final int EXIT_FAILURE = 1;
	public static final ConsoleHandler consoleHandler = new ConsoleHandler();
	public static final LogsHandler logsHandler = new LogsHandler();
	/**
	 * The maximum number of points in the progress bar.
	 */
	private static final int maxBarPoints = Formats.DEFAULT_LINE_LENGTH - 2;
	/**
	 * The number of points in the progress bar.
	 */
	private static int barPointsNumber = 0;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private IOManager()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// PRINT
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Prints the specified object in the console and writes it in the log.
	 * <p>
	 * @param object the {@link Object} to be printed
	 * @param error  the output type
	 */
	public static void print(final Object object, final boolean error)
	{
		consoleHandler.print(object, error);
		if (USE_LOGS)
		{
			logsHandler.print(object, error);
		}
	}

	/**
	 * Prints the specified object in the console and writes it in the log.
	 * <p>
	 * @param object the {@link Object} to be printed
	 * @param error  the output type
	 */
	public static void printLine(final Object object, final boolean error)
	{
		consoleHandler.printLine(object, error);
		if (USE_LOGS)
		{
			logsHandler.printLine(object, error);
		}
	}

	/**
	 * Prints a line in the console and writes it in the log.
	 */
	public static void printLine()
	{
		printLine(Strings.generateLine(), false);
	}

	/**
	 * Prints the indication of an input line.
	 */
	public static void printInput()
	{
		consoleHandler.print(Messages.createInputMessage(), false);
	}

	/**
	 * Gets the input line using the console handler and writes it in the log
	 * indicating the I/O type {@link IOType.INPUT}.
	 * <p>
	 * @return the input {@link String}
	 */
	public static String getInputLine()
	{
		final Message message = Messages.createInputMessage(consoleHandler.getInputLine());
		if (USE_LOGS)
		{
			logsHandler.printLine(message);
		}
		return message.getContent();
	}

	/**
	 * Prints the specified object in the console and writes it in the log
	 * indicating the severity level {@link SeverityLevel.RESULT}.
	 * <p>
	 * @param object the {@link Object} to be printed
	 * <p>
	 * @return a {@link Message} containing the specified object
	 */
	public static Message printResult(final Object object)
	{
		final Message message = Messages.createOutputMessage(SeverityLevel.RESULT, object);
		consoleHandler.printLine(message);
		if (USE_LOGS)
		{
			logsHandler.printLine(message);
		}
		return message;
	}

	/**
	 * Prints the specified object in the console and writes it in the log
	 * indicating the severity level {@link SeverityLevel.INFO}.
	 * <p>
	 * @param object the {@link Object} to be printed
	 * <p>
	 * @return a {@link Message} containing the specified object
	 */
	public static Message printInfo(final Object object)
	{
		final Message message = Messages.createOutputMessage(SeverityLevel.INFO, object);
		consoleHandler.printLine(message);
		if (USE_LOGS)
		{
			logsHandler.printLine(message);
		}
		return message;
	}

	/**
	 * Prints the specified object in the console and writes it in the log
	 * indicating the severity level {@link SeverityLevel.TEST}.
	 * <p>
	 * @param object the {@link Object} to be printed
	 * <p>
	 * @return a {@link Message} containing the specified object
	 */
	public static Message printTest(final Object object)
	{
		final Message message = Messages.createOutputMessage(SeverityLevel.TEST, object);
		consoleHandler.printLine(message);
		if (USE_LOGS)
		{
			logsHandler.printLine(message);
		}
		return message;
	}

	/**
	 * Prints the specified object in the console and writes it in the log
	 * indicating the severity level {@link SeverityLevel.DEBUG}.
	 * <p>
	 * @param object the {@link Object} to be printed
	 * <p>
	 * @return a {@link Message} containing the specified object
	 */
	public static Message printDebug(final Object object)
	{
		final Message message = Messages.createOutputMessage(SeverityLevel.DEBUG, object);
		consoleHandler.printLine(message);
		if (USE_LOGS)
		{
			logsHandler.printLine(message);
		}
		return message;
	}

	/**
	 * Prints the specified object in the console and writes it in the error log
	 * indicating the severity level {@link SeverityLevel.WARNING}.
	 * <p>
	 * @param object the {@link Object} to be printed
	 * <p>
	 * @return a {@link Message} containing the specified object
	 */
	public static Message printWarning(final Object object)
	{
		final Message message = Messages.createOutputMessage(SeverityLevel.WARNING, object);
		consoleHandler.printLine(message);
		if (USE_LOGS)
		{
			logsHandler.printLine(message);
		}
		return message;
	}

	/**
	 * Prints the message of the specified exception in the console and writes
	 * it in the error log indicating the severity level
	 * {@link SeverityLevel.ERROR}.
	 * <p>
	 * @param exception the {@link Exception} with the message to be printed
	 * <p>
	 * @return a {@link Message} containing the specified exception
	 */
	public static Message printError(final Exception exception)
	{
		final Message message = Messages.createOutputMessage(SeverityLevel.ERROR, exception.getMessage());
		consoleHandler.printLine(message);
		if (USE_LOGS)
		{
			logsHandler.printLine(message);
		}
		return message;
	}

	/**
	 * Prints the specified object in the console and writes it in the error log
	 * indicating the severity level {@link SeverityLevel.ERROR}.
	 * <p>
	 * @param object the {@link Object} to be printed
	 * <p>
	 * @return a {@link Message} containing the specified object
	 */
	public static Message printError(final Object object)
	{
		final Message message = Messages.createOutputMessage(SeverityLevel.ERROR, object);
		consoleHandler.printLine(message);
		if (USE_LOGS)
		{
			logsHandler.printLine(message);
		}
		return message;
	}

	/**
	 * Prints the message of the specified exception in the console and writes
	 * it in the error log indicating the severity level
	 * {@link SeverityLevel.CRITICAL}.
	 * <p>
	 * @param exception the {@link Exception} with the message to be printed
	 * <p>
	 * @return a {@link Message} containing the specified exception
	 */
	public static Message printFatalError(final Exception exception)
	{
		final Message message = Messages.createOutputMessage(SeverityLevel.CRITICAL, exception.getMessage());
		consoleHandler.printLine(message);
		if (USE_LOGS)
		{
			logsHandler.printLine(message);
		}
		Runtime.getRuntime().halt(EXIT_FAILURE);
		return message;
	}

	/**
	 * Prints the specified object in the console and writes it in the error log
	 * indicating the severity level {@link SeverityLevel.CRITICAL}.
	 * <p>
	 * @param object the {@link Object} to be printed
	 * <p>
	 * @return a {@link Message} containing the specified object
	 */
	public static Message printFatalError(final Object object)
	{
		final Message message = Messages.createOutputMessage(SeverityLevel.CRITICAL, object);
		consoleHandler.printLine(message);
		if (USE_LOGS)
		{
			logsHandler.printLine(message);
		}
		Runtime.getRuntime().halt(EXIT_FAILURE);
		return message;
	}


	////////////////////////////////////////////////////////////////////////////
	// LOADING BAR
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Prints a loading bar of {@code i/n} points.
	 * <p>
	 * @param i an {@code int} value
	 * @param n the upper bound of {@code i}
	 */
	public static void printLoadingBar(final double i, final double n)
	{
		final int nPoints = countPoints(i, n);
		if (nPoints > barPointsNumber)
		{
			startLoadingBar();
			printPoints(nPoints);
			printSpaces(maxBarPoints - nPoints);
			stopLoadingBar();
		}
		if (i >= n)
		{
			barPointsNumber = 0;
		}
	}

	/**
	 * Prints the beginning of a loading bar.
	 */
	public static void startLoadingBar()
	{
		barPointsNumber = 0;
		consoleHandler.print("[", false);
	}

	/**
	 * Prints {@code i/n} points.
	 * <p>
	 * @param i an {@code int} value
	 * @param n the upper bound of {@code i}
	 */
	public static void updateLoadingBar(final long i, final long n)
	{
		printPoints(countPoints(i, n) - barPointsNumber);
	}

	/**
	 * Prints the end of a loading bar.
	 */
	public static void stopLoadingBar()
	{
		consoleHandler.printLine("]", false);
	}

	/**
	 * Returns the number of points to be printed.
	 * <p>
	 * @param i the current number of iterations
	 * @param n the total number of iterations
	 * <p>
	 * @return the number of points to be printed
	 */
	private static int countPoints(final double i, final double n)
	{
		// Check the argument(s)
		Arguments.requireNonNegative(i);
		Arguments.requireLessOrEqualTo(i, n);
		Arguments.requireNonNegative(n);
		// Process
		return (int) ((i / n) * maxBarPoints);
	}

	/**
	 * Prints {@code n} points.
	 * <p>
	 * @param n the number of points to be printed
	 */
	private static void printPoints(final int n)
	{
		if (n > 0)
		{
			printStrings(".", n);
			barPointsNumber += n;
		}
	}

	/**
	 * Prints {@code n} spaces.
	 * <p>
	 * @param n the number of spaces to be printed
	 */
	private static void printSpaces(final int n)
	{
		printStrings(" ", n);
	}

	/**
	 * Prints {@code n} strings.
	 * <p>
	 * @param n the number of strings to be printed
	 */
	private static void printStrings(final String string, final int n)
	{
		if (n > 0)
		{
			consoleHandler.print(Strings.repeat(string, n), false);
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// SETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Sets the console of this handler.
	 * <p>
	 * @param console the {@link IConsole} to set
	 */
	public static void setConsole(final IConsole console)
	{
		consoleHandler.setConsole(console);
	}


	////////////////////////////////////////////////////////////////////////////
	// CLEAR
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Deletes all the event logs.
	 */
	public static void clear()
	{
		logsHandler.deleteLogs();
	}
}
