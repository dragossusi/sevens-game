package ro.sevens.game.room

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ro.sevens.game.Hand
import ro.sevens.game.PlayerSession
import ro.sevens.game.Round
import ro.sevens.game.deck.Deck
import ro.sevens.game.deck.DeckProvider
import ro.sevens.game.listener.PlayerNotifier
import ro.sevens.logger.TagLogger
import ro.sevens.payload.Card
import ro.sevens.payload.base.GameTypeData
import ro.sevens.payload.enums.RoomStatus

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
class NormalRoom constructor(
    override val id: Long,
    override val type: GameTypeData,
    deckProvider: DeckProvider,
    private val tagLogger: TagLogger?,
    private val playerNotifier: PlayerNotifier,
    override val roundEndDelay: Long = 1250L
) : Room {

    override val mutex = Mutex()

    var status = RoomStatus.WAITING
        set(value) {
            tagLogger?.i("$id changed from $field to $value")
            field = value
        }

    private val _players = mutableListOf<PlayerSession>()
    override val players: List<PlayerSession>
        get() = _players

    override val deck: Deck = deckProvider.createDeck(type)
    private val _remainingCards = mutableListOf<Card>()
    override val remainingCards: List<Card>
        get() = _remainingCards

    override val rounds = mutableListOf<Round>()

    override var currentPlayer: PlayerSession? = null

    override var currentRound: Round? = null

    override val canJoin: Boolean
        get() = players.size < type.maxPlayers && status == RoomStatus.WAITING

    override suspend fun startRound() {
        val player = currentPlayer!!
        val round = Round(player, players.count())
        rounds += round
        currentRound = round
        drawCards(player)
        playerNotifier.onRoundStarted(this)
    }

    override suspend fun addCard(player: PlayerSession, card: Card): Boolean {
        val result = mutex.withLock(this) {
            chooseCard(player, card)
        }
        if (!result) return result
        val nextPlayer = nextPlayer!!
        if (nextPlayer == startingPlayer) {
            if (currentRound!!.canCut(nextPlayer, playerCount)) {
                setPlayerTurn(nextPlayer)
            } else {
                newRound()
            }
        } else setPlayerTurn(nextPlayer)
        return result
    }

    private suspend fun setPlayerTurn(player: PlayerSession) {
        currentPlayer = player
        playerNotifier.onPlayerTurn(this)
    }

    private suspend fun chooseCard(player: PlayerSession, card: Card): Boolean {
        if (currentPlayer?.id == player.id) {
            return currentRound?.let {
                player.chooseCard(it, card)
            } ?: false
        }
        return false
    }

    override suspend fun endRound() {
        currentRound?.let {
            it.endRound()
            currentPlayer = it.owner
            playerNotifier.onRoundEnded(this)
            delay(roundEndDelay)
            drawCards(it.owner)
        } ?: throw IllegalStateException("No round started")
    }

    override suspend fun start() {
        when (status) {
            RoomStatus.WAITING -> {
                status = RoomStatus.IN_PROGRESS
                currentPlayer = startingPlayer
                initCards()
                initHands()
                startRound()
            }
            RoomStatus.ENDED -> throw IllegalStateException("Room already stopped")
            RoomStatus.IN_PROGRESS -> throw IllegalStateException("Room already started")
        }
    }

    override suspend fun stop() {
        when (status) {
            RoomStatus.ENDED -> throw IllegalStateException("Room already stopped")
            else -> {
                status = RoomStatus.ENDED
            }
        }
    }

    override suspend fun addPlayerSession(
        playerSession: PlayerSession,
        onRoomChanged: Room.OnRoomChanged
    ): Boolean {
        if (!canJoin || isFull) return false
        _players.add(playerSession)
        addListener(playerSession, onRoomChanged)
        onRoomChanged.onRoomConnected(id)
        if (isFull)
            start()
        return true
    }

    override fun addListener(player: PlayerSession, onRoomChanged: Room.OnRoomChanged) {
        playerNotifier.addListener(player, onRoomChanged)
    }

    override fun removeListener(player: PlayerSession) {
        playerNotifier.removeListener(player)
    }

    private fun initCards() {
        _remainingCards.clear()
        _remainingCards.addAll(deck.shuffle())
    }

    private fun initHands() {
        players.forEach {
            it.hand = Hand()
        }
    }

    private suspend fun drawCards(owner: PlayerSession) {
        mutex.withLock(deck) {
            val maxPlayers = type.maxPlayers
            while (owner.cardsCount <= 3 && remainingCards.isNotEmpty()) {
                var index = players.indexOf(owner)
                if (index == -1) throw Exception("How did this happen?")
                var times = maxPlayers
                while (times != 0) {
                    --times
                    players[index].addCard(drawCard())
                    index = (index + 1) % maxPlayers
                }
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun drawCard(): Card {
        return _remainingCards.removeLast()
    }
}