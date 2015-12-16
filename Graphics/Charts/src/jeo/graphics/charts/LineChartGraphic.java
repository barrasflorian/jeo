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
package jeo.graphics.charts;

import org.jfree.chart.JFreeChart;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;

import jeo.graphics.charts.util.Charts;

public class LineChartGraphic
	extends ChartGraphic
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	private static final long serialVersionUID = -3116483177273925286L;
	private XYDataset dataset;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public LineChartGraphic(final String title, final String xLabel, final String yLabel)
	{
		super(title, xLabel, yLabel);
	}


	////////////////////////////////////////////////////////////////////////////
	// CHART GRAPHIC
	////////////////////////////////////////////////////////////////////////////

	@Override
	public JFreeChart createChart()
	{
		return Charts.createLineChart(title, xLabel, yLabel, dataset);
	}


	////////////////////////////////////////////////////////////////////////////
	// LINE CHART GRAPHIC
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generates a dataset from the specified function {@code f}.
	 * <p>
	 * @param f         a 2D function f: R -> R
	 * @param start     the lowerbound of the domain
	 * @param end       the upperbound of the domain
	 * @param samples   the number of samples
	 * @param seriesKey the identifiant of the dataset to be created
	 * <p>
	 */
	public void createDataset(final Function2D f, final double start, final double end, final int samples, final Comparable<?> seriesKey)
	{
		dataset = DatasetUtilities.sampleFunction2D(f, start, end, samples, seriesKey);
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S) & SETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * @return the dataset
	 */
	public XYDataset getDataset()
	{
		return dataset;
	}

	/**
	 * @param dataset the dataset to set
	 */
	public void setDataset(final XYDataset dataset)
	{
		this.dataset = dataset;
	}
}
