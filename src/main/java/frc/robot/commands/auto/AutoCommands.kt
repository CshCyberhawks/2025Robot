package frc.robot.commands.auto

import edu.wpi.first.math.geometry.Pose2d
import frc.robot.commands.GoToPose
import frc.robot.constants.AutoScoringConstants
import frc.robot.util.AllianceFlipUtil
import frc.robot.util.input.CoralSide

object AutoCommands {
    fun coralReefAlign(position: AutoScoringConstants.ReefPositions, side: CoralSide) = GoToPose({
        AllianceFlipUtil.apply(when (side) {
            CoralSide.Left -> position.left
            CoralSide.Right -> position.right
        })
    })

    fun feederAlign() = GoToPose({
        // TODO: Put real position in (might want to have multiple per side that are auto selected
        Pose2d()
    })
}