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
package jeo.games.cards;

import java.util.Arrays;
import java.util.List;

import jeo.common.structure.ExtendedList;
import jeo.games.cards.Types.ColorType;

public class Constants
{
	////////////////////////////////////////////////////////////////////////////
	// CONSTANT(S)
	////////////////////////////////////////////////////////////////////////////

	// Players
	public static final int N_PLAYERS = 4;
	// Colors
	public static final List<ColorType> COLOR_TYPES = new ExtendedList<ColorType>(Arrays.asList(ColorType.values()));
	public static final int N_COLORS = COLOR_TYPES.size();
	// Numbers
	public static final int N_NUMBERS = 13;
	// Cards
	public static final int N_CARDS = N_COLORS * N_NUMBERS;
	public static final int N_CARDS_PER_PLAYER = N_CARDS / N_PLAYERS;


	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR(S)
	////////////////////////////////////////////////////////////////////////////

	private Constants()
	{
	}
}
