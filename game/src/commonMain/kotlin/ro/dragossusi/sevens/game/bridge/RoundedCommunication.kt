package ro.dragossusi.sevens.game.bridge

import ro.dragossusi.sevens.game.listener.OnRoundEnded
import ro.dragossusi.sevens.game.listener.OnRoundStarted

interface RoundedCommunication : Communication, RoundedClientActions {

    var onRoundStarted: OnRoundStarted?
    var onRoundEnded: OnRoundEnded?

}