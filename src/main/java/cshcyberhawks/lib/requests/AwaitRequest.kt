package cshcyberhawks.lib.requests

/**
 * A request that waits until a specified condition is met.
 *
 * @property condition A lambda function that returns a Boolean. The request will wait until this condition evaluates to true.
 */
class AwaitRequest(private val condition: () -> Boolean) : Request() {
    override fun execute() {}

    override fun isFinished() = condition()
}