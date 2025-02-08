package frc.robot.subsystems.superstructure.elevator

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX

interface ElevatorIO {
//    fun getElevatorEncoder(): Double

    //    fun setElevatorMotor(rotations: Double)
    fun setPosition(positionMeters: Double)
}
