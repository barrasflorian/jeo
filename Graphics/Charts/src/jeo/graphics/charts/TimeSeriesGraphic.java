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

import java.util.Date;

import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeries;

import jeo.graphics.charts.structure.TimeSeriesList;
import jeo.graphics.charts.util.Charts;

public class TimeSeriesGraphic
	extends ChartGraphic
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 7427486525135424502L;
	private final TimeSeriesList list;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public TimeSeriesGraphic(final String title, final String xLabel, final String yLabel)
	{
		super(title, xLabel, yLabel);
		list = new TimeSeriesList();
	}

	public TimeSeriesGraphic(final String title, final String xLabel, final String yLabel, final TimeSeriesList list)
	{
		super(title, xLabel, yLabel);
		this.list = list;
	}


	////////////////////////////////////////////////////////////////////////////
	// CHART GRAPHIC
	////////////////////////////////////////////////////////////////////////////

	@Override
	public JFreeChart createChart()
	{
		return Charts.createTimeSeriesChart(title, xLabel, yLabel, list);
	}


	////////////////////////////////////////////////////////////////////////////
	// TIME SERIES GRAPHIC
	////////////////////////////////////////////////////////////////////////////

	public int addSeries(final String title)
	{
		return list.addSeries(title);
	}

	public void addSeries(final TimeSeries timeSeries)
	{
		list.addSeries(timeSeries);
	}

	public void addValue(final int index, final Number value)
	{
		list.addValue(index, value);
	}

	public void addValue(final int index, final Date time, final Number value)
	{
		list.addValue(index, time, value);
	}

	public void addValues(final Number[] values)
	{
		list.addValues(values);
	}
}
