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
package jeo.common.structure.tree.node;

public class AVLBinaryNode<K extends Comparable<K>, V>
	extends BinaryNode<K, V, AVLBinaryNode<K, V>>
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 3892793058305732935L;
	private static boolean update = true;
	public long height, balance;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructs an AVL binary node with the specified key and value.
	 * <p>
	 * @param key   the key of the node
	 * @param value the value of the node
	 */
	public AVLBinaryNode(final K key, final V value)
	{
		super(key, value);
		height = 0;
		balance = 0;
	}


	////////////////////////////////////////////////////////////////////////////
	// UPDATE
	////////////////////////////////////////////////////////////////////////////

	public static void isNecessaryToUpdate(final boolean update)
	{
		AVLBinaryNode.update = update;
	}


	////////////////////////////////////////////////////////////////////////////
	// PARENT, LEFT AND RIGHT NODES
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Sets the parent and updates the parents of this node if
	 * {@code updateParents} is {@code true}.
	 * <p>
	 * @param parentNode the parent to set
	 */
	protected void setParentNode(final AVLBinaryNode<K, V> parentNode)
	{
		parent = parentNode;
		// Update the heights and the balances of the parents
		if (update)
		{
			updateParentNodes();
		}
	}

	/**
	 * Sets the left node of this node.
	 * <p>
	 * @param leftNode the left node to set
	 */
	@Override
	public void setLeftNode(final AVLBinaryNode<K, V> leftNode)
	{
		this.left = leftNode;
		if (update)
		{
			updateHeightAndBalance();
		}
		if (this.left != null)
		{
			this.left.isLeft = true;
			this.left.setParentNode(this);
		}
	}

	/**
	 * Sets the right node of this node.
	 * <p>
	 * @param rightNode the right node to set
	 */
	@Override
	public void setRightNode(final AVLBinaryNode<K, V> rightNode)
	{
		this.right = rightNode;
		if (update)
		{
			updateHeightAndBalance();
		}
		if (this.right != null)
		{
			this.right.isLeft = false;
			this.right.setParentNode(this);
		}
	}

	/**
	 * Returns {@code true} if this node is a leaf, or {@code false} otherwise.
	 * <p>
	 * @return {@code true} if this node is a leaf, or {@code false} otherwise
	 */
	public boolean isLeaf()
	{
		return (left == null) && (right == null);
	}


	////////////////////////////////////////////////////////////////////////////
	// HEIGHT & BALANCE
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Updates the height and the balance of this node.
	 */
	public void updateHeightAndBalance()
	{
		if (isLeaf())
		{
			height = 0;
			balance = 0;
		}
		else
		{
			long leftHeight;
			long rightHeight;
			// Update the height of this node
			if (left == null)
			{
				leftHeight = -1;
				rightHeight = right.height;
				height = 1 + right.height;
			}
			else if (right == null)
			{
				leftHeight = left.height;
				rightHeight = -1;
				height = 1 + left.height;
			}
			else
			{
				leftHeight = left.height;
				rightHeight = right.height;
				height = 1 + (leftHeight >= rightHeight ? leftHeight : rightHeight);
			}
			// Update the balance of this node
			balance = rightHeight - leftHeight;
		}
	}

	/**
	 * Updates the height and the balance of this node and if this node is a
	 * leaf, updates also the heights and the balances of the parents.
	 */
	public void updateHeightsAndBalances()
	{
		updateHeightAndBalance();
		if (isLeaf())
		{
			updateParentNodes();
		}
	}

	/**
	 * Updates the heights and the balances of the parents of this node.
	 */
	public void updateParentNodes()
	{
		AVLBinaryNode<K, V> node = parent;
		while (node != null)
		{
			node.updateHeightAndBalance();
			node = node.parent;
		}
	}
}
