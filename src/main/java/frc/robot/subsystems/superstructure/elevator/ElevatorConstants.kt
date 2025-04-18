package frc.robot.subsystems.superstructure.elevator

import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.math.util.Units

object ElevatorConstants {
    const val velocityInches = 70.0 // Guessed values for now
    const val accelationInches = 75.0

    // Conversion so one (I guess its mechanism rotation) is 1 inch on the elevator
    const val conversionFactor =
//        20.0 * 1.751 * Math.PI / 30.0
    9.0 * 1.751 * Math.PI / 30.0


    // Large tolerance, not used for high accuracy things
    const val positionTolerance = 0.5

    // The torque to hold the elevator against gravity (Newton-Meters)
    val kGNM = Units.inchesToMeters(1.751 / 2.0) * (25.6 * 4.4482216153)
}
