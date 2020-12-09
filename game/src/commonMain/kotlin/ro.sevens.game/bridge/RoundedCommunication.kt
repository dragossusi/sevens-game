package ro.sevens.game.bridge

import ro.sevens.game.listener.OnRoundEnded
import ro.sevens.game.listener.OnRoundStarted

interface RoundedCommunication : Communication, RoundedClientActions {

    var onRoundStarted: OnRoundStarted?
    var onRoundEnded: OnRoundEnded?

}