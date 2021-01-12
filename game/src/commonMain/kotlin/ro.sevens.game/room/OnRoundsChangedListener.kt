package ro.dragossusi.sevens.game.room

import ro.dragossusi.sevens.game.listener.OnRoundEnded
import ro.dragossusi.sevens.game.listener.OnRoundStarted

/**
 * Listener used to observe rounds
 */
interface OnRoundsChangedListener : OnRoundStarted, OnRoundEnded {
}