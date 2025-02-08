package frc.robot.subsystems.intake

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase

// By making a subsystem a Kotlin object, we ensure there is only ever one instance of it.
// It also reduces the need to have reference variables for the subsystems to be passed around.
class IntakeSystem(private val io: IntakeIO) : SubsystemBase() {
    fun setCoralIntakeState(state: CoralIntakeState) = run { io.setCoralIntakeState(state) }
    fun setAlgaeIntakeState(state: AlgaeIntakeState) = run { io.setAlgaeIntakeState(state) }

    fun awaitCoralState(state: CoralState) = Commands.waitUntil { io.getCoralState() == state }
    fun awaitAlgaeState(state: AlgaeState) = Commands.waitUntil { io.getAlgaeState() == state }

    override fun periodic() {
        // This method will be called once per scheduler run
    }

    override fun simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}
