package frc.robot.subsystems.superstructure.funnel.implementation

import frc.robot.subsystems.superstructure.funnel.FunnelIO

class FunnelIOEmpty(): FunnelIO {
    override fun getAngle(): Double {
        return 0.0
    }

    override fun setAngle(angleDegrees: Double) {
    }

    override fun periodic() {
    }
}