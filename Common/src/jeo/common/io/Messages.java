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

import jeo.common.util.Dates;
import jeo.common.util.Strings;

public class Messages
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private Messages()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// PREFIX
	////////////////////////////////////////////////////////////////////////////

	// - ALL
	public static String getPrefix(final IOType type, final SeverityLevel level)
	{
		switch (type)
		{
			case INPUT:
				return getInputPrefix();
			case OUTPUT:
				return getOutputPrefix(level);
			default:
				return Strings.EMPTY;
		}
	}

	// - INPUT
	public static String getInputPrefix()
	{
		return createInputPrefix();
	}

	// - OUTPUT
	public static String getOutputPrefix(final SeverityLevel level)
	{
		// Get information about the call (class name, method name and Message number)
		final StackTraceElement stackTraceElement = getStackTraceElement(6);
		final String simpleClassName = getSimpleClassName(stackTraceElement);
		// Create the prefix
		String prefix;
		switch (level)
		{
			case RESULT:
				prefix = ">";
				break;
			case INFO:
				prefix = createOutputPrefix(level);
				break;
			case WARNING:
				prefix = createOutputPrefix(level, simpleClassName);
				break;
			case TEST:
			case ERROR:
				prefix = createOutputPrefix(level, simpleClassName, stackTraceElement.getMethodName());
				break;
			case DEBUG:
			case CRITICAL:
				prefix = createOutputPrefix(level, simpleClassName, stackTraceElement.getMethodName(), stackTraceElement.getLineNumber());
				break;
			default:
				prefix = Strings.EMPTY;
		}
		return prefix;
	}

	public static StackTraceElement getStackTraceElement(final int index)
	{
		return new Throwable().fillInStackTrace().getStackTrace()[index];
	}

	public static String getSimpleClassName(final StackTraceElement stackTraceElement)
	{
		final String className = stackTraceElement.getClassName();
		return className.substring(className.lastIndexOf('.') + 1);
	}


	////////////////////////////////////////////////////////////////////////////
	// LABEL CREATION
	////////////////////////////////////////////////////////////////////////////

	// - ALL
	private static String createLabel(final IOType type)
	{
		String string;
		switch (type)
		{
			case INPUT:
				string = "INPUT";
				break;
			case OUTPUT:
				string = "OUTPUT";
				break;
			default:
				string = Strings.EMPTY;
		}
		return createLabel(string);
	}

	private static String createLabel(final SeverityLevel level)
	{
		String string;
		switch (level)
		{
			case RESULT:
				string = "RESULT";
				break;
			case INFO:
				string = "INFO";
				break;
			case TEST:
				string = "TEST";
				break;
			case DEBUG:
				string = "DEBUG";
				break;
			case WARNING:
				string = "WARNING";
				break;
			case ERROR:
				string = "ERROR";
				break;
			case CRITICAL:
				string = "FATAL ERROR";
				break;
			default:
				string = Strings.EMPTY;
		}
		return createLabel(string);
	}

	private static String createLabel(final String string)
	{
		return (string == null) || (string.length() == 0) ? Strings.EMPTY : "[" + string + "]";
	}


	////////////////////////////////////////////////////////////////////////////
	// PREFIX CREATION
	////////////////////////////////////////////////////////////////////////////

	// - ALL
	private static String createPrefix()
	{
		return createLabel(Dates.getCurrentDateTime());
	}

	private static String createPrefix(final IOType type)
	{
		return createPrefix() + createLabel(type);
	}

	// - INPUT
	private static String createInputPrefix()
	{
		return createPrefix(IOType.INPUT);
	}

	// - OUTPUT
	private static String createOutputPrefix()
	{
		return createPrefix();
	}

	private static String createOutputPrefix(final SeverityLevel level)
	{
		return createOutputPrefix() + createLabel(level);
	}

	private static String createOutputPrefix(final SeverityLevel level, final String className)
	{
		return createOutputPrefix(level) + createLabel(className);
	}

	private static String createOutputPrefix(final SeverityLevel level, final String className, final String methodName)
	{
		return createOutputPrefix(level, className) + createLabel(methodName);
	}

	private static String createOutputPrefix(final SeverityLevel level, final String className, final String methodName, final int lineNumber)
	{
		return createOutputPrefix(level, className, methodName) + createLabel(String.valueOf(lineNumber));
	}


	////////////////////////////////////////////////////////////////////////////
	// MESSAGE CREATION
	////////////////////////////////////////////////////////////////////////////

	// - INPUT
	public static Message createInputMessage()
	{
		return createInputMessage(Strings.EMPTY);
	}

	public static Message createInputMessage(final Object message)
	{
		return new Message(IOType.INPUT, SeverityLevel.RESULT, message);
	}

	// - OUTPUT
	public static Message createOutputMessage(final SeverityLevel level, final Object message)
	{
		return new Message(IOType.OUTPUT, level, message);
	}


	////////////////////////////////////////////////////////////////////////////
	// ENUM(S)
	////////////////////////////////////////////////////////////////////////////

	public enum IOType
	{
		INPUT,
		OUTPUT
	}

	public enum SeverityLevel
	{
		RESULT,
		INFO,
		TEST,
		DEBUG,
		WARNING,
		ERROR,
		CRITICAL
	}
}
