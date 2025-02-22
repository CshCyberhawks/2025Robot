package cshcyberhawks.lib.requests

/**
 * A request class that represents an empty request.
 * This class extends the `Request` class but does not add any additional functionality.
 */
class EmptyRequest() : Request() {
    override fun execute() {}

    override fun isFinished(): Boolean = true
}