package ro.sevens.game.room

import kotlinx.coroutines.CoroutineScope
import ro.sevens.game.deck.Deck
import ro.sevens.game.listener.*
import ro.sevens.game.round.Round
import ro.sevens.game.session.PlayerSession
import ro.sevens.payload.Card
import ro.sevens.payload.base.GameTypeData
import ro.sevens.payload.enums.RoomStatus
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
interface Room<S : PlayerSession,
        R : Round<S>> : RoomListeners<S>, CoroutineScope {

    val roundEndDelay: Long
    val id: Long

    val players: List<S>
    val isFull: Boolean
        get() = players.size >= maxPlayers

    val deck: Deck
    val remainingCards: List<Card>

    val rounds: List<R>

    val currentRound: R?

    //todo
    val startingPlayer: S
        get() = players[0]

    val type: GameTypeData

    var currentPlayer: S?
    val nextPlayer: S?
        get() = currentPlayer?.let {
            players[(players.indexOf(it) + 1) % maxPlayers]
        }

    val canJoin: Boolean
    val canStartRound: Boolean

    //funs

    suspend fun startRound()

    suspend fun addCard(player: S, card: Card): Boolean

    suspend fun chooseCardType(player: S, type: Card.Type): Boolean

    suspend fun endRound(player: S): Boolean

    suspend fun newRound(player: S): Boolean

    suspend fun start()

    suspend fun endGame()

    suspend fun stop()

    suspend fun addPlayerSession(
        playerSession: S,
        onRoomChanged: OnRoomChanged
    ): Boolean

    interface OnRoomChanged : OnRoundStarted, OnPlayerTurn, OnRoomConnected, OnRoundEnded, OnGameStarted, OnGameEnded {
        suspend fun onRoomStopped()
    }

    var status: RoomStatus
}

val Room<*,*>.playerCount: Int
    get() = players.size

val Room<*,*>.maxPlayers: Int
    get() = type.maxPlayers


val Room<*,*>.simplePlayers: List<SimplePlayerResponse>
    get() = players.map(PlayerSession::toSimplePlayer)