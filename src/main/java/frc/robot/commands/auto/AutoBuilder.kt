package frc.robot.commands.auto

import cshcyberhawks.lib.commands.ForCommand
import cshcyberhawks.lib.math.Timer
import edu.wpi.first.wpilibj2.command.*
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
            AutoCommands.leftFeederAlign()
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
            Commands.runOnce({ RobotState.actionConfirmed = true }),
            Commands.waitUntil { RobotState.gamePieceState == GamePieceState.Empty },
            Commands.waitSeconds(0.1),
            ParallelCommandGroup(
                AutoCommands.leftFeederAlign(),
                Commands.runOnce({ Superstructure.intakeFeeder() })
            ),
            Commands.waitUntil { RobotState.gamePieceState == GamePieceState.Coral }
        )
    }

    private fun createGenericAuto(
        targets: Array<CoralTarget>
    ): Command {
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
                    Commands.runOnce({
                        RobotState.actionConfirmed = true
                        println("Score $i: ${autoTimer.get()}")
                    }),
                    Commands.waitUntil { RobotState.gamePieceState == GamePieceState.Empty },
                    Commands.waitSeconds(0.1),
                    AutoCommands.safeReefExit(targets[i].position), // Backs up to exit the reef safely if necessary
                    ParallelDeadlineGroup(
                        Commands.waitUntil { RobotState.gamePieceState == GamePieceState.Coral },
                        AutoCommands.leftFeederAlign(),
                        Commands.runOnce({ Superstructure.intakeFeeder() })
                    ),
                    Commands.runOnce({ println("Intake $i: ${autoTimer.get()}") })
                )
            }
        )
    }

    fun threeL4Left() = createGenericAuto(
        arrayOf(
            CoralTarget(AutoScoringConstants.ReefPositions.C, CoralSide.Right),
            CoralTarget(AutoScoringConstants.ReefPositions.B, CoralSide.Left),
            CoralTarget(AutoScoringConstants.ReefPositions.B, CoralSide.Right)
        )
    )

    fun threeL4Right() = createGenericAuto(
        arrayOf(
            CoralTarget(AutoScoringConstants.ReefPositions.E, CoralSide.Left),
            CoralTarget(AutoScoringConstants.ReefPositions.F, CoralSide.Right),
            CoralTarget(AutoScoringConstants.ReefPositions.F, CoralSide.Left)
        )
    )
}