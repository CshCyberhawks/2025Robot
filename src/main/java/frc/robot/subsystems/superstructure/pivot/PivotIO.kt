package frc.robot.subsystems.superstructure.pivot

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX

interface PivotIO {
    fun getAngle(): Double
    fun getDesiredAngle(): Double
    fun atDesiredAngle(): Boolean
    fun setAngle(angleDegrees: Double)
    fun killPID()
    fun periodic()
}
