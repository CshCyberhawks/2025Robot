package frc.robot.subsystems.superstructure.elevator

import edu.wpi.first.wpilibj2.command.SubsystemBase
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.subsystems.superstructure.elevator.ElevatorIO

// By making a subsystem a Kotlin object, we ensure there is only ever one instance of it.
// It also reduces the need to have reference variables for the subsystems to be passed around.
class ElevatorSystem(private val io: ElevatorIO) : SubsystemBase() {
    fun getPosition(): Double = io.getPosition()

    fun atDesiredPosition() = io.atDesiredPosition()

    fun awaitDesiredPosition() = Commands.waitUntil { atDesiredPosition() }

    private fun setPosition(positionInches: Double): Command = runOnce {
        io.setPosition(positionInches)
    }

    override fun periodic() {
        io.periodic()
    }

    fun feederPosition() = setPosition(6.5)

    fun stowPosition() = setPosition(0.0)

    fun safeUpPosition() = setPosition(9.0)

    fun l4Position() = setPosition(30.0)
}
