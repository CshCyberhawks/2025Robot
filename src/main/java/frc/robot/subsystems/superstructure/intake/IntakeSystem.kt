package frc.robot.subsystems.superstructure.intake

import cshcyberhawks.lib.requests.Prerequisite
import cshcyberhawks.lib.requests.Request
import cshcyberhawks.lib.requests.SequentialRequest
import cshcyberhawks.lib.requests.WaitRequest
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.RobotState
import frc.robot.subsystems.superstructure.intake.*

// By making a subsystem a Kotlin object, we ensure there is only ever one instance of it.
// It also reduces the need to have reference variables for the subsystems to be passed around.
class IntakeSystem(private val io: IntakeIO) : SubsystemBase() {
    private fun setIntakeState(state: IntakeState) = Request.withAction { io.setIntakeState(state) }
    private fun watchForIntake() = Request.withAction { io.watchForIntake() }

    private fun setGamePieceState(state: GamePieceState) = Request.withAction { RobotState.gamePieceState = state }

    fun coralIntake() = SequentialRequest(
        setIntakeState(IntakeState.CoralIntake),
        WaitRequest(IntakeConstants.coralIntakeTimeoutSeconds),
        watchForIntake()
    )

    fun coralScore() = SequentialRequest(
        setIntakeState(IntakeState.CoralScore),
        WaitRequest(IntakeConstants.coralScoreTimeoutSeconds),
        setGamePieceState(GamePieceState.Empty),
        setIntakeState(IntakeState.Idle)
    )

    fun algaeIntake() = SequentialRequest(
        setIntakeState(IntakeState.AlgaeIntake),
        WaitRequest(IntakeConstants.algaeIntakeTimeoutSeconds),
        watchForIntake()
    )

    fun algaeScore() = SequentialRequest(
        setIntakeState(IntakeState.AlgaeIntake),
        WaitRequest(IntakeConstants.algaeScoreTimeoutSeconds),
        setGamePieceState(GamePieceState.Empty),
        setIntakeState(IntakeState.Idle)
    )


    override fun periodic() {
        io.periodic()
        when (io.getIntakeState()) {
            IntakeState.CoralIntake -> {
                if(io.hasCoral())
                    RobotState.gamePieceState = GamePieceState.Coral
            }

            IntakeState.AlgaeIntake -> {
                if (io.hasAlgae())
                    RobotState.gamePieceState = GamePieceState.Algae
            }

            else -> {}
        }

        setIntakeState(IntakeState.Idle)
    }

    override fun simulationPeriodic() {}
}
