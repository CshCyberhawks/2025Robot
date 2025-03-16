package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import frc.robot.RobotContainer
import frc.robot.constants.AutoScoringConstants
import frc.robot.util.AllianceFlipUtil
import frc.robot.util.input.CoralSide
import frc.robot.util.input.OperatorControls
import kotlin.math.min

object TeleopAutoAlign {
    fun coralReefAlign(): Command {
        return GoToPose({
            val position = OperatorControls.reefPosition
            val side = OperatorControls.coralSide

            val goalPose = AutoScoringConstants.getReefPoseAtOffset(position.ordinal, side, 0.0)
            // Make the position get closer as it gets closer to the final goal
//            val adjustX: Double = 0.3
            var adjustX: Double = min(1.0, RobotContainer.drivetrain.getSwervePose().translation.getDistance(goalPose.translation) * 0.75)
//            val adjustX = 1.0

            // Once we're close enough we can just jump to the goal
            if (adjustX < 0.25) {
                adjustX = 0.0
            }

            AllianceFlipUtil.apply(AutoScoringConstants.getReefPoseAtOffset(position.ordinal, side, adjustX))
        }, { false })
    }

    fun algaeReefAlign(): Command {
        return GoToPose({
            val position = OperatorControls.reefPosition

            val goalPose = AutoScoringConstants.getAlgaePoseAtOffset(position.ordinal, 0.0)
            // Make the position get closer as it gets closer to the final goal
//            val adjustX: Double = 0.3
            var adjustX: Double = min(1.0, RobotContainer.drivetrain.getSwervePose().translation.getDistance(goalPose.translation) * 0.75)
//            val adjustX = 1.0

            // Once we're close enough we can just jump to the goal
            if (adjustX < 0.25) {
                adjustX = 0.0
            }

            AllianceFlipUtil.apply(AutoScoringConstants.getAlgaePoseAtOffset(position.ordinal, adjustX))
        })
    }
}