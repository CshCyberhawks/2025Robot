package frc.robot.commands

import edu.wpi.first.wpilibj2.command.Command
import frc.robot.util.AllianceFlipUtil
import frc.robot.util.input.CoralSide
import frc.robot.util.input.OperatorControls

object AutoAlign {
    fun coralReefAlign(): Command {
        return GoToPose({
            val reefSide = OperatorControls.reefPosition

           AllianceFlipUtil.apply(when (OperatorControls.coralSide) {
                CoralSide.Left -> reefSide.left
                CoralSide.Right -> reefSide.right
            })
        }, { false })
    }

    fun algaeReefAlign(): Command {
        return GoToPose({
            AllianceFlipUtil.apply(OperatorControls.reefPosition.center)
        })
    }
}