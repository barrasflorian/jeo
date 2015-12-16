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

import jeo.common.io.IOManager;
import jeo.common.model.ICopyable;
import jeo.common.structure.Pair;

public abstract class Worker<W extends Worker<W, I, O>, I, O>
	extends Thread
	implements ICopyable<W>
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	private static long currentId = 0L;
	protected final IWorkQueue<W, I, O> workQueue;
	protected final long id;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public Worker(final IWorkQueue<W, I, O> workQueue)
	{
		this.workQueue = workQueue;
		this.id = currentId;
		++currentId;
	}


	////////////////////////////////////////////////////////////////////////////
	// ABSTRACT
	////////////////////////////////////////////////////////////////////////////

	public abstract W copy();

	public abstract Report<O> processInput(final I input);


	////////////////////////////////////////////////////////////////////////////
	// TASK
	////////////////////////////////////////////////////////////////////////////

	private Report<O> processTask(final Pair<Long, I> task)
	{
		IOManager.printDebug("Process task " + task.getFirst() + ": '" + task.getSecond() + "' ...");
		return processInput(task.getSecond());
	}


	////////////////////////////////////////////////////////////////////////////
	// THREAD
	////////////////////////////////////////////////////////////////////////////

	@Override
	public void run()
	{
		IOManager.printDebug("Pool worker " + id + " has started ...");
		Pair<Long, I> task;
		while (true)
		{
			task = workQueue.getNextTask();
			if (workQueue.isRunning())
			{
				Report<O> report;
				try
				{
					report = processTask(task);
				}
				catch (final RuntimeException ex)
				{
					report = new Report<O>(null, IOManager.printError(ex));
				}
				workQueue.addResult(task.getFirst(), report);
			}
			else
			{
				break;
			}
		}
		IOManager.printDebug("Pool worker " + id + " is finished.");
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public String toString()
	{
		return "PoolWorker(" + id + ")";
	}
}
