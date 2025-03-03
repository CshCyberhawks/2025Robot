package frc.robot.subsystems.superstructure.intake

import cshcyberhawks.lib.requests.*
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.RobotState
import frc.robot.subsystems.superstructure.intake.*

// By making a subsystem a Kotlin object, we ensure there is only ever one instance of it.
// It also reduces the need to have reference variables for the subsystems to be passed around.
class IntakeSystem(private val io: IntakeIO) : SubsystemBase() {
    private fun setIntakeState(state: IntakeState) = Request.withAction { io.setIntakeState(state) }
    private fun watchForIntake() = Request.withAction { io.watchForIntake() }

    fun idle() = setIntakeState(IntakeState.Idle)

    fun coralIntake() = SequentialRequest(
        setIntakeState(IntakeState.CoralIntake),
        WaitRequest(IntakeConstants.coralIntakeTimeoutSeconds),
        watchForIntake()
    )

    fun coralScore() = SequentialRequest(
        setIntakeState(IntakeState.CoralScore),
        WaitRequest(IntakeConstants.coralScoreTimeoutSeconds),
        setIntakeState(IntakeState.Idle)
    )

    fun algaeIntake() = SequentialRequest(
        setIntakeState(IntakeState.AlgaeIntake),
        watchForIntake()
    )

    fun algaeScore() = SequentialRequest(
        setIntakeState(IntakeState.AlgaeScore),
        WaitRequest(IntakeConstants.algaeScoreTimeoutSeconds).withPrerequisite(Prerequisite.withCondition { !io.hasAlgae() }),
        Request.withAction { println("Done Scoring") },
        setIntakeState(IntakeState.Idle)
    )


    override fun periodic() {
        io.periodic()

        RobotState.gamePieceState = if (io.hasCoral()) GamePieceState.Coral else if (io.hasAlgae()) GamePieceState
            .Algae else GamePieceState.Empty

        SmartDashboard.putBoolean("Has coral", io.hasCoral())
        SmartDashboard.putBoolean("Has algae", io.hasAlgae())
    }

    override fun simulationPeriodic() {}
}
