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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import jeo.common.exception.NoElementException;
import jeo.common.io.IOManager;
import jeo.common.io.Message;
import jeo.common.math.IGroup;
import jeo.common.math.Interval;
import jeo.common.math.IntervalCollection;
import jeo.common.structure.ExtendedList;
import jeo.common.thread.Report;
import jeo.common.thread.ReservedThreadPoolExecutor;
import jeo.common.util.Strings;
import jeo.math.calculator.model.BinaryOperation;
import jeo.math.calculator.model.Element;
import jeo.math.calculator.model.Element.Type;
import jeo.math.calculator.model.MatrixElement;
import jeo.math.calculator.model.ScalarElement;
import jeo.math.calculator.model.UnaryOperation;
import jeo.math.linearalgebra.Matrix;

public class ExpressionHandler
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
	 * The list of unary operators.
	 */
	private static final List<List<Character>> UNARY_OPERATORS = new ArrayList<List<Character>>(Arrays.asList(Arrays.asList('!', '\''), Arrays.asList('@')));
	/**
	 * The list of binary operators.
	 */
	private static final List<List<Character>> BINARY_OPERATORS = new ArrayList<List<Character>>(Arrays.asList(Arrays.asList('+', '-'), Arrays.asList('*', '/'), Arrays.asList('^'), Arrays.asList('~')));


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private ExpressionHandler()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// EXPRESSION HANDLER
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Initializes the pool of working threads.
	 */
	public static void init()
	{
		init(USE_THREADS);
	}

	/**
	 * Initializes the pool of working threads.
	 * <p>
	 * @param useThreads the flag for using threads
	 */
	public static void init(final boolean useThreads)
	{
		USE_THREADS = useThreads;
		end();
		if (USE_THREADS)
		{
			IOManager.printDebug("Create a pool of thread ...");
			THREAD_POOL = new ReservedThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
			IOManager.printDebug("Done!");
		}
	}

	/**
	 * Ends the pool of working threads.
	 */
	public static void end()
	{
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
	 * Returns a tree whose nodes and leaves correspond respectively to the
	 * operations and numbers of the specified expression.
	 * <p>
	 * @param expression the expression to be parsed
	 * @param context    the context containing the values of the variables
	 * <p>
	 * @return a tree of operations and numbers corresponding to
	 *         {@code expression}
	 */
	public static Report<Element> parseExpression(final String expression, final Map<String, Element> context)
	{
		return parseExpression(null, expression, context);
	}

	/**
	 * Returns a tree whose nodes and leaves correspond respectively to the
	 * operations and numbers of the specified expression.
	 * <p>
	 * @param parent     the parent node of the specified expression
	 * @param expression the expression to be parsed
	 * @param context    the context containing the values of the variables
	 * <p>
	 * @return a node (or leaf) corresponding to {@code expression} with
	 *         {@code parent}
	 */
	public static Report<Element> parseExpression(final Element parent, final String expression, final Map<String, Element> context)
	{
		// Trim the expression
		final String trimmedExpression = expression.trim();
		IOManager.printDebug("EXPRESSION: '" + trimmedExpression + "'");

		/*
		 * \ /\ getFirstParenthesesInterval (right is computed first)
		 *
		 * / /\ getLastParenthesesInterval (left is computed first)
		 */
		final Interval<Integer> parenthesesIndexes = getLastParenthesesInterval(trimmedExpression);

		// Get the index of the binary operator (if any)
		final int binaryOperatorIndex = getBinaryOperatorIndex(trimmedExpression, parenthesesIndexes);

		// Parse the binary operation
		if (binaryOperatorIndex >= 0)
		{
			return parseBinaryOperation(parent, trimmedExpression, binaryOperatorIndex, context);
		}

		// Parse the unary operation
		else
		{
			return parseUnaryOperation(parent, trimmedExpression, parenthesesIndexes, context);
		}
	}

	/**
	 * Returns the index of the binary operator in {@code trimmedExpression}, or
	 * {@code -1} if there is no such occurrence.
	 * <p>
	 * @param trimmedExpression the expression to be parsed
	 * @param parentheses       the indexes of the parenthesis in
	 *                          {@code trimmedExpression}
	 * <p>
	 * @return
	 */
	private static int getBinaryOperatorIndex(final String trimmedExpression, final Interval<Integer> parenthesesIndexes)
	{
		final int binaryOperatorIndex;

		if (USE_THREADS)
		{
			final IGroup<Integer> parentheses = getParenthesesIntervals(trimmedExpression);
			final ExtendedList<Integer> indexes = getBinaryOperatorIndexes(trimmedExpression, parentheses);

			IOManager.printDebug("Indexes: " + indexes.toString());
			if (indexes.size() > 0)
			{
				binaryOperatorIndex = indexes.getMiddleElement();
			}
			else
			{
				binaryOperatorIndex = -1;
			}
		}
		else
		{
			binaryOperatorIndex = getLastBinaryOperatorIndex(trimmedExpression, parenthesesIndexes);
		}

		return binaryOperatorIndex;
	}

	private static Report<Element> parseBinaryOperation(final Element parent, final String trimmedExpression, final int binaryOperatorIndex, final Map<String, Element> context)
	{
		// Get the binary operator
		final Type type = getType(trimmedExpression.charAt(binaryOperatorIndex));
		IOManager.printDebug("Type: " + type);

		// Extract the left and right expressions
		final String leftExpression = trimmedExpression.substring(0, binaryOperatorIndex);
		final String rightExpression = trimmedExpression.substring(binaryOperatorIndex + 1);

		// Parse the expressions
		Element leftTree, rightTree;
		Report<Element> leftTreeResult, rightTreeResult;
		if (USE_THREADS)
		{
			Future<Report<Element>> futureLeftTreeResult, futureRightTreeResult;

			// Submit the tasks
			IOManager.printDebug("Submit '" + leftExpression + "' ...");
			futureLeftTreeResult = THREAD_POOL.submit(() -> parseExpression(parent, leftExpression, context));

			IOManager.printDebug("Submit '" + rightExpression + "' ...");
			futureRightTreeResult = THREAD_POOL.submit(() -> parseExpression(parent, rightExpression, context));

			// Get the results
			// - Left expression
			if (futureLeftTreeResult != null)
			{
				try
				{
					IOManager.printDebug("-> Get '" + leftExpression + "' ...");
					leftTreeResult = futureLeftTreeResult.get();
				}
				catch (final Exception ex)
				{
					leftTreeResult = new Report<Element>(null, new Message(ex));
					IOManager.printError(ex);
				}
			}
			else
			{
				leftTreeResult = parseExpression(parent, leftExpression, context);
			}
			// - Right expression
			if (futureRightTreeResult != null)
			{
				try
				{
					IOManager.printDebug("-> Get '" + rightExpression + "' ...");
					rightTreeResult = futureRightTreeResult.get();
				}
				catch (final Exception ex)
				{
					rightTreeResult = new Report<Element>(null, new Message(ex));
					IOManager.printError(ex);
				}
			}
			else
			{
				rightTreeResult = parseExpression(parent, rightExpression, context);
			}
		}
		else
		{
			// Get the results
			leftTreeResult = parseExpression(parent, leftExpression, context);
			rightTreeResult = parseExpression(parent, rightExpression, context);
		}

		// Get the trees from the results
		// - Left
		leftTree = leftTreeResult.getOutput();
		if (leftTree == null)
		{
			return leftTreeResult;
		}
		// - Right
		rightTree = rightTreeResult.getOutput();
		if (rightTree == null)
		{
			return rightTreeResult;
		}

		IOManager.printDebug("Create new Node <" + leftTree.getExpression() + " " + type + " " + rightTree.getExpression() + ">");
		return new Report<Element>(new BinaryOperation(parent, trimmedExpression, type, leftTree, rightTree), null);
	}

	private static Report<Element> parseUnaryOperation(final Element parent, final String trimmedExpression, final Interval<Integer> parenthesesIndexes, final Map<String, Element> context)
	{
		final Element tree;

		// Unary operation
		final int unaryOperatorIndex = getLastUnaryOperatorIndex(trimmedExpression, parenthesesIndexes);
		if (unaryOperatorIndex >= 0)
		{
			// Get the unary operator
			final Character unaryOperator = trimmedExpression.charAt(unaryOperatorIndex);
			final Type type = getType(unaryOperator);
			IOManager.printDebug("Type: " + type);

			// Extract the embedded expression
			final String childExpression = Strings.split(trimmedExpression, unaryOperator).get(0);
			final Element child = parseExpression(parent, childExpression, context).getOutput();

			// Parse the expression
			IOManager.printDebug("Create new Node <" + type + " " + child.getExpression() + ">");
			tree = new UnaryOperation(parent, trimmedExpression, type, child);
		}
		// Parentheses
		else if (parenthesesIndexes.isCorrect())
		{
			final String nestedExpression = trimmedExpression.substring(parenthesesIndexes.getLowerBound() + 1, parenthesesIndexes.getUpperBound());
			IOManager.printDebug("NESTED EXPRESSION: '" + nestedExpression + "'");
			tree = parseExpression(parent, nestedExpression, context).getOutput();
		}
		// Variable, matrix or scalar
		else
		{
			IOManager.printDebug("Create new Leaf <" + trimmedExpression + ">");

			// Check if the expression is a variable
			if (context.containsKey(trimmedExpression))
			{
				tree = context.get(trimmedExpression);
				tree.setParent(parent);
				tree.setExpression(trimmedExpression);
			}
			// Check if the expression is a matrix
			else if (Matrix.isMatrix(trimmedExpression))
			{
				tree = new MatrixElement(parent, trimmedExpression);
			}
			// Else the expression is a scalar
			else if (Strings.isNumeric(trimmedExpression))
			{
				tree = new ScalarElement(parent, trimmedExpression);
			}
			else
			{
				return new Report<Element>(null, new Message(new NoElementException("Unkown variable '" + trimmedExpression + "'")));
			}
		}

		return new Report<Element>(tree, null);
	}

	/**
	 * Returns the indexes of all the binary operators in {@code expression}
	 * that are not in {@code parentheses}.
	 * <p>
	 * @param expression  the input {@link String}
	 * @param parentheses the intervals of parentheses in {@code expression}
	 * <p>
	 * @return the indexes of all the binary operators in {@code expression}
	 *         that are not in {@code parentheses}
	 */
	private static ExtendedList<Integer> getBinaryOperatorIndexes(final String expression, final IGroup<Integer> parentheses)
	{
		return getOperatorIndexes(expression, parentheses, expression.length(), BINARY_OPERATORS);
	}

	/**
	 * Returns the index of the last binary operator in {@code expression} that
	 * is not in {@code lastParentheses}.
	 * <p>
	 * @param expression      the input {@link String}
	 * @param lastParentheses the last interval of parentheses in
	 *                        {@code expression}
	 * <p>
	 * @return the index of the last binary operator in {@code expression} that
	 *         is not in {@code lastParentheses}
	 */
	private static Integer getLastBinaryOperatorIndex(final String expression, final Interval<Integer> lastParentheses)
	{
		return getLastOperatorIndexFromList(expression, lastParentheses, expression.length(), BINARY_OPERATORS);
	}

	/**
	 * Returns the index of the last unary operator in {@code expression} that
	 * is not in {@code lastParentheses}.
	 * <p>
	 * @param expression      the input {@link String}
	 * @param lastParentheses the intervals of parentheses in {@code expression}
	 * <p>
	 * @return the index of the last unary operator in {@code expression} that
	 *         is not in {@code lastParentheses}
	 */
	private static Integer getLastUnaryOperatorIndex(final String expression, final Interval<Integer> lastParentheses)
	{
		return getLastOperatorIndexFromList(expression, lastParentheses, expression.length(), UNARY_OPERATORS);
	}

	/**
	 * Returns the indexes of all the operators in {@code expression} that are
	 * not in {@code parentheses}.
	 * <p>
	 * @param expression      the input {@link String}
	 * @param parentheses     the intervals of parentheses in {@code expression}
	 * @param to              the index to finish the search at (exclusive)
	 * @param listOfOperators the list of operators to be searched
	 * <p>
	 * @return the indexes of all the operators in {@code expression} that are
	 *         not in {@code parentheses}
	 */
	private static ExtendedList<Integer> getOperatorIndexes(final String expression, final IGroup<Integer> parentheses, final int to, final List<List<Character>> listOfOperators)
	{
		final ExtendedList<Integer> indexes = new ExtendedList<Integer>();
		final int SIZE = listOfOperators.size();
		int currentBinaryOperatorsIndex = 0;

		do
		{
			final List<Character> operators = listOfOperators.get(currentBinaryOperatorsIndex);
			int index = getLastOperatorIndex(expression, parentheses, to, operators);
			while (index >= 0)
			{
				indexes.add(index);
				index = getLastOperatorIndex(expression, parentheses, index, operators);
			}
			++currentBinaryOperatorsIndex;
		}
		while ((currentBinaryOperatorsIndex < SIZE) && indexes.isEmpty());

		return indexes;
	}

	/**
	 * Returns the index of the last operator in {@code expression} that is not
	 * in {@code parentheses}.
	 * <p>
	 * @param expression  the input {@link String}
	 * @param parentheses the intervals of parentheses in {@code expression}
	 * @param operators   the operators to be searched
	 * <p>
	 * @return the index of the last operator in {@code expression} that is not
	 *         in {@code parentheses}
	 */
	private static int getLastOperatorIndex(final String expression, final IGroup<Integer> parentheses, final int to, final List<Character> operators)
	{
		int index = to;
		do
		{
			index = Strings.searchLastCharacter(expression, operators, index);
		}
		while (parentheses.isCorrect() && (index >= 0) && parentheses.isInside(index));
		return index;
	}

	/**
	 * Returns the index of the last operator in {@code expression} that is not
	 * in {@code parentheses}.
	 * <p>
	 * @param expression  the input {@link String}
	 * @param parentheses the intervals of parentheses in {@code expression}
	 * @param to          the index to finish the search at (exclusive)
	 * <p>
	 * @return the index of the last operator in {@code expression} that is not
	 *         in {@code parentheses}
	 */
	private static int getLastOperatorIndexFromList(final String expression, final IGroup<Integer> parentheses, final int to, final List<List<Character>> listOfOperators)
	{
		final int SIZE = listOfOperators.size();
		int index;
		int currentBinaryOperatorsIndex = 0;

		do
		{
			final List<Character> operators = listOfOperators.get(currentBinaryOperatorsIndex);
			index = getLastOperatorIndex(expression, parentheses, to, operators);
			++currentBinaryOperatorsIndex;
		}
		while ((index < 0) && (currentBinaryOperatorsIndex < SIZE));

		return index;
	}

	/**
	 * Returns the intervals of parentheses in {@code expression}.
	 * <p>
	 * @param expression the input {@link String}
	 * <p>
	 * @return the intervals of parentheses in {@code expression}
	 */
	private static IntervalCollection<Integer> getParenthesesIntervals(final String expression)
	{
		final Collection<Interval<Integer>> intervals = new ExtendedList<Interval<Integer>>();
		final int length = expression.length();
		int counter = 0;
		int lowerBound, upperBound = -1;

		for (int index = length - 1; index >= 0; --index)
		{
			final Type type = getType(expression.charAt(index));
			if (type == Type.RPARENTHESIS)
			{
				if (counter == 0)
				{
					upperBound = index;
				}
				++counter;
			}
			else if (type == Type.LPARENTHESIS)
			{
				--counter;
				if (counter == 0)
				{
					lowerBound = index;
					intervals.add(new Interval<Integer>(lowerBound, upperBound));
				}
			}
		}

		return new IntervalCollection<Integer>(intervals);
	}

	/**
	 * Returns the last interval of parentheses in {@code expression}.
	 * <p>
	 * @param expression the input {@link String}
	 * <p>
	 * @return the last interval of parentheses in {@code expression}
	 */
	private static Interval<Integer> getLastParenthesesInterval(final String expression)
	{
		final Interval<Integer> interval = new Interval<Integer>();
		final int length = expression.length();
		int counter = 0;

		for (int index = length - 1; index >= 0; --index)
		{
			final Type type = getType(expression.charAt(index));
			if (type == Type.RPARENTHESIS)
			{
				if (counter == 0)
				{
					interval.setUpperBound(index);
				}
				++counter;
			}
			else if (type == Type.LPARENTHESIS)
			{
				--counter;
				if (counter == 0)
				{
					interval.setLowerBound(index);
					break;
				}
			}
		}

		return interval;
	}

	/**
	 * Returns the {@link Type} of {@code token}.
	 * <p>
	 * @param token the input {@code char}
	 * <p>
	 * @return the {@link Type} of {@code token}
	 */
	private static Type getType(final char token)
	{
		return getType(String.valueOf(token));
	}

	/**
	 * Returns the {@link Type} of {@code token}.
	 * <p>
	 * @param token the input {@link String}
	 * <p>
	 * @return the {@link Type} of {@code token}
	 */
	private static Type getType(final String token)
	{
		Type type;

		if (token.equals("+"))
		{
			type = Type.ADDITION;
		}
		else if (token.equals("-"))
		{
			type = Type.SUBTRACTION;
		}
		else if (token.equals("*"))
		{
			type = Type.MULTIPLICATION;
		}
		else if (token.equals("/"))
		{
			type = Type.DIVISION;
		}
		else if (token.equals("^"))
		{
			type = Type.POWER;
		}
		else if (token.equals("~"))
		{
			type = Type.SOLUTION;
		}
		else if (token.equals("("))
		{
			type = Type.LPARENTHESIS;
		}
		else if (token.equals(")"))
		{
			type = Type.RPARENTHESIS;
		}
		else if (token.equals("!"))
		{
			type = Type.FACTORIAL;
		}
		else if (token.equals("'"))
		{
			type = Type.TRANSPOSE;
		}
		else if (token.equals("@"))
		{
			type = Type.INVERSE;
		}
		else
		{
			type = Type.NUMBER;
		}

		return type;
	}
}
