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

import java.io.Serializable;

import jeo.common.util.Bits;
import jeo.common.util.Strings;

public abstract class Card
	implements Comparable<Card>, Serializable
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = -2101916660574707862L;
	private final Color color;
	private final int colorIndex;
	private final int number;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public Card(final Color color, final int number)
	{
		this.color = color;
		colorIndex = color.getIndex();
		this.number = number;
	}


	////////////////////////////////////////////////////////////////////////////
	// CARD
	////////////////////////////////////////////////////////////////////////////

	public abstract int getValue(Color dominantColor, Card card);

	public abstract int getPower(Color dominantColor, Card firstCard, Card card);


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * @return the color
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * @return the color index
	 */
	public int getColorIndex()
	{
		return color.getIndex();
	}

	/**
	 * @return the number
	 */
	public int getNumber()
	{
		return number;
	}


	////////////////////////////////////////////////////////////////////////////
	// COMPARABLE
	////////////////////////////////////////////////////////////////////////////

	public int compareTo(final Card anotherCard)
	{
		final int order = colorIndex - anotherCard.getColorIndex();
		return order == 0 ? number - anotherCard.getNumber() : order;
	}

	public boolean equals(final Card anotherCard)
	{
		return compareTo(anotherCard) == 0;
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean equals(final Object object)
	{
		if ((object == null) || (getClass() != object.getClass()))
		{
			return false;
		}
		final Card otherCard = (Card) object;
		if ((colorIndex != otherCard.colorIndex) || (number != otherCard.number))
		{
			return false;
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		return Bits.generateHashCode(Bits.hash(serialVersionUID), colorIndex, number);
	}

	@Override
	public String toString()
	{
		final StringBuilder stringBuilder = Strings.createBuffer();
		switch (number)
		{
			case 9:
				stringBuilder.append("V");
				break;
			case 10:
				stringBuilder.append("D");
				break;
			case 11:
				stringBuilder.append("R");
				break;
			case 12:
				stringBuilder.append("A");
				break;
			default:
				stringBuilder.append(number + 2);
		}
		stringBuilder.append(color.toString());
		return stringBuilder.toString();
	}
}
