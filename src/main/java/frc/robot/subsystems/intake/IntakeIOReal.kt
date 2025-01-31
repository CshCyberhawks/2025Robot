package frc.robot.subsystems.intake

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.controls.MotionMagicVoltage
import frc.robot.constants.IntakeConstants

class IntakeIOReal : IntakeIO {

    var talonFX = TalonFX(IntakeConstants.id, IntakeConstants.canbus)
    var talonFXConfigurations = TalonFXConfiguration()

    init {
        talonFXConfigurations.Slot0.kG = IntakeConstants.kG
        talonFXConfigurations.Slot0.kS = IntakeConstants.kS
        talonFXConfigurations.Slot0.kV = IntakeConstants.kV
        talonFXConfigurations.Slot0.kA = IntakeConstants.kA
        talonFXConfigurations.Slot0.kP = IntakeConstants.kP
        talonFXConfigurations.Slot0.kI = IntakeConstants.kI
        talonFXConfigurations.Slot0.kD = IntakeConstants.kD
        talonFXConfigurations.MotionMagic.MotionMagicCruiseVelocity = IntakeConstants.targetVelocity
        talonFXConfigurations.MotionMagic.MotionMagicAcceleration = IntakeConstants.targetAcceleration
        talonFXConfigurations.MotionMagic.MotionMagicJerk = IntakeConstants.targetJerk
        talonFX.getConfigurator().apply(talonFXConfigurations)
    }

    fun getIntakeEncoder(): Double {
        TODO("Not yet implemented")
        return 0.0;
    }

    fun setIntakeMotor(x: Double) {
        talonFX.setControl(MotionMagicVoltage(x))
    }

    override fun setCoralIntakeState(state: CoralIntakeState) {
        TODO("Not yet implemented")
    }

    override fun setAlgaeIntakeState(state: AlgaeIntakeState) {
        TODO("Not yet implemented")
    }
}
