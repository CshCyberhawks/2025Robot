package frc.robot.util.IO

import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.RobotContainer
import frc.robot.RobotState
import frc.robot.subsystems.superstructure.Superstructure

object ManualOperatorInput {
    fun configureBindings() {
//        RobotContainer.xbox.x().onTrue(Commands.runOnce({
//            Superstructure.request(Superstructure.intakeSystem.algaeIntake())
//        }))
//        RobotContainer.xbox.b().onTrue(Commands.runOnce({
//            Superstructure.request(Superstructure.intakeSystem.algaeScore())
//        }))
//            Superstructure.request(Superstructure.elevatorSystem.algaeRemoveHighPosition())
//
//        }))
//        RobotContainer.xbox.a().onTrue(Commands.runOnce({
//            Superstructure.request(
//                Superstructure.elevatorSystem.l3Position()
//            )
//        }))
//        RobotContainer.xbox.b().onTrue(Commands.runOnce({
//            Superstructure.request(
//                Superstructure.elevatorSystem.stowPosition()
//            )
//        }))
//        RobotContainer.xbox.y().onTrue(Commands.runOnce({
//            Superstructure.scoreL4()
//        }))
//        RobotContainer.xbox.x().onTrue(Commands.runOnce({
//            Superstructure.intakeFeeder()
//        }))
//        RobotContainer.xbox.b().onTrue(Commands.runOnce({
//            Superstructure.scoreBarge()
//        }))
        RobotContainer.xbox.y().onTrue(Commands.runOnce({
            Superstructure.scoreBarge()
        }))
        RobotContainer.xbox.b().onTrue(Commands.runOnce({
            Superstructure.removeAlgaeHigh()
        }))
        RobotContainer.xbox.x().onTrue(Commands.runOnce({
            Superstructure.removeAlgaeLow()
        }))
        RobotContainer.xbox.back().onTrue(Commands.runOnce({
            RobotState.actionCancelled = true
        }))
        RobotContainer.xbox.start().onTrue(Commands.runOnce({
            RobotState.actionConfirmed = true
        }))
    }
}
