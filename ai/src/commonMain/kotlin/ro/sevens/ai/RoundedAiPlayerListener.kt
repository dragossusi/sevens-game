package ro.sevens.ai

import kotlinx.coroutines.*
import ro.sevens.ai.brain.SevensAiBrain
import ro.sevens.game.listener.PlayerListener
import ro.sevens.game.room.RoundedRoom
import ro.sevens.game.round.Round
import ro.sevens.logger.TagLogger
import ro.sevens.payload.game.PlayerTurnResponse

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
class RoundedAiPlayerListener<L : PlayerListener, R : Round>(
    private val player: SevensAiPlayer,
    private val room: RoundedRoom<L, R>,
    private val tagLogger: TagLogger?,
    private val operationDelay: Long,
    dispather: CoroutineDispatcher
) : SevensAiBrain<L>(player, room, tagLogger, operationDelay, dispather) {

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

}