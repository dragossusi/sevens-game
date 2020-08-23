package ro.sevens.ai

import kotlinx.coroutines.CoroutineDispatcher
import ro.sevens.game.room.Room
import ro.sevens.game.round.Round
import ro.sevens.game.session.PlayerSession
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
abstract class AiRoom<S : PlayerSession, RD : Round<S>, RM : Room<S, RD>> constructor(
    private val tagLogger: TagLogger?,
    private val dispatcher: CoroutineDispatcher,
    private val operationDelay: Long,
    internal val room: RM
) {

    val type: GameTypeData
        get() = room.type

    suspend fun addAi(name: String, tagLogger: TagLogger? = null) {
        val playerSession = createSession(
            room,
            Player(-room.players.size.toLong() - 1L, name, null)
        )
        val listener = AiPlayerListener<S>(
            player = createPlayerListener(playerSession),
            room = room,
            tagLogger = tagLogger, //todo change me
            dispather = dispatcher,
            operationDelay = operationDelay
        )
        room.addPlayerSession(playerSession, listener)
    }

    abstract fun createSession(room: RM, player: Player): S
    abstract fun createPlayerListener(session: S): SevensAiPlayer<S>

    suspend fun addPlayer(session: S, listener: Room.OnRoomChanged) {
        room.addPlayerSession(
            playerSession = session,
            onRoomChanged = listener
        )
    }

}