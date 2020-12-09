package ro.sevens.game.hand

import ro.sevens.game.ListContainer
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
abstract class Hand private constructor(cards: MutableList<Card>) : ListContainer<Card> {

    constructor() : this(mutableListOf())

    protected val _cards: MutableList<Card> = cards
    inline val cards: List<Card>
        get() = items

    override val items: List<Card>
        get() = _cards

    val cardsCount: Int
        get() = cards.size

    abstract val wonCardsCount: Int?

    abstract val wonPointsCount: Int?

    fun hasCard(card: Card): Boolean {
        return cards.any {
            it == card
        }
    }

    fun chooseCard(card: Card): Boolean {
        val index = _cards.indexOfFirst {
            it == card
        }
        if (index == -1) return false
        else _cards.removeAt(index)
        return true
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
                ")"
    }

    abstract fun addWonCards(cards: Collection<Card>)

}