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

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;

import jeo.common.structure.tree.node.Node;

public abstract class Tree<K extends Comparable<K>, V, N extends Node<K, V>>
	extends AbstractMap<K, V>
	implements Cloneable, Serializable
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = -8170321791788859556L;
	/**
	 * The root of this tree.
	 */
	protected N root = null;
	/**
	 * The number of entries in this tree.
	 */
	protected transient int size = 0;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	protected Tree()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// INSERT
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Copies all of the mappings from the specified map to this tree. These
	 * mappings replace any mappings that this tree had for any of the keys
	 * currently in the specified map.
	 * <p>
	 * @param map mappings to be stored in this tree
	 * <p>
	 * @throws ClassCastException   if the class of a key or value in the
	 *                              specified map prevents it from being stored
	 *                              in this tree
	 * @throws NullPointerException if the specified map is null or the
	 *                              specified map contains a null key and this
	 *                              tree does not permit null keys
	 */
	@Override
	public void putAll(final Map<? extends K, ? extends V> map)
	{
		super.putAll(map);
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
	 * @throws ClassCastException   if the specified key cannot be compared with
	 *                              the keys currently in this tree
	 * @throws NullPointerException if the specified key is null and this tree
	 *                              uses natural ordering, or its comparator
	 *                              does not permit null keys
	 */
	@Override
	public V get(final Object key)
	{
		// Find the node
		final N node = getNode(key);
		// Return the value of the node if present, null otherwise
		return node == null ? null : node.value;
	}

	/**
	 * Returns this tree's node for the given key, or {@code null} if this tree
	 * does not contain a node for the key.
	 * <p>
	 * @param key the key of the node to be searched
	 * <p>
	 * @return this tree's node for the given key, or {@code null} if this tree
	 *         does not contain a node for the key
	 * <p>
	 * @throws ClassCastException   if the specified key cannot be compared with
	 *                              the keys currently in this tree
	 * @throws NullPointerException if the specified key is null and this tree
	 *                              uses natural ordering, or its comparator
	 *                              does not permit null keys
	 */
	@SuppressWarnings("unchecked")
	protected N getNode(final Object key)
	{
		return getNode((Comparable<? super K>) key);
	}

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
	protected abstract N getNode(final Comparable<? super K> key);


	////////////////////////////////////////////////////////////////////////////
	// PRINT
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Prints this tree.
	 */
	public abstract void print();

	/**
	 * Returns a {@link String} representation of the specified object.
	 * <p>
	 * @param label  the label of {@code object}
	 * @param object the {@link Object} to be converted in {@link String}
	 * <p>
	 * @return a {@link String} representation of the specified object
	 */
	protected String convertToString(final String label, final Object object)
	{
		String value;
		if (object == null)
		{
			value = "-";
		}
		else
		{
			value = String.valueOf(object);
		}
		return label + ": " + value + " ";
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
