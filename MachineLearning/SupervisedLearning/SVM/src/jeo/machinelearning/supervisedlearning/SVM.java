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
package jeo.machinelearning.supervisedlearning;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import jeo.common.io.FileParser;
import jeo.common.io.IOManager;
import jeo.common.structure.Table;
import jeo.common.util.Arguments;
import jeo.common.util.Collections;
import jeo.common.util.Doubles;
import jeo.common.util.Integers;

public class SVM
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Default cache size.
	 */
	private static final double DEFAULT_CACHE_SIZE = 100;
	/**
	 * Default error tolerance.
	 */
	private static final double DEFAULT_ERROR_TOLERANCE = 0.001;
	/**
	 * The number of features to be used.
	 */
	private final int featuresNumber;
	/**
	 * The problem.
	 */
	private final svm_problem problem;
	/**
	 * The parameters.
	 */
	private final svm_parameter parameters;
	/**
	 * The set of classes.
	 */
	private final SortedSet<Integer> classes;
	/**
	 * Probability estimates of the classification.
	 */
	private final HashMap<Integer, Double> classificationProbabilityEstimates;
	/**
	 * The number of training examples.
	 */
	private int trainingExamplesNumber;
	/**
	 * The model.
	 */
	private svm_model model;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructs a support vector machine.
	 * <p>
	 * @param featuresNumber the number of features
	 */
	public SVM(final int featuresNumber)
	{
		this.featuresNumber = featuresNumber;
		problem = new svm_problem();
		parameters = new svm_parameter();
		classes = new TreeSet<Integer>();
		classificationProbabilityEstimates = new HashMap<Integer, Double>(Collections.DEFAULT_INITIAL_CAPACITY);
		trainingExamplesNumber = 0;
		setDefaultParameters();
	}


	////////////////////////////////////////////////////////////////////////////
	// TRAINING EXAMPLES AND CLASSES
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Loads the training examples from the specified arrays.
	 * <p>
	 * @param featureVectors the feature vectors of the training examples
	 * @param classes        the classes of the training examples
	 */
	public void loadTrainingExamples(final double[][] featureVectors, final int[] classes)
	{
		Arguments.requireSameSize(featureVectors.length, classes.length);
		if (featureVectors.length != 0)
		{
			final int n = featureVectors[0].length;
			if (n >= featuresNumber)
			{
				trainingExamplesNumber = featureVectors.length;
				updateProblem();
				for (int i = 0; i < trainingExamplesNumber; ++i)
				{
					updateClassification(i, classes[i]);
					updateFeatureVector(i, featureVectors[i]);
				}
			}
			else
			{
				IOManager.printError("Wrong feature vector size " + Arguments.expectedButFound(featuresNumber, n));
			}
		}
		else
		{
			IOManager.printWarning("No training example found");
		}
	}

	/**
	 * Loads the training examples from the specified CSV file.
	 * <p>
	 * @param filePath           the path to the CSV file
	 * @param classesColumnIndex the index of the column containing the classes
	 *                           of the training examples in the CSV file
	 */
	public void loadTrainingExamplesWithCSV(final String filePath, final int classesColumnIndex)
	{
		final Table<String> trainingExamples = FileParser.parseCSV(filePath);
		if (trainingExamples != null)
		{
			final int m = trainingExamples.getRowSize();
			if (m != 0)
			{
				final int n = trainingExamples.getColumnSize();
				if (n > featuresNumber)
				{
					if (classesColumnIndex >= (n - 1))
					{
						trainingExamplesNumber = m;
						updateProblem();
						for (int i = 0; i < trainingExamplesNumber; ++i)
						{
							updateClassification(i, Integer.valueOf(trainingExamples.get(i, classesColumnIndex)));
							for (int j = 0; j < featuresNumber; ++j)
							{
								updateValue(i, j, Double.valueOf(trainingExamples.get(i, j)));
							}
						}
					}
					else
					{
						IOManager.printError("Index out of bound " + Arguments.atLeastExpectedButFound(n - 1, classesColumnIndex));
					}
				}
				else
				{
					IOManager.printError("Not enough columns " + Arguments.atLeastExpectedButFound(featuresNumber + 1, n));
				}
			}
			else
			{
				IOManager.printWarning("No training example found");
			}
		}
	}

	/**
	 * Loads the training examples from the specified CSV file.
	 * <p>
	 * @param filePath           the path to the CSV file
	 * @param valuesColumnIndex  the index of the column containing the values
	 *                           of the feature vectors of the training examples
	 *                           in the CSV file
	 * @param classesColumnIndex the index of the column containing the classes
	 *                           of the training examples in the CSV file
	 */
	public void loadTrainingExamplesWithCSV(final String filePath, final int valuesColumnIndex, final int classesColumnIndex)
	{
		final Table<String> trainingExamples = FileParser.parseCSV(filePath);
		if ((trainingExamples.getRowSize() % featuresNumber) != 0)
		{
			IOManager.printWarning("Values are missing");
		}
		trainingExamplesNumber = trainingExamples.getRowSize() / featuresNumber;
		updateProblem();
		for (int i = 0; i < trainingExamplesNumber; ++i)
		{
			updateClassification(i, Integer.valueOf(trainingExamples.get(i * featuresNumber, classesColumnIndex)));
			for (int j = 0; j < featuresNumber; ++j)
			{
				updateValue(i, j, Double.valueOf(trainingExamples.get((i * featuresNumber) + j, valuesColumnIndex)));
			}
		}
	}

	/**
	 * Updates the problem.
	 */
	private void updateProblem()
	{
		problem.l = trainingExamplesNumber;
		problem.x = new svm_node[trainingExamplesNumber][featuresNumber];
		problem.y = new double[trainingExamplesNumber];
		classes.clear();
	}

	/**
	 * Updates the class of the training example at the specified index.
	 * <p>
	 * @param trainingExampleIndex the index of the training example
	 * @param classification       the class of the training example
	 */
	private void updateClassification(final int trainingExampleIndex, final int classification)
	{
		problem.y[trainingExampleIndex] = classification;
		classes.add(classification);
		IOManager.print(classification + ": ", false);
	}

	/**
	 * Updates the feature vector of the training example at the specified
	 * index.
	 * <p>
	 * @param trainingExampleIndex the index of the training example
	 * @param featureVector        the feature vector of the training example
	 */
	private void updateFeatureVector(final int trainingExampleIndex, final double[] featureVector)
	{
		for (int featureIndex = 0; featureIndex < featuresNumber; ++featureIndex)
		{
			updateValue(trainingExampleIndex, featureIndex, featureVector[featureIndex]);
		}
	}

	/**
	 * Updates the value of the feature vector of the training example at the
	 * specified indexes.
	 * <p>
	 * @param trainingExampleIndex the index of the training example
	 * @param featureIndex         the index of the feature
	 * @param value                the value at the specified indexes
	 */
	private void updateValue(final int trainingExampleIndex, final int featureIndex, final double value)
	{
		if (featureIndex == 0)
		{
			IOManager.print("( ", false);
		}
		final svm_node node = new svm_node();
		node.index = featureIndex;
		node.value = value;
		problem.x[trainingExampleIndex][featureIndex] = node;
		IOManager.print(node.value + " ", false);
		if (featureIndex == (featuresNumber - 1))
		{
			IOManager.printLine(")", false);
		}
	}

	/**
	 * Get the sorted set of classes.
	 * <p>
	 * @return the sorted set of classes
	 */
	public SortedSet<Integer> getClasses()
	{
		return classes;
	}


	////////////////////////////////////////////////////////////////////////////
	// MODEL
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Sets the default parameters.
	 */
	public final void setDefaultParameters()
	{
		parameters.cache_size = DEFAULT_CACHE_SIZE;
		parameters.eps = DEFAULT_ERROR_TOLERANCE;
		parameters.probability = 1; // do probability estimates
		parameters.shrinking = 1; // use the shrinking heuristics
		setTypeToCSVC();
	}

	/**
	 * Sets the type to C-SVC. This type regularizes the support vector
	 * classification.
	 */
	public void setTypeToCSVC()
	{
		setTypeToCSVC(1.);
	}

	/**
	 * Sets the type to C-SVC. This type regularizes the support vector
	 * classification.
	 * <p>
	 * @param c the parameter of C-SVC
	 */
	public void setTypeToCSVC(final double c)
	{
		parameters.svm_type = svm_parameter.C_SVC;
		parameters.C = c;
	}

	/**
	 * Sets the type to nu-SVC. This type automatically regularizes the support
	 * vector classification.
	 */
	public void setTypeToNuSVC()
	{
		setTypeToNuSVC(0.5);
	}

	/**
	 * Sets the type to nu-SVC. This type automatically regularizes the support
	 * vector classification.
	 * <p>
	 * @param nu the parameter of nu-SVC
	 */
	public void setTypeToNuSVC(final double nu)
	{
		parameters.svm_type = svm_parameter.NU_SVC;
		parameters.nu = nu;
	}

	/**
	 * Sets the type to one-class. This type selects a hyper-sphere to maximize
	 * density.
	 * <p>
	 * @param nu the parameter of one-class
	 */
	public void setTypeToOneClass(final double nu)
	{
		parameters.svm_type = svm_parameter.ONE_CLASS;
		parameters.nu = nu;
	}

	/**
	 * Sets the type to epsilon SVR. This type supports vector regression robust
	 * to small (epsilon) errors.
	 * <p>
	 * @param c       a parameter of epsilon SVR
	 * @param epsilon a parameter of epsilon SVR
	 */
	public void setTypeToEpsilonSVR(final double c, final double epsilon)
	{
		parameters.svm_type = svm_parameter.EPSILON_SVR;
		parameters.C = c;
		parameters.p = epsilon;
	}

	/**
	 * Sets the cache size.
	 * <p>
	 * @param cacheSize the cache size
	 */
	public void setCacheSize(final double cacheSize)
	{
		parameters.cache_size = cacheSize;
	}

	/**
	 * Sets the tolerance of error (or termination criterion).
	 * <p>
	 * @param errorTolerance the error tolerance
	 */
	public void setErrorTolerance(final double errorTolerance)
	{
		parameters.eps = errorTolerance;
	}

	/**
	 * Sets the kernel type to linear.
	 */
	public void setKernelTypeToLinear()
	{
		parameters.kernel_type = svm_parameter.LINEAR;
	}

	/**
	 * Sets the kernel type to polynomial with the specified parameters.
	 * <p>
	 * @param degree      the degree of the polynomial kernel
	 * @param coefficient the coefficient of the polynomial kernel
	 * @param constant    the constant of the polynomial kernel
	 */
	public void setKernelTypeToPolynomial(final int degree, final double coefficient, final double constant)
	{
		Arguments.requirePositive(degree);
		Arguments.requireNonZero(coefficient);
		Arguments.requireNonNegative(constant);
		parameters.kernel_type = svm_parameter.POLY;
		parameters.degree = degree;
		parameters.gamma = coefficient;
		parameters.coef0 = constant;
	}

	/**
	 * Sets the kernel type to Gaussian with the specified parameter.
	 * <p>
	 * @param variance the parameter of the Gaussian kernel
	 */
	public void setKernelTypeToGaussian(final double variance)
	{
		Arguments.requirePositive(variance);
		setKernelTypeToRBF(0.5 / variance);
	}

	/**
	 * Sets the kernel type to radial basis function (RBF) with the specified
	 * parameter.
	 * <p>
	 * @param gamma the parameter of the RBF kernel
	 */
	public void setKernelTypeToRBF(final double gamma)
	{
		Arguments.requirePositive(gamma);
		parameters.kernel_type = svm_parameter.RBF;
		parameters.gamma = gamma;
	}

	/**
	 * Sets the weights of the classes (only for C-SVC).
	 * <p>
	 * @param classWeigths the weights of the classes
	 */
	public void setClassWeights(final double[] classWeigths)
	{
		Arguments.requireMaximumSize(Doubles.arrayToObjectArray(classWeigths), classes.size());
		final int size = classWeigths.length;
		parameters.weight = classWeigths;
		parameters.nr_weight = size;
		parameters.weight_label = Integers.<Integer>collectionToArray(classes);
	}

	/**
	 * Trains this SVM using the problem and the parameters.
	 * <p>
	 * @return the model
	 */
	public svm_model train()
	{
		if (trainingExamplesNumber != 0)
		{
			model = svm.svm_train(problem, parameters);
			return model;
		}
		else
		{
			IOManager.printError("No training example found");
			return null;
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// EVALUTATION
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Classifies the specified example according to this model.
	 * <p>
	 * @param example the example to be classified
	 * <p>
	 * @return the estimated class of the specified example
	 */
	public Integer classify(final double[] example)
	{
		final Integer prediction;
		if (model != null)
		{
			final svm_node[] nodes = new svm_node[featuresNumber];
			for (int i = 0; i < featuresNumber; ++i)
			{
				final svm_node node = new svm_node();
				node.index = i;
				node.value = example[i];
				nodes[i] = node;
			}
			final int totalClasses = model.nr_class;
			final int[] labels = new int[totalClasses];
			svm.svm_get_labels(model, labels);
			final double[] probabilityEstimates = new double[totalClasses];
			prediction = (int) svm.svm_predict_probability(model, nodes, probabilityEstimates);
			for (int i = 0; i < totalClasses; ++i)
			{
				classificationProbabilityEstimates.put(labels[i], probabilityEstimates[i]);
			}
		}
		else
		{
			prediction = null;
		}
		return prediction;
	}

	/**
	 * Returns the probability estimates of the classification.
	 * <p>
	 * @return the probability estimates of the classification
	 */
	public HashMap<Integer, Double> getProbabilityEstimates()
	{
		return classificationProbabilityEstimates;
	}
}
