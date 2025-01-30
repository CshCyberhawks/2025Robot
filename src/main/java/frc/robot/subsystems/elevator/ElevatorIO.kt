package frc.robot.subsystems.elevator

import com.ctre.phoenix6.configs.TalonFXConfiguration

interface ElevatorIO {
    var talonFXConfigs:TalonFXConfiguration
    fun setVelocity(v:Double)
    fun setAcceleration(a:Double)
    fun setJerk(j:Double)
    fun getElevatorEncoder():Double
    fun setElevatorMotor(x:Double)
}
