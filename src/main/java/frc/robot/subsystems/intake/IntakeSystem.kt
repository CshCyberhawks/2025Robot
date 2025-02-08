package frc.robot.subsystems.intake

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.RobotState

// By making a subsystem a Kotlin object, we ensure there is only ever one instance of it.
// It also reduces the need to have reference variables for the subsystems to be passed around.
class IntakeSystem(private val io: IntakeIO) : SubsystemBase() {
    private fun setCoralIntakeState(state: CoralIntakeState) = run { io.setCoralIntakeState(state) }
    private fun setAlgaeIntakeState(state: AlgaeIntakeState) = run { io.setAlgaeIntakeState(state) }

    private fun awaitCoralState(state: CoralState) = Commands.waitUntil { io.getCoralState() == state }
    private fun awaitAlgaeState(state: AlgaeState) = Commands.waitUntil { io.getAlgaeState() == state }

    fun coralIntake() = Commands.sequence(
        setCoralIntakeState(CoralIntakeState.Intaking),
        awaitCoralState(CoralState.Stored),
        setCoralIntakeState(CoralIntakeState.Idle)
    )

    fun coralScore() = Commands.sequence(
        setCoralIntakeState(CoralIntakeState.Scoring),
        awaitCoralState(CoralState.Empty),
        Commands.waitSeconds(IntakeConstants.coralScoreTimeoutSeconds),
        setCoralIntakeState(CoralIntakeState.Idle)
    )

    fun algaeIntake() = Commands.sequence(
        setAlgaeIntakeState(AlgaeIntakeState.Intaking),
        awaitAlgaeState(AlgaeState.Stored),
        setAlgaeIntakeState(AlgaeIntakeState.Holding)
    )

    fun algaeScore() = Commands.sequence(
        setAlgaeIntakeState(AlgaeIntakeState.Scoring),
        awaitAlgaeState(AlgaeState.Empty),
        Commands.waitSeconds(IntakeConstants.algaeScoreTimeoutSeconds),
        setAlgaeIntakeState(AlgaeIntakeState.Idle)
    )


    override fun periodic() {
        RobotState.coralState = io.getCoralState()
        RobotState.algaeState = io.getAlgaeState()
    }

    override fun simulationPeriodic() {
    }
}
