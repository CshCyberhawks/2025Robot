package frc.robot.subsystems.wrist

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX

interface WristIO {
    var talonFX: TalonFX
    var talonFXConfigs: TalonFXConfiguration
    fun getWristEncoder(): Double
    fun setWristMotor(x: Double)
}
