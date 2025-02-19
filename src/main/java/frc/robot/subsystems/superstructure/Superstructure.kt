package frc.robot.subsystems.superstructure

import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.RobotConfiguration
import frc.robot.RobotType
import frc.robot.subsystems.superstructure.elevator.ElevatorSystem
import frc.robot.subsystems.superstructure.elevator.implementation.ElevatorIOEmpty
import frc.robot.subsystems.superstructure.elevator.implementation.ElevatorIOReal
import frc.robot.subsystems.superstructure.elevator.implementation.ElevatorIOSim
import frc.robot.subsystems.superstructure.intake.implementation.IntakeIOEmpty
import frc.robot.subsystems.superstructure.pivot.PivotSystem
import frc.robot.subsystems.superstructure.intake.IntakeSystem
import frc.robot.subsystems.superstructure.intake.implementation.IntakeIOReal
import frc.robot.subsystems.superstructure.pivot.implementation.PivotIOEmpty
import frc.robot.subsystems.superstructure.pivot.implementation.PivotIOReal
import frc.robot.subsystems.superstructure.pivot.implementation.PivotIOSim

class Superstructure() : SubsystemBase() {
    val pivotSystem = PivotSystem(
        when (RobotConfiguration.robotType) {
            RobotType.Real -> PivotIOReal()
            RobotType.Simulated -> PivotIOSim()
            RobotType.Empty -> PivotIOEmpty()
        }
    )
    val elevatorSystem = ElevatorSystem(
        when (RobotConfiguration.robotType) {
            RobotType.Real -> ElevatorIOReal()
            RobotType.Simulated -> ElevatorIOSim()
            RobotType.Empty -> ElevatorIOEmpty()
        }
    )
    val intakeSystem = IntakeSystem(
        when (RobotConfiguration.robotType) {
            RobotType.Real -> IntakeIOReal()
            RobotType.Simulated -> IntakeIOEmpty()
            RobotType.Empty -> IntakeIOEmpty()
        }
    )
}