package frc.robot.subsystems.superstructure.elevator

import edu.wpi.first.math.trajectory.TrapezoidProfile

object ElevatorConstants {
    const val velocityInches = 26.5 // Guessed values for now
    const val accelationInches = 30.0

    // Conversion so one (I guess its mechanism rotation) is 1 inch on the elevator
    const val conversionFactor =
        20.0 * 1.751

    const val positionTolerance = 0.1
}