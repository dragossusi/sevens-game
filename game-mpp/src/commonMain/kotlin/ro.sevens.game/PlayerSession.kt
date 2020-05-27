package ro.sevens.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ro.sevens.game.room.Room
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
@Serializable
class PlayerSession constructor(
    @SerialName("room")
    val room: Room,
    @SerialName("player")
    val player: Player
) {

    var hand: Hand? = null

    val cardsCount: Int
        get() = hand!!.cardsCount

    val wonCardsCount: Int
        get() = hand!!.wonCardsCount

    val wonPointsCount: Int
        get() = hand!!.wonPointsCount

    val id: Long
        get() = player.id

    suspend fun chooseCard(round: Round, card: Card): Boolean {
        val hand = hand ?: return false

        if (hand.chooseCard(card)) {
            round.addCard(card, this)
            hand.cards
            return true
        }

        return false
    }

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

    override fun toString(): String {
        return "PlayerSession(\n" +
                "room=$room, \n" +
                "player=$player, \n" +
                "hand=$hand\n" +
                ")"
    }

}