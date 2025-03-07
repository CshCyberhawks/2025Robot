package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.RobotContainer
import frc.robot.RobotState
import frc.robot.constants.AutoScoringConstants
import frc.robot.util.input.CoralSide
import frc.robot.util.input.OperatorControls
import java.util.*

object CoralReefAlign {

    init {

    }


    fun score(): Command {
        val reefSide = OperatorControls.reefPosition
//        val position = AutoScoringConstants.CoralScoringPositions.B.left

        val position = when (OperatorControls.coralSide) {
            CoralSide.Left -> reefSide.left
            CoralSide.Right -> reefSide.right
        }


        val driveCommand = GoToPose(position)
//        val driveCommand = Commands.runOnce({})
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