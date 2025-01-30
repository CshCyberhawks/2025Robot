package frc.robot.subsystems.elevator

import com.ctre.phoenix6.configs.TalonFXConfiguration
import frc.robot.constants.TalonFXConstants

class ElevatorIOReal : PivotIO {

    var TalonFXConfigurations=TalonFXConfiguration()
    init{
        TalonFXConfigurations.Slot0.kG=TalonFXConstants.kGElevator
        TalonFXConfigurations.Slot0.kS=TalonFXConstants.kSElevator
        TalonFXConfigurations.Slot0.kV=TalonFXConstants.kVElevator
        TalonFXConfigurations.Slot0.kA=TalonFXConstants.kAElevator
        TalonFXConfigurations.Slot0.kP=TalonFXConstants.kPElevator
        TalonFXConfigurations.Slot0.kI=TalonFXConstants.kIElevator
        TalonFXConfigurations.Slot0.kD=TalonFXConstants.kDElevator
    }

    override fun setVelocity(v:Double){ TalonFXConfigurations.MotionMagic.MotionMagicCruiseVelocity=v }
    override fun setAcceleration(a:Double){ TalonFXConfigurations.MotionMagic.MotionMagicAcceleration=a }
    override fun setJerk(j:Double){ TalonFXConfigurations.MotionMagic.MotionMagicJerk=j }

    override fun getElevatorEncoder(): Double {
        TODO("Not yet implemented")
        return 0.0;
    }

    override fun setElevatorMotor(x: Double) {
        TODO("Not yet implemented")
    }
}
