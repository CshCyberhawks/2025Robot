package frc.robot.subsystems.superstructure.pivot

import edu.wpi.first.math.trajectory.TrapezoidProfile

object PivotConstants {
    val trapConstraints = TrapezoidProfile.Constraints(180.0, 200.0) // Guessed values for now
}