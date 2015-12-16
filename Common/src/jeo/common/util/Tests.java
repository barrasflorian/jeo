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

import jeo.common.io.IOManager;
import jeo.common.io.Messages;
import jeo.common.math.Interval;
import jeo.common.math.Numbers;
import jeo.common.math.Statistics;

public class Tests
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private Tests()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// ASSERTIONS
	////////////////////////////////////////////////////////////////////////////

	public static void check(final boolean result)
	{
		if (!result)
		{
			final StackTraceElement stackTraceElement = Messages.getStackTraceElement(2);
			final String simpleClassName = Messages.getSimpleClassName(stackTraceElement);
			IOManager.printError("Test failed in class '" + simpleClassName + "' at line " + stackTraceElement.getLineNumber());
		}
	}

	public static void equal(final Number a, final Number b)
	{
		if (!Numbers.equals(a, b))
		{
			IOManager.printError("Equality test failed");
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// PRINT
	////////////////////////////////////////////////////////////////////////////

	public static void printTimes(final Collection<Long> times)
	{
		final double mean = Statistics.getMean(times);
		final double stddev = Statistics.getSampleStandardDeviation(times, mean);
		final Interval<Double> confidenceInterval = Statistics.getConfidenceInterval(times.size(), mean, stddev);
		IOManager.printTest("Avg time: " + mean + " +- " + (confidenceInterval.getUpperBound() - mean));
		IOManager.printTest("Interval: " + new Interval<Double>(Statistics.getMin(times), Statistics.getMax(times)));
	}
}
