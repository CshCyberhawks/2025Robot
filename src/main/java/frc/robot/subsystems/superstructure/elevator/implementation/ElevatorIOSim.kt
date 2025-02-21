package frc.robot.subsystems.superstructure.elevator.implementation

import MiscCalculations
import edu.wpi.first.math.trajectory.TrapezoidProfile
import frc.robot.subsystems.superstructure.elevator.ElevatorConstants
import frc.robot.subsystems.superstructure.elevator.ElevatorIO
import frc.robot.util.Timer

class ElevatorIOSim : ElevatorIO {
    val trapProfile = TrapezoidProfile(ElevatorConstants.trapConstraints)
    var currentState = TrapezoidProfile.State(0.0, 0.0)
    var desiredState = TrapezoidProfile.State(0.0, 0.0)
    var lastTime = Timer.getFPGATimestamp()

    override fun getPosition(): Double = currentState.position

    override fun atDesiredPosition() = MiscCalculations.appxEqual(currentState.position, desiredState.position, 0.05)

    override fun setPosition(positionInches: Double) {
        desiredState = TrapezoidProfile.State(positionInches, 0.0)
    }

    override fun periodic() {
        val currentTime = Timer.getFPGATimestamp()
        val dt = currentTime - lastTime

        currentState = trapProfile.calculate(dt, currentState, desiredState)

        lastTime = currentTime
    }
}