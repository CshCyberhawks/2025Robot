package frc.robot.subsystems.superstructure.intake

interface IntakeIO {
//    fun getIntakeEncoder(): Double

    //    fun setIntakeMotor(x: Double)
    fun setCoralIntakeState(state: CoralIntakeState)
    fun setAlgaeIntakeState(state: AlgaeIntakeState)

    fun getCoralState(): CoralState
    fun getAlgaeState(): AlgaeState
}
