package cshcyberhawks.lib.requests

import java.util.*

/**
 * A class representing a sequential request that executes multiple requests in sequence. Waits until the previous request is completed to execute the next one.
 *
 * @param requests The requests to be executed in sequence.
 */
class SequentialRequest(vararg requests: Request) : Request() {
    private val idleRequests = requests.toMutableList()
    private var currentRequest = if (idleRequests.isNotEmpty()) idleRequests.removeAt(0) else EmptyRequest()
    private var startedCurrentRequest = false

    private fun startNextRequest() {
        if (idleRequests.isNotEmpty() && (currentRequest.isFinished() || currentRequest.deadlineHit()) && startedCurrentRequest) {
            currentRequest = idleRequests.removeAt(0)
            startedCurrentRequest = false
            startIfAllowed()
        }
    }

    private fun startIfAllowed() {
        if (!startedCurrentRequest && currentRequest.allowed()) {
            currentRequest.execute()
            startedCurrentRequest = true
        }
    }

    override fun execute() {
        startIfAllowed()
        startNextRequest()
    }

    override fun isFinished(): Boolean {
        startIfAllowed()
        startNextRequest()

        return idleRequests.isEmpty() && startedCurrentRequest && currentRequest.isFinished()
    }
}