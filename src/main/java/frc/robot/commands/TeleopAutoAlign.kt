package frc.robot.commands

import cshcyberhawks.lib.math.MiscCalculations
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj2.command.Command
import frc.robot.RobotContainer
import frc.robot.constants.AutoScoringConstants
import frc.robot.constants.FieldConstants
import frc.robot.util.AllianceFlipUtil
import frc.robot.util.input.CoralSide
import frc.robot.util.input.OperatorControls
import kotlin.math.min

object TeleopAutoAlign {
    fun noWalkCoralReefAlign(): Command {
        return GoToPose({
            val position = OperatorControls.reefPosition
            val side = OperatorControls.coralSide

            AllianceFlipUtil.apply(AutoScoringConstants.getReefPoseAtOffset(position.ordinal, side, 0.0))
        })
    }

    fun noWalkAlgaeReefAlign(): Command {
        return GoToPose({
            val position = OperatorControls.reefPosition

            AllianceFlipUtil.apply(AutoScoringConstants.getAlgaePoseAtOffset(position.ordinal, 0.0))
        })
    }

    fun coralReefAlign(): Command {
        return GoToPose({
            val position = OperatorControls.reefPosition
            val side = OperatorControls.coralSide

            if (OperatorControls.noWalk) {
                AllianceFlipUtil.apply(AutoScoringConstants.getReefPoseAtOffset(position.ordinal, side, 0.0))
            } else {
                val goalPose =
                    AllianceFlipUtil.apply(AutoScoringConstants.getReefPoseAtOffset(position.ordinal, side, 0.0))
                // Make the position get closer as it gets closer to the final goal
//            val adjustX: Double = 0.3
                var adjustX: Double = min(
                    1.0,
                    RobotContainer.drivetrain.getSwervePose().translation.getDistance(goalPose.translation) * 0.5
                )
//            val adjustX = 1.0

                // Once we're close enough we can just jump to the goal
                if (adjustX < 0.3) {
                    adjustX = 0.0
                }

                AllianceFlipUtil.apply(AutoScoringConstants.getReefPoseAtOffset(position.ordinal, side, adjustX))
            }
        }, { false })
    }

    fun algaeReefAlign(): Command {
        return GoToPose({
            val position = OperatorControls.reefPosition

            if (OperatorControls.noWalk) {
                AllianceFlipUtil.apply(AutoScoringConstants.getAlgaePoseAtOffset(position.ordinal, 0.0))
            } else {
                val goalPose = AllianceFlipUtil.apply(AutoScoringConstants.getAlgaePoseAtOffset(position.ordinal, 0.0))
                // Make the position get closer as it gets closer to the final goal
//            val adjustX: Double = 0.3
                var adjustX: Double = min(
                    1.0,
                    RobotContainer.drivetrain.getSwervePose().translation.getDistance(goalPose.translation) * .5
                )
//            val adjustX = 1.0

                // Once we're close enough we can just jump to the goal
                if (adjustX < 0.3) {
                    adjustX = 0.0
                }

                AllianceFlipUtil.apply(AutoScoringConstants.getAlgaePoseAtOffset(position.ordinal, adjustX))
            }
        })
    }

        fun l1ReefAlign(): Command {
            return GoToPose({
                val position = OperatorControls.reefPosition

                if (OperatorControls.noWalk) {
                    AllianceFlipUtil.apply(AutoScoringConstants.getL1PoseAtOffset(position.ordinal, 0.0))
                } else {
                    val goalPose =
                        AllianceFlipUtil.apply(AutoScoringConstants.getL1PoseAtOffset(position.ordinal, 0.0))
                    // Make the position get closer as it gets closer to the final goal
//            val adjustX: Double = 0.3
                    var adjustX: Double = min(
                        1.0,
                        RobotContainer.drivetrain.getSwervePose().translation.getDistance(goalPose.translation) * 0.5
                    )
//            val adjustX = 1.0

                    // Once we're close enough we can just jump to the goal
                    if (adjustX < 0.3) {
                        adjustX = 0.0
                    }

                    AllianceFlipUtil.apply(AutoScoringConstants.getL1PoseAtOffset(position.ordinal, adjustX))
                }
            })
        }

        fun processorAlign(): Command = GoToPose({
            AllianceFlipUtil.apply(FieldConstants.processorPosition)
        }, { false })

        fun feederAlign(): Command = GoToPose({
            val leftPos = AllianceFlipUtil.apply(FieldConstants.leftFeederPosition)
            val rightPos = AllianceFlipUtil.apply(FieldConstants.rightFeederPosition)

            if (RobotContainer.drivetrain.getSwervePose().translation.getDistance(leftPos.translation) < RobotContainer.drivetrain.getSwervePose().translation.getDistance(
                    rightPos.translation
                )
            ) {
                leftPos
            } else {
                rightPos
            }
        }, { false })
    }