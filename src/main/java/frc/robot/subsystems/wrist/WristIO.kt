package frc.robot.subsystems.wrist

interface WristIO {
    fun getWristEncoder():Double
    fun setWristMotor(x:Double)
}
