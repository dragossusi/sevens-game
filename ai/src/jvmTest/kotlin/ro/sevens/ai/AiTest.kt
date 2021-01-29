package ro.dragossusi.sevens.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import ro.dragossusi.sevens.ai.room.SevensAiRoom
import ro.dragossusi.sevens.game.deck.DeckProviderImpl
import ro.dragossusi.sevens.game.room.MacaoRoom
import ro.dragossusi.sevens.game.room.SevensRoom
import ro.dragossusi.logger.ConsoleLogger
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
class AiTest {

    @Test
    fun testSevensAis() {
        val sevensRoom = SevensRoom(
            id = System.currentTimeMillis(),
            type = GameTypeEnum.DUEL,
            deckProvider = DeckProviderImpl,
            tagLogger = ConsoleLogger("AiRoom"),
            coroutineContext = Dispatchers.Unconfined,
            roundEndDelay = 1250L
        )
        //todo
        val aiRoom = SevensAiRoom(
            tagLogger = ConsoleLogger("AiRoom"),
            dispatcher = Dispatchers.Unconfined,
            operationDelay = 0L,
            room = sevensRoom
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
            aiRoom.addAi("asd")
            aiRoom.addAi("asdfg")
            aiRoom.room.start()
            delay(100000)
        }
    }

    @Test
    fun testMacaoAis() {
        val macaoRoom = MacaoRoom(
            id = System.currentTimeMillis(),
            deckProvider = DeckProviderImpl,
            tagLogger = ConsoleLogger("AiRoom"),
            coroutineContext = Dispatchers.Unconfined,
            roundEndDelay = 1250L
        )
    }

}