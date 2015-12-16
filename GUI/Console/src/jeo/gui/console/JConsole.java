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

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import jeo.common.io.IConsole;
import jeo.common.io.IOManager;
import jeo.common.structure.ExtendedList;
import jeo.common.util.Formats;
import jeo.common.util.Strings;

/**
 * A JFC/Swing based console for the BeanShell desktop. This is a descendant of
 * the old AWTConsole.
 * <p>
 * Improvements by: Mark Donszelmann <Mark.Donszelmann@cern.ch> including Cut &
 * Paste
 * <p>
 * Improvements by: Daniel Leuck including Color and Image support, key press
 * bug workaround
 * <p>
 * @author Patrick Niemeyer, http://www.pat.net/~pat/
 * @version 2.0b5
 */
public class JConsole
	extends JScrollPane
	implements IConsole, Runnable, KeyListener, MouseListener, ActionListener,
			   PropertyChangeListener
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = -1027840442933614210L;
	private static final Font font = new Font("Monospaced", Font.PLAIN, 14);
	private static final String COPY = "Copy";
	private static final String CUT = "Cut";
	private static final String PASTE = "Paste";
	private static final String ZEROS = "000";
	private final List<String> history = new ExtendedList<String>();
	private final LinkedList<String> inputLines = new LinkedList<String>();
	private OutputStream outPipe;
	private InputStream inPipe;
	private InputStream in;
	private PrintStream out;
	private int commandStart = 0;
	private String startedLine;
	private int histLine = 0;
	private JPopupMenu menu;
	private JTextPane text;
	private boolean gotUp = true;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public JConsole()
	{
		super();
		init(null, null);
	}

	public JConsole(final InputStream cin, final OutputStream cout)
	{
		super();
		init(cin, cout);
	}


	////////////////////////////////////////////////////////////////////////////
	// JCONSOLE
	////////////////////////////////////////////////////////////////////////////

	private void init(final InputStream cin, final OutputStream cout)
	{
		// Special TextPane which catches for cut and paste, both L&F keys and
		// programmatic behaviour
		text = new JTextPane(new DefaultStyledDocument())
		{
			/**
			 * Generated serial version ID.
			 */
			private static final long serialVersionUID = 57146434650473797L;

			@Override
			public void cut()
			{
				if (text.getCaretPosition() < commandStart)
				{
					super.copy();
				}
				else
				{
					super.cut();
				}
			}

			@Override
			public void paste()
			{
				forceCaretMoveToEnd();
				super.paste();
			}
		};
		text.setText(Strings.EMPTY);
		text.setFont(font);
		text.setMargin(new Insets(7, 5, 7, 5));
		text.addKeyListener(this);
		setViewportView(text);
		// create popup menu
		menu = new JPopupMenu("Menu");
		menu.add(new JMenuItem(CUT)).addActionListener(this);
		menu.add(new JMenuItem(COPY)).addActionListener(this);
		menu.add(new JMenuItem(PASTE)).addActionListener(this);
		text.addMouseListener(this);
		// make sure popup menu follows Look & Feel
		UIManager.addPropertyChangeListener(this);
		outPipe = cout;
		if (outPipe == null)
		{
			outPipe = new PipedOutputStream();
			try
			{
				in = new PipedInputStream((PipedOutputStream) outPipe);
			}
			catch (final IOException ex)
			{
				print("Console internal	error (1)...", Color.red);
			}
		}
		inPipe = cin;
		if (inPipe == null)
		{
			final PipedOutputStream pout = new PipedOutputStream();
			try
			{
				out = new PrintStream(pout, true, Formats.DEFAULT_CHARSET_NAME);
				inPipe = new BlockingPipedInputStream(pout);
			}
			catch (final IOException ex)
			{
				print("Console internal error: " + ex, Color.red);
			}
		}
		// Start the inpipe watcher
		new Thread(this).start();
		requestFocus();
	}


	////////////////////////////////////////////////////////////////////////////
	// ICONSOLE
	////////////////////////////////////////////////////////////////////////////

	public String getInputLine()
	{
		String input;
		synchronized (inputLines)
		{
			while (inputLines.isEmpty())
			{
				try
				{
					inputLines.wait();
				}
				catch (final InterruptedException ignored)
				{
				}
			}
			input = inputLines.removeFirst();
		}
		return input;
	}

	public PrintStream getOut()
	{
		return out;
	}

	public PrintStream getErr()
	{
		return out;
	}


	////////////////////////////////////////////////////////////////////////////
	// INPUT
	////////////////////////////////////////////////////////////////////////////

	public Reader getReader()
	{
		return new InputStreamReader(in, Formats.DEFAULT_CHARSET);
	}

	public InputStream getInputStream()
	{
		return inPipe;
	}

	public List<String> getLines()
	{
		return history;
	}

	public String getLastLine()
	{
		return history.get(histLine);
	}


	////////////////////////////////////////////////////////////////////////////
	// OUTPUT
	////////////////////////////////////////////////////////////////////////////

	public void print(final Object o)
	{
		append(o);
	}

	/**
	 * Prints "\\n" (i.e. newline)
	 */
	public void println()
	{
		print("\n");
		text.repaint();
	}

	public void println(final Object o)
	{
		append(String.valueOf(o) + "\n");
	}

	public void error(final Object o)
	{
		print(o, Color.red);
	}


	////////////////////////////////////////////////////////////////////////////
	// PRINT
	////////////////////////////////////////////////////////////////////////////

	public void println(final Icon icon)
	{
		print(icon);
		println();
		text.repaint();
	}

	public void print(final Icon icon)
	{
		if (icon == null)
		{
			return;
		}
		append(icon);
	}

	public void print(final Object s, final Font font)
	{
		print(s, font, null);
	}

	public final void print(final Object s, final Color color)
	{
		print(s, null, color);
	}

	public void print(final Object object, final Font font, final Color color)
	{
		final AttributeSet old = getStyle();
		setStyle(font, color);
		append(String.valueOf(object));
		setStyle(old, true);
	}

	public void print(final Object s, final String fontFamilyName, final int size, final Color color)
	{
		print(s, fontFamilyName, size, color, false, false, false);
	}

	public void print(final Object object, final String fontFamilyName, final int size, final Color color, final boolean bold, final boolean italic, final boolean underline)
	{
		final AttributeSet old = getStyle();
		setStyle(fontFamilyName, size, color, bold, italic, underline);
		append(object);
		setStyle(old, true);
	}


	////////////////////////////////////////////////////////////////////////////
	// KEY LISTENER
	////////////////////////////////////////////////////////////////////////////

	@Override
	public final void requestFocus()
	{
		super.requestFocus();
		text.requestFocus();
	}

	public void keyPressed(final KeyEvent event)
	{
		type(event);
		gotUp = false;
	}

	public void keyTyped(final KeyEvent event)
	{
		type(event);
	}

	public void keyReleased(final KeyEvent event)
	{
		gotUp = true;
		type(event);
	}

	private synchronized void type(final KeyEvent event)
	{
		switch (event.getKeyCode())
		{
			case KeyEvent.VK_ENTER:
				if (event.getID() == KeyEvent.KEY_PRESSED)
				{
					if (gotUp)
					{
						enter();
					}
				}
				event.consume();
				text.repaint();
				break;
			case KeyEvent.VK_UP:
				if (event.getID() == KeyEvent.KEY_PRESSED)
				{
					historyUp();
				}
				event.consume();
				break;
			case KeyEvent.VK_DOWN:
				if (event.getID() == KeyEvent.KEY_PRESSED)
				{
					historyDown();
				}
				event.consume();
				break;
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_BACK_SPACE:
			case KeyEvent.VK_DELETE:
				if (text.getCaretPosition() <= commandStart)
				{
					// This doesn't work for backspace.
					// See default case for workaround
					event.consume();
				}
				break;
			case KeyEvent.VK_RIGHT:
				forceCaretMoveToStart();
				break;
			case KeyEvent.VK_HOME:
				text.setCaretPosition(commandStart);
				event.consume();
				break;
			case KeyEvent.VK_U: // clear line
				if ((event.getModifiers() & InputEvent.CTRL_MASK) > 0)
				{
					replaceRange(Strings.EMPTY, commandStart, textLength());
					histLine = 0;
					event.consume();
				}
				break;
			case KeyEvent.VK_ALT:
			case KeyEvent.VK_CAPS_LOCK:
			case KeyEvent.VK_CONTROL:
			case KeyEvent.VK_META:
			case KeyEvent.VK_SHIFT:
			case KeyEvent.VK_PRINTSCREEN:
			case KeyEvent.VK_SCROLL_LOCK:
			case KeyEvent.VK_PAUSE:
			case KeyEvent.VK_INSERT:
			case KeyEvent.VK_F1:
			case KeyEvent.VK_F2:
			case KeyEvent.VK_F3:
			case KeyEvent.VK_F4:
			case KeyEvent.VK_F5:
			case KeyEvent.VK_F6:
			case KeyEvent.VK_F7:
			case KeyEvent.VK_F8:
			case KeyEvent.VK_F9:
			case KeyEvent.VK_F10:
			case KeyEvent.VK_F11:
			case KeyEvent.VK_F12:
			case KeyEvent.VK_ESCAPE:
				// only modifier pressed
				break;
			// Control-C
			case KeyEvent.VK_C:
				if (text.getSelectedText() == null)
				{
					if (((event.getModifiers() & InputEvent.CTRL_MASK) > 0) && (event.getID() == KeyEvent.KEY_PRESSED))
					{
						append("^C");
					}
					event.consume();
				}
				break;
			default:
				if ((event.getModifiers() & (InputEvent.CTRL_MASK | InputEvent.ALT_MASK | InputEvent.META_MASK)) == 0)
				{
					// plain character
					forceCaretMoveToEnd();
				}
				/*
				 * The getKeyCode function always returns VK_UNDEFINED for
				 * keyTyped events, so backspace is not fully consumed.
				 */
				if (event.paramString().indexOf("Backspace") != -1)
				{
					if (text.getCaretPosition() <= commandStart)
					{
						event.consume();
						break;
					}
				}
				break;
		}
	}

	private void enter()
	{
		String s = getCmd();
		// Special hack for empty return
		if (s.length() == 0)
		{
			s = ";\n";
		}
		else
		{
			history.add(s);
			synchronized (inputLines)
			{
				inputLines.add(s);
				inputLines.notifyAll();
			}
			s += "\n";
		}
		append("\n");
		histLine = 0;
		acceptLine(s);
	}

	private String getCmd()
	{
		String s = Strings.EMPTY;
		try
		{
			s = text.getText(commandStart, textLength() - commandStart);
		}
		catch (final BadLocationException ex)
		{
			IOManager.printError("Internal JConsole Error: " + ex);
		}
		return s;
	}

	public synchronized void append(final Object object)
	{
		if (object instanceof String)
		{
			final int slen = textLength();
			text.select(slen, slen);
			text.replaceSelection((String) object);
		}
		else if (object instanceof Icon)
		{
			text.insertIcon((Icon) object);
		}
		commandStart = textLength();
		text.setCaretPosition(commandStart);
		text.repaint();
	}


	////////////////////////////////////////////////////////////////////////////
	// RANGE
	////////////////////////////////////////////////////////////////////////////

	private String replaceRange(final Object s, final int start, final int end)
	{
		final String st = s.toString();
		text.select(start, end);
		text.replaceSelection(st);
		// text.repaint();
		return st;
	}


	////////////////////////////////////////////////////////////////////////////
	// CARET
	////////////////////////////////////////////////////////////////////////////

	private void forceCaretMoveToEnd()
	{
		if (text.getCaretPosition() < commandStart)
		{
			// move caret first!
			text.setCaretPosition(textLength());
		}
		text.repaint();
	}

	private void forceCaretMoveToStart()
	{
		if (text.getCaretPosition() < commandStart)
		{
			// move caret first!
			text.setCaretPosition(textLength());
		}
		text.repaint();
	}


	////////////////////////////////////////////////////////////////////////////
	// HISTORY
	////////////////////////////////////////////////////////////////////////////

	private void historyUp()
	{
		if (history.isEmpty())
		{
			return;
		}
		// save current line
		if (histLine == 0)
		{
			startedLine = getCmd();
		}
		if (histLine < history.size())
		{
			++histLine;
			showHistoryLine();
		}
	}

	private void historyDown()
	{
		if (histLine == 0)
		{
			return;
		}
		histLine--;
		showHistoryLine();
	}

	private void showHistoryLine()
	{
		String showline;
		if (histLine == 0)
		{
			showline = startedLine;
		}
		else
		{
			showline = history.get(history.size() - histLine);
		}
		replaceRange(showline, commandStart, textLength());
		text.setCaretPosition(textLength());
		text.repaint();
	}


	////////////////////////////////////////////////////////////////////////////
	// LINE
	////////////////////////////////////////////////////////////////////////////

	private void acceptLine(String line)
	{
		// Handle Unicode characters
		final StringBuilder buffer = Strings.createBuffer();
		final int lineLength = line.length();
		for (int i = 0; i < lineLength; ++i)
		{
			String val = Integer.toString(line.charAt(i), 16);
			val = ZEROS.substring(0, 4 - val.length()) + val;
			buffer.append("\\u").append(val);
		}
		line = String.valueOf(buffer);
		if (outPipe == null)
		{
			print("Console internal	error: cannot output ...", Color.red);
		}
		else
		{
			try
			{
				outPipe.write(line.getBytes(Formats.DEFAULT_CHARSET.name()));
				outPipe.flush();
			}
			catch (final IOException ex)
			{
				outPipe = null;
				throw new RuntimeException("Console pipe broken");
			}
		}
		// text.repaint();
	}


	////////////////////////////////////////////////////////////////////////////
	// STYLE
	////////////////////////////////////////////////////////////////////////////

	private AttributeSet setStyle(final Font font, final Color color)
	{
		if (font != null)
		{
			return setStyle(font.getFamily(), font.getSize(), color, font.isBold(), font.isItalic(), StyleConstants.isUnderline(getStyle()));
		}
		else
		{
			return setStyle(null, -1, color);
		}
	}

	private AttributeSet setStyle(final String fontFamilyName, final int size, final Color color)
	{
		final MutableAttributeSet attr = new SimpleAttributeSet();
		if (color != null)
		{
			StyleConstants.setForeground(attr, color);
		}
		if (fontFamilyName != null)
		{
			StyleConstants.setFontFamily(attr, fontFamilyName);
		}
		if (size != -1)
		{
			StyleConstants.setFontSize(attr, size);
		}
		setStyle(attr);
		return getStyle();
	}

	private AttributeSet setStyle(final String fontFamilyName, final int size, final Color color, final boolean bold, final boolean italic, final boolean underline)
	{
		final MutableAttributeSet attr = new SimpleAttributeSet();
		if (color != null)
		{
			StyleConstants.setForeground(attr, color);
		}
		if (fontFamilyName != null)
		{
			StyleConstants.setFontFamily(attr, fontFamilyName);
		}
		if (size != -1)
		{
			StyleConstants.setFontSize(attr, size);
		}
		StyleConstants.setBold(attr, bold);
		StyleConstants.setItalic(attr, italic);
		StyleConstants.setUnderline(attr, underline);
		setStyle(attr);
		return getStyle();
	}

	private void setStyle(final AttributeSet attributes)
	{
		setStyle(attributes, false);
	}

	private void setStyle(final AttributeSet attributes, final boolean overWrite)
	{
		text.setCharacterAttributes(attributes, overWrite);
	}

	private AttributeSet getStyle()
	{
		return text.getCharacterAttributes();
	}

	@Override
	public void setFont(final Font font)
	{
		super.setFont(font);
		if (text != null)
		{
			text.setFont(font);
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// RUNNABLE
	////////////////////////////////////////////////////////////////////////////

	public void run()
	{
		try
		{
			inPipeWatcher();
		}
		catch (final IOException ex)
		{
			print("Console: I/O Error: " + ex + "\n", Color.red);
		}
	}

	private void inPipeWatcher()
		throws IOException
	{
		// Arbitrary blocking factor
		final byte[] ba = new byte[256];
		int read;
		while ((read = inPipe.read(ba)) != -1)
		{
			print(new String(ba, 0, read, Formats.DEFAULT_CHARSET.name()));
			// text.repaint();
		}
		println("Console: Input	closed...");
	}


	////////////////////////////////////////////////////////////////////////////
	// MOUSE LISTENER
	////////////////////////////////////////////////////////////////////////////

	public void mouseClicked(final MouseEvent event)
	{
	}

	public void mousePressed(final MouseEvent event)
	{
		if (event.isPopupTrigger())
		{
			menu.show((Component) event.getSource(), event.getX(), event.getY());
		}
	}

	public void mouseReleased(final MouseEvent event)
	{
		if (event.isPopupTrigger())
		{
			menu.show((Component) event.getSource(), event.getX(), event.getY());
		}
		text.repaint();
	}

	public void mouseEntered(final MouseEvent event)
	{
	}

	public void mouseExited(final MouseEvent event)
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// PROPERTY CHANGE LISTENER
	////////////////////////////////////////////////////////////////////////////

	public void propertyChange(final PropertyChangeEvent event)
	{
		if (event.getPropertyName().equals("lookAndFeel"))
		{
			SwingUtilities.updateComponentTreeUI(menu);
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// ACTION LISTENER
	////////////////////////////////////////////////////////////////////////////

	// Handle cut, copy and paste
	public void actionPerformed(final ActionEvent event)
	{
		final String cmd = event.getActionCommand();
		if (cmd.equals(CUT))
		{
			text.cut();
		}
		else if (cmd.equals(COPY))
		{
			text.copy();
		}
		else if (cmd.equals(PASTE))
		{
			text.paste();
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// ACTION LISTENER
	////////////////////////////////////////////////////////////////////////////

	/**
	 * The overridden read method in this class will not throw "Broken pipe"
	 * IOExceptions; it will simply wait for new writers and data. This is used
	 * by the JConsole internal read thread to allow writers in different (and
	 * in particular ephemeral) threads to write to the pipe.
	 * <p>
	 * It also checks a little more frequently than the original read().
	 * <p>
	 * Warning: read() will not even error on a read to an explicitly closed
	 * pipe (override closed to for that).
	 */
	public static class BlockingPipedInputStream
		extends PipedInputStream
	{
		boolean closed;

		public BlockingPipedInputStream(final PipedOutputStream pout)
			throws IOException
		{
			super(pout);
		}

		@Override
		public synchronized int read()
			throws IOException
		{
			if (closed)
			{
				throw new IOException("Stream closed");
			}
			// While no data
			while (super.in < 0)
			{
				// Notify any writers to wake up
				notifyAll();
				try
				{
					wait(750);
				}
				catch (final InterruptedException ex)
				{
					throw new InterruptedIOException();
				}
			}
			// This is what the superclass does
			final int ret = buffer[super.out++] & 0xFF;
			if (super.out >= buffer.length)
			{
				super.out = 0;
			}
			if (super.in == super.out)
			{
				super.in = -1;
			}
			return ret;
		}

		@Override
		public void close()
			throws IOException
		{
			closed = true;
			super.close();
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// CURSOR
	////////////////////////////////////////////////////////////////////////////

	public void setWaitFeedback(final boolean on)
	{
		if (on)
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
		else
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// TEXT
	////////////////////////////////////////////////////////////////////////////

	private int textLength()
	{
		return text.getDocument().getLength();
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public String toString()
	{
		return "BeanShell console";
	}
}
