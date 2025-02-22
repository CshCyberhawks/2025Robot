package cshcyberhawks.lib.requests

class AwaitRequest(private val condition: () -> Boolean) : Request() {
    override fun execute() {}

    override fun isFinished() = condition()
}