package frc.robot.subsystems.superstructure.climb.implementation

import frc.robot.subsystems.superstructure.climb.ClimbIO

class ClimbIOEmpty(): ClimbIO {
    override fun getAngle(): Double {
        return 0.0
    }

    override fun setAngle(angleDegrees: Double) {
    }

    override fun periodic() {
    }
}