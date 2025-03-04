package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.AutoTargeting
import frc.robot.RobotContainer
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import frc.robot.RobotState
import frc.robot.subsystems.superstructure.Superstructure
import frc.robot.util.input.OperatorControls
import java.util.*

object TeleopAutoScore {
    fun score(): Command {
        val driveCommand = AutoTargeting.goToPose(Pose2d(2.37, 4.0, Rotation2d.fromDegrees(0.0)))
//        val driveCommand = AutoTargeting.autoToNearestCoral(OperatorControls.coralSideChooser.selected, RobotContainer.drivetrain.getSwervePose())

        RobotContainer.currentDriveCommand = Optional.of(driveCommand);
        RobotState.autoDriving = true

        return driveCommand.andThen(Commands.runOnce({
            println("drive command and then ending")
            RobotState.autoDriving = false
            RobotContainer.currentDriveCommand = Optional.empty()
        }))
    }
}