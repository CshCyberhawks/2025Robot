package frc.robot.subsystems.elevator

interface WristIO {
    fun getWristEncoder():Double
    fun setWristMotor(x:Double)
}
