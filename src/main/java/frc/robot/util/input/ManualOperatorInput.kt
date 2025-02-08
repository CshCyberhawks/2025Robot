package frc.robot.util.IO

import frc.robot.RobotContainer

object ManualOperatorInput {
    fun configureBindings() {
        RobotContainer.xbox.x().onTrue(RobotContainer.intakeSystem.coralIntake())
        RobotContainer.xbox.b().onTrue(RobotContainer.intakeSystem.coralScore())
        RobotContainer.xbox.a().onTrue(RobotContainer.intakeSystem.algaeIntake())
        RobotContainer.xbox.y().onTrue(RobotContainer.intakeSystem.algaeScore())
    }
}