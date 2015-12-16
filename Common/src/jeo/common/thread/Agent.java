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

public abstract class Agent<T>
	extends Thread
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	protected final T task;
	protected boolean running;
	protected boolean finished;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	protected Agent(final T task)
	{
		super();
		this.task = task;
	}


	////////////////////////////////////////////////////////////////////////////
	// THREAD
	////////////////////////////////////////////////////////////////////////////

	@Override
	public abstract void run();

	@Override
	public void start()
	{
		synchronized (task)
		{
			running = true;
			super.start();
		}
	}

	public void end()
	{
		synchronized (task)
		{
			running = false;
			task.notifyAll();
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// TASK
	////////////////////////////////////////////////////////////////////////////

	public void reset()
	{
		synchronized (task)
		{
			finished = false;
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// NOTIFY
	////////////////////////////////////////////////////////////////////////////

	public void notifyForTask()
	{
		notifyForTask(task);
	}

	public void notifyAllForTask()
	{
		notifyAllForTask(task);
	}

	protected void notifyForTask(final Object task)
	{
		synchronized (task)
		{
			finished = false;
			task.notifyAll();
		}
	}

	protected void notifyAllForTask(final Object task)
	{
		synchronized (task)
		{
			finished = false;
			task.notifyAll();
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// WAIT
	////////////////////////////////////////////////////////////////////////////

	protected void waitForTask()
	{
		waitForTask(task);
	}

	protected void waitForTask(final Object task)
	{
		synchronized (task)
		{
			finished = true;
			task.notifyAll();
			while (running && finished)
			{
				try
				{
					task.wait();
				}
				catch (final InterruptedException ex)
				{
				}
			}
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * @return {@code running}
	 */
	public boolean isRunning()
	{
		return running;
	}

	/**
	 * @return {@code finished}
	 */
	public boolean isFinished()
	{
		return finished;
	}
}
