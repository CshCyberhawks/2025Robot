package frc.robot.subsystems.elevator.implementation

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.controls.MotionMagicVoltage
import frc.robot.constants.ElevatorConstants
import frc.robot.subsystems.elevator.ElevatorIO

class ElevatorIOReal : ElevatorIO {

    var talonFX = TalonFX(ElevatorConstants.id, ElevatorConstants.canbus)
    var talonFXConfigurations = TalonFXConfiguration()

    init {
        talonFXConfigurations.Slot0.kG = ElevatorConstants.kG
        talonFXConfigurations.Slot0.kS = ElevatorConstants.kS
        talonFXConfigurations.Slot0.kV = ElevatorConstants.kV
        talonFXConfigurations.Slot0.kA = ElevatorConstants.kA
        talonFXConfigurations.Slot0.kP = ElevatorConstants.kP
        talonFXConfigurations.Slot0.kI = ElevatorConstants.kI
        talonFXConfigurations.Slot0.kD = ElevatorConstants.kD
        talonFXConfigurations.MotionMagic.MotionMagicCruiseVelocity = ElevatorConstants.targetVelocity
        talonFXConfigurations.MotionMagic.MotionMagicAcceleration = ElevatorConstants.targetAcceleration
        talonFXConfigurations.MotionMagic.MotionMagicJerk = ElevatorConstants.targetJerk
        talonFX.getConfigurator().apply(talonFXConfigurations)
    }

    fun getElevatorEncoder(): Double {
        TODO("Not yet implemented")
        return 0.0;
    }

    fun setElevatorMotor(rotations: Double) {
        TODO("Not yet implemented")
    }

    fun setMotorPosition(rotations: Double) {
        talonFX.setControl(MotionMagicVoltage(rotations))
    }

    override fun setPosition(positionMeters: Double) {
        TODO("Not yet implemented")
    }
}
