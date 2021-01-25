package ro.dragossusi.sevens.ai

import ro.dragossusi.sevens.ai.player.LocalPlayerListener
import ro.dragossusi.sevens.game.bridge.RoundedCommunication
import ro.dragossusi.sevens.game.listener.RoundedPlayerListener
import ro.dragossusi.logger.TagLogger
import ro.dragossusi.sevens.payload.game.NewRoundResponse


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
class LocalRoundedPlayerListener<C : RoundedCommunication>(
    private val tagLogger: TagLogger?
) : LocalPlayerListener<C>(tagLogger), RoundedPlayerListener {

    override fun onRoundEnded(response: NewRoundResponse) {
        sevensCommunication?.onRoundEnded?.onRoundEnded(response)
    }

    override fun onRoundStarted(response: NewRoundResponse) {
        tagLogger?.i("onRoundStarted: $response")
        sevensCommunication?.onRoundStarted?.onRoundStarted(response)
    }
}