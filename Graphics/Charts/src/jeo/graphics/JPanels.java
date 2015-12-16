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
package jeo.graphics;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

public class JPanels
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private JPanels()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// JPANELS
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Adds the scroll zoom to the specified panel.
	 * <p>
	 * @param panel the panel to be modified
	 */
	public static void addScrollZoom(final JPanel panel)
	{
		panel.addMouseWheelListener(new MouseWheelListener()
		{
			public void mouseWheelMoved(final MouseWheelEvent mouseWheelEvent)
			{
				if (mouseWheelEvent.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL)
				{
					if (mouseWheelEvent.getWheelRotation() < 0)
					{
						increaseZoom((JComponent) mouseWheelEvent.getComponent(), true);
					}
					else
					{
						decreaseZoom((JComponent) mouseWheelEvent.getComponent(), true);
					}
				}
			}

			private synchronized void increaseZoom(final JComponent chart, final boolean saveAction)
			{
				final ChartPanel chartPanel = (ChartPanel) chart;
				zoomChartAxis(chartPanel, true);
			}

			private synchronized void decreaseZoom(final JComponent chart, final boolean saveAction)
			{
				final ChartPanel chartPanel = (ChartPanel) chart;
				zoomChartAxis(chartPanel, false);
			}

			private void zoomChartAxis(final ChartPanel chartPanel, final boolean increase)
			{
				final int width = chartPanel.getMaximumDrawWidth() - chartPanel.getMinimumDrawWidth();
				final int height = chartPanel.getMaximumDrawHeight() - chartPanel.getMinimumDrawWidth();
				if (increase)
				{
					chartPanel.zoomInBoth(width / 2., height / 2.);
				}
				else
				{
					chartPanel.zoomOutBoth(width / 2., height / 2.);
				}
			}
		});
	}
}
