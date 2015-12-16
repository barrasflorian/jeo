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

public class Chronometer
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	// Time units
	public static final int N_TIME_UNITS = TimeUnit.values().length;
	// Information of the chronometer
	private final long[] time = new long[N_TIME_UNITS];
	private final double[] timeByUnit = new double[N_TIME_UNITS];
	private long begin = 0L, end = 0L;
	private Long difference = 0L;
	private String representation = Strings.EMPTY;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public Chronometer()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// CHRONOMETER
	////////////////////////////////////////////////////////////////////////////

	public void start()
	{
		begin = System.currentTimeMillis();
	}

	public Long stop()
	{
		end = System.currentTimeMillis();
		compute();
		return difference;
	}

	private void compute()
	{
		difference = end - begin;
		timeByUnit[0] = difference;
		time[0] = (long) (timeByUnit[0] % 1000);
		for (int i = 1; i < N_TIME_UNITS; ++i)
		{
			timeByUnit[i] = difference / (1000. * Math.pow(60, i - 1));
			time[i] = (long) (timeByUnit[i] % 60);
		}
		representation = time[TimeUnit.HOUR.index] + ":" + time[TimeUnit.MINUTE.index] + ":" + time[TimeUnit.SECOND.index] + "." + time[TimeUnit.MILLISECOND.index];
	}

	public long getMilliseconds()
	{
		return difference;
	}

	public double getSeconds()
	{
		return timeByUnit[TimeUnit.SECOND.index];
	}

	public double getMinutes()
	{
		return timeByUnit[TimeUnit.MINUTE.index];
	}

	public double getHours()
	{
		return timeByUnit[TimeUnit.HOUR.index];
	}

	public void setValue(final long value)
	{
		begin = 0;
		end = value;
		compute();
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public String toString()
	{
		return representation;
	}


	////////////////////////////////////////////////////////////////////////////
	// ENUM(S)
	////////////////////////////////////////////////////////////////////////////

	public enum TimeUnit
	{
		MILLISECOND(0),
		SECOND(1),
		MINUTE(2),
		HOUR(3);
		public int index;

		private TimeUnit(final int index)
		{
			this.index = index;
		}
	}
}
