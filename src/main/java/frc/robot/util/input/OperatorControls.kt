package frc.robot.util.input

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.robot.AutoTargeting

object OperatorControls {
    val actionChooser = SendableChooser<DriverAction>()

    val coralSideChooser = SendableChooser<AutoTargeting.CoralSide>()
    val coralLevelChooser = SendableChooser<AutoTargeting.CoralLevel>()
    init {
        for (action in DriverAction.entries) {
            actionChooser.addOption(action.name, action)
        }

        SmartDashboard.putData(actionChooser)

        for (side in AutoTargeting.CoralSide.entries) {
            coralSideChooser.addOption(side.name, side)
        }

        SmartDashboard.putData(coralSideChooser)

        for (level in AutoTargeting.CoralLevel.entries) {
            coralLevelChooser.addOption(level.name, level)
        }

        SmartDashboard.putData(coralLevelChooser)
    }
}