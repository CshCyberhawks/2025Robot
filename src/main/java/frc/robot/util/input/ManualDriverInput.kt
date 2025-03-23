package frc.robot.util.input

import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.RobotContainer
import frc.robot.RobotState
import frc.robot.subsystems.superstructure.Superstructure
import java.util.*

object ManualDriverInput {
    var alignCommand = Commands.none()

    val cancel = 4;
    fun configureBindings() {
        RobotContainer.rightJoystick.button(2)
        .onTrue(Commands.runOnce({ RobotContainer.drivetrain.seedFieldCentric(); println("seeding field relative") }))

        RobotContainer.rightJoystick.button(cancel).onTrue(Commands.runOnce({
            if (!RobotContainer.currentDriveCommand.isEmpty) {
                println("cancelling auto drive")
                RobotContainer.currentDriveCommand.get().cancel()
                RobotContainer.currentDriveCommand = Optional.empty()
                RobotState.autoDriving = false
            }
        }))

        RobotContainer.rightJoystick.button(3).onTrue(Commands.runOnce({
            OperatorControls.noWalk = false
            alignCommand.cancel()
            alignCommand = OperatorControls.action.alignCommand
            alignCommand.schedule()
        }))

        RobotContainer.rightJoystick.button(3).onFalse(Commands.runOnce({
            alignCommand.cancel()
        }))

        RobotContainer.leftJoystick.button(3).onTrue(Commands.runOnce({
            RobotState.actionCancelled = true
            println("action cancelled")
        }))

        RobotContainer.leftJoystick.button(4).onTrue(Commands.runOnce({
            RobotState.actionConfirmed = true
            println("action confirmed")
//            if (RobotContainer.currentDriveCommand.isEmpty && ) {
//            } else {
//            }
        }))

        RobotContainer.leftJoystick.button(2).onTrue(Commands.runOnce({
            OperatorControls.action.superStructureCommand.schedule()
            println("running superstructure command")
        }))

        RobotContainer.leftJoystick.button(1).onTrue(Commands.runOnce({
//            Superstructure.correctIntake()
            Superstructure.intakeFeeder()
        }))
    }

}