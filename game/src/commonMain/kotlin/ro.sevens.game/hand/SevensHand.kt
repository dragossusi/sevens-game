package ro.sevens.game.hand

import ro.sevens.payload.Card
import ro.sevens.payload.isSevensPoint

/**
 * sevens-game
 *
 * Copyright (C) 2020  Rachieru Dragos-Mihai
 *
 * sevens-game is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * sevens-game is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with sevens-game.  If not, see [License](http://www.gnu.org/licenses/) .
 *
 */
class SevensHand : Hand() {

    private val wonCards: MutableList<Card> = ArrayList()

    override val wonCardsCount: Int
        get() = wonCards.size

    override var wonPointsCount: Int = 0
        private set

    override fun addWonCards(cards: Collection<Card>) {
        wonCards += cards
        wonPointsCount += cards.count {
            it.isSevensPoint
        }
    }

    override fun toString(): String {
        return "SevensHand(\n" +
                "_cards=$_cards, \n" +
                "wonCards=$wonCards, \n" +
                "wonPointsCount=$wonPointsCount\n" +
                ")"
    }
}