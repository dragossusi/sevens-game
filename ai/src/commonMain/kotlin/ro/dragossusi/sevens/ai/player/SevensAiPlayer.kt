package ro.dragossusi.sevens.ai.player

import ro.dragossusi.sevens.game.hand.Hand
import ro.dragossusi.sevens.game.session.RoomPlayer
import ro.dragossusi.sevens.payload.Card
import ro.dragossusi.sevens.payload.base.GameTypeData
import ro.dragossusi.sevens.payload.card.isCut
import ro.dragossusi.sevens.payload.extensions.firstCut
import ro.dragossusi.sevens.payload.extensions.pointsCount


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
class SevensAiPlayer constructor(
    session: RoomPlayer,
    val type: GameTypeData
) : AiPlayer(session) {

    override fun pickCard(): Card {
        val hand = hand!!
        var selectedCard = hand.first()
        var times = 0
        for (i in hand.indices) {
            val currentCard = hand[i]
            if (currentCard.isCut(type.maxPlayers)) continue
            var currentTimes = 1
            for (j in i + 1 until hand.size) {
                if (hand[j] == currentCard) ++currentTimes
            }
            if (currentTimes > times) {
                selectedCard = currentCard
                times = currentTimes
            }
        }
        return selectedCard
    }

    override fun pickCard(placedCards: Array<Card>, canEnd: Boolean): Card? {
        if (placedCards.isEmpty()) return pickCard()
        val hand = hand!!
        val firstCard = placedCards.first()
        if (hand.hasCardNumber(firstCard.number)) return firstCard
        if (placedCards.pointsCount > 0) {
            return hand.firstCut(type.maxPlayers)
        }
        return if (canEnd) null
        else hand.chooseRandomCard()
    }


}