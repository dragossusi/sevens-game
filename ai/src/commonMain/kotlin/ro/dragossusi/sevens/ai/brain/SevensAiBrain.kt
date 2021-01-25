package ro.dragossusi.sevens.ai.brain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ro.dragossusi.sevens.ai.SevensAiPlayer
import ro.dragossusi.sevens.game.listener.PlayerListener
import ro.dragossusi.sevens.game.room.OnRoundsChangedListener
import ro.dragossusi.sevens.game.room.Room
import ro.dragossusi.logger.TagLogger
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
open class SevensAiBrain<L : PlayerListener>(
    private val player: SevensAiPlayer,
    private val room: Room<L>,
    private val tagLogger: TagLogger?,
    private val operationDelay: Long,
    dispather: CoroutineDispatcher
) : RoundedAiBrain(dispather), OnRoundsChangedListener, CoroutineScope {

    override fun onLobbyConnected(lobby: LobbyData) {
        tagLogger?.w("TODO Not yet implemented")
    }

    override fun onLobbyPlayerConnected(lobby: LobbyData) {
        tagLogger?.w("TODO Not yet implemented")
    }

    override fun onLobbyPlayerDiconnected(lobby: LobbyData) {
        tagLogger?.w("TODO Not yet implemented")
    }

    override suspend fun onRoomStopped() {
        tagLogger?.w("TODO Not yet implemented")
    }

    override fun onGameEnded(response: GameEndResponse) {
    }

    override fun onGameStarted(players: Array<SimplePlayerResponse>) {
    }

    override fun onRoundStarted(response: NewRoundResponse) {
        launch {
            delay(operationDelay)
            if (response.currentPlayerId == player.id) {
                room.addCard(player.session, player.pickCard())
            }
        }
    }

    override fun onPlayerTurn(playerTurn: PlayerTurnResponse) {
        launch {
            delay(operationDelay)
            if (playerTurn.currentPlayerId == player.id) {
                val canEnd = playerTurn.canEnd(room.type)
                val card = player.pickCard(playerTurn.roundCards, canEnd = canEnd)
                room.addCard(
                    player.session,
                    card ?: player.hand!!.chooseRandomCard()
                )
            }
        }
    }

    override fun onRoomConnected(id: Long) {
//        tagLogger?.w("TODO onRoomConnected")
    }

    override fun onRoundEnded(response: NewRoundResponse) {
//        tagLogger?.w("TODO onRoundEnded")
    }
}