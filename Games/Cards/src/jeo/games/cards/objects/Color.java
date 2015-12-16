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

import jeo.common.util.Strings;
import jeo.games.cards.Constants;
import jeo.games.cards.Types.ColorType;

public class Color
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	private final ColorType type;
	private final int index;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	public Color(final ColorType type)
	{
		this.type = type;
		index = Constants.COLOR_TYPES.indexOf(type);
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S)
	////////////////////////////////////////////////////////////////////////////

	public ColorType getType()
	{
		return type;
	}

	public int getIndex()
	{
		return index;
	}


	////////////////////////////////////////////////////////////////////////////
	// OBJECT
	////////////////////////////////////////////////////////////////////////////

	@Override
	public String toString()
	{
		final StringBuilder stringBuilder = Strings.createBuffer();
		switch (type)
		{
			case SPADE:
				stringBuilder.append("\u2660"); // spade
				break;
			case HEART:
				stringBuilder.append("\u2764"); // heart
				break;
			case CLUB:
				stringBuilder.append("\u2663"); // club
				break;
			case DIAMOND:
				stringBuilder.append("\u25C6"); // diamond
				break;
			default:
				stringBuilder.append("?"); // unknown
				break;
		}
		return stringBuilder.toString();
	}
}
