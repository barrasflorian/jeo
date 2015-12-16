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
package jeo.gui.console;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JPanel;

public class CapturePane
	extends JPanel
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 4533410671018353842L;
	private final JConsole output;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public CapturePane()
	{
		setLayout(new BorderLayout());
		output = new JConsole();
		add(output);
	}


	////////////////////////////////////////////////////////////////////////////
	// JPANEL
	////////////////////////////////////////////////////////////////////////////

	public void appendText(final String text)
	{
		if (EventQueue.isDispatchThread())
		{
			output.print(text);
		}
		else
		{
			EventQueue.invokeLater(new Runnable()
			{
				public void run()
				{
					appendText(text);
				}
			});
		}
	}
}
