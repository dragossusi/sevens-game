package ro.sevens.ai.brain

import kotlinx.coroutines.CoroutineDispatcher
import ro.sevens.game.listener.RoundedPlayerListener

abstract class RoundedAiBrain(dispather: CoroutineDispatcher) : AiBrain(dispather), RoundedPlayerListener {
}