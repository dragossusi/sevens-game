package ro.sevens.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import ro.sevens.game.deck.DeckProviderImpl
import ro.sevens.logger.ConsoleLogger
import ro.sevens.payload.enums.GameTypeEnum

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
class AiTest {

    @Test
    fun testAis() {

        val room = AiRoom(
            System.currentTimeMillis(),
            GameTypeEnum.DUEL,
            DeckProviderImpl,
            ConsoleLogger("AiRoom"),
            Dispatchers.Unconfined,
            0L
        )
//        val communication = LocalSevensCommunication(
//            aiRoom = room,
//            player = Player(
//                1L,
//                "Dragos",
//                null
//            ),
//            dispatcher = Dispatchers.IO,
//            tagLogger = ConsoleLogger("Communication")
//        )
        runBlocking {
            room.addAi("asd")
            room.addAi("asdfg")
            delay(100000)
        }
    }
}