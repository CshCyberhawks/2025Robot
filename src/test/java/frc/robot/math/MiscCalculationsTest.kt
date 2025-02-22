package frc.robot.math

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MiscCalculationsTest {
    // To learn more about how to write unit tests, see the
    // JUnit 5 User Guide at https://junit.org/junit5/docs/current/user-guide/

    @Test
    fun testPositionToRotations() {
        assertEquals(1.59155, MiscCalculations.positionToRotations(1.0, 0.1), 1e-5)
        assertEquals(3.1831, MiscCalculations.positionToRotations(2.0, 0.1), 1e-4)
        assertEquals(1.59155, MiscCalculations.positionToRotations(1.0, 0.1, 1.0), 1e-5)
    }
}
