package frc.robot.util.IO

import frc.robot.RobotContainer

object ManualOperatorInput {
    fun configureBindings() {
        RobotContainer.xbox.x().onTrue(RobotContainer.superstructure.intakeSystem.coralIntake())
        RobotContainer.xbox.b().onTrue(RobotContainer.superstructure.intakeSystem.coralScore())
        RobotContainer.xbox.a().onTrue(RobotContainer.superstructure.intakeSystem.algaeIntake())
        RobotContainer.xbox.y().onTrue(RobotContainer.superstructure.intakeSystem.algaeScore())
    }
}