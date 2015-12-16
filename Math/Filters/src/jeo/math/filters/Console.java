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
package jeo.math.filters;

import jeo.common.io.IOManager;
import jeo.common.util.Strings;
import jeo.math.linearalgebra.Scalar;

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
		// Interactions with the user to get the measurements of the position to
		// be searched and give the estimated position with the Kalman filter
		interactions();
	}

	/**
	 * Interacts with the user.
	 */
	private static void interactions()
	{
		// Initialization
		final KalmanFilter filter = new KalmanFilter();
		// Initial guess
		filter.x = new Scalar(1.);
		// double[][] F =
		// {
		// {
		// 1, 2
		// },
		// {
		// 3, 4
		// }
		// };
		// filter.F = new Matrix(F);
		boolean running = true;
		String expression;
		// Process
		do
		{
			// Predict the position (a priori)
			filter.predict();
			// Get the input
			expression = IOManager.getInputLine().trim();
			if (Strings.toLowerCase(expression).contains("exit"))
			{
				IOManager.printInfo("Good bye!");
				running = false;
			}
			// Correct the position (a posteriori)
			else
			{
				final Scalar y = new Scalar(Double.parseDouble(expression));
				filter.correct(y);
			}
		}
		while (running);
	}
}
