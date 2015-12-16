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

import jeo.common.util.Arguments;

public class ConsoleHandler
	extends IOHandler
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * The console to be handled.
	 */
	private IConsole console = new SystemConsole();


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public ConsoleHandler()
	{
		console = new SystemConsole();
	}

	public ConsoleHandler(final IConsole console)
	{
		this.console = console;
	}


	////////////////////////////////////////////////////////////////////////////
	// ICONSOLE
	////////////////////////////////////////////////////////////////////////////

	public String getInputLine()
	{
		return console.getInputLine();
	}

	/**
	 * Sets the console of this handler.
	 * <p>
	 * @param console the {@link IConsole} to set
	 */
	public void setConsole(final IConsole console)
	{
		this.console = console;
	}


	////////////////////////////////////////////////////////////////////////////
	// PRINT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public void print(final Object object, final boolean error)
	{
		// Check the argument(s)
		Arguments.requireNonNull(object);
		// Process
		if (error)
		{
			console.getErr().print(String.valueOf(object));
		}
		else
		{
			console.getOut().print(String.valueOf(object));
		}
	}

	/**
	 * Prints the specified object in the console and then terminates the line.
	 * <p>
	 * @param object the {@link Object} to be printed
	 * @param error  {@code true} if {@code object} is printed in
	 *               {@code console.getOut()} or {@code false} if {@code object}
	 *               is printed in {@code console.getErr()}
	 */
	@Override
	public void printLine(final Object object, final boolean error)
	{
		// Check the argument(s)
		Arguments.requireNonNull(object);
		// Process
		if (error)
		{
			console.getErr().println(object);
		}
		else
		{
			console.getOut().println(object);
		}
	}
}
