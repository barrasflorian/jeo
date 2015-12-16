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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dates
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private Dates()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// DATE TIME
	////////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT
	 *         represented by this date
	 */
	public static long getCurrentTime()
	{
		return new Date().getTime();
	}

	/**
	 *
	 * @return the current date and time {@link String}
	 */
	public static String getCurrentDateTime()
	{
		return getCurrentDateTime(Formats.DEFAULT_DATE_TIME_FORMAT);
	}

	/**
	 *
	 * @param format the format {@link String} of the date and time
	 * <p>
	 * @return the current date and time {@link String} formatted according to
	 *         {@code format}
	 */
	public static String getCurrentDateTime(final String format)
	{
		final DateFormat dateFormat = new SimpleDateFormat(format);
		final Date date = new Date();
		return dateFormat.format(date);
	}
}
