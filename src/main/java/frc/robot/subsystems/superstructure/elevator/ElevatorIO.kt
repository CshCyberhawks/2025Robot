package frc.robot.subsystems.superstructure.elevator

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX

interface ElevatorIO {
    fun getPosition(): Double
    fun setPosition(positionInches: Double)

    fun periodic()
}
