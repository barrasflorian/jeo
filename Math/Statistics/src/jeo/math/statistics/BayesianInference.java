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

import java.util.Arrays;

import jeo.common.util.Arguments;

public class BayesianInference
	implements Inference
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * The number of hypotheses.
	 */
	protected final int nHypotheses;
	/**
	 * This array contains, for all hypothesis H, the probability P(E | H) (the
	 * likelihood).
	 */
	protected final Double[] likelihoods;
	/**
	 * This array contains, for all hypothesis H, the probability P(H | E) (the
	 * posterior probability).
	 */
	protected final Double[] hypothesesProbabilities;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public BayesianInference(final int nHypotheses)
	{
		this.nHypotheses = nHypotheses;
		likelihoods = new Double[nHypotheses];
		hypothesesProbabilities = new Double[nHypotheses];
		reset();
	}


	////////////////////////////////////////////////////////////////////////////
	// INFERENCE
	////////////////////////////////////////////////////////////////////////////

	public final void reset()
	{
		Arrays.fill(getHypothesesProbabilities(), 1. / nHypotheses);
	}

	public void updateHypothesesProbabilities(final double value)
	{
		// Get the marginal likelihood P(E)
		double marginalLikelihood = 0.;
		for (int i = 0; i < nHypotheses; ++i)
		{
			// Compute P(E) = SUM[P(H) * P(E | H), H] (chain rule)
			marginalLikelihood += getHypothesesProbabilities()[i] * likelihoods[i];
		}
		// Update the probabilities of the hypotheses
		for (int i = 0; i < nHypotheses; ++i)
		{
			// Using the previous posterior probability P(H | E) as the current
			// prior P(H),
			// compute P(H | E) = P(H) * P(E | H) / P(E) (Bayes' rule)
			hypothesesProbabilities[i] *= likelihoods[i] / marginalLikelihood;
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S) & SETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the probabilities of the hypotheses.
	 * <p>
	 * @return the probabilities of the hypotheses
	 */
	public Double[] getHypothesesProbabilities()
	{
		return hypothesesProbabilities;
	}

	public void setLikelihood(final int i, final double likelihood)
	{
		// Check arguments
		Arguments.requireNonNegative(i);
		Arguments.requireLessThan(i, nHypotheses);
		likelihoods[i] = likelihood;
	}
}
