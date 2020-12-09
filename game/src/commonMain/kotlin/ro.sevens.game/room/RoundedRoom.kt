package ro.sevens.game.room

import ro.sevens.game.round.Round
import ro.sevens.game.session.PlayerSession

interface RoundedRoom<R : Round> : Room {

    val rounds: List<R>

    val currentRound: R?

    suspend fun startRound()

    suspend fun endRound(player: PlayerSession): Boolean

    suspend fun newRound(player: PlayerSession): Boolean

}