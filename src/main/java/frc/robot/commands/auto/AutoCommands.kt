package frc.robot.commands.auto

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.RobotContainer
import frc.robot.commands.GoToPose
import frc.robot.constants.AutoScoringConstants
import frc.robot.constants.FieldConstants
import frc.robot.subsystems.swerve.SwerveConstants
import frc.robot.util.AllianceFlipUtil
import frc.robot.util.input.CoralSide
import kotlin.math.abs
import kotlin.math.min

object AutoCommands {
    fun coralReefAlign(position: AutoScoringConstants.ReefPositions, side: CoralSide): Command {
        val goalPose = AllianceFlipUtil.apply(AutoScoringConstants.getReefPoseAtOffset(position.ordinal, side, 0.0))

        return GoToPose({
            // Make the position get closer as it gets closer to the final goal
//            val adjustX: Double = 0.3
            var adjustX: Double =
                min(1.0, RobotContainer.drivetrain.getSwervePose().translation.getDistance(goalPose.translation) * 0.75)
//            val adjustX = 1.0

            // Once we're close enough we can just jump to the goal
            if (adjustX < 0.3) {
                adjustX = 0.0
            }

            AllianceFlipUtil.apply(AutoScoringConstants.getReefPoseAtOffset(position.ordinal, side, adjustX))
        }, {
            val currentPose = RobotContainer.drivetrain.getSwervePose()

            (abs(currentPose.x - goalPose.x) < SwerveConstants.positionDeadzone && abs(currentPose.y - goalPose.y) < SwerveConstants.positionDeadzone && abs(
                currentPose.rotation.degrees - goalPose.rotation.degrees
            ) < SwerveConstants.rotationDeadzone)
        })
    }

    val unsafeReefPositions = arrayOf(
        AutoScoringConstants.ReefPositions.A,
        AutoScoringConstants.ReefPositions.C,
        AutoScoringConstants.ReefPositions.E,
    )

    fun safeReefExit(lastTarget: AutoScoringConstants.ReefPositions) = if (unsafeReefPositions.contains(lastTarget)) {
        GoToPose({ AllianceFlipUtil.apply(AutoScoringConstants.getAlgaePoseAtOffset(lastTarget.ordinal, 1.0)) }, {
            // TODO: Probably need to tune this
            AllianceFlipUtil.apply(FieldConstants.Reef.center)
                .getDistance(RobotContainer.drivetrain.getSwervePose().translation) > 2.0 // Can continue once we're 2 meters out from the center of the reef
        })
    } else Commands.none()

    fun leftFeederAlign() = GoToPose({
        // TODO: Probably want to generate these positions from the field constants
        AllianceFlipUtil.apply(Pose2d(1.546, 7.352, Rotation2d.fromDegrees(-55.0)))
    })

    fun rightFeederAlign() = GoToPose({
        AllianceFlipUtil.apply(Pose2d(1.546, 8.052 - 7.352, Rotation2d.fromDegrees(55.0)))
    })
}