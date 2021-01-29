package ro.dragossusi.sevens.ai.brain

import kotlinx.coroutines.CoroutineDispatcher
import ro.dragossusi.sevens.game.listener.RoundedPlayerListener

abstract class RoundedAiBrain(
    dispatcher: CoroutineDispatcher
) : AiBrain(dispatcher), RoundedPlayerListener {
}