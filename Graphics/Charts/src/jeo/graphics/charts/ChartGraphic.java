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

import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import jeo.graphics.Graphic;
import jeo.graphics.JPanels;
import jeo.graphics.charts.structure.SeriesStyle;

public abstract class ChartGraphic
	extends Graphic
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 3122223688000223225L;
	protected final String xLabel;
	protected final String yLabel;
	protected final Map<Integer, SeriesStyle> styles = new HashMap<Integer, SeriesStyle>(10);


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new chart graphic.
	 * <p>
	 * @param title  the title of the graphic to be created
	 * @param xLabel the label of the x-axis
	 * @param yLabel the label of the y-axis
	 */
	public ChartGraphic(final String title, final String xLabel, final String yLabel)
	{
		super(title);
		this.xLabel = xLabel;
		this.yLabel = yLabel;
	}


	////////////////////////////////////////////////////////////////////////////
	// CHART GRAPHIC
	////////////////////////////////////////////////////////////////////////////

	public abstract JFreeChart createChart();

	/**
	 * Sets the item renderer of the specified plot.
	 * <p>
	 * @param plot the input {@link XYPlot}
	 */
	public void setItemRenderer(final XYPlot plot)
	{
		// Create an item renderer containing the style of the plot
		final XYItemRenderer renderer = new XYLineAndShapeRenderer();
		// - Override the original styles of the series
		final Set<Map.Entry<Integer, SeriesStyle>> styleMaps = styles.entrySet();
		for (final Map.Entry<Integer, SeriesStyle> styleMap : styleMaps)
		{
			renderer.setSeriesPaint(styleMap.getKey(), styleMap.getValue().getColor());
			renderer.setSeriesShape(styleMap.getKey(), styleMap.getValue().getShape());
			renderer.setSeriesStroke(styleMap.getKey(), styleMap.getValue().getStroke());
		}
		// Set the renderer of the plot
		plot.setRenderer(renderer);
	}

	/**
	 * Display this graphic.
	 */
	public void display()
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				// Set the dimension
				final Dimension minDimension = new Dimension(800, 600);
				final Dimension dimension = new Dimension(1900, 1200);
				setMinimumSize(minDimension);
				setPreferredSize(dimension);
				// Create the chart panel
				final JFreeChart chart = createChart();
				final ChartPanel chartPanel = new ChartPanel(chart);
				chartPanel.setMinimumSize(minDimension);
				chartPanel.setPreferredSize(dimension);
				chartPanel.setMouseZoomable(true, false);
				JPanels.addScrollZoom(chartPanel);
				setContentPane(chartPanel);
				// Display
				setVisible(true);
			}
		});
	}
}
