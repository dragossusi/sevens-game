package ro.dragossusi.sevens.game.room

import ro.dragossusi.sevens.game.listener.PlayerListener
import ro.dragossusi.sevens.game.round.Round
import ro.dragossusi.sevens.game.session.PlayerSession

interface RoundedRoom<L : PlayerListener, R : Round> : Room<L> {

    val rounds: List<R>

    val currentRound: R?

    suspend fun startRound()

    suspend fun endRound(player: PlayerSession): Boolean

    suspend fun newRound(player: PlayerSession): Boolean

}