package frc.robot.subsystems.superstructure.pivot.implementation

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.controls.MotionMagicVoltage
import frc.robot.subsystems.superstructure.pivot.PivotConstants
import frc.robot.subsystems.superstructure.pivot.PivotIO

class PivotIOReal : PivotIO {

    var talonFX = TalonFX(PivotConstants.id, PivotConstants.canbus)
    var talonFXConfigurations = TalonFXConfiguration()

    init {
        talonFXConfigurations.Slot0.kG = PivotConstants.kG
        talonFXConfigurations.Slot0.kS = PivotConstants.kS
        talonFXConfigurations.Slot0.kV = PivotConstants.kV
        talonFXConfigurations.Slot0.kA = PivotConstants.kA
        talonFXConfigurations.Slot0.kP = PivotConstants.kP
        talonFXConfigurations.Slot0.kI = PivotConstants.kI
        talonFXConfigurations.Slot0.kD = PivotConstants.kD
        talonFXConfigurations.MotionMagic.MotionMagicCruiseVelocity = PivotConstants.targetVelocity
        talonFXConfigurations.MotionMagic.MotionMagicAcceleration = PivotConstants.targetAcceleration
        talonFXConfigurations.MotionMagic.MotionMagicJerk = PivotConstants.targetJerk
        talonFX.getConfigurator().apply(talonFXConfigurations)
    }

    fun getPivotEncoder(): Double {
        TODO("Not yet implemented")
        return 0.0;
    }

    fun setPivotMotor(x: Double) {
        talonFX.setControl(MotionMagicVoltage(x))
    }

    override fun setAngle(angleDegrees: Double) {
        TODO("Not yet implemented")
    }
}
