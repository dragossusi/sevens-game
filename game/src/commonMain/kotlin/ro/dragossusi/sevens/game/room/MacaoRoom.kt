package ro.dragossusi.sevens.game.room

import kotlinx.coroutines.withContext
import ro.dragossusi.sevens.game.deck.Deck
import ro.dragossusi.sevens.game.deck.DeckProvider
import ro.dragossusi.sevens.game.hand.Hand
import ro.dragossusi.sevens.game.hand.SevensHand
import ro.dragossusi.sevens.game.listener.MapPlayerNotifier
import ro.dragossusi.sevens.game.listener.PlayerListener
import ro.dragossusi.sevens.game.listener.PlayerNotifier
import ro.dragossusi.sevens.game.session.RoomPlayer
import ro.dragossusi.logger.TagLogger
import ro.dragossusi.sevens.payload.Card
import ro.dragossusi.sevens.payload.card.canDrawMoreMacao
import ro.dragossusi.sevens.payload.card.isChangeColorMacao
import ro.dragossusi.sevens.payload.card.isChangeColorMacao
import ro.dragossusi.sevens.payload.card.isDrawCardMacao
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
    deckProvider: DeckProvider,
    tagLogger: TagLogger?,
    playerNotifier: PlayerNotifier = MapPlayerNotifier(tagLogger),
    override val coroutineContext: CoroutineContext,
    override val roundEndDelay: Long = 1250L
) : BaseRoom<PlayerListener>(playerNotifier, tagLogger) {

    override val maxPlayers: Int
        get() = 6

    override val game: SupportedGame
        get() = SupportedGame.MACAO

    override val deck: Deck = deckProvider.createDeck(SupportedGame.MACAO, null)

    override var currentCards: MutableList<Card>? = null

    override suspend fun startGame() {
        currentPlayer = startingPlayer
        initCards()
        initHands()
        drawCards(currentPlayer!!)
    }

    var hasToDraw: Boolean = false

    override suspend fun canAddCard(card: Card, from: RoomPlayer): Boolean = withContext(coroutineContext) {
        //check if no card placed
        val currentCard = currentCards?.lastOrNull() ?: return@withContext false
        //check for forcing draws
        if (hasToDraw && card.canDrawMoreMacao(currentCard))
            return@withContext true
        //check for color change
        if (card.isChangeColorMacao)
            return@withContext true
        //check number and type
        if (card.number == currentCard.number || card.type == currentCard.type)
            return@withContext true
        //dont allow
        return@withContext false
    }

    override suspend fun canDrawCard(from: RoomPlayer): Boolean {
        return true
    }

    override suspend fun drawCard(player: RoomPlayer): Boolean = withContext(coroutineContext) {
        val card = drawCard() ?: return@withContext false
        player.addCard(card)
        return@withContext true
    }

    override suspend fun addCard(player: RoomPlayer, card: Card): Boolean = withContext(coroutineContext) {
        val currentCard = currentCards?.last() ?: return@withContext false
        if (currentCard.isChangeColorMacao) {
            currentCards!! += card
            return@withContext true
        }
        if (currentCard.number == card.number || currentCard.type == card.type) {
            currentCards!! += card
            if (card.isDrawCardMacao)
                setPlayerTurn(nextPlayer!!)
            return@withContext true
        }
        return@withContext false
    }

    override suspend fun canEndTurn(from: RoomPlayer): Boolean = withContext(coroutineContext) {
        return@withContext true //todo
    }

    override suspend fun endTurn(player: RoomPlayer): Boolean = withContext(coroutineContext) {
        setPlayerTurn(nextPlayer!!)
        true
    }

    override suspend fun canChooseCardType(
        player: RoomPlayer,
        type: Card.Type
    ): Boolean = withContext(coroutineContext) {
        currentPlayer == player && currentCards?.lastOrNull()?.isChangeColorMacao == true
    }

    override suspend fun chooseCardType(player: RoomPlayer, type: Card.Type): Boolean = withContext(coroutineContext) {
        val card = currentCards?.lastOrNull() ?: return@withContext false
        if (card.number != 11) return@withContext false
        currentCards!!.removeLast()
        currentCards!!.add(Card(11, type))
        return@withContext true
    }

    private fun drawCards(owner: RoomPlayer) {
        val maxPlayers = players.size
        while (owner.cardsCount <= 5 && remainingCards.isNotEmpty()) {
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

    override suspend fun addPlayerSession(
        player: RoomPlayer,
        listener: PlayerListener
    ): Boolean = withContext(coroutineContext) {
        if (!canJoin || isFull) return@withContext false
        _players.add(player)
        addRoomListener(player, listener)
        listener.onRoomConnected(id)
        return@withContext true
    }

}