package frc.robot.subsystems.intake

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX

interface IntakeIO {
//    fun getIntakeEncoder(): Double

    //    fun setIntakeMotor(x: Double)
    fun setCoralIntakeState(state: CoralIntakeState)
    fun setAlgaeIntakeState(state: AlgaeIntakeState)
}
