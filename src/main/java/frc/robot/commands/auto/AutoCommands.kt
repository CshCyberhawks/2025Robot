package frc.robot.commands.auto

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Transform2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.util.Units
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import frc.robot.RobotContainer
import frc.robot.commands.GoToPose
import frc.robot.constants.AutoScoringConstants
import frc.robot.constants.FieldConstants
import frc.robot.util.AllianceFlipUtil
import frc.robot.util.input.CoralSide
import kotlin.math.min

object AutoCommands {
    fun coralReefAlign(position: AutoScoringConstants.ReefPositions, side: CoralSide): Command {
        val goalPose = AutoScoringConstants.getReefPoseAtOffset(position.ordinal, side, 0.0)

        return GoToPose({
            // Make the position get closer as it gets closer to the final goal
//            val adjustX: Double = 0.3
            var adjustX: Double = min(1.0, RobotContainer.drivetrain.getSwervePose().translation.getDistance(goalPose.translation) * 0.75)
//            val adjustX = 1.0

            // Once we're close enough we can just jump to the goal
            if (adjustX < 0.25) {
                adjustX = 0.0
            }

            AutoScoringConstants.getReefPoseAtOffset(position.ordinal, side, adjustX)
        })
    }

    fun feederAlign() = GoToPose({
        // TODO: Probably want to generate these positions from the field constants
        Pose2d(1.546, 7.352, Rotation2d.fromDegrees(-55.0))
    })
}