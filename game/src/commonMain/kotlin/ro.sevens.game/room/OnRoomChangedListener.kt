package ro.dragossusi.sevens.game.room

import ro.dragossusi.sevens.game.listener.OnGameEnded
import ro.dragossusi.sevens.game.listener.OnGameStarted
import ro.dragossusi.sevens.game.listener.OnPlayerTurn
import ro.dragossusi.sevens.game.listener.OnRoomConnected

/**
 *
 * Listener used to observe the room
 *
 * @author dragos
 * @since 13/12/20
 */
interface OnRoomChangedListener : OnPlayerTurn, OnRoomConnected, OnGameStarted, OnGameEnded {
    /**
     * Called when room stopped
     */
    suspend fun onRoomStopped()
}