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
package jeo.math.statistics;

import jeo.common.util.Arguments;

public class DynamicSample
	extends OnlineStatistic
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * The lower bound of the sample size is set to 3 (2 to get the first sample
	 * standard deviation plus 1 to get the second one).
	 */
	public static final int SAMPLE_SIZE_LOWER_BOUND = 3;
	/**
	 * The upper bound of the sample size.
	 */
	public static final int SAMPLE_SIZE_UPPER_BOUND = Integer.MAX_VALUE;
	/**
	 * The default min sample size (inclusive).
	 */
	public static final int DEFAULT_MIN_SAMPLE_SIZE = SAMPLE_SIZE_LOWER_BOUND;
	/**
	 * The default max sample size (inclusive).
	 */
	public static final int DEFAULT_MAX_SAMPLE_SIZE = SAMPLE_SIZE_UPPER_BOUND;
	/**
	 * The previous sample mean.
	 */
	private Double previousSampleMean = null;
	/**
	 * The min sample size (inclusive).
	 */
	private int minSampleSize;
	/**
	 * The max sample size (inclusive).
	 */
	private int maxSampleSize;
	/**
	 * The previous confidence interval.
	 */
	private Double previousConfidenceInterval;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public DynamicSample()
	{
		// Set the min and max sample sizes
		setMinSampleSize(DEFAULT_MIN_SAMPLE_SIZE);
		setMaxSampleSize(DEFAULT_MAX_SAMPLE_SIZE);
		// Reset the fields
		reset();
	}

	public DynamicSample(final int minSampleSize)
	{
		// Check the argument(s)
		Arguments.requireGreaterOrEqualTo(minSampleSize, SAMPLE_SIZE_LOWER_BOUND);
		// Set the min and max sample sizes
		setMinSampleSize(minSampleSize);
		setMaxSampleSize(DEFAULT_MAX_SAMPLE_SIZE);
		// Reset the fields
		reset();
	}

	public DynamicSample(final int minSampleSize, final int maxSampleSize)
	{
		// Check the argument(s)
		Arguments.requireGreaterOrEqualTo(minSampleSize, SAMPLE_SIZE_LOWER_BOUND);
		Arguments.requireGreaterOrEqualTo(maxSampleSize, minSampleSize);
		// Set the min and max sample sizes
		setMinSampleSize(minSampleSize);
		setMaxSampleSize(maxSampleSize);
		// Reset the fields
		reset();
	}


	////////////////////////////////////////////////////////////////////////////
	// ONLINE STATISTIC
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Resets the fields.
	 */
	@Override
	public final void reset()
	{
		super.reset();
		previousConfidenceInterval = null;
	}


	////////////////////////////////////////////////////////////////////////////
	// DYNAMIC SAMPLE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Updates the sample mean and variance given an input value.
	 * <p>
	 * @param x an input value
	 */
	public void sample(final double x)
	{
		// Update the previous sample standard deviation
		if ((size % (minSampleSize - 1)) == 0)
		{
			previousConfidenceInterval = getSampleMeanConfidenceInterval();
		}
		// Update the sample mean and variance
		update(x);
	}

	/**
	 * Resamples if the size of the sample is greater than {@code maxSampleSize}
	 * or the precision is decreasing, returns {@code false} otherwise.
	 * <p>
	 * @return {@code true} if the size of the sample is greater than
	 *         {@code maxSampleSize} or the precision is decreasing,
	 *         {@code false} otherwise
	 */
	public boolean resample()
	{
		if (isResampling())
		{
			previousSampleMean = mean;
			reset();
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Returns {@code true} if the size of the sample is greater than
	 * {@code maxSampleSize} or the precision is decreasing, {@code false}
	 * otherwise.
	 * <p>
	 * @return {@code true} if the size of the sample is greater than
	 *         {@code maxSampleSize} or the precision is decreasing,
	 *         {@code false} otherwise
	 */
	public boolean isResampling()
	{
		return (size == maxSampleSize) || isPrecisionDecreasing();
	}

	/**
	 * Returns {@code true} if the dynamic sample size is greater or equal to
	 * the lower bound and the precision is decreasing, {@code false} otherwise.
	 * <p>
	 * @return {@code true} if the dynamic sample size is greater or equal to
	 *         the lower bound and the precision is decreasing, {@code false}
	 *         otherwise
	 */
	public boolean isPrecisionDecreasing()
	{
		final Double sampleMeanConfidenceInterval = getSampleMeanConfidenceInterval();
		return (size >= minSampleSize) && !sampleMeanConfidenceInterval.isNaN() && (previousConfidenceInterval != null) && (sampleMeanConfidenceInterval >= previousConfidenceInterval);
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S) & SETTER(S)
	////////////////////////////////////////////////////////////////////////////

	@Override
	public double getSampleMean()
	{
		if ((size < minSampleSize) && (previousSampleMean != null))
		{
			return previousSampleMean;
		}
		else
		{
			return mean;
		}
	}

	public final void setMinSampleSize(final int minSampleSize)
	{
		this.minSampleSize = minSampleSize;
	}

	public final void setMaxSampleSize(final int maxSampleSize)
	{
		this.maxSampleSize = maxSampleSize;
	}
}
