package frc.robot.subsystems.superstructure.pivot

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX

interface PivotIO {
//    fun getPivotEncoder(): Double

    //    fun setPivotMotor(x: Double)
    fun setAngle(angleDegrees: Double)
}
