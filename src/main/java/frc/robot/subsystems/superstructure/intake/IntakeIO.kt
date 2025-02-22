package frc.robot.subsystems.superstructure.intake

interface IntakeIO {
    fun setIntakeState(state: IntakeState)
    fun watchForIntake()

    fun periodic()
}
