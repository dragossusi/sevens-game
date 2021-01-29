package ro.dragossusi.sevens.ai.communication

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import ro.dragossusi.sevens.ai.room.AiRoom
import ro.dragossusi.sevens.game.bridge.RoundedCommunication
import ro.dragossusi.sevens.game.listener.OnRoundEnded
import ro.dragossusi.sevens.game.listener.OnRoundStarted
import ro.dragossusi.sevens.game.listener.RoundedPlayerListener
import ro.dragossusi.sevens.game.room.RoundedRoom
import ro.dragossusi.sevens.game.round.Round
import ro.dragossusi.sevens.payload.Player

class LocalRoundedCommunication<L : RoundedPlayerListener, R : Round, RM : RoundedRoom<L, R>>(
    aiRoom: AiRoom<L, RM>,
    player: Player,
    dispatcher: CoroutineDispatcher,
    playerListener: L
) : LocalCommunication<L, RM>(
    aiRoom,
    player,
    dispatcher,
    playerListener
), RoundedCommunication {

    override var onRoundStarted: OnRoundStarted? = null
    override var onRoundEnded: OnRoundEnded? = null

    override fun endTurn() {
        launch {
            if (room.canEndTurn(playerSession))
                room.endTurn(playerSession)
        }
    }

}