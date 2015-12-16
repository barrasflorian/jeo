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

import java.util.Collection;

import jeo.common.structure.ExtendedList;
import jeo.common.structure.tree.node.AVLBinaryNode;

public class AVLTreeMap<K extends Comparable<K>, V>
	extends BinaryTree<K, V, AVLBinaryNode<K, V>>
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 4744421447337482103L;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public AVLTreeMap()
	{
		super();
	}


	////////////////////////////////////////////////////////////////////////////
	// INSERT
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Associates the specified value with the specified key in this tree. If
	 * this tree previously contained a mapping for the key, the old value is
	 * replaced.
	 * <p>
	 * @param key   key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
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
	public V put(final K key, final V value)
	{
		AVLBinaryNode<K, V> treeRoot = root;
		// If the root is null
		if (root == null)
		{
			// The root is set to a new node containing the given key and value
			setRoot(new AVLBinaryNode<K, V>(key, value));
		}
		// Else browse this tree
		else
		{
			int cmp;
			AVLBinaryNode<K, V> parent;
			do
			{
				cmp = key.compareTo(treeRoot.key);
				// If the key is less than the key of the current node
				if (cmp < 0)
				{
					parent = treeRoot;
					treeRoot = treeRoot.left;
				}
				// If the key is greater than the key of the current node
				else if (cmp > 0)
				{
					parent = treeRoot;
					treeRoot = treeRoot.right;
				}
				else
				{
					// Replace and return the previous value of the node
					return treeRoot.setValue(value);
				}
			}
			while (treeRoot != null);
			// Create a new node containing the given key and value
			final AVLBinaryNode<K, V> newNode = new AVLBinaryNode<K, V>(key, value);
			if (cmp < 0)
			{
				// The new node is the left node of parent
				parent.setLeftNode(newNode);
			}
			else
			{
				// The new node is the right node of parent
				parent.setRightNode(newNode);
			}
			// Balance if necessary
			balance(newNode);
		}
		// Increase the size of this tree
		++size;
		// Return null since no previous value exists
		return null;
	}


	////////////////////////////////////////////////////////////////////////////
	// DELETE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Removes the specified node from this tree.
	 * <p>
	 * @param node the node to be removed
	 */
	@Override
	protected void removeNode(final AVLBinaryNode<K, V> node)
	{
		// Get the parent and the successor of the specified node
		final AVLBinaryNode<K, V> parentNode = node.parent;
		// 0 or 1 child (this tree is guaranteed to be balanced)
		if ((node.left == null) || (node.right == null))
		{
			// Get the child if present
			AVLBinaryNode<K, V> childNode;
			if (node.left != null)
			{
				childNode = node.left;
			}
			else if (node.right != null)
			{
				childNode = node.right;
			}
			else
			{
				childNode = null;
			}
			// Update the parent
			if (parentNode == null)
			{
				setRoot(childNode);
			}
			else if (node.isLeft)
			{
				parentNode.setLeftNode(childNode);
			}
			else
			{
				parentNode.setRightNode(childNode);
			}
		}
		// 2 children (this tree is not guaranteed to be balanced)
		else
		{
			// Get the successor of the node to be removed
			// Note: the successor cannot be null since the node has a right node
			final AVLBinaryNode<K, V> successor = getSuccessor(node);
			// Remove the successor from this tree
			removeNode(successor);
			// Override the key and value of the node with the successor
			node.key = successor.key;
			node.value = successor.value;
			// Balance this tree from the node
			balanceAfterDeletion(node);
		}
		// Decrement the number of nodes
		--size;
	}


	////////////////////////////////////////////////////////////////////////////
	// BALANCE
	////////////////////////////////////////////////////////////////////////////

	private void balance(AVLBinaryNode<K, V> treeRoot)
	{
		AVLBinaryNode<K, V> parent = treeRoot;
		do
		{
			treeRoot = parent;
			// Get the balance of the node
			final long balance = treeRoot.balance;
			// Check the balance
			// - If the balance is equal to -2
			if (balance == -2)
			{
				// Get the left node of the specified node
				// Note: the left node cannot be null since the balance is negative
				final AVLBinaryNode<K, V> leftNode = treeRoot.left;
				// LL imbalance
				if (getHeight(leftNode.left) >= getHeight(leftNode.right))
				{
					treeRoot = rotateRight(treeRoot);
				}
				// LR imbalance
				else
				{
					treeRoot = rotateLeftRight(treeRoot);
				}
			}
			// - If the balance is equal to 2
			else if (balance == 2)
			{
				// Get the right node of the specified node
				// Note: the right node cannot be null since the balance is positive
				final AVLBinaryNode<K, V> rightNode = treeRoot.right;
				// RR imbalance
				if (getHeight(rightNode.right) >= getHeight(rightNode.left))
				{
					treeRoot = rotateLeft(treeRoot);
				}
				// RL imbalance
				else
				{
					treeRoot = rotateRightLeft(treeRoot);
				}
			}
			parent = treeRoot.parent;
		}
		while (parent != null);
		// The node has no parent, set the root of this tree
		setRoot(treeRoot);
	}

	/**
	 * Balances the tree whose root is {@code treeRoot} after insertion.
	 * <p>
	 * @param treeRoot the root of the tree to be balanced
	 */
	@Override
	protected void balanceAfterInsertion(final AVLBinaryNode<K, V> treeRoot)
	{
		balance(treeRoot);
	}

	/**
	 * Balances the tree whose root is {@code treeRoot} after deletion.
	 * <p>
	 * @param treeRoot the root of the tree to be balanced
	 */
	@Override
	protected void balanceAfterDeletion(final AVLBinaryNode<K, V> treeRoot)
	{
		balance(treeRoot);
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Gets the height of the specified node.
	 * <p>
	 * @param node the node with the height to be gotten
	 * <p>
	 * @return the height of the specified node, or {@code -1} if the node is
	 *         {@code null}
	 */
	protected long getHeight(final AVLBinaryNode<K, V> node)
	{
		return node == null ? -1 : node.height;
	}


	////////////////////////////////////////////////////////////////////////////
	// SETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Sets the root of this tree.
	 * <p>
	 * @param node the root to set
	 */
	@Override
	protected void setRoot(final AVLBinaryNode<K, V> node)
	{
		root = node;
		if (root != null)
		{
			root.parent = null;
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// BINARY TREE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Rotates the tree whose root is {@code treeRoot} to the left.
	 * <p>
	 * @param treeRoot the root of the tree to be rotated
	 * <p>
	 * @return the root of the rotated tree
	 */
	@Override
	protected AVLBinaryNode<K, V> rotateLeft(final AVLBinaryNode<K, V> treeRoot)
	{
		AVLBinaryNode.isNecessaryToUpdate(false);
		final AVLBinaryNode<K, V> rotatedTreeRoot = super.rotateLeft(treeRoot);
		AVLBinaryNode.isNecessaryToUpdate(true);
		treeRoot.updateHeightAndBalance();
		treeRoot.updateParentNodes();
		return rotatedTreeRoot;
	}

	/**
	 * Rotates the tree whose root is {@code treeRoot} to the right.
	 * <p>
	 * @param treeRoot the root of the tree to be rotated
	 * <p>
	 * @return the root of the rotated tree
	 */
	@Override
	protected AVLBinaryNode<K, V> rotateRight(final AVLBinaryNode<K, V> treeRoot)
	{
		AVLBinaryNode.isNecessaryToUpdate(false);
		final AVLBinaryNode<K, V> rotatedTreeRoot = super.rotateRight(treeRoot);
		AVLBinaryNode.isNecessaryToUpdate(true);
		treeRoot.updateHeightAndBalance();
		treeRoot.updateParentNodes();
		return rotatedTreeRoot;
	}


	////////////////////////////////////////////////////////////////////////////
	// STATISTICS
	////////////////////////////////////////////////////////////////////////////

	protected void updateHeightsAndBalances()
	{
		updateHeightsAndBalances(root);
	}

	protected void updateHeightsAndBalances(final AVLBinaryNode<K, V> node)
	{
		if (node != null)
		{
			node.updateHeightsAndBalances();
			updateHeightsAndBalances(node.left);
			updateHeightsAndBalances(node.right);
		}
	}

	public Collection<Long> getBalances()
	{
		updateHeightsAndBalances();
		return getBalances(root, new ExtendedList<Long>(size));
	}

	protected Collection<Long> getBalances(final AVLBinaryNode<K, V> node, final Collection<Long> collection)
	{
		if (node != null)
		{
			collection.add(node.balance);
			getBalances(node.left, collection);
			getBalances(node.right, collection);
		}
		return collection;
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
