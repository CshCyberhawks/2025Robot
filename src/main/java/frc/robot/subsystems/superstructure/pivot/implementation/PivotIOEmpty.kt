package frc.robot.subsystems.superstructure.pivot.implementation

import frc.robot.subsystems.superstructure.pivot.PivotIO

class PivotIOEmpty() : PivotIO {
    override fun getAngle(): Double = 0.0
    override fun getDesiredAngle(): Double = 0.0

    override fun atDesiredAngle(): Boolean = false

    override fun setAngle(angleDegrees: Double) {}

    override fun periodic() {}
}