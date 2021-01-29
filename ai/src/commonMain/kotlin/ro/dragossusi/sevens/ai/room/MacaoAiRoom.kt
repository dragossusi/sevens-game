package ro.dragossusi.sevens.ai.room

import kotlinx.coroutines.CoroutineDispatcher
import ro.dragossusi.logger.TagLogger
import ro.dragossusi.sevens.ai.brain.MacaoAiBrain
import ro.dragossusi.sevens.ai.player.AiPlayer
import ro.dragossusi.sevens.ai.player.MacaoAiPlayer
import ro.dragossusi.sevens.game.listener.PlayerListener
import ro.dragossusi.sevens.game.listener.RoundedPlayerListener
import ro.dragossusi.sevens.game.room.MacaoRoom
import ro.dragossusi.sevens.game.room.SevensRoom
import ro.dragossusi.sevens.game.session.RoomPlayer
import ro.dragossusi.sevens.payload.Player

/**
 * sevens-game
 *
 * Copyright (C) 2020  Rachieru Dragos-Mihai
 *
 * sevens-game is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * sevens-game is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with sevens-game.  If not, see [License](http://www.gnu.org/licenses/) .
 *
 */
class MacaoAiRoom constructor(
    tagLogger: TagLogger?,
    dispatcher: CoroutineDispatcher,
    operationDelay: Long,
    room: MacaoRoom
) : AiRoom<PlayerListener, MacaoRoom>(tagLogger, dispatcher, operationDelay, room) {

    override fun createBrain(playerSession: RoomPlayer): RoundedPlayerListener {
        return MacaoAiBrain(
            player = createPlayerListener(playerSession),
            room = room,
            tagLogger = tagLogger, //todo change me
            dispather = dispatcher,
            operationDelay = operationDelay
        )
    }

    override fun createPlayerListener(session: RoomPlayer): AiPlayer {
        return MacaoAiPlayer(session)
    }
}