package ro.dragossusi.sevens.game.room

import kotlinx.coroutines.CoroutineScope
import ro.dragossusi.sevens.game.deck.Deck
import ro.dragossusi.sevens.game.listener.PlayerListener
import ro.dragossusi.sevens.game.session.RoomPlayer
import ro.dragossusi.sevens.payload.Card
import ro.dragossusi.sevens.payload.enums.RoomStatus
import ro.dragossusi.sevens.payload.enums.SupportedGame
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
    val players: List<RoomPlayer>

    /**
     * Check if the room is full(no more players can join)
     */
    val isFull: Boolean
        get() = players.size >= maxPlayers


    /**
     * Max players allowed in this room
     */
    val maxPlayers: Int

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
    val startingPlayer: RoomPlayer
        get() = players[0]

    /**
     * game type
     */
    val game: SupportedGame

    /**
     * current player
     */
    var currentPlayer: RoomPlayer?

    /**
     * next player
     */
    val nextPlayer: RoomPlayer?
        get() = currentPlayer?.let {
            players[(players.indexOf(it) + 1) % maxPlayers]
        }

    /**
     * Check if a player can join
     */
    val canJoin: Boolean

    //funs

    /**
     * Checks if user can draw card
     *
     * @param from the player who wants to draw a card
     */
    suspend fun canDrawCard(from: RoomPlayer): Boolean

    /**
     * Checks if user can add this card
     *
     * @param card the card to check
     * @param from the player who wants to add
     */
    suspend fun canAddCard(card: Card, from: RoomPlayer): Boolean

    /**
     * add a card
     *
     * @param player    player that adds
     * @param card      the card to add
     */
    suspend fun addCard(player: RoomPlayer, card: Card): Boolean

    /**
     * choose a card type
     *
     * @param player    Player that chooses
     * @param type      Card Type to choose
     */
    suspend fun canChooseCardType(player: RoomPlayer, type: Card.Type): Boolean

    /**
     * choose a card type
     *
     * @param player    Player that chooses
     * @param type      Card Type to choose
     */
    suspend fun chooseCardType(player: RoomPlayer, type: Card.Type): Boolean

    /**
     * draw a card
     *
     * @param player    Player that chooses
     * @param type      Card Type to choose
     */
    suspend fun drawCard(player: RoomPlayer): Boolean

    /**
     * Checks if user can end turn
     *
     * @param from the player who wants to add
     */
    suspend fun canEndTurn(from: RoomPlayer): Boolean

    /**
     * end turn
     *
     * @param player    Player that wants to end turn
     */
    suspend fun endTurn(player: RoomPlayer): Boolean

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
        playerSession: RoomPlayer,
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
 * Get players in the room in a simple object used for serialization
 */
val Room<*>.simplePlayers: List<SimplePlayerResponse>
    get() = players.map(RoomPlayer::toSimplePlayer)