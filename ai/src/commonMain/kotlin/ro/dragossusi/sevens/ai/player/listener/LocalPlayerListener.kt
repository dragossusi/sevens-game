package ro.dragossusi.sevens.ai.player.listener

import ro.dragossusi.logger.TagLogger
import ro.dragossusi.sevens.game.bridge.Communication
import ro.dragossusi.sevens.game.listener.PlayerListener
import ro.dragossusi.sevens.payload.base.LobbyData
import ro.dragossusi.sevens.payload.game.GameEndResponse
import ro.dragossusi.sevens.payload.game.PlayerTurnResponse
import ro.dragossusi.sevens.payload.game.SimplePlayerResponse


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
open class LocalPlayerListener<C : Communication>(
    private val tagLogger: TagLogger?
) : PlayerListener {

    var sevensCommunication: C? = null

    override fun onLobbyConnected(lobby: LobbyData) {
        tagLogger?.i("onLobbyConnected")
        sevensCommunication?.onLobbyConnected?.onLobbyConnected(lobby)
    }

    override fun onLobbyPlayerConnected(lobby: LobbyData) {
        tagLogger?.i("onLobbyPlayerConnected")
        sevensCommunication?.onLobbyPlayerConnected?.onLobbyPlayerConnected(lobby)
    }

    override fun onLobbyPlayerDiconnected(lobby: LobbyData) {
        tagLogger?.i("onLobbyPlayerDiconnected")
        sevensCommunication?.onLobbyPlayerDisconnected?.onLobbyPlayerDiconnected(lobby)
    }

    override suspend fun onRoomStopped() {
        tagLogger?.i("onRoomStopped")
        sevensCommunication?.onRoomStopped?.onRoomStopped()
    }

    override fun onGameEnded(response: GameEndResponse) {
        tagLogger?.i("onGameEnded: $response")
        sevensCommunication?.onGameEnded?.onGameEnded(response)
    }

    override fun onGameStarted(players: List<SimplePlayerResponse>) {
        tagLogger?.i("onGameStarted: $players")
        sevensCommunication?.onGameStarted?.onGameStarted(players)
    }

    override fun onPlayerTurn(playerTurn: PlayerTurnResponse) {
        tagLogger?.i("onPlayerTurn: $playerTurn")
        sevensCommunication?.onPlayerTurn?.onPlayerTurn(playerTurn)
    }

    override fun onRoomConnected(id: Long) {
        tagLogger?.i("onRoomConnected")
        sevensCommunication?.onRoomConnected?.onRoomConnected(id)
    }

}