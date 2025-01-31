package frc.robot.subsystems.wrist

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.controls.MotionMagicVoltage
import frc.robot.constants.WristConstants

class WristIOReal : WristIO {

    var talonFX = TalonFX(WristConstants.id, WristConstants.canbus)
    var talonFXConfigurations = TalonFXConfiguration()

    init {
        talonFXConfigurations.Slot0.kG = WristConstants.kG
        talonFXConfigurations.Slot0.kS = WristConstants.kS
        talonFXConfigurations.Slot0.kV = WristConstants.kV
        talonFXConfigurations.Slot0.kA = WristConstants.kA
        talonFXConfigurations.Slot0.kP = WristConstants.kP
        talonFXConfigurations.Slot0.kI = WristConstants.kI
        talonFXConfigurations.Slot0.kD = WristConstants.kD
        talonFXConfigurations.MotionMagic.MotionMagicCruiseVelocity = WristConstants.targetVelocity
        talonFXConfigurations.MotionMagic.MotionMagicAcceleration = WristConstants.targetAcceleration
        talonFXConfigurations.MotionMagic.MotionMagicJerk = WristConstants.targetJerk
        talonFX.getConfigurator().apply(talonFXConfigurations)
    }

    override fun getWristEncoder(): Double {
        TODO("Not yet implemented")
        return 0.0;
    }

    override fun setWristMotor(x: Double) {
        talonFX.setControl(MotionMagicVoltage(rotations))
    }
}
