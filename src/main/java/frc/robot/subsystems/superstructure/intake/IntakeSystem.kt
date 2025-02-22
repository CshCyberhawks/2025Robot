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
    private fun setCoralIntakeState(state: CoralIntakeState) = Request.withAction { io.setCoralIntakeState(state) }
    private fun setAlgaeIntakeState(state: AlgaeIntakeState) = Request.withAction { io.setAlgaeIntakeState(state) }

    private fun coralState(state: CoralState) = Prerequisite.withCondition { io.getCoralState() == state }
    private fun algaeState(state: AlgaeState) = Prerequisite.withCondition { io.getAlgaeState() == state }

    fun coralIntake() = SequentialRequest(
        setCoralIntakeState(CoralIntakeState.Intaking),
        WaitRequest(IntakeConstants.coralIntakeTimeoutSeconds).withPrerequisite(coralState(CoralState.Stored)),
        setCoralIntakeState(CoralIntakeState.Idle)
    )

    fun coralScore() = SequentialRequest(
        setCoralIntakeState(CoralIntakeState.Scoring),
        WaitRequest(IntakeConstants.coralScoreTimeoutSeconds).withPrerequisite(coralState(CoralState.Empty)),
        setCoralIntakeState(CoralIntakeState.Idle)
    )

    fun algaeIntake() = SequentialRequest(
        setAlgaeIntakeState(AlgaeIntakeState.Intaking),
        setAlgaeIntakeState(AlgaeIntakeState.Holding).withPrerequisite(algaeState(AlgaeState.Stored))
    )

    fun algaeScore() = SequentialRequest(
        setAlgaeIntakeState(AlgaeIntakeState.Scoring),
        WaitRequest(IntakeConstants.algaeScoreTimeoutSeconds).withPrerequisite(algaeState(AlgaeState.Empty)),
        setAlgaeIntakeState(AlgaeIntakeState.Idle)
    )


    override fun periodic() {
        RobotState.coralState = io.getCoralState()
        RobotState.algaeState = io.getAlgaeState()
    }

    override fun simulationPeriodic() {}
}
