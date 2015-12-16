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

import jeo.common.structure.tree.node.RedBlackBinaryNode;

public class RedBlackTreeMap<K extends Comparable<K>, V>
	extends BinaryTree<K, V, RedBlackBinaryNode<K, V>>
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = -6571980128241694585L;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public RedBlackTreeMap()
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
		RedBlackBinaryNode<K, V> treeRoot = root;
		// If the root is null
		if (root == null)
		{
			// The root is set to a new node containing the given key and value
			setRoot(new RedBlackBinaryNode<K, V>(key, value));
		}
		// Else browse this tree
		else
		{
			int cmp;
			RedBlackBinaryNode<K, V> parent;
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
			final RedBlackBinaryNode<K, V> newNode = new RedBlackBinaryNode<K, V>(key, value);
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
			balanceAfterInsertion(newNode);
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
	protected void removeNode(final RedBlackBinaryNode<K, V> node)
	{
		// Get the parent and the successor of the specified node
		final RedBlackBinaryNode<K, V> parentNode = node.parent;
		// 0 or 1 child (this tree is guaranteed to be balanced)
		if ((node.left == null) || (node.right == null))
		{
			// Get the child if present
			RedBlackBinaryNode<K, V> childNode;
			if (node.left != null)
			{
				childNode = node.left;
			}
			else if (node.right != null)
			{
				childNode = node.right;
			}
			// No children
			else
			{
				childNode = null;
				// If the node is black
				if (!node.isRed)
				{
					// Balance this tree from the node
					balanceAfterDeletion(node);
				}
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
			// If the node has at least one child and is black
			if ((childNode != null) && !node.isRed)
			{
				// Balance this tree from the child
				balanceAfterDeletion(childNode);
			}
		}
		// 2 children (this tree is not guaranteed to be balanced)
		else
		{
			// Get the successor of the node to be removed
			// Note: the successor cannot be null since the node has a right node
			final RedBlackBinaryNode<K, V> successor = getSuccessor(node);
			// Remove the successor from this tree
			removeNode(successor);
			// Override the key and value of the node with the successor
			node.key = successor.key;
			node.value = successor.value;
		}
		// Decrement the number of nodes
		--size;
	}


	////////////////////////////////////////////////////////////////////////////
	// BALANCE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Balances the tree whose root is {@code treeRoot} after insertion.
	 * <p>
	 * @param treeRoot the root of the tree to be balanced
	 */
	@Override
	protected void balanceAfterInsertion(RedBlackBinaryNode<K, V> treeRoot)
	{
		// Get the parent
		RedBlackBinaryNode<K, V> parent = treeRoot.parent;
		if (parent != null)
		{
			// Get the grandparent
			RedBlackBinaryNode<K, V> grandparent = parent.parent;
			RedBlackBinaryNode<K, V> uncle;
			while (parent.isRed)
			{
				if (parent.isLeft)
				{
					// Get the uncle
					uncle = grandparent == null ? null : grandparent.right;
					if ((uncle != null) && uncle.isRed)
					{
						// Update the colors
						parent.isRed = false;
						uncle.isRed = false;
						grandparent.isRed = true;
						// Update the references
						treeRoot = grandparent;
						parent = treeRoot.parent;
						if (parent != null)
						{
							grandparent = parent.parent;
						}
						else
						{
							break;
						}
					}
					else
					{
						if (!treeRoot.isLeft)
						{
							// Update the references and rotate left
							treeRoot = parent;
							parent = rotateLeft(treeRoot);
							if (parent != null)
							{
								grandparent = parent.parent;
							}
							else
							{
								break;
							}
						}
						// Update the colors and rotate right
						parent.isRed = false;
						if (grandparent != null)
						{
							grandparent.isRed = true;
							rotateRight(grandparent);
						}
					}
				}
				else
				{
					uncle = grandparent == null ? null : grandparent.left;
					if ((uncle != null) && uncle.isRed)
					{
						// Update the colors
						parent.isRed = false;
						uncle.isRed = false;
						grandparent.isRed = true;
						// Update the references
						treeRoot = grandparent;
						parent = treeRoot.parent;
						if (parent != null)
						{
							grandparent = parent.parent;
						}
						else
						{
							break;
						}
					}
					else
					{
						if (treeRoot.isLeft)
						{
							// Update the references and rotate right
							treeRoot = parent;
							parent = rotateRight(treeRoot);
							if (parent != null)
							{
								grandparent = parent.parent;
							}
							else
							{
								break;
							}
						}
						// Update the colors and rotate left
						parent.isRed = false;
						if (grandparent != null)
						{
							grandparent.isRed = true;
							rotateLeft(grandparent);
						}
					}
				}
			}
			root.isRed = false;
		}
	}

	/**
	 * Balances the tree whose root is {@code treeRoot} after deletion.
	 * <p>
	 * @param treeRoot the root of the tree to be balanced
	 */
	@Override
	protected void balanceAfterDeletion(RedBlackBinaryNode<K, V> treeRoot)
	{
		// Get the parent
		RedBlackBinaryNode<K, V> parent, node, leftNode, rightNode;
		while ((treeRoot != root) && !treeRoot.isRed)
		{
			parent = treeRoot.parent;
			if (treeRoot.isLeft)
			{
				node = parent.right;
				if (node.isRed)
				{
					node.isRed = false;
					parent.isRed = true;
					rotateLeft(parent);
					node = parent.right;
				}
				leftNode = node.left;
				rightNode = node.right;
				if (((leftNode == null) || !leftNode.isRed) && ((rightNode == null) || !rightNode.isRed))
				{
					node.isRed = true;
					treeRoot = parent;
				}
				else
				{
					if ((rightNode == null) || !rightNode.isRed)
					{
						if (leftNode != null)
						{
							leftNode.isRed = false;
						}
						node.isRed = true;
						rotateRight(node);
						node = parent.right;
					}
					node.isRed = parent.isRed;
					parent.isRed = false;
					rightNode.isRed = false;
					rotateLeft(parent);
					treeRoot = root;
					break;
				}
			}
			else
			{
				node = parent.left;
				if (node.isRed)
				{
					node.isRed = false;
					parent.isRed = true;
					rotateRight(parent);
					node = parent.left;
				}
				leftNode = node.left;
				rightNode = node.right;
				if (((leftNode == null) || !leftNode.isRed) && ((rightNode == null) || !rightNode.isRed))
				{
					node.isRed = true;
					treeRoot = parent;
				}
				else
				{
					if ((leftNode == null) || !leftNode.isRed)
					{
						if (rightNode != null)
						{
							rightNode.isRed = false;
						}
						node.isRed = true;
						rotateLeft(node);
						node = parent.left;
					}
					node.isRed = parent.isRed;
					parent.isRed = false;
					leftNode.isRed = false;
					rotateRight(parent);
					treeRoot = root;
					break;
				}
			}
		}
		treeRoot.isRed = false;
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
	protected void setRoot(final RedBlackBinaryNode<K, V> node)
	{
		root = node;
		if (root != null)
		{
			root.parent = null;
			root.isRed = false;
		}
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
