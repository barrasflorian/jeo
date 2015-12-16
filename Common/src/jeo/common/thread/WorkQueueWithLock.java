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
package jeo.common.thread;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jeo.common.io.IOManager;
import jeo.common.structure.Pair;
import jeo.common.util.Collections;

public class WorkQueueWithLock<W extends Worker<W, I, O>, I, O>
	implements IWorkQueue<W, I, O>
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	// Threads
	private static final int N_THREADS_MIN = 2;
	private static final int N_THREADS_MAX = Runtime.getRuntime().availableProcessors();
	private volatile boolean running = true;
	// Workers
	private W model;
	private final Stack<W> workers = new Stack<W>();
	private final Lock workersLock = new ReentrantLock();
	private int nWorkers = 0;
	private int nReservedWorkers = 0;
	// Tasks
	private final LinkedList<Pair<Long, I>> tasks = new LinkedList<Pair<Long, I>>();
	private final Lock tasksLock = new ReentrantLock();
	private final Condition tasksLockCondition = tasksLock.newCondition();
	private Long currentId = 0L;
	// Results
	private final Map<Long, Report<O>> results = new HashMap<Long, Report<O>>(Collections.DEFAULT_INITIAL_CAPACITY);
	private final Lock resultsLock = new ReentrantLock();
	private final Condition resultsLockCondition = resultsLock.newCondition();


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public WorkQueueWithLock()
	{
		model = null;
	}


	////////////////////////////////////////////////////////////////////////////
	// WORKERS
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Initializes the working threads.
	 * <p>
	 * @param model the model of the working threads
	 */
	public void initWorkers(final W model)
	{
		this.model = model;
		createWorkers(N_THREADS_MIN);
	}

	/**
	 * Instantiates n working threads according to the model.
	 * <p>
	 * @param n the number of working threads to be created
	 * <p>
	 * @return {@code true} if the working threads are created, {@code false}
	 *         otherwise
	 */
	private boolean createWorkers(final int n)
	{
		boolean success;
		try
		{
			for (int i = 0; i < n; ++i)
			{
				createWorker();
			}
			success = true;
		}
		catch (final Exception ex)
		{
			IOManager.printError(ex);
			success = false;
		}
		return success;
	}

	/**
	 * Instantiates a working thread according to the model.
	 * <p>
	 * @throws Exception if the maximum number of threads has been reached
	 */
	private void createWorker()
		throws Exception
	{
		workersLock.lock();
		try
		{
			IOManager.printDebug("Create the thread " + (nWorkers + 1) + " ...");
			if (nWorkers < N_THREADS_MAX)
			{
				final W worker = model.copy();
				workers.push(worker);
				++nWorkers;
				worker.start();
			}
			else
			{
				throw new Exception("The maximum number of threads (" + N_THREADS_MAX + ") has been reached");
			}
		}
		finally
		{
			workersLock.unlock();
		}
	}

	/**
	 * Reserves n working threads.
	 * <p>
	 * @param n the number of working threads to be reserved
	 * <p>
	 * @return {@code true} if the working threads are reserved, {@code false}
	 *         otherwise
	 */
	public boolean reserveWorkers(final int n)
	{
		boolean areReserved;
		workersLock.lock();
		try
		{
			IOManager.printDebug("Reserve " + n + " threads ...");
			if ((N_THREADS_MAX - nReservedWorkers) >= n)
			{
				nReservedWorkers += n;
				// Create more pool workers if required
				if (!createWorkers(nReservedWorkers - nWorkers))
				{
					IOManager.printDebug("Reserve: OK (total reserved: " + nReservedWorkers + ")");
				}
				else
				{
					IOManager.printDebug("Reserve: NO (total reserved: " + nReservedWorkers + ")");
				}
				areReserved = true;
			}
			else
			{
				IOManager.printDebug("Reserve: NO (total reserved: " + nReservedWorkers + ")");
				areReserved = false;
			}
		}
		finally
		{
			workersLock.unlock();
		}
		return areReserved;
	}


	////////////////////////////////////////////////////////////////////////////
	// TASK
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Adds a task with the specified input to the list and returns its
	 * identifier.
	 * <p>
	 * @param input the input of the task to be added
	 * <p>
	 * @return the identifier of the added task
	 */
	public Long addTask(final I input)
	{
		tasksLock.lock();
		try
		{
			++currentId;
			IOManager.printDebug("Add the task " + currentId + ": '" + input + "'");
			tasks.add(new Pair<Long, I>(currentId, input));
			tasksLockCondition.signal();
			return currentId;
		}
		finally
		{
			tasksLock.unlock();
		}
	}

	/**
	 * Gets the next task from the list.
	 * <p>
	 * @return the next task of the list to be processed
	 */
	public Pair<Long, I> getNextTask()
	{
		Pair<Long, I> task = null;
		tasksLock.lock();
		try
		{
			IOManager.printDebug("Get the next task ...");
			while (running && tasks.isEmpty())
			{
				tasksLockCondition.await();
			}
			if (running)
			{
				task = tasks.removeFirst();
			}
		}
		catch (final InterruptedException ignored)
		{
		}
		finally
		{
			tasksLock.unlock();
		}
		return task;
	}


	////////////////////////////////////////////////////////////////////////////
	// RESULT
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Adds the result of the task with the specified identifier to the list.
	 * <p>
	 * @param id     the identifier of the task
	 * @param result the result of the task
	 */
	public void addResult(final Long id, final Report<O> result)
	{
		resultsLock.lock();
		try
		{
			IOManager.printDebug("Add the result of the task " + id);
			results.put(id, result);
			resultsLockCondition.signalAll();
		}
		finally
		{
			resultsLock.unlock();
		}
		workersLock.lock();
		try
		{
			--nReservedWorkers;
		}
		finally
		{
			workersLock.unlock();
		}
	}

	/**
	 * Gets the result of the task with the specified identifier.
	 * <p>
	 * @param id the identifier of the task
	 * <p>
	 * @return the result of the task with the specified identifier
	 */
	public Report<O> getResult(final Long id)
	{
		Report<O> result = null;
		resultsLock.lock();
		try
		{
			IOManager.printDebug("Get the result of the task " + id + " ...");
			while (!results.containsKey(id))
			{
				try
				{
					resultsLockCondition.await();
				}
				catch (final InterruptedException ignored)
				{
				}
			}
			result = results.remove(id);
		}
		finally
		{
			resultsLock.unlock();
		}
		return result;
	}

	/**
	 * Ends the pool of working threads.
	 */
	public void end()
	{
		tasksLock.lock();
		try
		{
			IOManager.printDebug("End the pool ...");
			running = false;
			tasksLockCondition.signalAll();
		}
		finally
		{
			tasksLock.unlock();
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// POOL
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns {@code true} if the pool of working threads is running,
	 * {@code false} otherwise.
	 * <p>
	 * @return {@code true} if the pool of working threads is running,
	 *         {@code false} otherwise
	 */
	public boolean isRunning()
	{
		return running;
	}
}
