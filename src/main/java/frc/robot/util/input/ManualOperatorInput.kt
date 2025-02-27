package frc.robot.util.IO

import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.RobotContainer
import frc.robot.subsystems.superstructure.Superstructure

object ManualOperatorInput {
    fun configureBindings() {
//        RobotContainer.xbox.x().onTrue(Superstructure.intakeSystem.coralIntake())
//        RobotContainer.xbox.b().onTrue(Superstructure.intakeSystem.coralScore())
        RobotContainer.xbox.y().onTrue(Commands.runOnce({
            Superstructure.request(Superstructure.elevatorSystem.algaeRemoveHighPosition())

        }))
        RobotContainer.xbox.a().onTrue(Commands.runOnce({
            Superstructure.request(
                Superstructure.elevatorSystem.l3Position()
            )
        }))
    }
}