package ro.dragossusi.sevens.ai.room

import kotlinx.coroutines.CoroutineDispatcher
import ro.dragossusi.sevens.ai.brain.SevensAiBrain
import ro.dragossusi.sevens.ai.SevensAiPlayer
import ro.dragossusi.sevens.game.listener.RoundedPlayerListener
import ro.dragossusi.sevens.game.room.SevensRoom
import ro.dragossusi.sevens.game.round.SevensRound
import ro.dragossusi.sevens.game.session.PlayerSession
import ro.dragossusi.logger.TagLogger
import ro.dragossusi.sevens.payload.Player
import ro.dragossusi.sevens.payload.enums.GameTypeEnum

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
class SevensAiRoom(
    tagLogger: TagLogger?,
    dispatcher: CoroutineDispatcher,
    operationDelay: Long,
    room: SevensRoom
) : AiRoom<RoundedPlayerListener, SevensRound, SevensRoom>(tagLogger, dispatcher, operationDelay, room) {

    override fun createBrain(playerSession: PlayerSession): RoundedPlayerListener {
        return SevensAiBrain(
            player = createPlayerListener(playerSession),
            room = room,
            tagLogger = tagLogger, //todo change me
            dispather = dispatcher,
            operationDelay = operationDelay
        )
    }

    override fun createSession(room: SevensRoom, player: Player): PlayerSession {
        return PlayerSession(room, player)
    }

    override fun createPlayerListener(session: PlayerSession): SevensAiPlayer {
        return SevensAiPlayer(session, GameTypeEnum.NORMAL)
    }
}