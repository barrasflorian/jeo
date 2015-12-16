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

import java.util.List;

import jeo.common.io.IOManager;
import jeo.common.structure.ExtendedList;
import jeo.games.cards.CardGame;
import jeo.games.cards.Constants;
import jeo.games.cards.objects.Card;
import jeo.games.cards.objects.Cards;
import jeo.games.cards.objects.Color;
import jeo.games.cards.objects.Colors;
import jeo.games.cards.objects.Deck;
import jeo.games.cards.objects.Hand;
import jeo.games.cards.objects.Player;

public class Jass
	implements CardGame
{
	////////////////////////////////////////////////////////////////////////////
	// ATTRIBUTE(S)
	////////////////////////////////////////////////////////////////////////////

	private Deck deck;
	private List<Player> players;
	private Color dominantColor;


	////////////////////////////////////////////////////////////////////////////
	// CARD GAME
	////////////////////////////////////////////////////////////////////////////

	public void init()
	{
		// Initialize
		IOManager.printInfo("Initialize ...");
		// Init the colors
		Colors.init();
		// Generate a new deck
		deck = new JassDeck();
		// Create the players
		players = new ExtendedList<Player>();
		for (int playerNumber = 0; playerNumber < Constants.N_PLAYERS; ++playerNumber)
		{
			players.add(new JassPlayer(playerNumber));
		}
		// Distribute the cards
		for (int cardIndex = 0; cardIndex < Constants.N_CARDS_PER_PLAYER; ++cardIndex)
		{
			for (int playerNumber = 0; playerNumber < Constants.N_PLAYERS; ++playerNumber)
			{
				players.get(playerNumber).receiveCard(deck.takeFirstCard());
			}
		}
		// Choose the dominant color
		dominantColor = Colors.getRandomColor();
		IOManager.printInfo("Dominant color: " + dominantColor);
		// Show the hands of the players
		for (int playerNumber = 0; playerNumber < Constants.N_PLAYERS; ++playerNumber)
		{
			final Hand hand = players.get(playerNumber).getHand();
			// hand.sort();
			IOManager.printInfo(hand);
		}
	}

	public void process()
	{
		// Play
		IOManager.printInfo("Play ...");
		for (int i = 0; i < Constants.N_CARDS_PER_PLAYER; ++i)
		{
			final Cards cards = new Cards();
			Card firstCard = null;
			for (int playerNumber = 0; playerNumber < Constants.N_PLAYERS; ++playerNumber)
			{
				final Player player = players.get(playerNumber);
				final Card card = player.playCard(cards);
				cards.addCard(card);
				if (playerNumber == 0)
				{
					firstCard = card;
				}
				IOManager.printInfo(card.getPower(dominantColor, firstCard, card));
			}
			IOManager.printInfo(cards);
			IOManager.printInfo("Winning card: " + cards.get(getWinningCard(cards)));
		}
	}


	////////////////////////////////////////////////////////////////////////////
	// JASS
	////////////////////////////////////////////////////////////////////////////

	public int getWinningCard(final Cards cards)
	{
		int winningCard = 0;
		Card card;
		final Card firstCard = cards.get(0);
		int power, maxPower = -1;
		for (int i = 0; i < cards.getNumberOfCards(); ++i)
		{
			card = cards.get(i);
			power = card.getPower(dominantColor, firstCard, card);
			if (power > maxPower)
			{
				maxPower = power;
				winningCard = i;
			}
		}
		return winningCard;
	}
}
