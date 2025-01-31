package frc.robot.subsystems.wrist

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX

interface WristIO {
    //    fun getWristEncoder(): Double
//    fun setWristMotor(x: Double)
    fun setAngle(angleDegrees: Double)
}
