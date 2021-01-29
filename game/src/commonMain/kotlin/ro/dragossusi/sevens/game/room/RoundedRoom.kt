package ro.dragossusi.sevens.game.room

import ro.dragossusi.sevens.game.listener.PlayerListener
import ro.dragossusi.sevens.game.round.Round
import ro.dragossusi.sevens.game.session.RoomPlayer

interface RoundedRoom<L : PlayerListener, R : Round> : Room<L> {

    /**
     * Rounds of the game
     */
    val rounds: List<R>

    /**
     * The current round
     */
    val currentRound: R?

    /**
     * Starts the round
     */
    suspend fun startRound()

    /**
     * Ends the round
     *
     * @return true for success
     */
    suspend fun endRound(player: RoomPlayer): Boolean

    /**
     * Ends the current round and starts a new one
     *
     * @return true for success
     */
    suspend fun newRound(player: RoomPlayer): Boolean

}