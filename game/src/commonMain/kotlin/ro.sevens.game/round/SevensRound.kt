package ro.sevens.game.round

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ro.sevens.game.session.SevensPlayerSession
import ro.sevens.payload.Card
import ro.sevens.payload.canCut

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
class SevensRound constructor(
    override val startingPlayer: SevensPlayerSession,
    val numOfPlayers: Int
) : Round<SevensPlayerSession> {
    override var owner: SevensPlayerSession = startingPlayer
    private val _cards = mutableListOf<Card>()
    private val mutex = Mutex()
    private var status: Status = Status.NONE

    override val cards: List<Card>
        get() = _cards

    override suspend fun canAddCard(card: Card, from: SevensPlayerSession): Boolean {
        return if (_cards.size < numOfPlayers) true
        else card.canCut(cards.first(), numOfPlayers)
    }

    override suspend fun addCard(card: Card, from: SevensPlayerSession) {
        mutex.withLock(this) {
            val firstCard = cards.firstOrNull()
            firstCard?.let {
                if (card.canCut(firstCard, numOfPlayers)) {
                    owner = from
                }
            }
            _cards += card
        }
    }

    override fun canCut(playerSession: SevensPlayerSession, playerCount: Int): Boolean {
        val firstCard = cards.first()
        playerSession.hand!!.cards.forEach {
            if (it.canCut(firstCard, playerCount))
                return true
        }
        return false
    }

    override fun canContinue(playerSession: SevensPlayerSession, playerCount: Int): Boolean {
        return playerSession.id != owner.id && canCut(playerSession, playerCount)
    }

    override fun start(): Boolean {
        if (status != Status.NONE) return false
        status = Status.STARTED
        return true
    }

    override suspend fun end(playerSession: SevensPlayerSession): Boolean {
        if (playerSession.id != startingPlayer.id) return false
        if (status != Status.STARTED) return false
        owner.addWonCards(cards)
        status = Status.ENDED
        return true
    }

    override fun toString(): String {
        return "Round(\n" +
                "owner=${owner.player.name}, \n" +
                "cards=$_cards\n" +
                ")"
    }

    enum class Status {
        NONE,
        STARTED,
        ENDED
    }


}