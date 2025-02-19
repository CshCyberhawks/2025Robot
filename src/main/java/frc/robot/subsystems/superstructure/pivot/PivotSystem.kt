package frc.robot.subsystems.superstructure.pivot

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase

// By making a subsystem a Kotlin object, we ensure there is only ever one instance of it.
// It also reduces the need to have reference variables for the subsystems to be passed around.
class PivotSystem(private val io: PivotIO) : SubsystemBase() {
    fun getAngle(): Double = io.getAngle()

    private fun setAngle(angleDegrees: Double): Command = runOnce {
        io.setAngle(angleDegrees)
    }

    fun stowAngle() = setAngle(270.0)

    fun feederAngle() = setAngle(320.0)

    override fun periodic() {
        io.periodic()
    }
}
