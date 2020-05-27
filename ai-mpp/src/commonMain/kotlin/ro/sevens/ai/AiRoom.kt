package ro.sevens.ai

import kotlinx.coroutines.CoroutineDispatcher
import ro.sevens.game.PlayerSession
import ro.sevens.game.deck.DeckProvider
import ro.sevens.game.listener.MapPlayerNotifier
import ro.sevens.game.listener.PlayerNotifier
import ro.sevens.game.room.NormalRoom
import ro.sevens.game.room.Room
import ro.sevens.logger.TagLogger
import ro.sevens.payload.Player
import ro.sevens.payload.base.GameTypeData


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
class AiRoom(
    id: Long,
    type: GameTypeData,
    deckProvider: DeckProvider,
    private val tagLogger: TagLogger?,
    private val dispatcher: CoroutineDispatcher,
    playerNotifier: PlayerNotifier = MapPlayerNotifier(tagLogger)
) {

    internal val room: Room = NormalRoom(id, type, deckProvider, tagLogger, playerNotifier, 1250L)

    suspend fun addAi(name: String) {
        val playerSession = PlayerSession(
            room,
            Player(room.players.size.toLong(), name, null)
        )
        val listener = AiPlayerListener(
            player = AiPlayer(playerSession, room.type),
            room = room,
            tagLogger = null, //todo change me
            dispather = dispatcher
        )
        room.addPlayerSession(playerSession, listener)
    }

    suspend fun addPlayer(session: PlayerSession, listener: Room.OnRoomChanged) {
        room.addPlayerSession(
            playerSession = session,
            onRoomChanged = listener
        )
    }

}