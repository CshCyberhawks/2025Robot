package cshcyberhawks.lib.requests

/**
 * A class that represents a parallel request, which runs multiple requests simultaneously.
 *
 * @param requests Request objects to be executed in parallel.
 */
class ParallelRequest(vararg requests: Request) : Request() {
    private val idleRequests = requests.toMutableList()
    private val inProgressRequests: MutableList<Request> = mutableListOf()

    private fun startRequests() {
        val toRemove: MutableList<Request> = mutableListOf()

        for (request in idleRequests) {
            if (request.allowed()) {
                request.execute()
                inProgressRequests.add(request)

                // Java is stupid and doesn't let you remove from a list while iterating over it
                toRemove.add(request)
            }
        }

        for (request in toRemove) {
            idleRequests.remove(request)
        }
    }

    override fun execute() {
        startRequests()
    }

    override fun isFinished(): Boolean {
        startRequests()
        inProgressRequests.removeIf { request -> request.isFinished() }

        return idleRequests.isEmpty() && inProgressRequests.isEmpty()
    }
}