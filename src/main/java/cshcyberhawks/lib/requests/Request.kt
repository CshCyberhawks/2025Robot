package cshcyberhawks.lib.requests

import java.util.Optional

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
    private var deadline: Optional<() -> Boolean> = Optional.empty()

    /**
     * Adds a prerequisite to the current request.
     *
     * @param prerequisite The prerequisite to be added.
     * @return The current request with the added prerequisite.
     */
    fun withPrerequisite(prerequisite: Prerequisite): Request {
        prerequisites.add(prerequisite)

        return this
    }

    /**
     * Adds the specified prerequisites to the request.
     *
     * @param prerequisites Prerequisites to be added.
     * @return The current Request instance with the added prerequisites.
     */
    fun withPrerequisites(vararg prerequisites: Prerequisite): Request {
        for (prerequisite in prerequisites) {
            this.prerequisites.add(prerequisite)
        }

        return this
    }

    fun withDeadline(deadline: () -> Boolean): Request {
        this.deadline = Optional.of(deadline)

        return this
    }

    fun allowed(): Boolean {
        return prerequisites.all { prerequisite -> prerequisite.met() }
    }

    fun deadlineHit() = deadline.isPresent && deadline.get()()
}