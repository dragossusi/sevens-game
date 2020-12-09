package ro.sevens.game.listener

import ro.sevens.game.room.Room
import ro.sevens.game.room.RoundedRoom
import ro.sevens.game.round.Round
import ro.sevens.game.session.PlayerSession

interface RoundsNotifier<R : Round> : RoomListeners {
    suspend fun onRoundStarted(room: RoundedRoom<R>)
    suspend fun onRoundEnded(room: RoundedRoom<R>)
}