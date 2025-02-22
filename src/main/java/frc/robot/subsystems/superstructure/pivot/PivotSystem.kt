package frc.robot.subsystems.superstructure.pivot

import cshcyberhawks.lib.requests.AwaitRequest
import cshcyberhawks.lib.requests.Prerequisite
import cshcyberhawks.lib.requests.Request
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase

// By making a subsystem a Kotlin object, we ensure there is only ever one instance of it.
// It also reduces the need to have reference variables for the subsystems to be passed around.
class PivotSystem(private val io: PivotIO) : SubsystemBase() {
    fun getAngle(): Double = io.getAngle()

    fun atDesiredAngle() = io.atDesiredAngle()

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

    fun safeTravelUp() = Prerequisite.withCondition { getAngle() < 245.0 }
    fun safeTravelDown() = Prerequisite.withCondition { getAngle() > 180.0 }

    fun stowAngle() = setAngle(290.0)

    fun travelAngle() = setAngle(230.0)

    fun feederAngle() = setAngle(320.0)

    fun l4Angle() = setAngle(135.0)

    override fun periodic() {
        io.periodic()

        SmartDashboard.putNumber("Pivot Angle", getAngle())
    }
}
