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
    private val safeUpPosition = 7.0
    private val safeDownPosition = 4.0

    fun getPosition(): Double = io.getPosition()

    fun aboveSafeUpPosition() = getPosition() > safeUpPosition
    fun aboveSafeDownPosition() = getPosition() > safeDownPosition

    fun atDesiredPosition() = io.atDesiredPosition()

    fun awaitDesiredPosition() = AwaitRequest { atDesiredPosition() }
    fun prereqAtDesiredPosition() = Prerequisite.withCondition { io.atDesiredPosition() }

    fun belowSafeUpPosition() = Prerequisite.withCondition { getPosition() < safeUpPosition }
    fun safeIntakePosition() = Prerequisite.withCondition { getPosition() < 7.0 }
    fun safeToFlipPivot() = Prerequisite.withCondition { getPosition() > 4.5 }
    fun closeL3() = Prerequisite.withCondition { getPosition() > 3.5 }
    fun closeL4() = Prerequisite.withCondition { getPosition() > 29.5 }

    private fun setPosition(positionInches: Double) = Request.withAction {
        io.setPosition(positionInches)
        println("requested position: $positionInches")
    }

    override fun periodic() {
        io.periodic()

        SmartDashboard.putNumber("Elevator Position", getPosition())
    }

    fun feederPosition() = setPosition(6.5)

    fun stowPosition() = setPosition(1.0)

    fun safeUpPosition() = setPosition(safeUpPosition)
    fun safeDownPosition() = setPosition(safeDownPosition)

    fun l1Position() = setPosition(0.0)
    fun l2Position() = setPosition(0.0)
    fun l3Position() = setPosition(4.0)
    fun l4Position() = setPosition(29.95) // Should be 30 eventually but not safe right now

    fun climbPosition() = setPosition(6.0)

    fun algaeRemoveLowPosition() = setPosition(1.0)
    fun algaeRemoveHighPosition() = setPosition(18.0)

    fun processorPosition() = setPosition(1.0)
    fun bargePosition() = setPosition(29.95) // Should be 30 eventually but not safe right now
}
