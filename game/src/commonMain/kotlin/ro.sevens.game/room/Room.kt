package ro.dragossusi.sevens.game.room

import kotlinx.coroutines.CoroutineScope
import ro.dragossusi.sevens.game.deck.Deck
import ro.dragossusi.sevens.game.listener.PlayerListener
import ro.dragossusi.sevens.game.session.PlayerSession
import ro.dragossusi.sevens.payload.Card
import ro.dragossusi.sevens.payload.base.GameTypeData
import ro.dragossusi.sevens.payload.enums.RoomStatus
import ro.dragossusi.sevens.payload.game.SimplePlayerResponse

/**
 *
 * Class containing game logic
 *
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
interface Room<L : PlayerListener> : CoroutineScope {

    /**
     * Delay starting a new round in millis
     */
    val roundEndDelay: Long

    /**
     * Room id
     */
    val id: Long

    /**
     * Players playing in this room
     */
    val players: List<PlayerSession>

    /**
     * Check if the room is full(no more players can join)
     */
    val isFull: Boolean
        get() = players.size >= maxPlayers

    /**
     * deck used
     */
    val deck: Deck

    /**
     * current cards placed
     */
    val currentCards: List<Card>?

    /**
     * remaining cards to be drawn
     */
    val remainingCards: List<Card>

    /**
     * starting player
     */
    val startingPlayer: PlayerSession
        get() = players[0]

    /**
     * game type
     */
    val type: GameTypeData

    /**
     * current player
     */
    var currentPlayer: PlayerSession?

    /**
     * next player
     */
    val nextPlayer: PlayerSession?
        get() = currentPlayer?.let {
            players[(players.indexOf(it) + 1) % maxPlayers]
        }

    /**
     * Check if a player can join
     */
    val canJoin: Boolean

    /**
     * Check if a round can start
     */
    val canStartRound: Boolean

    //funs

    /**
     * add a card
     *
     * @param player    player that adds
     * @param card      the card to add
     */
    suspend fun addCard(player: PlayerSession, card: Card): Boolean

    /**
     * choose a card type
     *
     * @param player    Player that chooses
     * @param type      Card Type to choose
     */
    suspend fun chooseCardType(player: PlayerSession, type: Card.Type): Boolean

    /**
     * starts the room
     */
    suspend fun start()

    /**
     * ends the game
     */
    suspend fun endGame()

    /**
     * stop the room
     */
    suspend fun stop()

    /**
     * Add a player with a listener
     *
     * @param playerSession     the player
     * @param listener          player listener
     */
    suspend fun addPlayerSession(
        playerSession: PlayerSession,
        listener: L
    ): Boolean

    /**
     * Status of the room
     */
    var status: RoomStatus
}

/**
 * Number of players in the room
 */
val Room<*>.playerCount: Int
    get() = players.size

/**
 * Max number of players allowed in the room
 */
val Room<*>.maxPlayers: Int
    get() = type.maxPlayers

/**
 * Get players in the room in a simple object used for serialization
 */
val Room<*>.simplePlayers: List<SimplePlayerResponse>
    get() = players.map(PlayerSession::toSimplePlayer)