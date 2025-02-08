package frc.robot.subsystems.superstructure

import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.subsystems.superstructure.elevator.ElevatorSystem
import frc.robot.subsystems.superstructure.elevator.implementation.ElevatorIOEmpty
import frc.robot.subsystems.superstructure.intake.implementation.IntakeIOEmpty
import frc.robot.subsystems.superstructure.pivot.PivotSystem
import frc.robot.subsystems.superstructure.pivot.implementation.PivotIOEmpty
import frc.robot.subsystems.superstructure.intake.IntakeSystem
import frc.robot.subsystems.superstructure.wrist.WristSystem
import frc.robot.subsystems.superstructure.wrist.implementation.WristIOEmpty

class Superstructure() : SubsystemBase() {
    val pivotSystem = PivotSystem(PivotIOEmpty())
    val wristSystem = WristSystem(WristIOEmpty())
    val elevatorSystem = ElevatorSystem(ElevatorIOEmpty())
    val intakeSystem = IntakeSystem(IntakeIOEmpty())
}