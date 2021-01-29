package ro.dragossusi.sevens.game.room

import kotlinx.coroutines.withContext
import ro.dragossusi.sevens.game.deck.Deck
import ro.dragossusi.sevens.game.deck.DeckProvider
import ro.dragossusi.sevens.game.hand.Hand
import ro.dragossusi.sevens.game.hand.SevensHand
import ro.dragossusi.sevens.game.listener.*
import ro.dragossusi.sevens.game.round.SevensRound
import ro.dragossusi.sevens.game.session.RoomPlayer
import ro.dragossusi.logger.TagLogger
import ro.dragossusi.sevens.payload.Card
import ro.dragossusi.sevens.payload.base.GameTypeData
import ro.dragossusi.sevens.payload.enums.SupportedGame
import kotlin.coroutines.CoroutineContext

/**
 *
 * Septica implementation
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
class SevensRoom constructor(
    override val id: Long,
    val type: GameTypeData,
    deckProvider: DeckProvider,
    tagLogger: TagLogger?,
    playerNotifier: PlayerNotifier = MapPlayerNotifier(tagLogger),
    roundsNotifier: RoundsNotifier<RoundedPlayerListener, SevensRound> = MapRoundsNotifier(tagLogger),
    override val coroutineContext: CoroutineContext,
    override val roundEndDelay: Long = 1250L
) : BaseRoundedRoom<RoundedPlayerListener, SevensRound>(roundsNotifier, playerNotifier, tagLogger) {

    override val game: SupportedGame
        get() = SupportedGame.SEVENS

    override val maxPlayers: Int
        get() = type.maxPlayers

    override suspend fun startGame() {
        currentPlayer = startingPlayer
        initCards()
        initHands()
        startRound()
    }

    override val deck: Deck = deckProvider.createDeck(game, type)

    override val canStartRound: Boolean
        get() = players.all {
            it.cardsCount != 0
        }

    override val currentCards: List<Card>?
        get() = currentRound?.cards

    override suspend fun canAddCard(card: Card, from: RoomPlayer): Boolean {
        return currentRound?.canAddCard(card, from) ?: false
    }

    override suspend fun startRound() = withContext(coroutineContext) {
        val player = currentPlayer!!
        val round = SevensRound(player, players.count())
        rounds += round
        currentRound = round
        drawCards(player)
        round.start()
        roundsNotifier.onRoundStarted(this@SevensRoom)
    }

    override suspend fun newRound(player: RoomPlayer): Boolean {
        val result = endRound(player)
        if (!result) return result
        if (canStartRound) startRound()
        else endGame()
        return result
    }

    override suspend fun addCard(player: RoomPlayer, card: Card): Boolean =
        withContext(coroutineContext) {
            val result = chooseCard(player, card)
            if (!result) return@withContext result
            val nextPlayer = nextPlayer!!
            val roundStartingPlayer = currentRound!!.startingPlayer
            if (nextPlayer.id == roundStartingPlayer.id) {
                if (currentRound!!.canContinue(nextPlayer, playerCount)) {
                    setPlayerTurn(nextPlayer)
                } else {
                    val res = newRound(roundStartingPlayer)
                    if (!res) throw Exception("failed to start new round")
                }
            } else setPlayerTurn(nextPlayer)
            return@withContext result
        }

    override suspend fun canEndTurn(from: RoomPlayer): Boolean = withContext(coroutineContext) {
        val cardSize = currentCards?.size ?: return@withContext false
        cardSize > 0 && cardSize % maxPlayers == 0
    }

    override suspend fun endTurn(player: RoomPlayer): Boolean = withContext(coroutineContext) {
        newRound(player)
    }

    override suspend fun canChooseCardType(player: RoomPlayer, type: Card.Type): Boolean {
        return false
    }

    override suspend fun chooseCardType(player: RoomPlayer, type: Card.Type): Boolean {
        tagLogger?.w("${player.player.name} tried to choose a card which is not possible in this mode")
        return false
    }

    override suspend fun addPlayerSession(
        playerSession: RoomPlayer,
        listener: RoundedPlayerListener
    ): Boolean = withContext(coroutineContext) {
        if (!canJoin || isFull) return@withContext false
        _players.add(playerSession)
        addRoomListener(playerSession, listener)
        addRoundsListener(playerSession, listener)
        listener.onRoomConnected(id)
        return@withContext true
    }

    private fun drawCards(owner: RoomPlayer) {
        val maxPlayers = type.maxPlayers
        while (owner.cardsCount <= 3 && remainingCards.isNotEmpty()) {
            var index = players.indexOf(owner)
            if (index == -1) throw Exception("How did this happen?")
            var times = maxPlayers
            while (times != 0) {
                --times
                players[index].addCard(drawCard()!!)
                index = (index + 1) % maxPlayers
            }
        }
    }

    override fun createHand(): Hand {
        return SevensHand()
    }

    override suspend fun canDrawCard(from: RoomPlayer): Boolean {
        return false
    }

    override suspend fun drawCard(player: RoomPlayer): Boolean {
        return false
    }

}