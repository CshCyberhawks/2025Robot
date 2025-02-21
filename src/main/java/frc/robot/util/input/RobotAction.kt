package frc.robot.util.input

import edu.wpi.first.wpilibj2.command.Command
import frc.robot.subsystems.superstructure.Superstructure

enum class RobotAction(val cmd: Command) {
    Stow(Superstructure.stow()),
    IntakeFeeder(Superstructure.intakeFeeder()),
    ScoreL4(Superstructure.scoreL4())
}