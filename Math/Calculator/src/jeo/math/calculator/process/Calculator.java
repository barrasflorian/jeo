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
package jeo.math.calculator.process;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import jeo.common.exception.UnknownClassException;
import jeo.common.exception.UnknownTypeException;
import jeo.common.io.IOManager;
import jeo.common.io.Message;
import jeo.common.math.Maths;
import jeo.common.structure.tree.RedBlackTreeMap;
import jeo.common.thread.Report;
import jeo.common.thread.ReservedThreadPoolExecutor;
import jeo.common.util.Strings;
import jeo.math.calculator.model.BinaryOperation;
import jeo.math.calculator.model.Element;
import jeo.math.calculator.model.Element.Type;
import jeo.math.calculator.model.MatrixElement;
import jeo.math.calculator.model.Result;
import jeo.math.calculator.model.ScalarElement;
import jeo.math.calculator.model.UnaryOperation;
import jeo.math.linearalgebra.Entity;
import jeo.math.linearalgebra.Matrix;
import jeo.math.linearalgebra.Scalar;

public class Calculator
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * The flag for using threads.
	 */
	private static boolean USE_THREADS = true;
	/**
	 * The pool of threads.
	 */
	private static ExecutorService THREAD_POOL = null;
	/**
	 * The context containing the values of the variables.
	 */
	private volatile Map<String, Element> context;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public Calculator()
	{
		context = new RedBlackTreeMap<String, Element>();
	}


	////////////////////////////////////////////////////////////////////////////
	// CALCULATOR
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Initializes the working threads.
	 */
	public static void start()
	{
		start(USE_THREADS);
	}

	/**
	 * Initializes the working threads.
	 * <p>
	 * @param useThreads the flag for using threads
	 */
	public static void start(final boolean useThreads)
	{
		USE_THREADS = useThreads;
		end();
		if (USE_THREADS)
		{
			IOManager.printDebug("Create a pool of thread ...");
			THREAD_POOL = new ReservedThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
			IOManager.printDebug("Done!");
		}
		ExpressionHandler.init(useThreads);
	}

	/**
	 * Ends the pool of working threads.
	 */
	public static void end()
	{
		ExpressionHandler.end();
		if (USE_THREADS)
		{
			if (THREAD_POOL != null)
			{
				IOManager.printDebug("Shutdown the pool of threads ...");
				THREAD_POOL.shutdown();
				IOManager.printDebug("Done!");
			}
		}
	}

	/**
	 * Evaluates the specified tree of operations and numbers.
	 * <p>
	 * @param tree    the input {@link Element} to be evaluated
	 * @param context the context containing the values of the variables
	 * <p>
	 * @return the evaluation of {@code tree}
	 */
	public static Report<Entity> evaluateTree(final Element tree, final Map<String, Element> context)
	{
		if ((tree instanceof ScalarElement) || (tree instanceof MatrixElement))
		{
			final Entity entity = tree.getEntity();
			IOManager.printDebug("COMPUTE Get entity <" + entity + ">");
			return new Report<Entity>(entity, null);
		}
		else if (tree instanceof BinaryOperation)
		{
			return evaluateBinaryOperation((BinaryOperation) tree, context);
		}
		else if (tree instanceof UnaryOperation)
		{
			return evaluateUnaryOperation((UnaryOperation) tree, context);
		}
		else
		{
			return new Report<Entity>(null, new Message(new UnknownClassException(tree.getClass())));
		}
	}

	/**
	 * Evaluates the specified binary operation.
	 * <p>
	 * @param binaryOperation the input {@link BinaryOperation} to be evaluated
	 * @param context         the context containing the values of the variables
	 * <p>
	 * @return the evaluation of {@code binaryOperation}
	 */
	private static Report<Entity> evaluateBinaryOperation(final BinaryOperation binaryOperation, final Map<String, Element> context)
	{
		Report<Entity> leftEntityResult, rightEntityResult;
		if (USE_THREADS)
		{
			// Add the tasks
			final Future<Report<Entity>> futureLeftEntityResult = THREAD_POOL.submit(() -> Calculator.evaluateTree(binaryOperation.getLeft(), context));
			final Future<Report<Entity>> futureRightEntityResult = THREAD_POOL.submit(() -> Calculator.evaluateTree(binaryOperation.getRight(), context));

			// Get the results
			// - Left entity
			if (futureLeftEntityResult != null)
			{
				try
				{
					leftEntityResult = futureLeftEntityResult.get();
				}
				catch (final Exception ex)
				{
					leftEntityResult = new Report<Entity>(null, new Message(ex));
				}
			}
			else
			{
				leftEntityResult = evaluateTree(binaryOperation.getLeft(), context);
			}
			// - Right entity
			if (futureRightEntityResult != null)
			{
				try
				{
					rightEntityResult = futureRightEntityResult.get();
				}
				catch (final Exception ex)
				{
					rightEntityResult = new Report<Entity>(null, new Message(ex));
				}
			}
			else
			{
				rightEntityResult = evaluateTree(binaryOperation.getRight(), context);
			}
		}
		else
		{
			// Get the results
			leftEntityResult = evaluateTree(binaryOperation.getLeft(), context);
			rightEntityResult = evaluateTree(binaryOperation.getRight(), context);
		}

		// Get the entities from the results
		// - Left
		final Entity leftEntity = leftEntityResult.getOutput();
		if (leftEntity == null)
		{
			return leftEntityResult;
		}
		// - Right
		final Entity rightEntity = rightEntityResult.getOutput();
		if (rightEntity == null)
		{
			return rightEntityResult;
		}

		// Get the type of binary operation
		final Type type = binaryOperation.getType();
		IOManager.printDebug("COMPUTE " + leftEntity + " " + type + " " + rightEntity);

		// Evaluate the operation
		final Entity subresult;
		switch (type)
		{
			case ADDITION:
				subresult = leftEntity.plus(rightEntity);
				break;
			case SUBTRACTION:
				subresult = leftEntity.minus(rightEntity);
				break;
			case MULTIPLICATION:
				subresult = leftEntity.times(rightEntity);
				break;
			case DIVISION:
				subresult = leftEntity.division(rightEntity);
				break;
			case POWER:
				subresult = leftEntity.power(rightEntity);
				break;
			case SOLUTION:
				subresult = leftEntity.solution(rightEntity);
				break;
			default:
				return new Report<Entity>(null, new Message(new UnknownTypeException(type)));
		}

		return new Report<Entity>(subresult, null);
	}

	/**
	 * Evaluates the specified unary operation.
	 * <p>
	 * @param unaryOperation the input {@link UnaryOperation} to be evaluated
	 * @param context        the context containing the values of the variables
	 * <p>
	 * @return the evaluation of {@code unaryOperation}
	 */
	private static Report<Entity> evaluateUnaryOperation(final UnaryOperation unaryOperation, final Map<String, Element> context)
	{
		// Get the result
		final Report<Entity> result = evaluateTree(unaryOperation.getElement(), context);
		final Entity entity = result.getOutput();
		if (entity == null)
		{
			return new Report<Entity>(null, result.getMessage());
		}

		// Get the type of unary operation
		final Type type = unaryOperation.getType();
		IOManager.printDebug("COMPUTE " + type + " " + entity);

		// Evaluate the operation
		final Entity subresult;
		switch (type)
		{
			case FACTORIAL:
				final Scalar scalar = (Scalar) entity;
				final BigInteger bigInt = Maths.factorial(scalar.intValue());
				subresult = new Scalar(bigInt.doubleValue());
				break;
			case TRANSPOSE:
				subresult = entity.transpose();
				break;
			case INVERSE:
				subresult = entity.inverse();
				break;
			default:
				return new Report<Entity>(null, new Message(new UnknownTypeException(type)));
		}

		return new Report<Entity>(subresult, null);
	}

	/**
	 * Parses the specified expression to a tree of operations and numbers and
	 * evaluates it.
	 * <p>
	 * @param expression the input {@link String} to be parsed
	 * @param context    the context containing the values of the variables
	 * <p>
	 * @return the evaluation of {@code expression}
	 */
	private Report<Entity> evaluate(final String expression, final Map<String, Element> context)
		throws Exception
	{
		final Report<Element> result = ExpressionHandler.parseExpression(expression, context);
		final Element element = result.getOutput();
		if (element == null)
		{
			return new Report<Entity>(null, result.getMessage());
		}
		return evaluateTree(element, context);
	}

	/**
	 * Processes the specified expression (assignment or simple evaluation).
	 * <p>
	 * @param expression the input {@link String} to be parsed
	 * <p>
	 * @return the evaluation of {@code expression}
	 */
	public Result process(final String expression)
	{
		try
		{
			// Trim the expression
			String trimmedExpression = expression.trim();

			// Check if the epression is an assignment
			final List<String> expressions = Strings.split(trimmedExpression, '=');
			final int size = expressions.size();

			// If the expression is an assignment (or several assignments)
			if (size > 1)
			{
				// Extract the right-hand side of the expression
				trimmedExpression = expressions.get(size - 1).trim();
			}

			// Evaluate the (right-hand side) expression
			final Report<Entity> result = evaluate(trimmedExpression, context);
			final Entity entity = result.getOutput();
			if (entity == null)
			{
				return new Result(null, result.getMessage());
			}

			// Get the corresponding element
			final Element element;
			if (entity instanceof Scalar)
			{
				element = new ScalarElement(null, trimmedExpression, (Scalar) entity);
			}
			else if (entity instanceof Matrix)
			{
				element = new MatrixElement(null, trimmedExpression, (Matrix) entity);
			}
			else
			{
				return new Result(null, new Message(new UnknownClassException(entity.getClass())));
			}

			// If the expression is an assignment (or several assignments)
			if (size > 1)
			{
				// Set the corresponding variable(s)
				for (int i = 0; i < (size - 1); ++i)
				{
					context.put(expressions.get(i).trim(), element);
				}
			}

			// Return the evaluation of the (right-hand side) expression
			return new Result(entity, null);
		}
		catch (final Exception ex)
		{
			return new Result(null, IOManager.printError(ex));
		}
	}
}
