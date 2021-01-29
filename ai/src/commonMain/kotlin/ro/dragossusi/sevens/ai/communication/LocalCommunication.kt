package ro.dragossusi.sevens.ai.communication

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ro.dragossusi.sevens.ai.room.AiRoom
import ro.dragossusi.sevens.game.bridge.AbsCommunication
import ro.dragossusi.sevens.game.listener.PlayerListener
import ro.dragossusi.sevens.game.room.Room
import ro.dragossusi.sevens.game.session.RoomPlayer
import ro.dragossusi.sevens.payload.Card
import ro.dragossusi.sevens.payload.Player
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
open class LocalCommunication<L : PlayerListener, RM : Room<L>> constructor(
    protected val aiRoom: AiRoom<L, RM>,
    player: Player,
    dispatcher: CoroutineDispatcher,
    protected val playerListener: L
) : AbsCommunication(), CoroutineScope {

    protected val room: RM
        get() = aiRoom.room

    protected val playerSession = RoomPlayer(room, player)

    override val coroutineContext: CoroutineContext = dispatcher + Job()

    override fun placeCard(card: Card) {
        launch {
            if (room.canAddCard(card, playerSession))
                room.addCard(playerSession, card)
        }
    }

    override fun chooseCardType(type: Card.Type) {
        launch {
            if (room.canChooseCardType(playerSession, type))
                room.chooseCardType(playerSession, type)
        }
    }

    override fun connect() {
        launch {
            aiRoom.addPlayer(playerSession, playerListener)
            for (i in 1 until aiRoom.room.maxPlayers)
                aiRoom.addAi("AI $i")
            room.start()
        }
    }

    override fun disconnect() {
        launch {
            room.stop()
        }
    }

    override fun drawCard() {
        launch {
            room.drawCard(playerSession)
        }
    }

}