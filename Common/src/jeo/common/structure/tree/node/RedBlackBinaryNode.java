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

public class RedBlackBinaryNode<K extends Comparable<K>, V>
	extends BinaryNode<K, V, RedBlackBinaryNode<K, V>>
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = -8756370834035469941L;
	public boolean isRed;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructs a red-black binary node with the specified key and value.
	 * <p>
	 * @param key   the key of the node
	 * @param value the value of the node
	 */
	public RedBlackBinaryNode(final K key, final V value)
	{
		super(key, value);
		isRed = true;
	}


	////////////////////////////////////////////////////////////////////////////
	// PARENT, LEFT AND RIGHT NODES
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Sets the left node of this node.
	 * <p>
	 * @param leftNode the left node to set
	 */
	@Override
	public void setLeftNode(final RedBlackBinaryNode<K, V> leftNode)
	{
		this.left = leftNode;
		if (this.left != null)
		{
			this.left.isLeft = true;
			this.left.parent = this;
		}
	}

	/**
	 * Sets the right node of this node.
	 * <p>
	 * @param rightNode the right node to set
	 */
	@Override
	public void setRightNode(final RedBlackBinaryNode<K, V> rightNode)
	{
		this.right = rightNode;
		if (this.right != null)
		{
			this.right.isLeft = false;
			this.right.parent = this;
		}
	}
}
