package ro.sevens.game.listener

import ro.sevens.game.PlayerSession
import ro.sevens.game.room.Room
import ro.sevens.game.room.simplePlayers
import ro.sevens.game.round.Round
import ro.sevens.game.round.toResponse
import ro.sevens.logger.TagLogger
import ro.sevens.payload.game.GameEndResponse
import ro.sevens.payload.game.NewRoundResponse
import ro.sevens.payload.game.PlayerTurnResponse


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
class MapPlayerNotifier(
    private val tagLogger: TagLogger?
) : PlayerNotifier {

    private val listeners = mutableMapOf<PlayerSession, Room.OnRoomChanged>()

    override fun addListener(player: PlayerSession, onRoomChanged: Room.OnRoomChanged) {
        listeners[player] = onRoomChanged
    }

    override fun removeListener(player: PlayerSession) {
        listeners.remove(player)
    }

    override suspend fun onRoomStopped(room: Room) {
        room.run {
            tagLogger?.d("onRoomStopped ${room.id}")
            listeners.forEach {
                it.value.onRoomStopped()
            }
        }
    }

    override suspend fun onGameEnded(room: Room) {
        room.run {
            tagLogger?.d("onGameEnded ${room.id}")
            val simplePlayers = simplePlayers.toTypedArray()
            listeners.forEach {
                val hand = it.key.hand!!
                it.value.onGameEnded(
                    GameEndResponse(
                        players = simplePlayers,
                        winner = currentPlayer!!.id,
                        wonPoints = hand.wonPointsCount,
                        wonCards = hand.wonCardsCount,
                        rounds = rounds.map(Round::toResponse).toTypedArray()
                    )
                )
            }
        }
    }

    override suspend fun onGameStarted(room: Room) {
        room.run {
            tagLogger?.d("onGameStarted ${room.id}")
            val simplePlayers = simplePlayers.toTypedArray()
            listeners.forEach {
                it.value.onGameStarted(simplePlayers)
            }
        }
    }

    override suspend fun onRoundStarted(room: Room) {
        room.run {
            tagLogger?.d("onRoundStarted ${room.id}")
            val simplePlayers = simplePlayers.toTypedArray()
            listeners.forEach {
                val hand = it.key.hand!!
                it.value.onRoundStarted(
                    NewRoundResponse(
                        hand.cards.toTypedArray(),
                        simplePlayers,
                        startingPlayer.id,
                        currentPlayer!!.id,
                        currentRound!!.cards.toTypedArray(),
                        wonPoints = hand.wonPointsCount,
                        wonCards = hand.wonCardsCount
                    )
                )
            }
        }
    }

    override suspend fun onPlayerTurn(room: Room) {
        room.run {
            tagLogger?.d("onPlayerTurn ${id}")
            val simplePlayers = simplePlayers.toTypedArray()
            listeners.forEach {
                it.value.onPlayerTurn(
                    PlayerTurnResponse(
                        it.key.hand!!.cards,
                        simplePlayers,
                        startingPlayer.id,
                        currentPlayer!!.id,
                        currentRound!!.cards.toTypedArray()
                    )
                )
            }
        }
    }

    override suspend fun onRoundEnded(room: Room) {
        room.run {
            tagLogger?.d("onRoundEnded ${room.rounds.last()}")
            val simplePlayers = simplePlayers.toTypedArray()
            listeners.forEach { (key, value) ->
                val hand = key.hand!!
                value.onRoundEnded(
                    NewRoundResponse(
                        hand.cards.toTypedArray(),
                        simplePlayers,
                        startingPlayer.id,
                        currentPlayer!!.id,
                        currentRound!!.cards.toTypedArray(),
                        wonPoints = hand.wonPointsCount,
                        wonCards = hand.wonCardsCount
                    )
                )
            }
        }
    }
}