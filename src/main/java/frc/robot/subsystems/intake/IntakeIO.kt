package frc.robot.subsystems.intake

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX

interface IntakeIO {
    var talonFX: TalonFX
    var talonFXConfigs: TalonFXConfiguration
    fun getIntakeEncoder(): Double
    fun setIntakeMotor(x: Double)
}
