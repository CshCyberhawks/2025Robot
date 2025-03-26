package frc.robot.subsystems.superstructure.pivot

import cshcyberhawks.lib.requests.AwaitRequest
import cshcyberhawks.lib.requests.Prerequisite
import cshcyberhawks.lib.requests.Request
import cshcyberhawks.lib.requests.SequentialRequest
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase

// By making a subsystem a Kotlin object, we ensure there is only ever one instance of it.
// It also reduces the need to have reference variables for the subsystems to be passed around.
class PivotSystem(private val io: PivotIO) : SubsystemBase() {
    fun getAngle(): Double = io.getAngle()

    fun atDesiredAngle() = io.atDesiredAngle()

    init {
        SmartDashboard.putNumber("L3 Angle", 125.0)
        SmartDashboard.putNumber("L4 Angle", 127.0)
    }

    private fun setAngle(angleDegrees: Double) = Request.withAction {
        io.setAngle(angleDegrees)
    }

    fun awaitDesiredAngle() = AwaitRequest { atDesiredAngle() }

    fun awaitAnglePast(angle: Double) =
        Commands.waitUntil {
            if (io.getDesiredAngle() - io.getAngle() > 0.0) {
                io.getAngle() > angle
            } else {
                io.getAngle() < angle
            }
        }

    fun prereqAtDesiredAngle() = Prerequisite.withCondition { atDesiredAngle() }

    fun safeTravelUp() = Prerequisite.withCondition { getAngle() < 245.0 }
    fun safeTravelDown() = Prerequisite.withCondition { getAngle() > 180.0 }
    fun safeToClimbDisable() = Prerequisite.withCondition { getAngle() < 20.0 }

    fun stowAngle() = setAngle(295.0)

    fun travelAngle() = setAngle(220.0)
    fun travelDownAngle() = setAngle(180.0)

    fun feederAngle() = setAngle(310.0)

    fun climbAngle() = SequentialRequest(
        setAngle(50.0)
//        Request.withAction { io.killPID() }.withPrerequisite(safeToClimbDisable())
    )

    fun oldClimbAngleWithPID() = setAngle(345.0)

    fun oldClimbAngle() = SequentialRequest(
        setAngle(360.0),
        Request.withAction { io.killPID() }.withPrerequisite(safeToClimbDisable())
    )

    fun climbStowAngle() = setAngle(325.0)

    fun highStowAngle() = setAngle(130.0)

    fun l1Angle() = setAngle(300.0)
    fun l2Angle() = setAngle(150.0)
//    fun l3Angle() = setAngle(SmartDashboard.getNumber("L3 Angle", 125.0))
    fun l3Angle() = setAngle(120.0)

    //    fun l4Angle() = setAngle(SmartDashboard.getNumber("L4 Angle", 133.0))
    fun l4Angle() = setAngle(131.0)

    fun algaeRemoveAngle() = setAngle(228.0)
    fun bargeAngle() = setAngle(130.0)

    fun processorAngle() = setAngle(288.0)

    override fun periodic() {
        io.periodic()

        SmartDashboard.putBoolean("Safe travel up", safeTravelUp().met())

        SmartDashboard.putNumber("Pivot Angle", getAngle())
    }
}
