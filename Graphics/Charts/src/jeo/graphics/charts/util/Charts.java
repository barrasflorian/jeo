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
package jeo.graphics.charts.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

public class Charts
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private Charts()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// CHARTS
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a line chart.
	 * <p>
	 * @param title   the title of the chart to be created
	 * @param xLabel  the label of the x-axis of the chart to be created
	 * @param yLabel  the label of the y-axis of the chart to be created
	 * @param dataset the dataset of the chart to be created
	 * <p>
	 * @return a line chart
	 */
	public static JFreeChart createLineChart(final String title, final String xLabel, final String yLabel, final XYDataset dataset)
	{
		// Create a line chart
		final JFreeChart chart = ChartFactory.createXYLineChart(title, // title
																xLabel, // x-axis label
																yLabel, // y-axis label
																dataset, // dataset
																PlotOrientation.VERTICAL, // orientation
																true, // include legends
																true, // tooltips
																false // urls
		);
		chart.setBackgroundPaint(Color.white);
		// Set up the plot
		final XYPlot plot = (XYPlot) chart.getPlot();
		Charts.setXYPlot(plot);
		return chart;
	}

	/**
	 * Creates a scatter chart.
	 * <p>
	 * @param title   the title of the chart to be created
	 * @param xLabel  the label of the x-axis of the chart to be created
	 * @param yLabel  the label of the y-axis of the chart to be created
	 * @param dataset the dataset of the chart to be created
	 * <p>
	 * @return a scatter chart
	 */
	public static JFreeChart createScatterPlot(final String title, final String xLabel, final String yLabel, final XYDataset dataset)
	{
		final JFreeChart chart = ChartFactory.createScatterPlot(title, // title
																xLabel, // x-axis label
																yLabel, // y-axis label
																dataset, // dataset
																PlotOrientation.VERTICAL, // orientation
																true, // include legends
																true, // tooltips
																false // urls
		);
		chart.setBackgroundPaint(Color.white);
		// Set up the plot
		final XYPlot plot = (XYPlot) chart.getPlot();
		Charts.setXYPlot(plot);
		return chart;
	}

	/**
	 * Creates a time series chart.
	 * <p>
	 * @param title   the title of the chart to be created
	 * @param xLabel  the label of the x-axis of the chart to be created
	 * @param yLabel  the label of the y-axis of the chart to be created
	 * @param dataset the dataset of the chart to be created
	 * <p>
	 * @return a time series chart
	 */
	public static JFreeChart createTimeSeriesChart(final String title, final String xLabel, final String yLabel, final XYDataset dataset)
	{
		// Create a time series chart
		final JFreeChart chart = ChartFactory.createTimeSeriesChart(title, // title
																	xLabel, // x-axis label
																	yLabel, // y-axis label
																	dataset, // dataset
																	true, // include legends
																	true, // tooltips
																	false // urls
		);
		chart.setBackgroundPaint(Color.white);
		// Set up the plot
		final XYPlot plot = (XYPlot) chart.getPlot();
		Charts.setXYPlot(plot);
		// final StandardLegend sl = (StandardLegend) chart.getLegend();
		// sl.setDisplaySeriesShapes(true);
		final XYItemRenderer renderer = plot.getRenderer();
		if (renderer instanceof StandardXYItemRenderer)
		{
			final BasicStroke dashedStroke = new BasicStroke(2.f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.f, new float[]
														 {
															 6.f, 6.f
			}, 0.f); // new BasicStroke(2.f)
			renderer.setSeriesStroke(0, dashedStroke);
			renderer.setSeriesStroke(1, dashedStroke);
		}
		final DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("hh:mm:ss.SSS"));
		return chart;
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S) & SETTER(S)
	////////////////////////////////////////////////////////////////////////////

	public static List<Color> getColors()
	{
		return new ArrayList<Color>(Arrays.asList(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW));
	}

	public static void setXYPlot(final XYPlot plot)
	{
		// Domain axis
		plot.getDomainAxis().setLowerMargin(0.);
		plot.getDomainAxis().setUpperMargin(0.);
		// Colors
		plot.setOutlinePaint(Color.black);
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.lightGray);
		// Axis
		// - Remove the offset of the axis
		plot.setAxisOffset(new RectangleInsets(0., 0., 0., 0.));
		// Crosshair
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(false);
	}
}
