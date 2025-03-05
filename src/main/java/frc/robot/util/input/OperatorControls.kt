package frc.robot.util.input

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.AutoTargeting
import frc.robot.constants.AutoScoringConstants
import frc.robot.subsystems.superstructure.Superstructure

enum class DriverAction(val cmd: Command) {
    Stow(Commands.runOnce({ Superstructure.stow() })),
    IntakeFeeder(Commands.runOnce({ Superstructure.intakeFeeder() })),
    ScoreL2(Commands.runOnce({ Superstructure.scoreL2() })),
    ScoreL3(Commands.runOnce({ Superstructure.scoreL3() })),
    ScoreL4(Commands.runOnce({ Superstructure.scoreL4() })),
    RemoveAlgaeLow(Commands.runOnce({ Superstructure.removeAlgaeLow() })),
    RemoveAlgaeHigh(Commands.runOnce({ Superstructure.removeAlgaeHigh() })),
    ScoreBarge(Commands.runOnce({ Superstructure.scoreBarge() })),
    ScoreProcessor(Commands.runOnce({ Superstructure.scoreProcessor() })),
    None(Commands.runOnce({}))
}

enum class CoralSide {
    Left,
    Right
}

object OperatorControls {
    val action
        get() = when (SmartDashboard.getString("ConsoleDriverAction", "")) {
            "L3" -> DriverAction.ScoreL3
            "L4" -> DriverAction.ScoreL4
            "Barge" -> DriverAction.ScoreBarge
            "Processor" -> DriverAction.ScoreProcessor
            "Algae High" -> DriverAction.RemoveAlgaeHigh
            "Algae Low" -> DriverAction.RemoveAlgaeLow
            else -> DriverAction.None
    }

    val side
        get() = when (SmartDashboard.getString("ConsoleCoralSide", "")) {
            "Right" -> CoralSide.Right
            else -> CoralSide.Left
        }

    val position
        get() = when (SmartDashboard.getString("ConsoleCoralPosition", "")) {
            "A" -> AutoScoringConstants.CoralScoringPositions.A
            "B" -> AutoScoringConstants.CoralScoringPositions.B
            "C" -> AutoScoringConstants.CoralScoringPositions.C
            "D" -> AutoScoringConstants.CoralScoringPositions.D
            "E" -> AutoScoringConstants.CoralScoringPositions.E
            "F" -> AutoScoringConstants.CoralScoringPositions.F
            else -> AutoScoringConstants.CoralScoringPositions.A
        }

//    val actionChooser = SendableChooser<DriverAction>()

//    val coralSideChooser = SendableChooser<AutoTargeting.CoralSide>()
//    val coralLevelChooser = SendableChooser<AutoTargeting.CoralLevel>()
//    val reefSideSelector = SendableChooser<AutoScoringConstants.CoralScoringPositions>()

    init {

    }
}