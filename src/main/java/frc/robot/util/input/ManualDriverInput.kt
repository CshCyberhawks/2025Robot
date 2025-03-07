package frc.robot.util.input

import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.RobotContainer
import frc.robot.RobotState
import frc.robot.commands.TeleopAutoScore
import java.util.*

object ManualDriverInput {

    val cancel = 4;
    fun configureBindings() {
        RobotContainer.rightJoystick.button(2)
        .onTrue(Commands.runOnce({ RobotContainer.drivetrain.seedFieldCentric(); println("seeding field relative") }))

        RobotContainer.rightJoystick.button(cancel).onTrue(Commands.runOnce({
            if (!RobotContainer.currentDriveCommand.isEmpty) {
                RobotContainer.currentDriveCommand.get().cancel()
                RobotContainer.currentDriveCommand = Optional.empty()
            }
        }))

        RobotContainer.leftJoystick.button(3).onTrue(Commands.runOnce({
            RobotState.actionCancelled = true
            println("action cancelled")
        }))

        RobotContainer.leftJoystick.button(4).onTrue(Commands.runOnce({
            println("action confirmed")
            RobotState.actionConfirmed = true
//            if (RobotContainer.currentDriveCommand.isEmpty) {
//                TeleopAutoScore.score()
//            } else {
//            }
        }))
    }

}