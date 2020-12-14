package ro.sevens.game.listener

import ro.sevens.game.session.PlayerSession

open class MapNotifier<N> : ListenerCollection<N> {

    protected val listeners = mutableMapOf<PlayerSession, N>()

    override fun addListener(player: PlayerSession, onRoomChanged: N) {
        listeners[player] = onRoomChanged
    }

    override fun removeListener(player: PlayerSession) {
        listeners.remove(player)
    }

}