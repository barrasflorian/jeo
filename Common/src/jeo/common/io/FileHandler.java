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
package jeo.common.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.Charset;

import jeo.common.util.Formats;
import jeo.common.util.Strings;

public class FileHandler
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	private final String pathname;
	private BufferedWriter writer;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public FileHandler(final String pathname)
	{
		this.pathname = pathname;
		this.writer = null;
	}

	public FileHandler(final File file)
		throws IOException
	{
		this.pathname = file.getCanonicalPath();
		this.writer = null;
	}


	////////////////////////////////////////////////////////////////////////////
	// PATHS
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Gets the current pathname.
	 * <p>
	 * @return the current pathname {@link String}
	 */
	public static String getCurrentPath()
	{
		return new File(".").getAbsolutePath();
	}


	////////////////////////////////////////////////////////////////////////////
	// CREATE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates all the directories of this pathname.
	 * <p>
	 * @return {@code true} if the directories are created, {@code false}
	 *         otherwise
	 */
	public boolean createDirectories()
	{
		return createDirectories(pathname);
	}

	/**
	 * Creates all the directories of the specified pathname.
	 * <p>
	 * @param pathname the pathname {@link String}
	 * <p>
	 * @return {@code true} if the directories are created, {@code false}
	 *         otherwise
	 */
	public static boolean createDirectories(final String pathname)
	{
		final File file = new File(pathname);
		if (!file.exists())
		{
			if (!file.mkdirs())
			{
				IOManager.printError("Unable to create the directories '" + pathname + "'");
				return false;
			}
		}
		return true;
	}


	////////////////////////////////////////////////////////////////////////////
	// READ
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns a buffered reader.
	 * <p>
	 * @return a buffered reader
	 * <p>
	 * @throws FileNotFoundException
	 */
	public BufferedReader getReader()
		throws FileNotFoundException
	{
		return getReader(pathname, Formats.DEFAULT_CHARSET);
	}

	/**
	 * Returns a buffered reader.
	 * <p>
	 * @param charset the character set of the file to be read from
	 * <p>
	 * @return a buffered reader
	 * <p>
	 * @throws FileNotFoundException
	 */
	public BufferedReader getReader(final Charset charset)
		throws FileNotFoundException
	{
		return getReader(pathname, charset);
	}

	/**
	 * Returns a buffered reader.
	 * <p>
	 * @param pathname the pathname of the file to be read from
	 * <p>
	 * @return a buffered reader
	 * <p>
	 * @throws FileNotFoundException
	 */
	public static BufferedReader getReader(final String pathname)
		throws FileNotFoundException
	{
		return getReader(pathname, Formats.DEFAULT_CHARSET);
	}

	/**
	 * Returns a buffered reader.
	 * <p>
	 * @param pathname the pathname of the file to be read from
	 * @param charset  the character set of the file to be read from
	 * <p>
	 * @return a buffered reader
	 * <p>
	 * @throws FileNotFoundException
	 */
	public static BufferedReader getReader(final String pathname, final Charset charset)
		throws FileNotFoundException
	{
		return new BufferedReader(new InputStreamReader(new FileInputStream(pathname), charset));
	}

	/**
	 * Closes the stream and releases any system resources associated with it.
	 * Once the stream has been closed, further read(), ready(), mark(),
	 * reset(), or skip() invocations will throw an IOException. Closing a
	 * previously closed stream has no effect.
	 * <p>
	 * @param reader the reader to be closed
	 */
	public static void closeReader(final Reader reader)
	{
		if (reader != null)
		{
			try
			{
				reader.close();
			}
			catch (final IOException ex)
			{
				IOManager.printError(ex);
			}
		}
		else
		{
			IOManager.printWarning("The reader to be closed is null");
		}
	}

	/**
	 * Reads the file denoted by this pathname.
	 * <p>
	 * @return the content of the file
	 */
	public FileContent read()
	{
		return read(pathname, Formats.DEFAULT_CHARSET);
	}

	/**
	 * Reads the file denoted by the specified pathname.
	 * <p>
	 * @param pathname the pathname of the file to be read from
	 * <p>
	 * @return the content of the file
	 */
	public static FileContent read(final String pathname)
	{
		return read(pathname, Formats.DEFAULT_CHARSET);
	}

	/**
	 * Reads the file denoted by this pathname.
	 * <p>
	 * @param charset the character set of the file to be read from
	 * <p>
	 * @return the content of the file
	 */
	public FileContent read(final Charset charset)
	{
		return read(pathname, charset);
	}

	/**
	 * Reads the file denoted by the specified pathname.
	 * <p>
	 * @param pathname the pathname of the file to be read from
	 * @param charset  the character set of the file to be read from
	 * <p>
	 * @return the content of the file
	 */
	public static FileContent read(final String pathname, final Charset charset)
	{
		// File reader
		BufferedReader reader = null;
		final StringBuilder buffer = Strings.createBuffer();
		String currentLine;
		int linesNumber = 0;
		try
		{
			// Create a new file reader
			reader = getReader(pathname, charset);
			// Store the content of the file in buffer
			while ((currentLine = reader.readLine()) != null)
			{
				buffer.append(currentLine).append("\n");
				++linesNumber;
			}
		}
		catch (final IOException ex)
		{
			IOManager.printError(ex);
		}
		finally
		{
			// Close the file reader
			if (reader != null)
			{
				try
				{
					reader.close();
				}
				catch (final IOException ex)
				{
					IOManager.printError(ex);
				}
			}
		}
		return new FileContent(String.valueOf(buffer), linesNumber);
	}


	////////////////////////////////////////////////////////////////////////////
	// WRITE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Writes the specified string in the file denoted by this pathname.
	 * <p>
	 * @param string the {@link String} to be written
	 * <p>
	 * @return {@code true} if {@code string} is written in the file,
	 *         {@code false} otherwise
	 */
	public boolean writeLine(final String string)
	{
		return writeLine(string, true, Formats.DEFAULT_CHARSET);
	}

	/**
	 * Writes the specified string in the file denoted by the specified
	 * pathname.
	 * <p>
	 * @param string   the {@link String} to be written
	 * @param pathname the pathname of the file to be written to
	 * <p>
	 * @return {@code true} if {@code string} is written in the file,
	 *         {@code false} otherwise
	 */
	public static boolean writeLine(final String string, final String pathname)
	{
		return writeLine(string, pathname, true, Formats.DEFAULT_CHARSET);
	}

	/**
	 * Initializes this writer.
	 * <p>
	 * @throws FileNotFoundException
	 */
	public void initWriter()
		throws FileNotFoundException
	{
		if (writer == null)
		{
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathname)));
		}
	}

	/**
	 * Writes the specified string in the file denoted by this pathname.
	 * <p>
	 * @param string  the {@link String} to be written
	 * @param append  option specifying how the file is opened
	 * @param charset the character set of the file to be written to
	 * <p>
	 * @return {@code true} if {@code string} is written in the file,
	 *         {@code false} otherwise
	 */
	public boolean writeLine(final String string, final boolean append, final Charset charset)
	{
		try
		{
			// Initialize this writer
			initWriter();
			// Append string to the file
			writer.write(string + "\n");
			return true;
		}
		catch (final FileNotFoundException ex)
		{
			IOManager.printError(ex);
		}
		catch (final IOException ex)
		{
			IOManager.printError(ex);
		}
		return false;
	}

	/**
	 * Closes this writer.
	 */
	public void closeWriter()
	{
		try
		{
			if (writer != null)
			{
				writer.close();
			}
			else
			{
				IOManager.printWarning("The writer of '" + pathname + "' has already been closed");
			}
		}
		catch (final IOException ex)
		{
			IOManager.printWarning(ex);
		}
		writer = null;
	}

	/**
	 * Writes the specified string in the file denoted by the specified
	 * pathname.
	 * <p>
	 * @param string   the {@link String} to be written
	 * @param pathname the pathname of the file to be written to
	 * @param append   option specifying how the file is opened
	 * @param charset  the character set of the file to be written to
	 * <p>
	 * @return {@code true} if {@code string} is written in the file,
	 *         {@code false} otherwise
	 */
	public static boolean writeLine(final String string, final String pathname, final boolean append, final Charset charset)
	{
		// Flag
		boolean success = false;
		// File writer
		BufferedWriter writer = null;
		try
		{
			// Create a new file writer
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathname, append), charset));
			// Append string to the file
			writer.write(string + "\n");
			success = true;
		}
		catch (final FileNotFoundException ex)
		{
			IOManager.printError(ex);
		}
		catch (final IOException ex)
		{
			IOManager.printError(ex);
		}
		finally
		{
			// Close the file writer
			if (writer != null)
			{
				try
				{
					writer.close();
				}
				catch (final IOException ex)
				{
					IOManager.printError(ex);
				}
			}
		}
		return success;
	}


	////////////////////////////////////////////////////////////////////////////
	// EXISTS
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Tests whether the file or directory denoted by this pathname exists.
	 * <p>
	 * @return {@code true} if and only if the file (or directory) denoted by
	 *         this {@code pathname} exists, {@code false} otherwise
	 * <p>
	 * @throws SecurityException If a security manager exists and its
	 *                           {@link java.lang.SecurityManager#checkRead(java.lang.String)}
	 *                           method denies read access to the file or
	 *                           directory
	 */
	public boolean exists()
		throws SecurityException
	{
		return new File(pathname).exists();
	}

	/**
	 * Tests whether the file or directory denoted by this abstract pathname
	 * exists.
	 * <p>
	 * @param pathname the pathname of the file (or directory) to be tested
	 * <p>
	 * @return {@code true} if and only if the file (or directory) denoted by
	 *         {@code pathname} exists, {@code false} otherwise
	 * <p>
	 * @throws SecurityException If a security manager exists and its
	 *                           {@link java.lang.SecurityManager#checkRead(java.lang.String)}
	 *                           method denies read access to the file or
	 *                           directory
	 */
	public static boolean exists(final String pathname)
		throws SecurityException
	{
		return new File(pathname).exists();
	}


	////////////////////////////////////////////////////////////////////////////
	// DELETE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Deletes the file (or directory) denoted by this pathname.
	 * <p>
	 * @return {@code true} if the file (or directory) is deleted, {@code false}
	 *         otherwise
	 */
	public boolean delete()
	{
		closeWriter();
		return delete(pathname, false);
	}

	/**
	 * Deletes the file (or directory) denoted by the specified pathname.
	 * <p>
	 * @param pathname the pathname of the file (or directory) to be deleted
	 * <p>
	 * @return {@code true} if the file (or directory) is deleted, {@code false}
	 *         otherwise
	 */
	public static boolean delete(final String pathname)
	{
		return delete(pathname, false);
	}

	/**
	 * Deletes the file (or directory) denoted by this pathname.
	 * <p>
	 * @param force option specifying how to delete the file (or directory)
	 * <p>
	 * @return {@code true} if the file (or directory) is deleted, {@code false}
	 *         otherwise
	 */
	public boolean delete(final boolean force)
	{
		closeWriter();
		return delete(new File(pathname), force);
	}

	/**
	 * Deletes the file (or directory) denoted by the specified pathname.
	 * <p>
	 * @param pathname the pathname of the file (or directory) to be deleted
	 * @param force    option specifying how to delete the file (or directory)
	 * <p>
	 * @return {@code true} if the file (or directory) is deleted, {@code false}
	 *         otherwise
	 */
	public static boolean delete(final String pathname, final boolean force)
	{
		return delete(new File(pathname), force);
	}

	/**
	 * Deletes the specified file (or directory).
	 * <p>
	 * @param file  the file (or directory) to be deleted
	 * @param force option specifying how to delete the file (or directory)
	 * <p>
	 * @return {@code true} if the file (or directory) is deleted, {@code false}
	 *         otherwise
	 */
	public static boolean delete(final File file, final boolean force)
	{
		// Flag
		boolean success = false;
		// Make sure the file (or directory) exists
		if (file.exists())
		{
			// Check if it is not write protected
			if (file.canWrite())
			{
				// If it is a directory
				if (file.isDirectory())
				{
					final File[] files = file.listFiles();
					// And it contains files, then
					if (files.length > 0)
					{
						// If force is true, delete the files of the directory recursively
						if (force)
						{
							for (final File f : files)
							{
								delete(f, force);
							}
						}
						// Else, make sure the directory is empty
						else
						{
							IOManager.printWarning("Directory not empty '" + file + "'");
						}
					}
				}
				// Attempt to delete it
				success = file.delete();
				if (!success)
				{
					IOManager.printError("Deletion failed '" + file + "'");
				}
			}
			else
			{
				IOManager.printWarning("Write protected '" + file + "'");
			}
		}
		else
		{
			IOManager.printWarning("No such file or directory '" + file + "'");
		}
		return success;
	}
}
