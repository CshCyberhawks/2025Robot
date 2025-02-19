package frc.robot.subsystems.superstructure.elevator

import edu.wpi.first.wpilibj2.command.SubsystemBase
import edu.wpi.first.wpilibj2.command.Command
import frc.robot.subsystems.superstructure.elevator.ElevatorIO

// By making a subsystem a Kotlin object, we ensure there is only ever one instance of it.
// It also reduces the need to have reference variables for the subsystems to be passed around.
class ElevatorSystem(private val io: ElevatorIO) : SubsystemBase() {
    fun getPosition(): Double = io.getPosition()

    private fun setPosition(positionInches: Double): Command = runOnce {
        io.setPosition(positionInches)
    }

    override fun periodic() {
        io.periodic()
    }

    fun feederPosition() = setPosition(6.5)

    fun stowPosition() = setPosition(0.0)
}
