package ro.sevens.game.listener

import ro.sevens.game.room.OnRoundsChangedListener
import ro.sevens.game.room.RoundedRoom
import ro.sevens.game.room.simplePlayers
import ro.sevens.game.round.Round
import ro.sevens.logger.TagLogger
import ro.sevens.payload.game.NewRoundResponse

class MapRoundsNotifier<L : RoundedPlayerListener, R : Round>(
    private val tagLogger: TagLogger?,
) : MapNotifier<OnRoundsChangedListener>(), RoundsNotifier<L, R> {

    override suspend fun onRoundStarted(room: RoundedRoom<L, R>) {
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

    override suspend fun onRoundEnded(room: RoundedRoom<L, R>) {
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