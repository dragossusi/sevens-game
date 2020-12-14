package ro.sevens.game.room

import ro.sevens.game.listener.OnGameEnded
import ro.sevens.game.listener.OnGameStarted
import ro.sevens.game.listener.OnPlayerTurn
import ro.sevens.game.listener.OnRoomConnected

/**
 *
 * @author dragos
 * @since 13/12/20
 */
interface OnRoomChangedListener : OnPlayerTurn, OnRoomConnected, OnGameStarted, OnGameEnded {
    suspend fun onRoomStopped()
}