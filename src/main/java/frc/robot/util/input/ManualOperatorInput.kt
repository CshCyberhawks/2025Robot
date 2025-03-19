package frc.robot.util.IO

import cshcyberhawks.lib.requests.Request
import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.RobotContainer
import frc.robot.RobotState
import frc.robot.subsystems.superstructure.Superstructure
import frc.robot.subsystems.superstructure.pivot.PivotSystem

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
            Superstructure.scoreL4()
        }))
        RobotContainer.xbox.b().onTrue(Commands.runOnce({
//            Superstructure.request(Superstructure.pivotSystem.algaeRemoveAngle())
//            Superstructure.scoreL3()
            Superstructure.climbStowThenStow()
        }))
        RobotContainer.xbox.x().onTrue(Commands.runOnce({
            Superstructure.intakeFeeder()
//            Superstructure.pivotTest()
//            Superstructure.request(Superstructure.pivotSystem.l3Angle())
        }))
        RobotContainer.xbox.leftBumper().onTrue(Commands.runOnce({
            Superstructure.requestSuperstructureAction(Superstructure.climbSystem.stow())

//            Superstructure.requestSuperstructureAction(Superstructure.funnelSystem.stow())
//            Superstructure.removeAlgaeLow()
        }))
        RobotContainer.xbox.rightBumper().onTrue(Commands.runOnce({
//            println("doing the dpl")
            Superstructure.requestSuperstructureAction(Superstructure.climbSystem.deploy())
//            Superstructure.requestSuperstructureAction(Superstructure.funnelSystem.deploy())
//            Superstructure.removeAlgaeHigh()
        }))
        RobotContainer.xbox.a().onTrue(Commands.runOnce({
            Superstructure.deployClimb()
//            Superstructure.scoreBarge()
//            Superstructure.request(Superstructure.pivotSystem.stowAngle())
        }))
        RobotContainer.xbox.povUp().onTrue(Commands.runOnce({
            Superstructure.scoreProcessor()
        }))
        RobotContainer.xbox.back().onTrue(Commands.runOnce({
            RobotState.actionCancelled = true
        }))
        RobotContainer.xbox.start().onTrue(Commands.runOnce({
            RobotState.actionConfirmed = true
        }))
    }
}
