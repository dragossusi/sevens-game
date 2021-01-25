package ro.dragossusi.sevens.game.room

import kotlinx.coroutines.withContext
import ro.dragossusi.sevens.game.deck.Deck
import ro.dragossusi.sevens.game.deck.DeckProvider
import ro.dragossusi.sevens.game.hand.Hand
import ro.dragossusi.sevens.game.hand.SevensHand
import ro.dragossusi.sevens.game.listener.MapPlayerNotifier
import ro.dragossusi.sevens.game.listener.PlayerListener
import ro.dragossusi.sevens.game.listener.PlayerNotifier
import ro.dragossusi.sevens.game.session.PlayerSession
import ro.dragossusi.logger.TagLogger
import ro.dragossusi.sevens.payload.Card
import ro.dragossusi.sevens.payload.base.GameTypeData
import ro.dragossusi.sevens.payload.enums.SupportedGame
import kotlin.coroutines.CoroutineContext

/**
 *
 * Macao implementation
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
class MacaoRoom constructor(
    override val id: Long,
    override val type: GameTypeData,
    deckProvider: DeckProvider,
    tagLogger: TagLogger?,
    playerNotifier: PlayerNotifier = MapPlayerNotifier(tagLogger),
    override val coroutineContext: CoroutineContext,
    override val roundEndDelay: Long = 1250L
) : BaseRoom<PlayerListener>(playerNotifier, tagLogger) {

    override val deck: Deck = deckProvider.createDeck(SupportedGame.SEVENS, type)

    override var currentCards: MutableList<Card>? = null

    override suspend fun startGame() {
        TODO("Not yet implemented")
    }

    override suspend fun canAddCard(card: Card, from: PlayerSession): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun canDrawCard(from: PlayerSession): Boolean {
        return false
    }

    override suspend fun drawCard(player: PlayerSession): Boolean {
        TODO("Not yet implemented")
    }

    override val canStartRound: Boolean
        get() = players.all {
            it.cardsCount != 0
        }

    override suspend fun addCard(player: PlayerSession, card: Card): Boolean = withContext(coroutineContext) {
        val currentCard = currentCards?.last() ?: return@withContext false
        if (currentCard.number == 11) {
            TODO("change color")
        }
        if (currentCard.number == card.number || currentCard.type == card.type) {
            currentCards!! += card
            return@withContext true
        }
        return@withContext false
    }

    override suspend fun chooseCardType(player: PlayerSession, type: Card.Type): Boolean {
        tagLogger?.w("${player.player.name} tried to choose a card which is not possible in this mode")
        return false
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

    override suspend fun addPlayerSession(
        playerSession: PlayerSession,
        listener: PlayerListener
    ): Boolean = withContext(coroutineContext) {
        if (!canJoin || isFull) return@withContext false
        _players.add(playerSession)
        addRoomListener(playerSession, listener)
        listener.onRoomConnected(id)
        return@withContext true
    }

}