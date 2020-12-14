package ro.sevens.ai.communication

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import ro.sevens.ai.room.AiRoom
import ro.sevens.game.bridge.RoundedCommunication
import ro.sevens.game.listener.OnRoundEnded
import ro.sevens.game.listener.OnRoundStarted
import ro.sevens.game.listener.RoundedPlayerListener
import ro.sevens.game.room.RoundedRoom
import ro.sevens.game.round.Round
import ro.sevens.payload.Player

class LocalRoundedCommunication<L : RoundedPlayerListener, R : Round, RM : RoundedRoom<L, R>>(
    aiRoom: AiRoom<L, R, RM>,
    player: Player,
    dispatcher: CoroutineDispatcher,
    playerListener: L
) : LocalCommunication<L, R, RM>(
    aiRoom,
    player,
    dispatcher,
    playerListener
), RoundedCommunication {

    override var onRoundStarted: OnRoundStarted? = null
    override var onRoundEnded: OnRoundEnded? = null

    override fun endRound() {
        launch {
            aiRoom.room.newRound(playerSession)
        }
    }

}