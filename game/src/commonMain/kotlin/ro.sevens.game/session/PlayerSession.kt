package ro.sevens.game.session

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ro.sevens.game.hand.Hand
import ro.sevens.game.room.Room
import ro.sevens.game.round.Round
import ro.sevens.payload.Card
import ro.sevens.payload.Player
import ro.sevens.payload.game.SimplePlayerResponse

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
class PlayerSession constructor(
    val room: Room<*>,
    val player: Player,
    var hand: Hand? = null
) {

    val mutex = Mutex()

    val wonCardsCount: Int?
        get() = hand?.wonCardsCount

    val wonPointsCount: Int?
        get() = hand?.wonPointsCount

    val cardsCount: Int
        get() = hand?.cardsCount ?: 0

    val id: Long
        get() = player.id

    fun addCard(card: Card) {
        hand!!.addCard(card)
    }

    fun toSimplePlayer() = SimplePlayerResponse(
        id,
        player.name,
        player.image,
        cardsCount,
        wonCardsCount,
        wonPointsCount
    )

    fun addWonCards(cards: List<Card>) {
        hand!!.addWonCards(cards)
    }

    //player fields
    val name: String
        get() = player.name

    override fun toString(): String {
        return "PlayerSession(\n" +
                "room=$room, \n" +
                "player=$player, \n" +
                "hand=$hand\n" +
                ")"
    }

}

suspend fun <S : PlayerSession> S.chooseCard(round: Round, card: Card): Boolean {
    val hand = hand ?: return false

    mutex.withLock(hand) {
        if (hand.chooseCard(card)) {
            round.addCard(card, this)
            hand.cards
            return true
        }
    }
    return false
}