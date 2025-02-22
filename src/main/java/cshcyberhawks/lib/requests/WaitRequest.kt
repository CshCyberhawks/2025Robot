package cshcyberhawks.lib.requests

import cshcyberhawks.lib.math.Timer

/**
 * A request that waits for a specified amount of time before completing.
 *
 * @property time The amount of time to wait in seconds.
 */
class WaitRequest(private val time: Double) : Request() {
    val timer = Timer()

    override fun execute() {
        timer.reset()
        timer.start()
    }

    override fun isFinished(): Boolean {
        return timer.hasElapsed(time)
    }
}