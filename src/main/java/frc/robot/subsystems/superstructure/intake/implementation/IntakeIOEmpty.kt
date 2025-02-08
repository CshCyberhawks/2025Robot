package frc.robot.subsystems.superstructure

import frc.robot.subsystems.intake.*

class IntakeIOEmpty() : IntakeIO {
    override fun setAlgaeIntakeState(state: AlgaeIntakeState) {}
    override fun setCoralIntakeState(state: CoralIntakeState) {}
    override fun getCoralState() = CoralState.Empty
    override fun getAlgaeState() = AlgaeState.Empty
}