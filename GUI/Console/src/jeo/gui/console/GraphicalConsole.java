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
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import jeo.common.io.IOManager;
import jeo.common.util.Formats;

public class GraphicalConsole
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	private static final String title = "Console v" + Formats.VERSION;
	private final JFrame frame;
	private final JConsole console;
	private PrintStream ps;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public GraphicalConsole()
	{
		// Define a frame
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setSize(600, 400);
		// Define a console
		console = new JConsole();
		frame.add(console);
		// Redirect the input of IOManager to this input
		IOManager.setConsole(console);
		try
		{
			// Redirect the system output to the console
			ps = new PrintStream(new OutputStreamCapturer(console, System.out), true, Formats.DEFAULT_CHARSET_NAME);
			System.setOut(ps);
			System.setErr(ps);
			// Display the frame
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (final ClassNotFoundException ignored)
		{
		}
		catch (final IllegalAccessException ignored)
		{
		}
		catch (final InstantiationException ignored)
		{
		}
		catch (final UnsupportedLookAndFeelException ignored)
		{
		}
		catch (final UnsupportedEncodingException ex)
		{
			IOManager.printFatalError(ex);
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// GRAPHICAL CONSOLE
	////////////////////////////////////////////////////////////////////////////

	public void println(final String message)
	{
		console.println(message);
	}

	/**
	 * @return the input line of the console
	 */
	public String getInputLine()
	{
		return console.getInputLine();
	}

	/**
	 * @return the last line of the console
	 */
	public String getLastLine()
	{
		return console.getLastLine();
	}

	public void close()
	{
		ps.close();
	}
}
