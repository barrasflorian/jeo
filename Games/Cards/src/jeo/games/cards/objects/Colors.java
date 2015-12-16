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

import java.util.List;
import java.util.Random;

import jeo.common.structure.ExtendedList;
import jeo.games.cards.Constants;
import jeo.games.cards.Types.ColorType;

public class Colors
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	public final static List<Color> VALUES = new ExtendedList<Color>();
	private final static Random randomGenerator = new Random();


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private Colors()
	{
	}


	////////////////////////////////////////////////////////////////////////////
	// COLORS
	////////////////////////////////////////////////////////////////////////////

	public static void init()
	{
		for (final ColorType colorType : Constants.COLOR_TYPES)
		{
			VALUES.add(new Color(colorType));
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// GETTER(S)
	////////////////////////////////////////////////////////////////////////////

	public static int getIndex(final Color color)
	{
		return VALUES.indexOf(color);
	}

	public static Color getColor(final int index)
	{
		return VALUES.get(index);
	}

	public static Color getRandomColor()
	{
		return getColor(randomGenerator.nextInt(Constants.N_COLORS));
	}
}
