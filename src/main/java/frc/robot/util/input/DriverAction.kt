package frc.robot.util.input

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.subsystems.superstructure.Superstructure

enum class DriverAction(val cmd: Command) {
    Stow(Commands.runOnce({ Superstructure.stow() })),
    IntakeFeeder(Commands.runOnce({ Superstructure.intakeFeeder() })),
    ScoreL4(Commands.runOnce({ Superstructure.scoreL4() }))
}