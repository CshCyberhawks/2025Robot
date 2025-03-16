package frc.robot.subsystems.superstructure.intake

interface IntakeIO {
    fun setIntakeState(state: IntakeState)

    fun hasCoral():Boolean
    fun hasAlgae():Boolean

    fun periodic()
}
