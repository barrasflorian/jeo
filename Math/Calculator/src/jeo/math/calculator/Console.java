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
package jeo.math.calculator;

import jeo.common.io.IOManager;
import jeo.common.util.Strings;
import jeo.math.calculator.process.Calculator;

public class Console
{
	/**
	 * Starts the console.
	 * <p>
	 * @param args the command line arguments
	 */
	public static void main(final String[] args)
	{
		// Clear the console and the logs
		IOManager.clear();
		// Initialization
		Calculator.start();
		// Process
		interactions();
		// End the working pool of threads
		Calculator.end();
	}

	/**
	 * Interacts with the user.
	 */
	private static void interactions()
	{
		// Initialization
		final Calculator calc = new Calculator();
		boolean running = true;
		String expression;
		boolean hello = false;
		// Process
		do
		{
			expression = IOManager.getInputLine().trim();
			if (Strings.toLowerCase(expression).contains("bye") || Strings.toLowerCase(expression).contains("exit"))
			{
				IOManager.printInfo("Good bye!");
				running = false;
			}
			else if (Strings.toLowerCase(expression).contains("hello"))
			{
				if (hello)
				{
					IOManager.printInfo("Hi again!");
				}
				else
				{
					IOManager.printInfo("Hello human!");
					hello = true;
				}
			}
			else if (Strings.toLowerCase(expression).contains("lucky"))
			{
				IOManager.printInfo(":)");
			}
			else if (Strings.toLowerCase(expression).contains("cool"))
			{
				IOManager.printInfo("8)");
			}
			else
			{
				IOManager.printResult(calc.process(expression));
			}
		}
		while (running);
	}
}
