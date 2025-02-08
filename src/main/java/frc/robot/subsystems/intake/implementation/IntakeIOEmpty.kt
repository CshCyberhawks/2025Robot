package frc.robot.subsystems.intake.implementation

import frc.robot.subsystems.intake.AlgaeIntakeState
import frc.robot.subsystems.intake.CoralIntakeState
import frc.robot.subsystems.intake.IntakeIO

class IntakeIOEmpty() : IntakeIO {
    override fun setAlgaeIntakeState(state: AlgaeIntakeState) {}
    override fun setCoralIntakeState(state: CoralIntakeState) {}
}