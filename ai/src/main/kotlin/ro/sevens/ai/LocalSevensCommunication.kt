package ro.sevens.ai

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import ro.sevens.game.PlayerSession
import ro.sevens.game.bridge.ServerActions
import ro.sevens.game.bridge.SevensCommunication
import ro.sevens.game.listener.*
import ro.sevens.game.room.newRound
import ro.sevens.payload.Card
import ro.sevens.payload.Player
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

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
class LocalSevensCommunication constructor(
    private val aiRoom: AiRoom,
    player: Player
) : SevensCommunication, ServerActions, CoroutineScope {

    private val playerSession = PlayerSession(aiRoom.room, player)
    private val localOnRoomChanged = LocalOnRoomChanged(this)

    override val coroutineContext: CoroutineContext =
        Executors.newSingleThreadExecutor().asCoroutineDispatcher() + Job()

    override var onRoomConnected: OnRoomConnected? = null
    override var onRoomStarted: OnRoomStarted? = null

    override var onRoundStarted: OnRoundStarted? = null
    override var onPlayerTurn: OnPlayerTurn? = null
    override var onRoundEnded: OnRoundEnded? = null

    override fun placeCard(card: Card) {
        launch {
            aiRoom.room.addCard(playerSession, card)
        }
    }

    override fun endRound() {
        launch {
            aiRoom.room.newRound()
        }
    }

    override fun connect() {
        launch {
            aiRoom.addPlayer(playerSession, localOnRoomChanged)
            aiRoom.addAi("AI")
        }
    }

    override fun disconnect() {
        launch {
            aiRoom.room.stop()
        }
    }
}