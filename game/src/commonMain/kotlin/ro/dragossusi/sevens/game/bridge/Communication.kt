package ro.dragossusi.sevens.game.bridge

import ro.dragossusi.sevens.game.listener.*
import ro.dragossusi.sevens.payload.base.GameTypeData

/**
 * sevens-client
 *
 * Copyright (C) 2020  Rachieru Dragos-Mihai
 *
 * sevens-client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * sevens-client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with sevens-client.  If not, see [License](http://www.gnu.org/licenses/) .
 *
 */
interface Communication : ServerActions {

    var onLobbyPlayerConnected: OnLobbyPlayerConnected?
    var onLobbyPlayerDisconnected: OnLobbyPlayerDisconnected?
    var onLobbyConnected: OnLobbyConnected?

    var onRoomStarted: OnRoomStarted?
    var onRoomStopped: OnRoomStopped?
    var onPlayerTurn: OnPlayerTurn?

    var onRoomConnected: OnRoomConnected?
    var onGameEnded: OnGameEnded?
    var onGameStarted: OnGameStarted?
}