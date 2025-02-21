package frc.robot.subsystems.superstructure.pivot.implementation

import edu.wpi.first.math.trajectory.TrapezoidProfile
import frc.robot.subsystems.superstructure.pivot.PivotConstants
import frc.robot.subsystems.superstructure.pivot.PivotIO
import cshcyberhawks.lib.math.Timer

class PivotIOSim : PivotIO {
    val trapProfile = TrapezoidProfile(PivotConstants.trapConstraints)
    var currentState = TrapezoidProfile.State(270.0, 0.0)
    var desiredState = TrapezoidProfile.State(270.0, 0.0)
    var lastTime = Timer.getFPGATimestamp()

    override fun getAngle(): Double = currentState.position

    override fun getDesiredAngle(): Double = desiredState.position

    override fun atDesiredAngle() = MiscCalculations.appxEqual(currentState.position, desiredState.position, 0.1)

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