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

public abstract class BinaryNode<K extends Comparable<K>, V, N extends BinaryNode<K, V, N>>
	extends Node<K, V>
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 5288646753242971859L;
	public N parent, left, right;
	public boolean isLeft;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructs a binary node with the specified key and value.
	 * <p>
	 * @param key   the key of the node
	 * @param value the value of the node
	 */
	protected BinaryNode(final K key, final V value)
	{
		super(key, value);
		parent = left = right = null;
	}


	////////////////////////////////////////////////////////////////////////////
	// PARENT, LEFT AND RIGHT NODES
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Sets the left node of this node.
	 * <p>
	 * @param leftNode the left node to set
	 */
	public abstract void setLeftNode(final N leftNode);

	/**
	 * Sets the right node of this node.
	 * <p>
	 * @param rightNode the right node to set
	 */
	public abstract void setRightNode(final N rightNode);
}
