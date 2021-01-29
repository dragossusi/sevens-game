package ro.dragossusi.sevens.ai.room

import kotlinx.coroutines.CoroutineDispatcher
import ro.dragossusi.logger.TagLogger
import ro.dragossusi.sevens.ai.player.AiPlayer
import ro.dragossusi.sevens.ai.player.SevensAiPlayer
import ro.dragossusi.sevens.game.listener.PlayerListener
import ro.dragossusi.sevens.game.room.Room
import ro.dragossusi.sevens.game.session.RoomPlayer
import ro.dragossusi.sevens.payload.Player


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
abstract class AiRoom<L : PlayerListener, RM : Room<L>> constructor(
    protected val tagLogger: TagLogger?,
    protected val dispatcher: CoroutineDispatcher,
    protected val operationDelay: Long,
    internal val room: RM
) {

    suspend fun addAi(name: String, tagLogger: TagLogger? = null) {
        val playerSession = createSession(
            room,
            Player(-room.players.size.toLong() - 1L, name, null)
        )
        val listener = createBrain(playerSession)
        room.addPlayerSession(playerSession, listener)
    }

    fun createSession(room: RM, player: Player): RoomPlayer {
        return RoomPlayer(
            room,
            player
        )
    }

    abstract fun createPlayerListener(session: RoomPlayer): AiPlayer

    suspend fun addPlayer(session: RoomPlayer, listener: L) {
        room.addPlayerSession(
            playerSession = session,
            listener = listener
        )
    }

    abstract fun createBrain(playerSession: RoomPlayer): L

}