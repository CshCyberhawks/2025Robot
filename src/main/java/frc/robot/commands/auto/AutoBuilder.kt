package frc.robot.commands.auto

import cshcyberhawks.lib.commands.ForCommand
import cshcyberhawks.lib.math.Timer
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.WaitCommand
import frc.robot.Robot
import frc.robot.RobotState
import frc.robot.constants.AutoScoringConstants
import frc.robot.subsystems.superstructure.Superstructure
import frc.robot.subsystems.superstructure.intake.GamePieceState
import frc.robot.util.input.CoralSide

data class CoralTarget(val position: AutoScoringConstants.ReefPositions, val side: CoralSide)

object AutoBuilder {
    fun justDriveOneL4Left(): Command {
        return SequentialCommandGroup(
            AutoCommands.coralReefAlign(AutoScoringConstants.ReefPositions.B, CoralSide.Left),
            AutoCommands.feederAlign()
        )
    }

    fun oneL4Left(): Command {
        val autoTimer = Timer()

        return SequentialCommandGroup(
            Commands.runOnce({
                autoTimer.reset()
                autoTimer.start()
            }),
            ParallelCommandGroup(
                Commands.runOnce({ Superstructure.scoreL4() }),
                AutoCommands.coralReefAlign(AutoScoringConstants.ReefPositions.B, CoralSide.Left)
            ),
            WaitCommand(0.5),
//            Commands.runOnce({Superstructure.Auto.justScoreL4()}),
            Commands.runOnce({RobotState.actionConfirmed = true}),
            Commands.waitUntil { RobotState.gamePieceState == GamePieceState.Empty },
            Commands.waitSeconds(0.1),
            ParallelCommandGroup(
                AutoCommands.feederAlign(),
                Commands.runOnce({ Superstructure.intakeFeeder()})
            ),
            Commands.waitUntil { RobotState.gamePieceState == GamePieceState.Coral }
        )
    }

    fun twoL4Left(): Command {
        val targets = arrayOf(
            CoralTarget(AutoScoringConstants.ReefPositions.B, CoralSide.Left),
            CoralTarget(AutoScoringConstants.ReefPositions.B, CoralSide.Right)
        )
        val autoTimer = Timer()

        return SequentialCommandGroup(
            Commands.runOnce({
                autoTimer.reset()
                autoTimer.start()
            }),
            ForCommand(targets.size) { i ->
                SequentialCommandGroup(
                    ParallelCommandGroup(
                        Commands.runOnce({ Superstructure.scoreL4() }),
                        AutoCommands.coralReefAlign(targets[i].position, targets[i].side)
                    ),
                    WaitCommand(0.5),
                    Commands.runOnce({RobotState.actionConfirmed = true}),
                    Commands.waitUntil { RobotState.gamePieceState == GamePieceState.Empty },
                    Commands.waitSeconds(0.1),
                    ParallelDeadlineGroup(
                        Commands.waitUntil { RobotState.gamePieceState == GamePieceState.Coral }.andThen(Commands.runOnce({println("Has coral")})),
                        AutoCommands.feederAlign(),
                        Commands.runOnce({ Superstructure.intakeFeeder() })
                    ),
                    Commands.runOnce({ println("Done") })
                )
            }
        )
    }
}