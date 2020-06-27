package ro.sevens.ai

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ro.sevens.game.PlayerSession
import ro.sevens.game.bridge.AbsSevensCommunication
import ro.sevens.game.room.newRound
import ro.sevens.logger.TagLogger
import ro.sevens.payload.Card
import ro.sevens.payload.Player
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
    player: Player,
    dispatcher: CoroutineDispatcher,
    tagLogger: TagLogger
) : AbsSevensCommunication(), CoroutineScope {

    private val playerSession = PlayerSession(aiRoom.room, player)
    private val localOnRoomChanged = LocalOnRoomChanged(this, tagLogger)

    override val coroutineContext: CoroutineContext = dispatcher + Job()

    override fun placeCard(card: Card) {
        launch {
            aiRoom.room.addCard(playerSession, card)
        }
    }

    override fun endRound() {
        launch {
            aiRoom.room.newRound(playerSession)
        }
    }

    override fun connect() {
        launch {
            aiRoom.addPlayer(playerSession, localOnRoomChanged)
            for (i in 1 until aiRoom.type.maxPlayers)
                aiRoom.addAi("AI $i")
        }
    }

    override fun disconnect() {
        launch {
            aiRoom.room.stop()
        }
    }
}