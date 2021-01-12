package ro.dragossusi.sevens.game.room

import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ro.dragossusi.sevens.game.listener.PlayerListener
import ro.dragossusi.sevens.game.listener.PlayerNotifier
import ro.dragossusi.sevens.game.listener.RoundedPlayerListener
import ro.dragossusi.sevens.game.listener.RoundsNotifier
import ro.dragossusi.sevens.game.round.Round
import ro.dragossusi.sevens.game.session.PlayerSession
import ro.dragossusi.sevens.game.session.chooseCard
import ro.dragossusi.logger.TagLogger
import ro.dragossusi.sevens.payload.Card

abstract class BaseRoundedRoom<L : RoundedPlayerListener, R : Round>(
    protected val roundsNotifier: RoundsNotifier<L, R>,
    playerNotifier: PlayerNotifier,
    tagLogger: TagLogger?
) : BaseRoom<L>(playerNotifier, tagLogger), RoundedRoom<L, R> {

    override val rounds = mutableListOf<R>()

    override var currentRound: R? = null

    /**
     * Add round listener for a player
     *
     * @param player            the player that needs to be notified
     * @param onRoundsChanged   the listener used to notify
     */
    fun addRoundsListener(
        player: PlayerSession,
        onRoundsChanged: OnRoundsChangedListener
    ) {
        roundsNotifier.addListener(player, onRoundsChanged)
    }

    /**
     * Remove round listener for a player
     *
     * @param player    the player that needs the listener removed
     */
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

    /**
     * Chooses a card for a player
     *
     * @return true is successful
     */
    protected suspend fun chooseCard(player: PlayerSession, card: Card): Boolean {
        val currentPlayer = currentPlayer ?: return false
        if (currentPlayer.id == player.id) {
            val currentRound = currentRound ?: return false
            return player.chooseCard(currentRound, card)
        }
        return false
    }

}