package cshcyberhawks.lib.requests

/**
 * Abstract class representing a prerequisite for a certain action or condition.
 */
abstract class Prerequisite {
    companion object {
        /**
         * Creates a prerequisite with the given condition.
         *
         * @param condition A lambda function that returns a Boolean. This function represents the condition that must be met.
         * @return An instance of Prerequisite with the specified condition.
         */
        fun withCondition(condition: () -> Boolean) = object : Prerequisite() {
            override fun met(): Boolean = condition()
        }
    }

    abstract fun met(): Boolean

    val booleanSupplier: () -> Boolean
        get() =
            { met() }
}