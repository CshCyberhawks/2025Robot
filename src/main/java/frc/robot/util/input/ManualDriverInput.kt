package frc.robot.util.input

import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.RobotContainer
import frc.robot.RobotState
import frc.robot.commands.TeleopAutoScore
import java.util.*

object ManualDriverInput {

    val cancel = 2;
    fun configureBindings() {
        RobotContainer.rightJoystick.button(2)
        .onTrue(Commands.runOnce({ RobotContainer.drivetrain.seedFieldCentric() }))

        RobotContainer.leftJoystick.button(cancel).onTrue(Commands.runOnce({
            if (!RobotContainer.currentDriveCommand.isEmpty) {
                RobotContainer.currentDriveCommand.get().cancel()
                RobotContainer.currentDriveCommand = Optional.empty()
                RobotState.actionCancelled = true
            }
        }))

        RobotContainer.leftJoystick.button(3).onTrue(Commands.runOnce({
            if (RobotContainer.currentDriveCommand.isEmpty) {
                TeleopAutoScore.score()
            } else {
                RobotState.actionConfirmed = true
            }
        }))
    }

}