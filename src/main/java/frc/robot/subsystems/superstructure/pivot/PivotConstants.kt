package frc.robot.subsystems.superstructure.pivot

import edu.wpi.first.math.trajectory.TrapezoidProfile

object PivotConstants {
    const val velocityDegrees = 180.0 // Guessed values for now
    const val accelerationDegrees = 200.0

    // Conversion so one (I guess its mechanism rotation) is 1 rotation of the pivot
    const val conversionFactor =
        100.0 * (32.0 / 24.0) / 360.0

    const val angleToleranceDegrees = 2.0
}
