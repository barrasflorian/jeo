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
package jeo.common.structure.tree;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jeo.common.io.IOManager;
import jeo.common.structure.ExtendedList;
import jeo.common.structure.tree.node.BinaryNode;
import jeo.common.util.Arguments;
import jeo.common.util.Strings;

public abstract class BinaryTree<K extends Comparable<K>, V, N extends BinaryNode<K, V, N>>
	extends Tree<K, V, N>
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 7422191110233616440L;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	protected BinaryTree()
	{
		super();
	}


	////////////////////////////////////////////////////////////////////////////
	// SEARCH
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns this tree's node for the given key, or {@code null} if this tree
	 * does not contain a node for the key.
	 * <p>
	 * @param key the key of the node to be searched
	 * <p>
	 * @return this tree's node for the given key, or {@code null} if this tree
	 *         does not contain a node for the key
	 * <p>
	 * @throws NullPointerException if the specified key is null and this tree
	 *                              uses natural ordering, or its comparator
	 *                              does not permit null keys
	 */
	@Override
	protected N getNode(final Comparable<? super K> key)
	{
		// 		Arguments.requireNonNull(key);
		N node = root;
		int cmp;
		while (node != null)
		{
			cmp = key.compareTo(node.key);
			if (cmp < 0)
			{
				node = node.left;
			}
			else if (cmp > 0)
			{
				node = node.right;
			}
			else
			{
				return node;
			}
		}
		return null;
	}


	////////////////////////////////////////////////////////////////////////////
	// DELETION
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Removes the mapping for this key from this tree if present.
	 * <p>
	 * @param key key for which mapping should be removed
	 * <p>
	 * @return the previous value associated with {@code key}, or {@code null}
	 *         if there was no mapping for {@code key}. (A {@code null} return
	 *         can also indicate that this tree previously associated
	 *         {@code null} with {@code key}.)
	 * <p>
	 * @throws ClassCastException   if the specified key cannot be compared with
	 *                              the keys currently in this tree
	 * @throws NullPointerException if the specified key is null and this tree
	 *                              uses natural ordering, or its comparator
	 *                              does not permit null keys
	 */
	@Override
	public V remove(final Object key)
	{
		// Find the node
		final N node = getNode(key);
		// Remove the node from this tree if present
		if (node != null)
		{
			removeNode(node);
			return node.value;
		}
		return null;
	}

	/**
	 * Removes the specified node from this tree.
	 * <p>
	 * @param node the node to be removed
	 */
	protected abstract void removeNode(final N node);


	////////////////////////////////////////////////////////////////////////////
	// BALANCE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Balances the tree whose root is {@code treeRoot} after insertion.
	 * <p>
	 * @param treeRoot the root of the tree to be balanced
	 */
	protected abstract void balanceAfterInsertion(N treeRoot);

	/**
	 * Balances the tree whose root is {@code treeRoot} after deletion.
	 * <p>
	 * @param treeRoot the root of the tree to be balanced
	 */
	protected abstract void balanceAfterDeletion(N treeRoot);


	////////////////////////////////////////////////////////////////////////////
	// ROTATIONS
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Rotates the tree whose root is {@code treeRoot} to the left. Corrects a
	 * RR imbalance.
	 * <p>
	 * @param treeRoot the root of the tree to be rotated
	 * <p>
	 * @return the root of the rotated tree
	 */
	protected N rotateLeft(final N treeRoot)
	{
		// Get the root of the rotated tree
		N rotatedTreeRoot;
		rotatedTreeRoot = treeRoot.right;
		// Update the parent
		final N treeRootParent = treeRoot.parent;
		if (treeRootParent == null)
		{
			setRoot(rotatedTreeRoot);
		}
		else if (treeRoot.isLeft)
		{
			treeRootParent.setLeftNode(rotatedTreeRoot);
		}
		else
		{
			treeRootParent.setRightNode(rotatedTreeRoot);
		}
		// Update the children of the rotated nodes
		treeRoot.setRightNode(rotatedTreeRoot.left);
		rotatedTreeRoot.setLeftNode(treeRoot);
		return rotatedTreeRoot;
	}

	/**
	 * Rotates the tree whose root is {@code treeRoot} to the right. Corrects a
	 * LL imbalance.
	 * <p>
	 * @param treeRoot the root of the tree to be rotated
	 * <p>
	 * @return the root of the rotated tree
	 */
	protected N rotateRight(final N treeRoot)
	{
		// Get the root of the rotated tree
		N rotatedTreeRoot;
		rotatedTreeRoot = treeRoot.left;
		// Update the parent
		final N treeRootParent = treeRoot.parent;
		if (treeRootParent == null)
		{
			setRoot(rotatedTreeRoot);
		}
		else if (treeRoot.isLeft)
		{
			treeRootParent.setLeftNode(rotatedTreeRoot);
		}
		else
		{
			treeRootParent.setRightNode(rotatedTreeRoot);
		}
		// Update the children of the rotated nodes
		treeRoot.setLeftNode(rotatedTreeRoot.right);
		rotatedTreeRoot.setRightNode(treeRoot);
		return rotatedTreeRoot;
	}

	/**
	 * Corrects a LR imbalance.
	 * <p>
	 * @param treeRoot the root of the tree to be rotated
	 * <p>
	 * @return the root of the rotated tree
	 */
	protected N rotateLeftRight(final N treeRoot)
	{
		treeRoot.setLeftNode(rotateLeft(treeRoot.left));
		return rotateRight(treeRoot);
	}

	/**
	 * Corrects a RL imbalance.
	 * <p>
	 * @param treeRoot the root of the tree to be rotated
	 * <p>
	 * @return the root of the rotated tree
	 */
	protected N rotateRightLeft(final N treeRoot)
	{
		treeRoot.setRightNode(rotateRight(treeRoot.right));
		return rotateLeft(treeRoot);
	}


	////////////////////////////////////////////////////////////////////////////
	// SETTER
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Sets the root of this tree.
	 * <p>
	 * @param node the root to set
	 */
	protected abstract void setRoot(final N node);


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the parent of the specified node, or null if the specified node
	 * is null.
	 * <p>
	 * @param node the node with the height to be gotten
	 * <p>
	 * @return the height of the specified node, or {@code -1} if the node is
	 *         {@code null}
	 */
	protected N getParent(final N node)
	{
		return node == null ? null : node.parent;
	}

	/**
	 * Returns the first node in this tree (according to this tree's key-sort
	 * function). Returns null if this tree is empty.
	 * <p>
	 * @return the first node in this tree, or {@code null} if this tree is
	 *         empty
	 */
	protected N getFirstEntry()
	{
		N node = root;
		if (node != null)
		{
			while (node.left != null)
			{
				node = node.left;
			}
		}
		return node;
	}

	/**
	 * Returns the last node in this tree (according to this tree's key-sort
	 * function). Returns null if this tree is empty.
	 * <p>
	 * @return the last node in this tree, or {@code null} if this tree is empty
	 */
	protected N getLastEntry()
	{
		N node = root;
		if (node != null)
		{
			while (node.right != null)
			{
				node = node.right;
			}
		}
		return node;
	}

	/**
	 * Returns the predecessor of the specified node in this tree.
	 * <p>
	 * @param node the successor of the node to be searched
	 * <p>
	 * @return the predecessor of {@code node}
	 */
	protected N getPredecessor(final N node)
	{
		N predecessor;
		if (node.left != null)
		{
			predecessor = node.left;
			while (predecessor.right != null)
			{
				predecessor = predecessor.right;
			}
		}
		else
		{
			N successor = node;
			predecessor = successor.parent;
			while ((predecessor != null) && predecessor.isLeft)
			{
				successor = predecessor;
				predecessor = successor.parent;
			}
		}
		return predecessor;
	}

	/**
	 * Returns the successor of the specified node in this tree.
	 * <p>
	 * @param node the predecessor of the node to be searched
	 * <p>
	 * @return the successor of {@code node}
	 */
	protected N getSuccessor(final N node)
	{
		N successor;
		if (node.right != null)
		{
			successor = node.right;
			while (successor.left != null)
			{
				successor = successor.left;
			}
		}
		else
		{
			N predecessor = node;
			successor = predecessor.parent;
			while ((successor != null) && !predecessor.isLeft)
			{
				predecessor = successor;
				successor = predecessor.parent;
			}
		}
		return successor;
	}


	////////////////////////////////////////////////////////////////////////////
	// ABSTRACT MAP
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Removes all of the mappings from this tree. This tree will be empty after
	 * this call returns.
	 */
	@Override
	public void clear()
	{
		root = null;
		size = 0;
	}

	/**
	 * Returns {@code true} if this tree contains a mapping for the specified
	 * key.
	 * <p>
	 * @param key key whose presence in this tree is to be tested
	 * <p>
	 * @return {@code true} if this tree contains a mapping for the specified
	 *         key
	 * <p>
	 * @throws ClassCastException   if the specified key cannot be compared with
	 *                              the keys currently in this tree
	 * @throws NullPointerException if the specified key is null and this tree
	 *                              uses natural ordering, or its comparator
	 *                              does not permit null keys
	 */
	@Override
	public boolean containsKey(final Object key)
	{
		return getNode(key) != null;
	}

	/**
	 * Returns {@code true} if this tree maps one or more keys to the specified
	 * value. More formally, returns {@code true} if and only if this tree
	 * contains at least one mapping to a value {@code v} such that
	 * {@code (value==null ? v==null : value.equals(v))}. This operation will
	 * probably require time linear in this tree size for most implementations.
	 * <p>
	 * @param value value whose presence in this tree is to be tested
	 * <p>
	 * @return {@code true} if a mapping to {@code value} exists; {@code false}
	 *         otherwise
	 */
	@Override
	public boolean containsValue(final Object value)
	{
		for (N node = getFirstEntry(); node != null; node = getSuccessor(node))
		{
			if (Arguments.equals(value, node.value))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a {@link Set} view of the mappings contained in this tree. The
	 * set's iterator returns the entries in ascending key order. The set is
	 * backed by this tree, so changes to this tree are reflected in the set,
	 * and vice-versa. If this tree is modified while an iteration over the set
	 * is in progress (except through the iterator's own {@code remove}
	 * operation, or through the {@code setValue} operation on a map entry
	 * returned by the iterator) the results of the iteration are undefined. The
	 * set supports element removal, which removes the corresponding mapping
	 * from this tree, via the {@link Iterator.remove}, {@link Set.remove},
	 * {@code removeAll}, {@code retainAll} and {@code clear} operations. It
	 * does not support the {@code add} or {@code addAll} operations.
	 * <p>
	 * @return a {@link Set} view of the mappings contained in this tree
	 */
	@Override
	public Set<Entry<K, V>> entrySet()
	{
		return getEntries(root, new TreeSet<Entry<K, V>>());
	}

	/**
	 * Performs the in-order traversal of the specified tree and returns the
	 * visited nodes in the specified set.
	 * <p>
	 * @param treeRoot the root of the tree
	 * @param set      the set to be filled
	 * <p>
	 * @return the nodes of this tree in {@code set}
	 */
	public Set<Entry<K, V>> getEntries(final N treeRoot, final Set<Entry<K, V>> set)
	{
		if (treeRoot != null)
		{
			getEntries(treeRoot.left, set);
			set.add(treeRoot);
			getEntries(treeRoot.right, set);
		}
		return set;
	}

	/**
	 * Returns the number of key-value mappings in this tree.
	 * <p>
	 * @return the number of key-value mappings in this tree
	 */
	@Override
	public int size()
	{
		return size;
	}


	////////////////////////////////////////////////////////////////////////////
	// LIST
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Performs the in-order traversal of this tree and returns the keys of the
	 * visited nodes in a list.
	 * <p>
	 * @return a list of the keys of the nodes of this tree
	 */
	public List<K> getKeys()
	{
		final List<K> keys = new ExtendedList<K>(size);
		getKeys(root, keys);
		return keys;
	}

	/**
	 * Performs the in-order traversal of the tree whose root is
	 * {@code treeRoot} and returns the keys of the visited nodes in
	 * {@code keys}.
	 * <p>
	 * @param treeRoot the root of the tree
	 * @param keys     the list of the keys of the nodes of the tree
	 */
	protected void getKeys(final N treeRoot, final List<K> keys)
	{
		if (treeRoot != null)
		{
			getKeys(treeRoot.left, keys);
			keys.add(treeRoot.key);
			getKeys(treeRoot.right, keys);
		}
	}

	/**
	 * Performs the in-order traversal of this tree and returns the values of
	 * the visited nodes in a list.
	 * <p>
	 * @return a list of the values of the nodes of this tree
	 */
	public List<V> getValues()
	{
		final List<V> values = new ExtendedList<V>(size);
		getValues(root, values);
		return values;
	}

	/**
	 * Performs the in-order traversal of the tree whose root is
	 * {@code treeRoot} and returns the values of the visited nodes in
	 * {@code values}.
	 * <p>
	 * @param treeRoot the root of the tree
	 * @param values   the list of the values of the nodes of the tree
	 */
	protected void getValues(final N treeRoot, final List<V> values)
	{
		if (treeRoot != null)
		{
			getValues(treeRoot.left, values);
			values.add(treeRoot.value);
			getValues(treeRoot.right, values);
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// PRINT
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Prints all the nodes of this tree.
	 */
	@Override
	public void print()
	{
		printAllNodes(root);
	}

	/**
	 * Prints all the nodes of the tree whose root is {@code treeRoot}.
	 * <p>
	 * @param treeRoot the root of the tree to be printed
	 */
	protected void printAllNodes(final N treeRoot)
	{
		printNode(treeRoot);
		if (treeRoot.left != null)
		{
			printAllNodes(treeRoot.left);
		}
		if (treeRoot.right != null)
		{
			printAllNodes(treeRoot.right);
		}
	}

	/**
	 * Prints the specified node.
	 * <p>
	 * @param node the node to be printed
	 */
	protected void printNode(final N node)
	{
		String line = Strings.EMPTY;
		line += convertToString("Node", node);
		line += convertToString("Parent", node.parent);
		line += convertToString("Left", node.left);
		line += convertToString("Right", node.right);
		IOManager.printLine(line, false);
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public Object clone()
		throws CloneNotSupportedException
	{
		return super.clone();
	}
}
