package frc.robot.subsystems.pivot

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX

interface PivotIO {
    var talonFX: TalonFX
    var talonFXConfigs: TalonFXConfiguration
    fun getPivotEncoder(): Double
    fun setPivotMotor(x: Double)
}
