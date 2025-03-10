package frc.robot.math

import cshcyberhawks.lib.math.MiscCalculations
import edu.wpi.first.math.geometry.Translation2d
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MiscCalculationsTest {
    @Test
    fun testGToMetersPerSecond() {
        assertEquals(9.8066, MiscCalculations.gToMetersPerSecond(1.0), 1e-4)
        assertEquals(19.6132, MiscCalculations.gToMetersPerSecond(2.0), 1e-4)
        assertEquals(0.0, MiscCalculations.gToMetersPerSecond(0.0), 1e-4)
    }

    @Test
    fun testCalculateDeadzone() {
        assertEquals(0.0, MiscCalculations.calculateDeadzone(0.1, 0.2), 1e-4)
        assertEquals(0.0, MiscCalculations.calculateDeadzone(-0.1, 0.2), 1e-4)
        assertEquals(0.5, MiscCalculations.calculateDeadzone(0.6, 0.2), 1e-4)
        assertEquals(-0.5, MiscCalculations.calculateDeadzone(-0.6, 0.2), 1e-4)
    }

    @Test
    fun testAppxEqual() {
        assertTrue(MiscCalculations.appxEqual(1.0, 1.05, 0.1))
        assertFalse(MiscCalculations.appxEqual(1.0, 1.2, 0.1))
        assertTrue(MiscCalculations.appxEqual(-1.0, -1.05, 0.1))
    }

    @Test
    fun testTranslation2dWithinRange() {
        val range = Pair(Translation2d(0.0, 0.0), Translation2d(2.0, 2.0))
        assertTrue(MiscCalculations.translation2dWithinRange(Translation2d(1.0, 1.0), range))
        assertFalse(MiscCalculations.translation2dWithinRange(Translation2d(3.0, 1.0), range))
        assertFalse(MiscCalculations.translation2dWithinRange(Translation2d(1.0, 3.0), range))
    }

    @Test
    fun testFindMatchingTranslation2dRange() {
        val ranges = arrayOf(
            Pair(Translation2d(0.0, 0.0), Translation2d(2.0, 2.0)),
            Pair(Translation2d(3.0, 3.0), Translation2d(5.0, 5.0))
        )
        val defaultRange = Pair(Translation2d(-1.0, -1.0), Translation2d(-1.0, -1.0))
        assertEquals(
            ranges[0],
            MiscCalculations.findMatchingTranslation2dRange(Translation2d(1.0, 1.0), ranges, defaultRange)
        )
        assertEquals(
            ranges[1],
            MiscCalculations.findMatchingTranslation2dRange(Translation2d(4.0, 4.0), ranges, defaultRange)
        )
        assertEquals(
            defaultRange,
            MiscCalculations.findMatchingTranslation2dRange(Translation2d(6.0, 6.0), ranges, defaultRange)
        )
    }

    @Test
    fun testPositionToRotations() {
        assertEquals(1.59155, MiscCalculations.positionToRotations(1.0, 0.1), 1e-5)
        assertEquals(3.1831, MiscCalculations.positionToRotations(2.0, 0.1), 1e-4)
        assertEquals(1.59155, MiscCalculations.positionToRotations(1.0, 0.1, 1.0), 1e-5)
    }
}
