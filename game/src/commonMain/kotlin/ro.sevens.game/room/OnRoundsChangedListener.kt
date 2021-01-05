package ro.dragossusi.sevens.game.room

import ro.dragossusi.sevens.game.listener.OnRoundEnded
import ro.dragossusi.sevens.game.listener.OnRoundStarted

interface OnRoundsChangedListener : OnRoundStarted, OnRoundEnded {
}