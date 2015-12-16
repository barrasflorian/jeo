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
package jeo.common.math;

import java.util.Collection;

import jeo.common.util.Arguments;

public class Statistics
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private Statistics()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// BASICS
	////////////////////////////////////////////////////////////////////////////

	// MIN / MAX
	public static <T extends Number> Double getMin(final Collection<T> collection)
	{
		Arguments.requireMinimumSize(collection, 1);
		double min = Double.POSITIVE_INFINITY;
		for (final T element : collection)
		{
			if ((element != null) && (element.doubleValue() < min))
			{
				min = element.doubleValue();
			}
		}
		return min;
	}

	public static <T extends Number> Double getMax(final Collection<T> collection)
	{
		Arguments.requireMinimumSize(collection, 1);
		double max = Double.NEGATIVE_INFINITY;
		for (final T element : collection)
		{
			if ((element != null) && (element.doubleValue() > max))
			{
				max = element.doubleValue();
			}
		}
		return max;
	}

	// MEAN
	public static <T extends Number> Double getMean(final T[] array)
	{
		Arguments.requireMinimumSize(array, 1);
		return Maths.sumWithoutNaN(array) / array.length;
	}

	public static <T extends Number> Double getMean(final Collection<T> collection)
	{
		Arguments.requireMinimumSize(collection, 1);
		return Maths.sumWithoutNaN(collection) / collection.size();
	}

	// VARIANCE
	public static <T extends Number> Double getVariance(final T[] array)
	{
		return getVariance(array, getMean(array));
	}

	public static <T extends Number> Double getVariance(final Collection<T> collection)
	{
		return getVariance(collection, getMean(collection));
	}

	public static <T extends Number> Double getVariance(final T[] array, final double mean)
	{
		Arguments.requireMinimumSize(array, 1);
		return Maths.sumOfSquaresWithoutNaN(array, mean) / array.length;
	}

	public static <T extends Number> Double getVariance(final Collection<T> collection, final double mean)
	{
		Arguments.requireMinimumSize(collection, 1);
		return Maths.sumOfSquaresWithoutNaN(collection, mean) / collection.size();
	}

	// SAMPLE VARIANCE
	public static <T extends Number> Double getSampleVariance(final Collection<T> collection)
	{
		return getVariance(collection, getMean(collection));
	}

	public static <T extends Number> Double getSampleVariance(final Collection<T> collection, final double mean)
	{
		Arguments.requireMinimumSize(collection, 2);
		return Maths.sumOfSquaresWithoutNaN(collection, mean) / (collection.size() - 1);
	}

	// STANDARD DEVIATION
	public static <T extends Number> Double getStandardDeviation(final Collection<T> collection)
	{
		return Math.sqrt(getVariance(collection));
	}

	public static <T extends Number> Double getStandardDeviation(final Collection<T> collection, final double mean)
	{
		return Math.sqrt(getVariance(collection, mean));
	}

	// SAMPLE STANDARD DEVIATION
	public static <T extends Number> Double getSampleStandardDeviation(final Collection<T> collection)
	{
		return Math.sqrt(getSampleVariance(collection));
	}

	public static <T extends Number> Double getSampleStandardDeviation(final Collection<T> collection, final double mean)
	{
		return Math.sqrt(getSampleVariance(collection, mean));
	}

	// CONFIDENCE INTERVAL
	public static double getVariation(final long sampleSize, final double standardDeviation)
	{
		return (Maths.DEFAULT_Z * standardDeviation) / Math.sqrt(sampleSize);
	}

	public static double getVariation(final long sampleSize, final double standardDeviation, final double alpha)
	{
		return (getNormalCDFInverse(alpha) * standardDeviation) / Math.sqrt(sampleSize);
	}

	public static Interval<Double> getConfidenceInterval(final long sampleSize, final double mean, final double standardDeviation)
	{
		final double variation = getVariation(sampleSize, standardDeviation);
		return new Interval<Double>(mean - variation, mean + variation);
	}

	public static Interval<Double> getConfidenceInterval(final long sampleSize, final double mean, final double standardDeviation, final double alpha)
	{
		final double variation = getVariation(sampleSize, standardDeviation, alpha);
		return new Interval<Double>(mean - variation, mean + variation);
	}


	////////////////////////////////////////////////////////////////////////////
	// GAUSSIAN DISTRIBUTION
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns phi(x) = standard Gaussian PDF.
	 * <p>
	 * @param x an input value
	 * <p>
	 * @return phi(x) = standard Gaussian PDF
	 */
	public static double getNormalPDF(final double x)
	{
		return Math.exp(-Math.pow(x, 2) / 2) / Math.sqrt(2 * Math.PI);
	}

	/**
	 * Returns phi(x, mu, signma) = Gaussian PDF with mean {@code mu} and
	 * standard deviation {@code sigma}.
	 * <p>
	 * @param x     an input value
	 * @param mu    the mean of the distribution
	 * @param sigma the standard deviation of the distribution
	 * <p>
	 * @return phi(x, mu, signma) = Gaussian PDF with mean {@code mu} and
	 *         standard deviation {@code sigma}
	 */
	public static double getNormalPDF(final double x, final double mu, final double sigma)
	{
		return getNormalPDF((x - mu) / sigma) / sigma;
	}

	/**
	 * Returns Phi(z) = standard Gaussian CDF using Taylor approximation.
	 * <p>
	 * @param z an input value
	 * <p>
	 * @return Phi(z) = standard Gaussian CDF using Taylor approximation
	 */
	public static double getNormalCDF(final double z)
	{
		return getNormalCDF(z, Maths.DEFAULT_EPSILON);
	}

	/**
	 * Returns Phi(z) = standard Gaussian CDF using Taylor approximation.
	 * <p>
	 * @param z       an input value
	 * @param epsilon the floating epsilon
	 * <p>
	 * @return Phi(z) = standard Gaussian CDF using Taylor approximation
	 */
	public static double getNormalCDF(final double z, final double epsilon)
	{
		if (z < -8.)
		{
			return 0.;
		}
		if (z > 8.)
		{
			return 1.;
		}
		double sum = 0., term = z;
		for (long i = 3L; term < epsilon; i += 2L)
		{
			sum += term;
			term *= Math.pow(z, 2) / i;
		}
		return 0.5 + (sum * getNormalPDF(z));
	}

	/**
	 * Returns Phi(z, mu, sigma) = Gaussian CDF with mean {@code mu} and
	 * standard deviation {@code sigma}.
	 * <p>
	 * @param z     an input value
	 * @param mu    the mean of the distribution
	 * @param sigma the standard deviation of the distribution
	 * <p>
	 * @return Phi(z, mu, sigma) = Gaussian CDF with mean {@code mu} and
	 *         standard deviation {@code sigma}
	 */
	public static double getNormalCDF(final double z, final double mu, final double sigma)
	{
		return getNormalCDF((z - mu) / sigma);
	}

	/**
	 * Returns z such that Phi(z) = y via bisection search.
	 * <p>
	 * @param y an input value
	 * <p>
	 * @return z such that Phi(z) = y via bisection search
	 */
	public static double getNormalCDFInverse(final double y)
	{
		return getNormalCDFInverse(y, .00000001, -8, 8);
	}

	/**
	 * Returns z such that Phi(z) = y via bisection search.
	 * <p>
	 * @param y     an input value
	 * @param delta the precision of the result
	 * @param lo    the lower bound of the search
	 * @param hi    the upper bound of the search
	 * <p>
	 * @return z such that Phi(z) = y via bisection search
	 */
	private static double getNormalCDFInverse(final double y, final double delta, final double lo, final double hi)
	{
		final double mid = lo + ((hi - lo) / 2);
		if ((hi - lo) < delta)
		{
			return mid;
		}
		if (getNormalCDF(mid) > y)
		{
			return getNormalCDFInverse(y, delta, lo, mid);
		}
		else
		{
			return getNormalCDFInverse(y, delta, mid, hi);
		}
	}
}
