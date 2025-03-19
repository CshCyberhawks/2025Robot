package frc.robot.subsystems.superstructure.climb.implementation

import frc.robot.subsystems.superstructure.climb.ClimbIO

class ClimbIOEmpty(): ClimbIO {

    override var climbing = false;
    override fun getAngle(): Double {
        return 0.0
    }

    override fun angleDegrees(angleDegrees: Double) {
    }

    override fun periodic() {
    }
}