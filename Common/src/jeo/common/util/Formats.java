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

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Formats
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTANT(S)
	////////////////////////////////////////////////////////////////////////////

	// The default encoding
	public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	public static final Charset DEFAULT_CHARSET = UTF8_CHARSET;
	public static final String DEFAULT_CHARSET_NAME = DEFAULT_CHARSET.name();
	// The default locale
	public static final Locale DEFAULT_LOCALE = Locale.getDefault();
	// The current version
	public static final String VERSION = "1.5.0";
	// The default length of a line (useful for IO)
	public static final int DEFAULT_LINE_LENGTH = 72;
	// The default date time format
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	// The parameters of decimal formats
	// - Pattern
	public static final String DEFAULT_PATTERN = "0.####";
	public static final int NUMBER_SIZE = DEFAULT_PATTERN.length();
	// - Scientific pattern
	public static final String DEFAULT_SCIENTIFIC_PATTERN = DEFAULT_PATTERN + "E0";
	// - Integer digits
	public static final int DEFAULT_MIN_INTEGER_DIGITS = 1;
	// - Fraction digits
	public static final int DEFAULT_MIN_FRACTION_DIGITS = 0;
	public static final int DEFAULT_MAX_FRACTION_DIGITS = NUMBER_SIZE - 2;
	public static int MIN_FRACTION_DIGITS = DEFAULT_MIN_FRACTION_DIGITS;
	public static int MAX_FRACTION_DIGITS = DEFAULT_MAX_FRACTION_DIGITS;
	// The minimum size of a number (useful for Linear Algebra)
	public static int MIN_NUMBER_SIZE = 1;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private Formats()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// NUMBER FORMATS
	////////////////////////////////////////////////////////////////////////////

	public static String format(final Number number)
	{
		final String formattedNumber;
		final String numberString = String.valueOf(number);
		int digitsNumber = numberString.length();
		if (numberString.contains("-"))
		{
			--digitsNumber;
		}
		if (numberString.contains("."))
		{
			--digitsNumber;
		}
		if (digitsNumber > (MAX_FRACTION_DIGITS + 2))
		{
			formattedNumber = getScientificDecimalFormat().format(number);
		}
		else
		{
			formattedNumber = getDecimalFormat().format(number);
		}
		return formattedNumber;
	}

	private static DecimalFormat getDecimalFormat()
	{
		return getDecimalFormat(DEFAULT_PATTERN);
	}

	private static DecimalFormat getScientificDecimalFormat()
	{
		return getDecimalFormat(DEFAULT_SCIENTIFIC_PATTERN);
	}

	private static DecimalFormat getDecimalFormat(final String pattern)
	{
		return getDecimalFormat(pattern, DEFAULT_LOCALE);
	}

	private static DecimalFormat getDecimalFormat(final String pattern, final Locale locale)
	{
		final DecimalFormat format = Strings.isNotEmpty(pattern) ? new DecimalFormat(pattern) : new DecimalFormat();
		format.setDecimalFormatSymbols(new DecimalFormatSymbols(locale));
		format.setGroupingUsed(false);
		format.setMaximumFractionDigits(MAX_FRACTION_DIGITS);
		format.setMinimumFractionDigits(MIN_FRACTION_DIGITS);
		format.setMinimumIntegerDigits(DEFAULT_MIN_INTEGER_DIGITS);
		return format;
	}
}
