package frc.robot.subsystems.elevator

interface ElevatorIO {
    fun getElevatorEncoder():Double
    fun setElevatorMotor(x:Double)
}
