package ro.sevens.game.listener

import ro.sevens.game.room.Room
import ro.sevens.game.session.PlayerSession

open class MapNotifier : RoomListeners {

    protected val listeners = mutableMapOf<PlayerSession, Room.OnRoomChanged>()

    override fun addListener(player: PlayerSession, onRoomChanged: Room.OnRoomChanged) {
        listeners[player] = onRoomChanged
    }

    override fun removeListener(player: PlayerSession) {
        listeners.remove(player)
    }

}