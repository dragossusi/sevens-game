package ro.sevens.game.listener

import ro.sevens.game.room.OnRoundsChangedListener
import ro.sevens.game.room.RoundedRoom
import ro.sevens.game.round.Round

interface RoundsNotifier<L : RoundedPlayerListener, R : Round> : ListenerCollection<OnRoundsChangedListener> {
    suspend fun onRoundStarted(room: RoundedRoom<L, R>)
    suspend fun onRoundEnded(room: RoundedRoom<L, R>)
}