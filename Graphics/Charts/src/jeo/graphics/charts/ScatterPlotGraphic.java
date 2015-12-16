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
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import jeo.common.util.Arguments;
import jeo.graphics.charts.structure.SeriesStyle;
import jeo.graphics.charts.util.Charts;

public class ScatterPlotGraphic
	extends ChartGraphic
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = -6532972707229459752L;
	private final XYSeriesCollection collection;
	private int size;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public ScatterPlotGraphic(final String title, final String xLabel, final String yLabel)
	{
		super(title, xLabel, yLabel);
		collection = new XYSeriesCollection();
		size = 0;
	}


	////////////////////////////////////////////////////////////////////////////
	// CHART GRAPHIC
	////////////////////////////////////////////////////////////////////////////

	@Override
	public JFreeChart createChart()
	{
		final JFreeChart chart = Charts.createScatterPlot(title, xLabel, yLabel, collection);
		// Add the collections to the chart
		final XYPlot plot = chart.getXYPlot();
		plot.setDataset(collection);
		// Set the style of the plot
		setItemRenderer(plot);
		return chart;
	}


	////////////////////////////////////////////////////////////////////////////
	// SCATTER PLOT GRAPHIC
	////////////////////////////////////////////////////////////////////////////

	public int addSeries(final String title)
	{
		final int seriesIndex = size;
		collection.addSeries(new XYSeries(title));
		++size;
		return seriesIndex;
	}

	public int addSeries(final String title, final SeriesStyle style)
	{
		final int seriesIndex = addSeries(title);
		styles.put(seriesIndex, style);
		return seriesIndex;
	}

	public int addSeries(final String title, final SeriesStyle style, final Number[] xValues, final Number[] yValues)
	{
		// Check the argument(s)
		Arguments.<Number>requireSameSize(xValues, yValues);
		// Create the series
		final int seriesIndex = addSeries(title, style);
		// Add points
		final int n = xValues.length;
		for (int i = 0; i < n; ++i)
		{
			addPoint(seriesIndex, xValues[i], yValues[i]);
		}
		return seriesIndex;
	}

	public void addPoint(final int index, final Number x, final Number y)
	{
		collection.getSeries(index).add(x, y);
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S) & SETTER(S)
	////////////////////////////////////////////////////////////////////////////

	public int getSize(final int index)
	{
		return collection.getSeries(index).getItemCount();
	}
}
