package ro.dragossusi.sevens.game.listener

import ro.dragossusi.sevens.game.room.OnRoundsChangedListener
import ro.dragossusi.sevens.game.room.RoundedRoom
import ro.dragossusi.sevens.game.round.Round

interface RoundsNotifier<L : RoundedPlayerListener, R : Round> : ListenerCollection<OnRoundsChangedListener> {
    suspend fun onRoundStarted(room: RoundedRoom<L, R>)
    suspend fun onRoundEnded(room: RoundedRoom<L, R>)
}