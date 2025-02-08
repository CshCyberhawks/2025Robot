package frc.robot.subsystems.superstructure

import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.subsystems.elevator.ElevatorSystem
import frc.robot.subsystems.elevator.implementation.ElevatorIOEmpty
import frc.robot.subsystems.superstructure.IntakeSystem
import frc.robot.subsystems.superstructure.IntakeIOEmpty
import frc.robot.subsystems.pivot.PivotSystem
import frc.robot.subsystems.pivot.implementation.PivotIOEmpty
import frc.robot.subsystems.wrist.WristSystem
import frc.robot.subsystems.wrist.implementation.WristIOEmpty

class Superstructure() : SubsystemBase() {
    val pivotSystem = PivotSystem(PivotIOEmpty())
    val wristSystem = WristSystem(WristIOEmpty())
    val elevatorSystem = ElevatorSystem(ElevatorIOEmpty())
    val intakeSystem = IntakeSystem(IntakeIOEmpty())
}