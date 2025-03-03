package frc.robot.util.input

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.commands.TeleopAutoScore
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
}