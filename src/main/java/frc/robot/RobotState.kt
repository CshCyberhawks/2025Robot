package frc.robot

import frc.robot.subsystems.superstructure.intake.AlgaeState
import frc.robot.subsystems.superstructure.intake.CoralState

object RobotState {
    // I'm not sure we need these
    // I don't think we want to use them in the superstructure, but it might make sense to reference them in swerve or auto
    var coralState = CoralState.Empty
    var algaeState = AlgaeState.Empty
}