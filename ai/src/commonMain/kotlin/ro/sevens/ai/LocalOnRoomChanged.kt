package ro.sevens.ai

import ro.sevens.game.bridge.Communication
import ro.sevens.game.room.Room
import ro.sevens.logger.TagLogger
import ro.sevens.payload.game.GameEndResponse
import ro.sevens.payload.game.NewRoundResponse
import ro.sevens.payload.game.PlayerTurnResponse
import ro.sevens.payload.game.SimplePlayerResponse


/**
 * Sevens
 *
 * Copyright (C) 2020  Rachieru Dragos-Mihai
 *
 * Sevens is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * sSevens is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sevens.  If not, see [License](http://www.gnu.org/licenses/) .
 *
 */
open class LocalOnRoomChanged(
    open val sevensCommunication: Communication,
    private val tagLogger: TagLogger?
) : Room.OnRoomChanged {

    override suspend fun onRoomStopped() {
//        TODO("Not yet implemented")
    }

    override fun onGameEnded(response: GameEndResponse) {
        tagLogger?.i("onGameEnded: $response")
        sevensCommunication.onGameEnded?.onGameEnded(response)
    }

    override fun onGameStarted(players: Array<SimplePlayerResponse>) {
        tagLogger?.i("onGameStarted: $players")
        sevensCommunication.onGameStarted?.onGameStarted(players)
    }

    override fun onPlayerTurn(playerTurn: PlayerTurnResponse) {
        tagLogger?.i("onPlayerTurn: $playerTurn")
        sevensCommunication.onPlayerTurn?.onPlayerTurn(playerTurn)
    }

    override fun onRoomConnected(id: Long) {
        sevensCommunication.onRoomConnected?.onRoomConnected(id)
    }

}