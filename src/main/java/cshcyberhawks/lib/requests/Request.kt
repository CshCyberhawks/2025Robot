package cshcyberhawks.lib.requests

/**
 * Abstract base class representing a generic request.
 * Requests system inspired by the one in Citrus Circuits' 2024 Robot Code
 */
abstract class Request {
    companion object {
        /**
         * Creates a new instance of a Request with the specified action.
         *
         * @param action The action to be executed when the request is processed.
         * @return A new instance of a Request with the specified action.
         */
        fun withAction(action: () -> Unit) = object : Request() {
            override fun execute() {
                action()
            }
        }
    }

    abstract fun execute()

    open fun isFinished(): Boolean = true

    private var prerequisites: MutableList<Prerequisite> = mutableListOf()

    fun withPrerequisite(prerequisite: Prerequisite): Request {
        prerequisites.add(prerequisite)

        return this
    }

    fun withPrerequisites(vararg prerequisites: Prerequisite): Request {
        for (prerequisite in prerequisites) {
            this.prerequisites.add(prerequisite)
        }

        return this
    }

    fun allowed(): Boolean {
        return prerequisites.all { prerequisite -> prerequisite.met() }
    }
}