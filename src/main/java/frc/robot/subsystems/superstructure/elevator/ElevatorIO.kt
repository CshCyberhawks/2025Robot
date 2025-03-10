package frc.robot.subsystems.superstructure.elevator

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.signals.NeutralModeValue

interface ElevatorIO {
    fun getPosition(): Double
    fun atDesiredPosition(): Boolean
    fun setPosition(positionInches: Double)

    fun periodic()
}
