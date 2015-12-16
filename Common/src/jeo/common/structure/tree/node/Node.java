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

import java.io.Serializable;
import java.util.Map.Entry;

import jeo.common.util.Arguments;
import jeo.common.util.Bits;

public class Node<K extends Comparable<K>, V>
	implements Comparable<Entry<K, V>>, Entry<K, V>, Serializable
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = -7430716714165080236L;
	public K key;
	public V value;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Constructs a node with the specified key and value.
	 * <p>
	 * @param key   the key of the node
	 * @param value the value of the node
	 */
	public Node(final K key, final V value)
	{
		this.key = key;
		this.value = value;
	}


	////////////////////////////////////////////////////////////////////////////
	// COMPARABLE
	////////////////////////////////////////////////////////////////////////////

	public int compareTo(final Entry<K, V> anotherEntry)
	{
		return key.compareTo(anotherEntry.getKey());
	}


	////////////////////////////////////////////////////////////////////////////
	// ENTRY
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the key.
	 * <p>
	 * @return the key
	 */
	public K getKey()
	{
		return key;
	}

	/**
	 * Returns the value associated with the key.
	 * <p>
	 * @return the value associated with the key
	 */
	public V getValue()
	{
		return value;
	}

	/**
	 * Replaces the value currently associated with the key with the given
	 * value.
	 * <p>
	 * @param value the value to set
	 * <p>
	 * @return the value associated with the key before this method was called
	 */
	public V setValue(final V value)
	{
		final V oldValue = this.value;
		this.value = value;
		return oldValue;
	}

	@Override
	public boolean equals(final Object object)
	{
		if (object instanceof Entry)
		{
			final Entry<?, ?> entry = (Entry<?, ?>) object;
			return Arguments.equals(key, entry.getKey()) && Arguments.equals(value, entry.getValue());
		}
		else
		{
			return false;
		}
	}

	@Override
	public int hashCode()
	{
		return Bits.generateHashCode(Bits.hash(key), Bits.hash(value));
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public String toString()
	{
		return "'" + key + "' -> '" + value + "'";
	}
}
