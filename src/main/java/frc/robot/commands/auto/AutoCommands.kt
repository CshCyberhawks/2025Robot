package frc.robot.commands.auto

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Transform2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.util.Units
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
        val directionVector = Pose2d(FieldConstants.Reef.center, Rotation2d.fromDegrees((180 - (60 * position.ordinal)).toDouble()))

        val goalPose = when (side) {
            CoralSide.Left -> position.left
            CoralSide.Right -> position.right
        }

        return GoToPose({
            // Make the position get closer as it gets closer to the final goal
//            val adjustX: Double = 0.3
            val adjustX: Double = min(0.3, RobotContainer.drivetrain.getSwervePose().translation.getDistance(goalPose.translation) / 4.0)

            val offsetPose =
                Pose2d(
                    Translation2d(
                        directionVector
                            .transformBy(Transform2d(adjustX, 0.0, Rotation2d()))
                            .x,
                        directionVector
                            .transformBy(Transform2d(adjustX, 0.0, Rotation2d()))
                            .y
                    ),
                    Rotation2d(
                        directionVector.rotation.radians
                    ).rotateBy(Rotation2d.k180deg)
                )

            AllianceFlipUtil.apply(goalPose.plus(Transform2d(offsetPose.x, offsetPose.y, offsetPose.rotation)))
        })
    }

    fun feederAlign() = GoToPose({
        // TODO: Put real position in (might want to have multiple per side that are auto selected
        Pose2d()
    })
}