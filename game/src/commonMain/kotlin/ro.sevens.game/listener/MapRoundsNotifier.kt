package ro.dragossusi.sevens.game.listener

import ro.dragossusi.sevens.game.room.OnRoundsChangedListener
import ro.dragossusi.sevens.game.room.RoundedRoom
import ro.dragossusi.sevens.game.room.simplePlayers
import ro.dragossusi.sevens.game.round.Round
import ro.dragossusi.logger.TagLogger
import ro.dragossusi.sevens.payload.game.NewRoundResponse

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