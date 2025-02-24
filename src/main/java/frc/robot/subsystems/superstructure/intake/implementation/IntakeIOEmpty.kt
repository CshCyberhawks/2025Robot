package frc.robot.subsystems.superstructure.intake.implementation

import frc.robot.subsystems.superstructure.intake.IntakeIO
import frc.robot.subsystems.superstructure.intake.IntakeState


class IntakeIOEmpty() : IntakeIO {
    override fun setIntakeState(state: IntakeState) {}
    override fun getIntakeState(): IntakeState {return IntakeState.Idle}
    override fun hasCoral():Boolean{return true}
    override fun hasAlgae():Boolean{return false}
    override fun periodic() {}
}