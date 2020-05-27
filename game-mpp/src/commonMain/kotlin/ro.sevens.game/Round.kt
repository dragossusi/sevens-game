package ro.sevens.game

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
class Round constructor(
    var owner: PlayerSession,
    val numOfPlayers: Int
) {
    private val _cards = mutableListOf<Card>()
    private val mutex = Mutex()

    val cards: List<Card>
        get() = _cards

    suspend fun addCard(card: Card, from: PlayerSession) {
        mutex.withLock(this) {
            val lastCard = cards.lastOrNull()
            lastCard?.let {
                if (card.canCut(lastCard, numOfPlayers)) {
                    owner = from
                }
            }
            _cards += card
        }
    }

    fun canCut(playerSession: PlayerSession, playerCount: Int): Boolean {
        val lastCard = cards.first()
        playerSession.hand!!.cards.forEach {
            if (it.canCut(lastCard, playerCount))
                return true
        }
        return false
    }

    fun endRound() {
        owner.hand!!.addWonCards(cards)
    }

    override fun toString(): String {
        return "Round(\n" +
                "owner=${owner.player.name}, \n" +
                "cards=$_cards\n" +
                ")"
    }


}