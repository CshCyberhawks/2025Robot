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
        targets: Array<CoralTarget>,
        rightFeeder: Boolean,
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
                    WaitCommand(0.55),
                    Commands.runOnce({
                        RobotState.actionConfirmed = true
                        println("Score $i: ${autoTimer.get()}")
                    }),
                    Commands.waitUntil { RobotState.gamePieceState == GamePieceState.Empty },
                    Commands.waitSeconds(0.1),
//                    AutoCommands.safeReefExit(targets[i].position), // Backs up to exit the reef safely if necessary
                    ParallelDeadlineGroup(
                        Commands.waitUntil { RobotState.gamePieceState == GamePieceState.Coral },
                        if (rightFeeder) AutoCommands.rightFeederAlign() else AutoCommands.leftFeederAlign(),
                        Commands.runOnce({ Superstructure.intakeFeeder() })
                    ),
                    Commands.waitSeconds(0.2),
                    Commands.runOnce({ println("Intake $i: ${autoTimer.get()}") })
                )
            }
        )
    }

    fun twoL4Left() = createGenericAuto(
        arrayOf(
            CoralTarget(AutoScoringConstants.ReefPositions.B, CoralSide.Left),
            CoralTarget(AutoScoringConstants.ReefPositions.B, CoralSide.Right)
        ),
        false
    )

    fun twoL4Right() = createGenericAuto(
        arrayOf(
            CoralTarget(AutoScoringConstants.ReefPositions.F, CoralSide.Right),
            CoralTarget(AutoScoringConstants.ReefPositions.F, CoralSide.Left)
        ),
        true
    )

    fun threeL4Left() = createGenericAuto(
        arrayOf(
            CoralTarget(AutoScoringConstants.ReefPositions.C, CoralSide.Right),
            CoralTarget(AutoScoringConstants.ReefPositions.B, CoralSide.Left),
            CoralTarget(AutoScoringConstants.ReefPositions.B, CoralSide.Right)
        ),
        false
    )

    fun threeL4Right() = createGenericAuto(
        arrayOf(
            CoralTarget(AutoScoringConstants.ReefPositions.E, CoralSide.Left),
            CoralTarget(AutoScoringConstants.ReefPositions.F, CoralSide.Right),
            CoralTarget(AutoScoringConstants.ReefPositions.F, CoralSide.Left)
        ),
        true
    )

    fun backL4Algae() = SequentialCommandGroup(
        ParallelCommandGroup(
            Commands.runOnce({ Superstructure.scoreL4() }),
            AutoCommands.coralReefAlign(AutoScoringConstants.ReefPositions.D, CoralSide.Left)
        ),
        WaitCommand(0.35),
        Commands.runOnce({
            RobotState.actionConfirmed = true
        }),
        Commands.waitUntil { RobotState.gamePieceState == GamePieceState.Empty },
        Commands.waitSeconds(0.1),
        AutoCommands.safeReefExit(AutoScoringConstants.ReefPositions.D),
        ParallelDeadlineGroup(
            Commands.waitUntil { RobotState.gamePieceState == GamePieceState.Algae },
            Commands.runOnce({ Superstructure.removeAlgaeLow() }),
            AutoCommands.algaeReefAlign(AutoScoringConstants.ReefPositions.D)
        ),
        ParallelCommandGroup(
            AutoCommands.safeReefExit(AutoScoringConstants.ReefPositions.D),
            Commands.runOnce({ Superstructure.algaeSpit() }),
        ),
        ParallelDeadlineGroup(
            Commands.waitUntil { RobotState.gamePieceState == GamePieceState.Algae },
            Commands.runOnce({ Superstructure.removeAlgaeHigh() }),
            AutoCommands.algaeReefAlign(AutoScoringConstants.ReefPositions.C)
        ),
        AutoCommands.safeReefExit(AutoScoringConstants.ReefPositions.C)
    )
}