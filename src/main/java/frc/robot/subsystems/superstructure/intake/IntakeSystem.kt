package frc.robot.subsystems.superstructure.intake

import cshcyberhawks.lib.requests.Prerequisite
import cshcyberhawks.lib.requests.Request
import cshcyberhawks.lib.requests.SequentialRequest
import cshcyberhawks.lib.requests.WaitRequest
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.RobotState

// By making a subsystem a Kotlin object, we ensure there is only ever one instance of it.
// It also reduces the need to have reference variables for the subsystems to be passed around.
class IntakeSystem(private val io: IntakeIO) : SubsystemBase() {
    private fun setIntakeState(state: IntakeState) = Request.withAction { io.setIntakeState(state) }

    fun idle() = setIntakeState(IntakeState.Idle)

    fun coralHalfSpit() = SequentialRequest(
        setIntakeState(IntakeState.CoralHalfSpit),
        WaitRequest(IntakeConstants.coralHalfSpitTimeSeconds),
        setIntakeState(IntakeState.Idle)
    )

    fun coralIntake() = SequentialRequest(
        setIntakeState(IntakeState.CoralIntake),
    )

    fun coralScore() = SequentialRequest(
        setIntakeState(IntakeState.CoralScore),
        WaitRequest(IntakeConstants.coralScoreTimeoutSeconds),
        setIntakeState(IntakeState.Idle)
    )

    fun algaeIntake() = SequentialRequest(
        setIntakeState(IntakeState.AlgaeIntake),
    )

    fun algaeScore() = SequentialRequest(
        setIntakeState(IntakeState.AlgaeScore),
        WaitRequest(IntakeConstants.algaeScoreTimeoutSeconds).withPrerequisite(Prerequisite.withCondition { !io.hasAlgae() }),
        setIntakeState(IntakeState.Idle)
    )

    fun coralHolding() = setIntakeState(IntakeState.CoralHolding)

    fun algaeHolding() = setIntakeState(IntakeState.AlgaeHolding)

    override fun periodic() {
        io.periodic()

        RobotState.gamePieceState = if (io.hasCoral()) GamePieceState.Coral else if (io.hasAlgae()) GamePieceState
            .Algae else GamePieceState.Empty

        SmartDashboard.putBoolean("Has coral", io.hasCoral())
        SmartDashboard.putBoolean("Has algae", io.hasAlgae())
        SmartDashboard.putString("Game Piece State", RobotState.gamePieceState.name)
    }

    override fun simulationPeriodic() {}
}
