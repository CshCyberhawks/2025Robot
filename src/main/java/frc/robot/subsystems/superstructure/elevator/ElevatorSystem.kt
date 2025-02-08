package frc.robot.subsystems.superstructure.elevator

import edu.wpi.first.wpilibj2.command.SubsystemBase
import edu.wpi.first.wpilibj2.command.Command
import frc.robot.subsystems.superstructure.elevator.ElevatorIO

// By making a subsystem a Kotlin object, we ensure there is only ever one instance of it.
// It also reduces the need to have reference variables for the subsystems to be passed around.
class ElevatorSystem(private val io: ElevatorIO) : SubsystemBase() {
    fun setPosition(positionMeters: Double): Command = runOnce {
        io.setPosition(positionMeters)
    }

    override fun periodic() {
        // This method will be called once per scheduler run
    }

    override fun simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}
