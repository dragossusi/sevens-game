package ro.sevens.ai

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import ro.sevens.game.bridge.RoundedCommunication
import ro.sevens.game.listener.OnRoundEnded
import ro.sevens.game.listener.OnRoundStarted
import ro.sevens.game.room.Room
import ro.sevens.game.room.RoundedRoom
import ro.sevens.game.round.Round
import ro.sevens.game.session.PlayerSession
import ro.sevens.logger.TagLogger
import ro.sevens.payload.Player

class LocalRoundedCommunication<R : Round, RM : RoundedRoom<R>>(
    aiRoom: AiRoom<R, RM>,
    player: Player,
    dispatcher: CoroutineDispatcher,
    tagLogger: TagLogger,
    playerInit: (Room, Player) -> PlayerSession
) : LocalCommunication<R, RM>(
    aiRoom,
    player,
    dispatcher,
    tagLogger,
    playerInit
), RoundedCommunication {

    override var onRoundStarted: OnRoundStarted? = null
    override var onRoundEnded: OnRoundEnded? = null

    override fun endRound() {
        launch {
            aiRoom.room.newRound(playerSession)
        }
    }

}