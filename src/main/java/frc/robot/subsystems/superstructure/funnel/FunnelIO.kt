package frc.robot.subsystems.superstructure.funnel

interface FunnelIO {
    fun getAngle(): Double
    fun setAngle(angleDegrees: Double)
    fun periodic()
}