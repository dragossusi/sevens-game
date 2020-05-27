package ro.sevens.ai

import kotlinx.coroutines.runBlocking
import org.junit.Test
import ro.sevens.game.deck.DeckProviderImpl
import ro.sevens.game.listener.MapPlayerNotifier
import ro.sevens.logger.JavaLogger
import ro.sevens.payload.enums.GameTypeEnum
import java.util.logging.Logger


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
class TestAis {

    @Test
    fun test() {
        val logger = JavaLogger(Logger.getLogger(AiRoom::class.java.name), "AI")
        val aiRoom: AiRoom = AiRoom(
            System.currentTimeMillis(),
            GameTypeEnum.DUEL,
            DeckProviderImpl,
            logger,
            MapPlayerNotifier(
                logger
            )
        )
        runBlocking {
            aiRoom.addAi("Ivan")
            aiRoom.addAi("Dragos")
            println("${aiRoom.room.rounds}")
        }
    }

}