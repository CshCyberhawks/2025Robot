package frc.robot.subsystems.superstructure.intake

interface IntakeIO {
//    fun getIntakeEncoder(): Double

    //    fun setIntakeMotor(x: Double)
    fun setCoralIntakeState(state: CoralIntakeState)
    fun setAlgaeIntakeState(state: AlgaeIntakeState)


    fun getCoralIntakeCurrent(): Double
    fun getAlgaeState(): AlgaeState
    fun getCoralState(): CoralState
}
