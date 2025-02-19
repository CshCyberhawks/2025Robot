package frc.robot.subsystems.superstructure.pivot.implementation

import edu.wpi.first.math.trajectory.TrapezoidProfile
import frc.robot.subsystems.superstructure.pivot.PivotConstants
import frc.robot.subsystems.superstructure.pivot.PivotIO
import frc.robot.util.Timer

class PivotIOSim : PivotIO {
    val trapProfile = TrapezoidProfile(PivotConstants.trapConstraints)
    var currentState = TrapezoidProfile.State(0.0, 0.0)
    var desiredState = TrapezoidProfile.State(0.0, 0.0)
    var lastTime = Timer.getFPGATimestamp()

    override fun getAngle(): Double = currentState.position

    override fun setAngle(angleDegrees: Double) {
        desiredState = TrapezoidProfile.State(angleDegrees, 0.0)
    }

    override fun periodic() {
        val currentTime = Timer.getFPGATimestamp()
        val dt = currentTime - lastTime

        currentState = trapProfile.calculate(dt, currentState, desiredState)

        lastTime = currentTime
    }
}