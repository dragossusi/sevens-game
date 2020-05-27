package ro.sevens.ai

import ro.sevens.game.bridge.SevensCommunication
import ro.sevens.game.room.Room
import ro.sevens.payload.game.NewRoundResponse
import ro.sevens.payload.game.PlayerTurnResponse


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
class LocalOnRoomChanged(
    val sevensCommunication: SevensCommunication
) : Room.OnRoomChanged {

    override suspend fun onRoomStopped() {
//        TODO("Not yet implemented")
    }

    override suspend fun onRoundStarted(response: NewRoundResponse) {
        sevensCommunication.onRoundStarted?.onRoundStarted(response)
    }

    override suspend fun onPlayerTurn(playerTurn: PlayerTurnResponse) {
        sevensCommunication.onPlayerTurn?.onPlayerTurn(playerTurn)
    }

    override suspend fun onRoomConnected(id: Long) {
        sevensCommunication.onRoomConnected?.onRoomConnected(id)
    }

    override suspend fun onRoundEnded(response: NewRoundResponse) {
        sevensCommunication.onRoundEnded?.onRoundEnded(response)
    }
}