package frc.robot.subsystems.pivot

interface PivotIO {
    fun getPivotEncoder():Double
    fun setPivotMotor(x:Double)
}
