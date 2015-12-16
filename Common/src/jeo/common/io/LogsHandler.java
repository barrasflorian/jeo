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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jeo.common.util.Arguments;
import jeo.common.util.Strings;

public class LogsHandler
	extends IOHandler
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	// The names of the logs
	private static final String logName = "log.txt";
	private static final String errorLogName = "log_error.txt";
	// The path to the logs
	private final String logsPath;
	// The log
	private final Lock logLock = new ReentrantLock();
	private final String logPath;
	private final StringBuilder lineBuffer = Strings.createBuffer();
	// The error log
	private final Lock errorLogLock = new ReentrantLock();
	private final String errorLogPath;
	private final StringBuilder errorLineBuffer = Strings.createBuffer();


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public LogsHandler()
	{
		logsPath = FileHandler.getCurrentPath() + "\\logs";
		logPath = getPath(logName);
		errorLogPath = getPath(errorLogName);
	}

	public LogsHandler(final String logsPath)
	{
		this.logsPath = logsPath;
		logPath = getPath(logName);
		errorLogPath = getPath(errorLogName);
	}


	////////////////////////////////////////////////////////////////////////////
	// GENERAL
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Gets the pathname of the log denoted by the specified file name.
	 * <p>
	 * @param fileName the file name of the log
	 * <p>
	 * @return the pathname of the log
	 */
	private String getPath(final String fileName)
	{
		return logsPath + "/" + fileName;
	}

	/**
	 * Creates all the directories of the logs pathname.
	 * <p>
	 * @return {@code true} if the directories are created, {@code false}
	 *         otherwise
	 */
	private boolean createDirectories()
	{
		return FileHandler.createDirectories(logsPath);
	}


	////////////////////////////////////////////////////////////////////////////
	// PRINT
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Appends the specified object to the line buffer with the specified type.
	 * Note that the line buffer is written to the log when either
	 * {@code printLine} or {@code flush} is called.
	 * <p>
	 * @param object the {@link Object} to be written
	 * @param error  the type of log to be written to
	 */
	@Override
	public void print(final Object object, final boolean error)
	{
		// Check the argument(s)
		Arguments.requireNonNull(object);
		// Process
		updateLogLine(object, error);
	}

	/**
	 * Writes the specified object to the log with the specified type.
	 * <p>
	 * @param object the {@link Object} to be written
	 * @param error  the type of log to be written to
	 */
	@Override
	public void printLine(final Object object, final boolean error)
	{
		// Check the argument(s)
		Arguments.requireNonNull(object);
		// Process
		logLock.lock();
		try
		{
			if (createDirectories())
			{
				updateLogLine(object, error);
				FileHandler.writeLine(getLogLine(error), error ? errorLogPath : logPath);
				clearLogLine(error);
			}
		}
		finally
		{
			logLock.unlock();
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// FLUSH
	////////////////////////////////////////////////////////////////////////////

	public void flush()
	{
		flush(false);
		flush(true);
	}

	public void flush(final boolean error)
	{
		FileHandler.writeLine(getLogLine(error), error ? errorLogPath : logPath);
		clearLogLine(error);
	}


	////////////////////////////////////////////////////////////////////////////
	// LOG LINE
	////////////////////////////////////////////////////////////////////////////

	private void updateLogLine(final Object linePart, final boolean error)
	{
		if (error)
		{
			errorLineBuffer.append(linePart);
		}
		else
		{
			lineBuffer.append(linePart);
		}
	}

	private String getLogLine(final boolean error)
	{
		return error ? String.valueOf(errorLineBuffer) : String.valueOf(lineBuffer);
	}

	private void clearLogLine(final boolean error)
	{
		if (error)
		{
			errorLineBuffer.setLength(0);
		}
		else
		{
			lineBuffer.setLength(0);
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// DELETE
	////////////////////////////////////////////////////////////////////////////

	public void deleteLogs()
	{
		deleteLog();
		deleteErrorLog();
		if (FileHandler.exists(logsPath))
		{
			logLock.lock();
			try
			{
				errorLogLock.lock();
				try
				{
					FileHandler.delete(logsPath);
				}
				finally
				{
					errorLogLock.unlock();
				}
			}
			finally
			{
				logLock.unlock();
			}
		}
	}

	/**
	 * Deletes the log.
	 */
	public void deleteLog()
	{
		logLock.lock();
		try
		{
			if (FileHandler.exists(logPath))
			{
				FileHandler.delete(logPath);
			}
		}
		finally
		{
			logLock.unlock();
		}
	}

	/**
	 * Deletes the error log.
	 */
	public void deleteErrorLog()
	{
		errorLogLock.lock();
		try
		{
			if (FileHandler.exists(errorLogPath))
			{
				FileHandler.delete(errorLogPath);
			}
		}
		finally
		{
			errorLogLock.unlock();
		}
	}
}
