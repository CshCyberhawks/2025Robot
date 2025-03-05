package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.AutoTargeting
import frc.robot.RobotContainer
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import frc.robot.RobotState
import frc.robot.constants.AutoScoringConstants
import frc.robot.subsystems.superstructure.Superstructure
import frc.robot.util.input.OperatorControls
import java.util.*

object TeleopAutoScore {

    init {

    }


    fun score(): Command {
//        val reefSide = OperatorControls.reefSideSelector.selected
        val position = AutoScoringConstants.CoralScoringPositions.A.left

//        val position = when (OperatorControls.coralSideChooser.selected) {
//            AutoTargeting.CoralSide.Left -> reefSide.left
//            AutoTargeting.CoralSide.Right -> reefSide.right
//        }

        val driveCommand = GoToPose(position)
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