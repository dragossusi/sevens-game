package ro.dragossusi.sevens.ai.player

import ro.dragossusi.sevens.game.hand.Hand
import ro.dragossusi.sevens.game.session.RoomPlayer
import ro.dragossusi.sevens.payload.Card

/**
 * SevensGame
 *
 * @author dragos
 * @since 29/01/2021
 *
 * Copyright (C) 2020  Rachieru Dragos-Mihai
 *
 * SevensGame is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * SevensGame is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SevensGame.  If not, see [License](http://www.gnu.org/licenses/) .
 *
 */
abstract class AiPlayer(
    val session: RoomPlayer,
) {
    val id: Long
        get() = session.id

    val hand: Hand?
        get() = session.hand

    abstract fun pickCard(placedCards: Collection<Card>, canEnd: Boolean): Card?
    abstract fun pickCard(): Card
}