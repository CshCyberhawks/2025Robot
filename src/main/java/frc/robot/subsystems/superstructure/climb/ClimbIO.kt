package frc.robot.subsystems.superstructure.climb

interface ClimbIO {
    fun getAngle(): Double
    fun setAngle(angleDegrees: Double)
    fun periodic()
}