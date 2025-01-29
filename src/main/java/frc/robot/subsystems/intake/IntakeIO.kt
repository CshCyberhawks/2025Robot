package frc.robot.subsystems.elevator

interface IntakeIO {
    fun getIntakeEncoder():Double
    fun setIntakeMotor(x:Double)
}
