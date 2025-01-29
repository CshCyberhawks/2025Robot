package frc.robot.subsystems.elevator

interface PivotIO {
    fun getPivotEncoder():Double
    fun setPivotMotor(x:Double)
}
