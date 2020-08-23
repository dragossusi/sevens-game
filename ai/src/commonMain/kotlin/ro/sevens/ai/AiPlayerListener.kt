package ro.sevens.ai

import kotlinx.coroutines.*
import ro.sevens.game.room.Room
import ro.sevens.game.session.PlayerSession
import ro.sevens.logger.TagLogger
import ro.sevens.payload.game.GameEndResponse
import ro.sevens.payload.game.NewRoundResponse
import ro.sevens.payload.game.PlayerTurnResponse
import ro.sevens.payload.game.SimplePlayerResponse
import kotlin.coroutines.CoroutineContext

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
class AiPlayerListener<S:PlayerSession>(
    private val player: SevensAiPlayer<S>,
    private val room: Room<S,*>,
    private val tagLogger: TagLogger?,
    private val operationDelay: Long,
    dispather: CoroutineDispatcher
) : Room.OnRoomChanged, CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext = dispather + job

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
                if (card == null && canEnd) room.newRound(player.session)
                else room.addCard(
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