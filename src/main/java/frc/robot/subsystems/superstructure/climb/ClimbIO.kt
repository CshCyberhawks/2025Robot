package frc.robot.subsystems.superstructure.climb

interface ClimbIO {
    fun getAngle(): Double
    fun angleDegrees(angleDegrees: Double)
    fun periodic()

    var climbing: Boolean
    var disable: Boolean
}