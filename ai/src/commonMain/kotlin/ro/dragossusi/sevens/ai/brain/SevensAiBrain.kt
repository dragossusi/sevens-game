package ro.dragossusi.sevens.ai.brain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ro.dragossusi.logger.TagLogger
import ro.dragossusi.sevens.ai.player.SevensAiPlayer
import ro.dragossusi.sevens.game.room.OnRoundsChangedListener
import ro.dragossusi.sevens.game.room.SevensRoom
import ro.dragossusi.sevens.payload.Card
import ro.dragossusi.sevens.payload.base.LobbyData
import ro.dragossusi.sevens.payload.game.GameEndResponse
import ro.dragossusi.sevens.payload.game.NewRoundResponse
import ro.dragossusi.sevens.payload.game.PlayerTurnResponse
import ro.dragossusi.sevens.payload.game.SimplePlayerResponse

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
open class SevensAiBrain constructor(
    private val player: SevensAiPlayer,
    private val room: SevensRoom,
    protected val tagLogger: TagLogger?,
    protected val operationDelay: Long,
    dispather: CoroutineDispatcher
) : RoundedAiBrain(dispather), OnRoundsChangedListener, CoroutineScope {

    override fun onLobbyConnected(lobby: LobbyData) {
        tagLogger?.w("onLobbyConnected")
    }

    override fun onLobbyPlayerConnected(lobby: LobbyData) {
        tagLogger?.w("onLobbyPlayerConnected")
    }

    override fun onLobbyPlayerDiconnected(lobby: LobbyData) {
        tagLogger?.w("onLobbyPlayerDiconnected")
    }

    override suspend fun onRoomStopped() {
        tagLogger?.w("onRoomStopped")
    }

    override fun onGameEnded(response: GameEndResponse) {
        tagLogger?.w("onGameEnded")
    }

    override fun onGameStarted(players: List<SimplePlayerResponse>) {
        tagLogger?.w("onGameStarted")
    }

    override fun onRoundStarted(response: NewRoundResponse) {
        launch {
            tagLogger?.w("onRoundStarted $response")
            delay(operationDelay)
            if (response.currentPlayerId == player.id) {
                room.addCard(player.session, player.pickCard())
            }
        }
    }

    override fun onPlayerTurn(playerTurn: PlayerTurnResponse) {
        launch {
            tagLogger?.w("onPlayerTurn $playerTurn")
            delay(operationDelay)
            if (playerTurn.currentPlayerId == player.id) {
                val session = player.session
                val canEnd = room.canEndTurn(session)
                var card: Card? = player.pickCard(playerTurn.roundCards, canEnd = canEnd)
                val result = if (card == null && canEnd) {
                    println("tried to end turn")
                    room.endTurn(player.session)
                } else {
                    card?.let {
                        if (room.canAddCard(it, session)) {
                            if (room.addCard(session, it))
                                return@launch
                        }

                    }
                    println("tried to choose random card")
                    card = player.hand!!.chooseRandomCard()
                    room.addCard(
                        player.session,
                        card
                    )
                }
                if (!result) {
                    println("tried placing card $card")
                    throw Exception("AI NOT GOOD")
                }
            }
        }
    }

    override fun onRoomConnected(id: Long) {
        tagLogger?.w("onRoomConnected")
    }

    override fun onRoundEnded(response: NewRoundResponse) {
        tagLogger?.w("onRoundEnded")
    }
}