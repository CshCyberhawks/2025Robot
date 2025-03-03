package frc.robot.subsystems.superstructure.elevator

import cshcyberhawks.lib.requests.AwaitRequest
import cshcyberhawks.lib.requests.Prerequisite
import cshcyberhawks.lib.requests.Request
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.subsystems.superstructure.elevator.ElevatorIO

// By making a subsystem a Kotlin object, we ensure there is only ever one instance of it.
// It also reduces the need to have reference variables for the subsystems to be passed around.
class ElevatorSystem(private val io: ElevatorIO) : SubsystemBase() {
    private val safeUpPosition = 9.0
    private val safeDownPosition = 4.0

    fun getPosition(): Double = io.getPosition()

    fun aboveSafeUpPosition() = getPosition() > safeUpPosition
    fun aboveSafeDownPosition() = getPosition() > safeDownPosition

    fun atDesiredPosition() = io.atDesiredPosition()

    fun awaitDesiredPosition() = AwaitRequest { atDesiredPosition() }

    fun belowSafeUpPosition() = Prerequisite.withCondition { getPosition() < safeUpPosition }

    private fun setPosition(positionInches: Double) = Request.withAction {
        io.setPosition(positionInches)
    }

    override fun periodic() {
        io.periodic()

        SmartDashboard.putNumber("Elevator Position", getPosition())
    }

    fun feederPosition() = setPosition(6.5)

    fun stowPosition() = setPosition(1.0)

    fun safeUpPosition() = setPosition(safeUpPosition)
    fun safeDownPosition() = setPosition(safeDownPosition)

    fun l3Position() = setPosition(4.0)
    fun l4Position() = setPosition(29.5) // Should be 30 eventually but not safe right now

    fun algaeRemoveLowPosition() = setPosition(1.0)
    fun algaeRemoveHighPosition() = setPosition(18.0)

    fun processorPosition() = setPosition(1.0)
    fun bargePosition() = setPosition(29.0) // Should be 30 eventually but not safe right now
}
