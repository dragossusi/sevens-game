package ro.sevens.game.room

import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ro.sevens.game.listener.PlayerListener
import ro.sevens.game.listener.PlayerNotifier
import ro.sevens.game.listener.RoundedPlayerListener
import ro.sevens.game.listener.RoundsNotifier
import ro.sevens.game.round.Round
import ro.sevens.game.session.PlayerSession
import ro.sevens.game.session.chooseCard
import ro.sevens.logger.TagLogger
import ro.sevens.payload.Card

abstract class BaseRoundedRoom<L : RoundedPlayerListener, R : Round>(
    protected val roundsNotifier: RoundsNotifier<L, R>,
    playerNotifier: PlayerNotifier,
    tagLogger: TagLogger?
) : BaseRoom<L>(playerNotifier, tagLogger), RoundedRoom<L, R> {

    override val rounds = mutableListOf<R>()

    override var currentRound: R? = null


    fun addRoundsListener(
        player: PlayerSession,
        onRoundsChanged: OnRoundsChangedListener
    ) {
        roundsNotifier.addListener(player, onRoundsChanged)
    }

    fun removeRoundsListener(player: PlayerSession) {
        roundsNotifier.removeListener(player)
    }

    override suspend fun endRound(player: PlayerSession): Boolean = withContext(coroutineContext) {
        currentRound?.let {
            if (it.end(player)) {
                currentPlayer = it.owner
                roundsNotifier.onRoundEnded(this@BaseRoundedRoom)
                delay(roundEndDelay)
                return@withContext true
            }
        } ?: throw IllegalStateException("No round started")
        return@withContext false
    }

    protected suspend fun chooseCard(player: PlayerSession, card: Card): Boolean {
        val currentPlayer = currentPlayer ?: return false
        if (currentPlayer.id == player.id) {
            val currentRound = currentRound ?: return false
            return player.chooseCard(currentRound, card)
        }
        return false
    }

}