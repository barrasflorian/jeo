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

import jeo.common.structure.Pair;

public interface IWorkQueue<W extends Worker<W, I, O>, I, O>
{
	////////////////////////////////////////////////////////////////////////////
	// WORKERS
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Initializes the working threads.
	 * <p>
	 * @param model the model of the working threads
	 */
	public void initWorkers(final W model);

	/**
	 * Reserves n working threads.
	 * <p>
	 * @param n the number of working threads to be reserved
	 * <p>
	 * @return {@code true} if the working threads are reserved, {@code false}
	 *         otherwise
	 */
	public boolean reserveWorkers(final int n);


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
	public Long addTask(final I input);

	/**
	 * Gets the next task from the list.
	 * <p>
	 * @return the next task of the list to be processed
	 */
	public Pair<Long, I> getNextTask();


	////////////////////////////////////////////////////////////////////////////
	// RESULT
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Adds the result of the task with the specified identifier to the list.
	 * <p>
	 * @param id     the identifier of the task
	 * @param result the result of the task
	 */
	public void addResult(final Long id, final Report<O> result);

	/**
	 * Gets the result of the task with the specified identifier.
	 * <p>
	 * @param id the identifier of the task
	 * <p>
	 * @return the result of the task with the specified identifier
	 */
	public Report<O> getResult(final Long id);


	////////////////////////////////////////////////////////////////////////////
	// POOL
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Ends the pool of working threads.
	 */
	public void end();

	/**
	 * Returns {@code true} if the pool of working threads is running,
	 * {@code false} otherwise.
	 * <p>
	 * @return {@code true} if the pool of working threads is running,
	 *         {@code false} otherwise
	 */
	public boolean isRunning();
}
