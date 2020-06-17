package ro.sevens.game

import ro.sevens.payload.Card

/**
 * server
 *
 * Copyright (C) 2020  Rachieru Dragos-Mihai
 *
 * server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with server.  If not, see [License](http://www.gnu.org/licenses/) .
 *
 */
class Hand private constructor(cards: MutableList<Card>) : ListContainer<Card> {

    constructor() : this(mutableListOf())

    private val _cards: MutableList<Card> = cards
    inline val cards: List<Card>
        get() = items

    override val items: List<Card>
        get() = _cards

    private val wonCards: MutableList<Card> = ArrayList()

    var wonPointsCount: Int = 0
        private set

    val cardsCount: Int
        get() = cards.size

    val wonCardsCount: Int
        get() = wonCards.size

    fun hasCard(card: Card): Boolean {
        return cards.any {
            it.theSame(card)
        }
    }

    fun chooseCard(card: Card): Boolean {
        val index = _cards.indexOfFirst { it.theSame(card) }
        if (index == -1) return false
        else _cards.removeAt(index)
        return true
    }

    fun addWonCards(cards: Collection<Card>) {
        wonCards += cards
        wonPointsCount += cards.count {
            it.isPoint
        }
    }

    fun chooseRandomCard(): Card {
        return cards.random()
    }

    fun addCard(card: Card) {
        _cards += card
    }

    override fun toString(): String {
        return "Hand(\n" +
                "_cards=$_cards, \n" +
                "wonCards=$wonCards, \n" +
                "wonPointsCount=$wonPointsCount\n" +
                ")"
    }

}