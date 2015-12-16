/*
 * The MIT License
 *
 * Copyright 2015 Florian Barras.
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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReservedThreadPoolExecutor
	extends ThreadPoolExecutor
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * The maximum pool size.
	 */
	private volatile int maximumPoolSize;
	/**
	 * The internal lock for submission.
	 */
	private final ReentrantLock submitLock = new ReentrantLock();


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public ReservedThreadPoolExecutor(final int poolSize)
	{
		super(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		this.maximumPoolSize = poolSize;
	}

	public ReservedThreadPoolExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit, final BlockingQueue<Runnable> workQueue)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		this.maximumPoolSize = maximumPoolSize;
	}

	public ReservedThreadPoolExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit, final BlockingQueue<Runnable> workQueue, final ThreadFactory threadFactory)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
		this.maximumPoolSize = maximumPoolSize;
	}

	public ReservedThreadPoolExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit, final BlockingQueue<Runnable> workQueue, final RejectedExecutionHandler handler)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
		this.maximumPoolSize = maximumPoolSize;
	}

	public ReservedThreadPoolExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit, final BlockingQueue<Runnable> workQueue, final ThreadFactory threadFactory, final RejectedExecutionHandler handler)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		this.maximumPoolSize = maximumPoolSize;
	}


	////////////////////////////////////////////////////////////////////////////
	// CONDITIONAL SUBMIT
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Submits the specified task to the {@link ThreadPoolExecutor} if there is
	 * at least one thread that is not actively executing tasks. Returns a
	 * {@link Future} representing the pending results if the task is submitted,
	 * {@code null} otherwise.
	 * <p>
	 * @param task the task to be submitted
	 * <p>
	 * @return a {@link Future} representing the pending results if the task is
	 *         submitted, {@code null} otherwise
	 * <p>
	 * @throws RejectedExecutionException {@inheritDoc}
	 * @throws NullPointerException       {@inheritDoc}
	 */
	@Override
	public Future<?> submit(final Runnable task)
	{
		submitLock.lock();
		try
		{
			if (getActiveCount() < maximumPoolSize)
			{
				return super.submit(task);
			}
		}
		finally
		{
			submitLock.unlock();
		}
		return null;
	}

	/**
	 * Submits the specified task to the {@link ThreadPoolExecutor} if there is
	 * at least one thread that is not actively executing tasks. Returns a
	 * {@link Future} representing the pending results if the task is submitted,
	 * {@code null} otherwise.
	 * <p>
	 * @param task   the task to be submitted
	 * @param result the default value for the returned future
	 * <p>
	 * @return a {@link Future} representing the pending results if the task is
	 *         submitted, {@code null} otherwise
	 * <p>
	 * @throws RejectedExecutionException {@inheritDoc}
	 * @throws NullPointerException       {@inheritDoc}
	 */
	@Override
	public <T> Future<T> submit(final Runnable task, final T result)
	{
		submitLock.lock();
		try
		{
			if (getActiveCount() < maximumPoolSize)
			{
				return super.submit(task, result);
			}
		}
		finally
		{
			submitLock.unlock();
		}
		return null;
	}

	/**
	 * Submits the specified task to the {@link ThreadPoolExecutor} if there is
	 * at least one thread that is not actively executing tasks. Returns a
	 * {@link Future} representing the pending results if the task is submitted,
	 * {@code null} otherwise.
	 * <p>
	 * @param task the task to be submitted
	 * <p>
	 * @return a {@link Future} representing the pending results if the task is
	 *         submitted, {@code null} otherwise
	 * <p>
	 * @throws RejectedExecutionException {@inheritDoc}
	 * @throws NullPointerException       {@inheritDoc}
	 */
	@Override
	public <T> Future<T> submit(final Callable<T> task)
	{
		submitLock.lock();
		try
		{
			if (getActiveCount() < maximumPoolSize)
			{
				return super.submit(task);
			}
		}
		finally
		{
			submitLock.unlock();
		}
		return null;
	}
}
