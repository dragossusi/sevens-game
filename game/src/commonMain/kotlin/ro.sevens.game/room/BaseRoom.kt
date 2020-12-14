package ro.sevens.game.room

import kotlinx.coroutines.withContext
import ro.sevens.game.hand.Hand
import ro.sevens.game.listener.PlayerListener
import ro.sevens.game.listener.PlayerNotifier
import ro.sevens.game.session.PlayerSession
import ro.sevens.logger.TagLogger
import ro.sevens.payload.Card
import ro.sevens.payload.enums.RoomStatus

/**
 * sevens-game
 *
 * Copyright (C) 2020  Rachieru Dragos-Mihai
 *
 * sevens-game is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * sevens-game is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with sevens-game.  If not, see [License](http://www.gnu.org/licenses/) .
 *
 */
abstract class BaseRoom<L : PlayerListener>(
    protected val playerNotifier: PlayerNotifier,
    protected val tagLogger: TagLogger?
) : Room<L> {

    override var status = RoomStatus.WAITING
        set(value) {
            tagLogger?.i("$id changed from $field to $value")
            field = value
        }

    override val canJoin: Boolean
        get() = players.size < type.maxPlayers && status == RoomStatus.WAITING


    protected val _players = mutableListOf<PlayerSession>()
    override val players: List<PlayerSession>
        get() = _players

    private val _remainingCards = mutableListOf<Card>()
    override val remainingCards: List<Card>
        get() = _remainingCards

    override var currentPlayer: PlayerSession? = null

    protected fun initCards() {
        _remainingCards.clear()
        _remainingCards.addAll(deck.shuffle())
    }

    abstract fun createHand(): Hand

    protected fun initHands() {
        players.forEach {
            it.hand = createHand()
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    protected fun drawCard(): Card {
        return _remainingCards.removeLast()
    }

    fun addRoomListener(
        player: PlayerSession,
        onRoomChanged: OnRoomChangedListener
    ) {
        playerNotifier.addListener(player, onRoomChanged)
    }

    fun removeRoomListener(player: PlayerSession) {
        playerNotifier.removeListener(player)
    }

    override suspend fun start() = withContext(coroutineContext) {
        when (status) {
            RoomStatus.WAITING, RoomStatus.ENDED -> {
                playerNotifier.onGameStarted(this@BaseRoom)
                status = RoomStatus.IN_PROGRESS
                startGame()
            }
            RoomStatus.STOPPED -> throw IllegalStateException("Room already stopped")
            RoomStatus.IN_PROGRESS -> throw IllegalStateException("Room already started")
        }
    }

    protected abstract suspend fun startGame()

    override suspend fun endGame() = withContext(coroutineContext) {
        when (status) {
            RoomStatus.ENDED -> throw IllegalStateException("Game already ended")
            else -> {
                status = RoomStatus.ENDED
                playerNotifier.onGameEnded(this@BaseRoom)
            }
        }
    }

    override suspend fun stop() = withContext(coroutineContext) {
        when (status) {
            RoomStatus.STOPPED -> throw IllegalStateException("Room already stopped")
            else -> {
                status = RoomStatus.STOPPED
                playerNotifier.onRoomStopped(this@BaseRoom)
            }
        }
    }

    protected suspend fun setPlayerTurn(player: PlayerSession) {
        currentPlayer = player
        playerNotifier.onPlayerTurn(this)
    }

}