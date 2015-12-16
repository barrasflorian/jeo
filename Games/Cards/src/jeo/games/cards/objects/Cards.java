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
package jeo.games.cards.objects;

import java.util.Collections;
import java.util.List;

import jeo.common.structure.ExtendedList;
import jeo.common.util.Strings;

public class Cards
	extends ExtendedList<Card>
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 1405514153329258881L;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public Cards()
	{
		super();
	}

	public Cards(final Cards cards)
	{
		super();
		addAll(cards);
	}


	////////////////////////////////////////////////////////////////////////////
	// CARDS
	////////////////////////////////////////////////////////////////////////////

	public void sort()
	{
		Collections.<Card>sort(this);
	}

	public Card takeFirstCard()
	{
		return takeCard(0);
	}

	public Card takeCard(final int index)
	{
		return remove(index);
	}

	public void addCard(final Card card)
	{
		add(card);
	}

	public void addCard(final int index, final Card card)
	{
		add(index, card);
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * @return the cards
	 */
	public List<Card> getCards()
	{
		return this;
	}

	/**
	 * @return the number of cards
	 */
	public int getNumberOfCards()
	{
		return size();
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public Object clone()
	{
		return super.clone();
	}

	@Override
	public String toString()
	{
		final StringBuilder stringBuilder = Strings.createBuffer();
		stringBuilder.append("|");
		for (final Card card : this)
		{
			stringBuilder.append(" ").append(card).append(" |");
		}
		return stringBuilder.toString();
	}
}
