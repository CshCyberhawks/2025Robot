package frc.robot.util.input

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.commands.AutoAlign
import frc.robot.constants.AutoScoringConstants
import frc.robot.subsystems.superstructure.Superstructure

enum class DriverAction(val superStructureCommand: Command, val alignCommand: Command = Commands.runOnce({})) {
//    Stow(Commands.runOnce({ Superstructure.stow() })),
    IntakeFeeder(Commands.runOnce({ Superstructure.intakeFeeder() })),
    ScoreL2(Commands.runOnce({ Superstructure.scoreL2() }), AutoAlign.coralReefAlign()),
    ScoreL3(Commands.runOnce({ Superstructure.scoreL3() }), AutoAlign.coralReefAlign()),
    ScoreL4(Commands.runOnce({ Superstructure.scoreL4() }), AutoAlign.coralReefAlign()),
    RemoveAlgaeLow(Commands.runOnce({ Superstructure.removeAlgaeLow() }), AutoAlign.coralReefAlign()),
    RemoveAlgaeHigh(Commands.runOnce({ Superstructure.removeAlgaeHigh() }), AutoAlign.coralReefAlign()),
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
            "Feeder" -> DriverAction.IntakeFeeder
            "L3" -> DriverAction.ScoreL3
            "L4" -> DriverAction.ScoreL4
            "Barge" -> DriverAction.ScoreBarge
            "Processor" -> DriverAction.ScoreProcessor
            "Algae High" -> DriverAction.RemoveAlgaeHigh
            "Algae Low" -> DriverAction.RemoveAlgaeLow
            else -> DriverAction.None
    }

    val coralSide
        get() = when (SmartDashboard.getString("ConsoleCoralSide", "")) {
            "Right" -> CoralSide.Right
            else -> CoralSide.Left
        }

    val reefPosition
        get() = when (SmartDashboard.getString("ConsoleCoralPosition", "")) {
            "A" -> AutoScoringConstants.ReefPositions.A
            "B" -> AutoScoringConstants.ReefPositions.B
            "C" -> AutoScoringConstants.ReefPositions.C
            "D" -> AutoScoringConstants.ReefPositions.D
            "E" -> AutoScoringConstants.ReefPositions.E
            "F" -> AutoScoringConstants.ReefPositions.F
            else -> AutoScoringConstants.ReefPositions.A
        }

//    val actionChooser = SendableChooser<DriverAction>()

//    val coralSideChooser = SendableChooser<AutoTargeting.CoralSide>()
//    val coralLevelChooser = SendableChooser<AutoTargeting.CoralLevel>()
//    val reefSideSelector = SendableChooser<AutoScoringConstants.CoralScoringPositions>()

    init {

    }
}