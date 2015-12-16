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
import jeo.math.linearalgebra.Entity;
import jeo.math.linearalgebra.Matrix;
import jeo.math.linearalgebra.Scalar;

public class KalmanFilter
{
	/**
	 * Estimated state.
	 */
	public Entity x;
	/**
	 * State transition matrix (model the transitions between states).
	 */
	public Entity F;
	/**
	 * Control variables.
	 */
	public Entity u;
	/**
	 * Control matrix (map control variables to state variables).
	 */
	public Entity B;
	/**
	 * State variance matrix (error of estimation).
	 */
	public Entity P;
	/**
	 * Process variance matrix (error due to process).
	 */
	public Entity Q;
	/**
	 * Measurement matrix (map measurements onto state).
	 */
	public Entity H;
	/**
	 * Kalman gain.
	 */
	public Entity K;
	/**
	 * Measurement variance matrix (error from measurements).
	 */
	public Entity R;

	public KalmanFilter()
	{
		x = new Scalar(0.);
		F = new Scalar(1.);
		u = new Scalar(0.);
		B = new Scalar(0.);
		P = new Scalar(1.);
		Q = new Scalar(0.);
		H = new Scalar(1.);
		K = new Scalar(0.);
		R = new Scalar(1.);
	}

	/**
	 * Predicts the estimation {@code x} and the state variance matrix {@code P}
	 * (a priori).
	 */
	public void predict()
	{
		// Compute x = F * x + B * u
		x = F.times(x);
		if (!((x instanceof Matrix) && (B instanceof Scalar)))
		{
			x = x.plus(B.times(u));
			IOManager.printDebug("x = F * x + B * u = " + F + " * " + x + " + " + B + " * " + u + " = " + x.toString());
		}
		else
		{
			IOManager.printDebug("x = F * x = " + F + " * " + x + " = " + x.toString());
		}
		// Compute P = F * P * F' + Q
		P = F.times(P).times(F.transpose()).plus(Q);
		IOManager.printDebug("P = F * P * F' + Q = " + F + " * " + P + " * " + F + "' + " + Q + " = " + P.toString());
	}

	/**
	 * Corrects the estimation {@code x}, updates the Kalman gain {@code K} and
	 * the state variance matrix {@code P} (a posteriori).
	 * <p>
	 * @param y the measurement
	 */
	public void correct(final Entity y)
	{
		// Compute K = P * H' * inv(H * P * H' + R),
		// where (H * P * H' + R) is the innovation covariance
		K = P.times(H.transpose()).times(H.times(P).times(H.transpose()).plus(R).inverse());
		IOManager.printDebug("K = P * H' * inv(H * P * H' + R) = " + P + " * " + H + "' * inv(" + H + " * " + P + " * " + H + "' + " + R + ") = " + K.toString());
		// Compute x = x + K * (y - H * x),
		// where (y - H * x) is the innovation
		x = x.plus(K.times(y.minus(H.times(x))));
		IOManager.printDebug("x = x + K * (y - H * x) = " + x + " + " + K + " * (" + y + " - " + H + " * " + x + ") = " + x.toString());
		// Compute P = (I - K * H) * P
		final Entity KH = K.times(H);
		final Entity I = KH.identity();
		IOManager.printDebug("P = (I - K * H) * P = (" + I + " - " + K + " * " + H + ") * " + P + " = " + P.toString());
		P = I.minus(KH).times(P);
	}
}
