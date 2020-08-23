package ro.sevens.game.room

import kotlinx.coroutines.withContext
import ro.sevens.game.deck.Deck
import ro.sevens.game.deck.DeckProvider
import ro.sevens.game.hand.Hand
import ro.sevens.game.hand.SevensHand
import ro.sevens.game.listener.MapPlayerNotifier
import ro.sevens.game.listener.PlayerNotifier
import ro.sevens.game.round.MacaoRound
import ro.sevens.game.session.MacaoPlayerSession
import ro.sevens.game.session.PlayerSession
import ro.sevens.logger.TagLogger
import ro.sevens.payload.Card
import ro.sevens.payload.base.GameTypeData
import ro.sevens.payload.enums.SupportedGame
import kotlin.coroutines.CoroutineContext

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
class MacaoRoom constructor(
    override val id: Long,
    override val type: GameTypeData,
    deckProvider: DeckProvider,
    tagLogger: TagLogger?,
    playerNotifier: PlayerNotifier<MacaoPlayerSession, MacaoRound> = MapPlayerNotifier(tagLogger),
    override val coroutineContext: CoroutineContext,
    override val roundEndDelay: Long = 1250L
) : BaseRoom<MacaoPlayerSession, MacaoRound>(playerNotifier, tagLogger) {

    override val deck: Deck = deckProvider.createDeck(SupportedGame.SEVENS, type)

    override val canStartRound: Boolean
        get() = players.all {
            it.cardsCount != 0
        }

    override suspend fun startRound() = withContext(coroutineContext) {
        val player = currentPlayer!!
        val round = MacaoRound(player, players.count())
        rounds += round
        currentRound = round
        drawCards(player)
        round.start()
        playerNotifier.onRoundStarted(this@MacaoRoom)
    }

    override suspend fun newRound(player: MacaoPlayerSession): Boolean {
        val result = endRound(player)
        if (!result) return result
        if (canStartRound) startRound()
        else endGame()
        return result
    }

    override suspend fun addCard(player: MacaoPlayerSession, card: Card): Boolean = withContext(coroutineContext) {
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

    override suspend fun chooseCardType(player: MacaoPlayerSession, type: Card.Type): Boolean {
        tagLogger?.w("${player.player.name} tried to choose a card which is not possible in this mode")
        return false
    }

    override suspend fun addPlayerSession(
        playerSession: MacaoPlayerSession,
        onRoomChanged: Room.OnRoomChanged
    ): Boolean = withContext(coroutineContext) {
        if (!canJoin || isFull) return@withContext false
        _players.add(playerSession)
        addListener(playerSession, onRoomChanged)
        onRoomChanged.onRoomConnected(id)
        if (isFull)
            start()
        return@withContext true
    }

    private fun drawCards(owner: PlayerSession) {
        val maxPlayers = type.maxPlayers
        while (owner.cardsCount <= 5 && remainingCards.isNotEmpty()) {
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

    override fun createHand(): Hand {
        return SevensHand()
    }

}