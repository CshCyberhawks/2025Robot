package frc.robot.subsystems.superstructure.elevator.implementation

import frc.robot.subsystems.superstructure.elevator.ElevatorIO

class ElevatorIOEmpty() : ElevatorIO {
    override fun getPosition(): Double = 0.0

    override fun setPosition(positionInches: Double) {}

    override fun periodic() {}
}