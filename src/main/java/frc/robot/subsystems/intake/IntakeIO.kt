package frc.robot.subsystems.intake

interface IntakeIO {
    fun getIntakeEncoder():Double
    fun setIntakeMotor(x:Double)
}
