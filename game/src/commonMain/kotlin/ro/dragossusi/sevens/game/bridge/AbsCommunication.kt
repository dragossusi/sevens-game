package ro.dragossusi.sevens.game.bridge

import ro.dragossusi.sevens.game.listener.*

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
abstract class AbsCommunication : Communication {
    override var onLobbyPlayerConnected: OnLobbyPlayerConnected? = null
    override var onLobbyPlayerDisconnected: OnLobbyPlayerDisconnected? = null
    override var onLobbyConnected: OnLobbyConnected? = null
    override var onRoomStarted: OnRoomStarted? = null
    override var onRoomStopped: OnRoomStopped? = null
    override var onPlayerTurn: OnPlayerTurn? = null

    override var onRoomConnected: OnRoomConnected? = null
    override var onGameEnded: OnGameEnded? = null
    override var onGameStarted: OnGameStarted? = null
}