package ro.dragossusi.sevens.game.round

import ro.dragossusi.sevens.game.session.RoomPlayer
import ro.dragossusi.sevens.payload.Card
import ro.dragossusi.sevens.payload.game.CardsContainer
import ro.dragossusi.sevens.payload.game.RoundResponse

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
interface Round : CardsContainer {
    val startingPlayer: RoomPlayer
    var owner: RoomPlayer

    fun canCut(playerSession: RoomPlayer, playerCount: Int): Boolean
    fun canContinue(playerSession: RoomPlayer, playerCount: Int): Boolean

    suspend fun addCard(card: Card, from: RoomPlayer)
    fun canAddCard(card: Card, from: RoomPlayer): Boolean
    suspend fun end(playerSession: RoomPlayer): Boolean

    fun start(): Boolean
}

fun Round.toResponse() = RoundResponse(startingPlayer.id, owner.id, cards)