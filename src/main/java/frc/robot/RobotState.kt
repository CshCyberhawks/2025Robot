package frc.robot

import frc.robot.subsystems.superstructure.intake.GamePieceState


object RobotState {
    var gamePieceState = GamePieceState.Empty

    var actionCancelled = false
    var actionConfirmed = false
}