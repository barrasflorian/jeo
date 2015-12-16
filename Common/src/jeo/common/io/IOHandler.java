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

public abstract class IOHandler
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	protected IOHandler()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// PRINT
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Prints the specified object.
	 * <p>
	 * @param object the {@link Object} to be printed
	 * @param error  {@code true} if {@code object} is printed in the standard
	 *               error or {@code false} if {@code object} is printed in the
	 *               standard output
	 */
	public abstract void print(final Object object, final boolean error);

	/**
	 * Prints the specified object and then terminates the line.
	 * <p>
	 * @param object the {@link Object} to be printed
	 * @param error  {@code true} if {@code object} is printed in the standard
	 *               error or {@code false} if {@code object} is printed in the
	 *               standard output
	 */
	public abstract void printLine(final Object object, final boolean error);

	/**
	 * Prints the specified message in the console.
	 * <p>
	 * @param message the {@link Message} to be printed
	 */
	public void printLine(final Message message)
	{
		switch (message.getLevel())
		{
			case DEBUG:
				if (!IOManager.DEBUG_MODE)
				{
					break;
				}
			case RESULT:
			case INFO:
			case TEST:
				printLine(message, false);
				break;
			case WARNING:
			case ERROR:
			case CRITICAL:
				printLine(message, true);
				break;
		}
	}
}
