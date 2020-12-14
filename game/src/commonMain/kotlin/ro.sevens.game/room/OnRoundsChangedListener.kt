package ro.sevens.game.room

import ro.sevens.game.listener.OnRoundEnded
import ro.sevens.game.listener.OnRoundStarted

interface OnRoundsChangedListener : OnRoundStarted, OnRoundEnded {
}