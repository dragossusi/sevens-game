package ro.dragossusi.sevens.game.listener

import ro.dragossusi.sevens.game.session.RoomPlayer

open class MapNotifier<N> : ListenerCollection<N> {

    protected val listeners = mutableMapOf<RoomPlayer, N>()

    override fun addListener(player: RoomPlayer, onRoomChanged: N) {
        listeners[player] = onRoomChanged
    }

    override fun removeListener(player: RoomPlayer) {
        listeners.remove(player)
    }

}