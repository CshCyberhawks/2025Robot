package frc.robot.subsystems.elevator

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX

interface ElevatorIO {
    var talonFX: TalonFX
    var talonFXConfigs: TalonFXConfiguration
    fun getElevatorEncoder(): Double
    fun setElevatorMotor(rotations: Double)
}
