package frc.robot.subsystems.superstructure.intake.implementation

import frc.robot.subsystems.superstructure.intake.AlgaeIntakeState
import frc.robot.subsystems.superstructure.intake.AlgaeState
import frc.robot.subsystems.superstructure.intake.CoralIntakeState
import frc.robot.subsystems.superstructure.intake.CoralState
import frc.robot.subsystems.superstructure.intake.IntakeIO

class IntakeIOEmpty() : IntakeIO {
    override fun setAlgaeIntakeState(state: AlgaeIntakeState) {}
    override fun setCoralIntakeState(state: CoralIntakeState) {}
    override fun getAlgaeState() = AlgaeState.Empty
    override fun getCoralIntakeCurrent(): Double {
        TODO("Not yet implemented")
    }

    override fun getCoralState(): CoralState {
        TODO("Not yet implemented")
    }
}