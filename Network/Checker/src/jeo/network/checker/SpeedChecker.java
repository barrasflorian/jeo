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
package jeo.network.checker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jeo.common.io.FileHandler;
import jeo.common.io.IOManager;
import jeo.common.thread.IWorkQueue;
import jeo.common.thread.Report;
import jeo.common.thread.WorkQueue;
import jeo.common.thread.Worker;
import jeo.common.util.Chronometer;
import jeo.common.util.Dates;
import jeo.common.util.Strings;

public class SpeedChecker
{
	private static final List<String> URLS = new ArrayList<String>(Arrays.asList("http://cachefly.cachefly.net/1mb.test", "http://cachefly.cachefly.net/10mb.test"));
	private static final int N_RUNS = 1000;
	private static final int TIME_INTERVAL = 30000; // [ms]
	private static final int TIME_OUT = 10000;
	private static final String TEMP_DIR = "C:/Temp";
	// The file handlers of the data files storing the downloading times of the files pointed by the URLs
	private static final Map<String, FileHandler> DATA_FILES = new HashMap<String, FileHandler>(URLS.size());
	// The pool of working threads
	private static final WorkQueue<Checker, String, Double> THREAD_POOL = new WorkQueue<Checker, String, Double>();
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

	/**
	 * Starts the speed checker.
	 * <p>
	 * @param args ignored
	 */
	public static void main(final String[] args)
	{
		init();

		for (int i = 0; i < N_RUNS; ++i)
		{
			check();
			try
			{
				Thread.sleep(TIME_INTERVAL);
			}
			catch (final InterruptedException ex)
			{
				IOManager.printError(ex);
			}
		}

		clear();
	}

	private static void init()
	{
		// Initialize the file handlers of the data files storing the downloading times of the files pointed by the URLs
		for (final String urlName : URLS)
		{
			// Create an URL
			final URL url;
			try
			{
				url = new URL(urlName);

				// Get the name of the file pointed by the URL
				final String fileName = url.getFile().replace("/", Strings.EMPTY);

				// Create a file handler of the data file storing the downloading times of the file pointed by the URL
				DATA_FILES.put(urlName, new FileHandler(TEMP_DIR + "/downloading_times_of_" + fileName + ".csv"));
			}
			catch (final MalformedURLException ex)
			{
				IOManager.printError("The URL '" + urlName + "' is malformed:");
				IOManager.printError(ex);
			}
		}

		// Initialize the workers of the thread pool
		THREAD_POOL.initWorkers(new Checker(THREAD_POOL));
	}

	private static void check()
	{
		// Process the downloads of the files pointed by the URLs (one by one)
		for (final String urlName : URLS)
		{
			final Long taskId = THREAD_POOL.addTask(urlName);
			final Report<Double> report = THREAD_POOL.getResult(taskId);
			final Double result = report.getOutput();
			IOManager.printInfo(DECIMAL_FORMAT.format(result));
			DATA_FILES.get(urlName).writeLine(Dates.getCurrentTime() + ";" + Double.toString(result));
		}
	}

	private static void clear()
	{
		// End the pool of working threads
		THREAD_POOL.end();

		// Close the file handlers of the data files storing the downloading times of the files pointed by the URLs
		for (final String urlName : URLS)
		{
			DATA_FILES.get(urlName).closeWriter();
		}
	}

	public static class Checker
		extends Worker<Checker, String, Double>
	{
		private final Chronometer chrono = new Chronometer();

		public Checker(final IWorkQueue<Checker, String, Double> workQueue)
		{
			super(workQueue);
		}

		private static boolean ping(final String hostname)
			throws IOException
		{
			final InetAddress host = InetAddress.getByName(hostname);
			return host.isReachable(TIME_OUT);
		}

		@Override
		public Checker copy()
		{
			IOManager.printDebug("Create copy of " + Checker.class.getSimpleName());
			return new Checker(workQueue);
		}

		@Override
		public Report<Double> processInput(final String urlName)
		{
			// Create an URL
			try
			{
				IOManager.printInfo("- Process URL '" + urlName + "'");
				// Create an URL
				final URL url = new URL(urlName);
				// Get the name of the file pointed by the URL
				final String fileName = url.getFile().replace("/", Strings.EMPTY);

				// Check if the host in the URL is reachable
				try
				{
					final String hostname = url.getHost();
					ping(hostname);
					IOManager.printInfo("  -> The host '" + hostname + "' is reachable");

					// Download the file pointed by the URL
					final File destinationFile = new File("C:/Temp/" + fileName);
					IOManager.printInfo("  -> Download the file '" + fileName + "' specified by the URL ...");
					ReadableByteChannel rbc = null;
					FileOutputStream tempFile = null;
					try
					{
						rbc = Channels.newChannel(url.openStream());
						tempFile = new FileOutputStream(destinationFile);
						chrono.start();
						tempFile.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
						chrono.stop();
						final double length = (destinationFile.length() / 1048576) * 8;
						final double time = chrono.getMilliseconds() / 1000.;
						final double speed = length / time;
						IOManager.printInfo("  => Downloaded " + DECIMAL_FORMAT.format(length) + " [Mbits] in " + DECIMAL_FORMAT.format(time) + " [s] => " + DECIMAL_FORMAT.format(speed) + " [Mbits/s]");

						IOManager.printInfo("  Done.");

						return new Report<Double>(speed, null);
					}
					catch (final IOException ex)
					{
						IOManager.printError("The file '" + urlName + "' cannot be transferred to '" + destinationFile.getCanonicalPath() + "':");
						return new Report<Double>(0., IOManager.printError(ex));
					}
					finally
					{
						if (rbc != null)
						{
							rbc.close();
						}
						if (tempFile != null)
						{
							tempFile.close();
						}
					}
				}
				catch (final IOException ex)
				{
					IOManager.printError("The URL '" + urlName + "' is not reachable:");
					return new Report<Double>(0., IOManager.printError(ex));
				}
			}
			catch (final MalformedURLException ex)
			{
				IOManager.printError("The URL '" + urlName + "' is malformed:");
				return new Report<Double>(0., IOManager.printError(ex));
			}

		}

	}
}
