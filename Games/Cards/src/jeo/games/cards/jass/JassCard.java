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
package jeo.games.cards.jass;

import jeo.games.cards.Constants;
import jeo.games.cards.objects.Card;
import jeo.games.cards.objects.Color;

public class JassCard
	extends Card
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 2598328382261446090L;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public JassCard(final Color color, final int number)
	{
		super(color, number);
	}


	////////////////////////////////////////////////////////////////////////////
	// CARD
	////////////////////////////////////////////////////////////////////////////

	@Override
	public int getValue(final Color dominantColor, final Card card)
	{
		int value;
		final Color cardColor = card.getColor();
		switch (card.getNumber())
		{
			// Nombre 9 (ou nell)
			case 7:
				if (cardColor.equals(dominantColor))
				{
					value = 14;
				}
				else
				{
					value = 0;
				}
				break;
			// Nombre 10
			case 8:
				value = 10;
				break;
			// Valet (ou bourg)
			case 9:
				if (cardColor.equals(dominantColor))
				{
					value = 20;
				}
				else
				{
					value = 2;
				}
				break;
			// Dame
			case 10:
				value = 3;
				break;
			// Roi
			case 11:
				value = 5;
				break;
			// As
			case 12:
				value = 11;
				break;
			// Sinon
			default:
				value = 0;
		}
		return value;
	}

	@Override
	public int getPower(final Color dominantColor, final Card firstCard, final Card card)
	{
		int power;
		final Color firstCardColor = firstCard.getColor();
		final Color cardColor = card.getColor();
		power = card.getNumber();
		if (cardColor.equals(dominantColor))
		{
			switch (card.getNumber())
			{
				// Nell
				case 7:
					power = 13;
					break;
				// Bourg
				case 9:
					power = 14;
					break;
				default:
			}
			power += 2 * Constants.N_NUMBERS;
		}
		else if (cardColor.equals(firstCardColor))
		{
			power += Constants.N_NUMBERS;
		}
		return power;
	}
}
