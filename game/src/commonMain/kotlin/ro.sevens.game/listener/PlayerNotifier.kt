package ro.sevens.game.listener

import ro.sevens.game.room.Room
import ro.sevens.game.round.Round
import ro.sevens.game.session.PlayerSession


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
interface PlayerNotifier : RoomListeners {
    suspend fun onRoomStopped(room: Room)
    suspend fun onPlayerTurn(room: Room)
    suspend fun onGameEnded(room: Room)
    suspend fun onGameStarted(room: Room)
}