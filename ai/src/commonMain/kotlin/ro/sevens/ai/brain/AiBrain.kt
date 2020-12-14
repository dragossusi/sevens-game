package ro.sevens.ai.brain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import ro.sevens.game.listener.PlayerListener
import kotlin.coroutines.CoroutineContext

abstract class AiBrain(
    dispather: CoroutineDispatcher
) : PlayerListener, CoroutineScope {

    private val job = Job()

    final override val coroutineContext: CoroutineContext = dispather + job

}