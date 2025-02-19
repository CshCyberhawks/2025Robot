package frc.robot.subsystems.superstructure.elevator

import edu.wpi.first.math.trajectory.TrapezoidProfile

object ElevatorConstants {
    val trapConstraints = TrapezoidProfile.Constraints(26.5, 30.0) // Guessed values for now
}