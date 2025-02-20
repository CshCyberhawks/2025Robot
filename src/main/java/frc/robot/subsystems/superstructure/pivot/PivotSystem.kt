package frc.robot.subsystems.superstructure.pivot

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase

// By making a subsystem a Kotlin object, we ensure there is only ever one instance of it.
// It also reduces the need to have reference variables for the subsystems to be passed around.
class PivotSystem(private val io: PivotIO) : SubsystemBase() {
    fun getAngle(): Double = io.getAngle()

    fun atDesiredAngle() = io.atDesiredAngle()

    private fun setAngle(angleDegrees: Double): Command = runOnce {
        io.setAngle(angleDegrees)
    }

    fun awaitDesiredAngle() = Commands.waitUntil { atDesiredAngle() }

    fun awaitAnglePast(angle: Double) =
        Commands.waitUntil {
            if (io.getDesiredAngle() - io.getAngle() > 0.0) {
                io.getAngle() > angle
            } else {
                io.getAngle() < angle
            }
        }

    fun stowAngle() = setAngle(290.0)

    fun feederAngle() = setAngle(320.0)

    fun l4Angle() = setAngle(135.0)

    override fun periodic() {
        io.periodic()
    }
}
