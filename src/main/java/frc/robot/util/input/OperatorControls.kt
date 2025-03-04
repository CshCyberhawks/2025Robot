package frc.robot.util.input

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.robot.AutoTargeting
import frc.robot.constants.AutoScoringConstants

object OperatorControls {
    val actionChooser = SendableChooser<DriverAction>()

    val coralSideChooser = SendableChooser<AutoTargeting.CoralSide>()
    val coralLevelChooser = SendableChooser<AutoTargeting.CoralLevel>()
    val reefSideSelector = SendableChooser<AutoScoringConstants.CoralScoringPositions>()
    init {
        for (action in DriverAction.entries) {
            actionChooser.addOption(action.name, action)
        }

        SmartDashboard.putData(actionChooser)

        for (side in AutoTargeting.CoralSide.entries) {
            coralSideChooser.addOption(side.name, side)
        }

        SmartDashboard.putData(coralSideChooser)

        for (side in AutoScoringConstants.CoralScoringPositions.entries) {
            reefSideSelector.addOption(side.name, side)
        }

        SmartDashboard.putData(reefSideSelector)


        for (level in AutoTargeting.CoralLevel.entries) {
            coralLevelChooser.addOption(level.name, level)
        }

        SmartDashboard.putData(coralLevelChooser)
    }
}