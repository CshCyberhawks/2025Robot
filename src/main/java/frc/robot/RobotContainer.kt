package frc.robot

import MiscCalculations
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType
import com.ctre.phoenix6.swerve.SwerveRequest
import edu.wpi.first.units.Units.*
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.FunctionalCommand
import edu.wpi.first.wpilibj2.command.button.CommandJoystick
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.commands.SimTeleopDriveCommand
import frc.robot.commands.TeleopDriveCommand
import frc.robot.subsystems.superstructure.IntakeIOEmpty
import frc.robot.subsystems.superstructure.IntakeIOReal
import frc.robot.subsystems.superstructure.IntakeSystem
import frc.robot.subsystems.swerve.TunerConstants
import frc.robot.subsystems.swerve.CommandSwerveDrivetrain
import frc.robot.subsystems.swerve.SwerveIOBase
import frc.robot.subsystems.swerve.SwerveIOReal
import frc.robot.subsystems.swerve.SwerveIOSim
import frc.robot.util.IO.ManualOperatorInput
import frc.robot.util.Telemetry

object RobotContainer {
    val leftJoystick: CommandJoystick = CommandJoystick(0)
    val rightJoystick: CommandJoystick = CommandJoystick(1)

    val xbox = CommandXboxController(2)

    val vision = VisionSystem()

    val drivetrain = when (RobotConfiguration.robotType) {
        RobotType.Real -> SwerveIOReal()
        RobotType.Simulated -> SwerveIOSim()
        RobotType.Empty -> SwerveIOBase()
    }

    val teleopDriveCommand = when (RobotConfiguration.robotType) {
        RobotType.Real -> TeleopDriveCommand()
        RobotType.Simulated -> SimTeleopDriveCommand()
        RobotType.Empty -> Commands.run({})
    }

    val intakeSystem = IntakeSystem(
        when (RobotConfiguration.robotType) {
            RobotType.Real -> IntakeIOReal()
            else -> IntakeIOEmpty()
        }
    )

    init {
        configureBindings()
    }

    private fun configureBindings() {
        drivetrain.setDefaultCommand(teleopDriveCommand)

// We might need this?
//        drivetrain.registerTelemetry(logger::telemeterize)


        when (RobotConfiguration.operatorType) {
            OperatorType.Manual -> ManualOperatorInput.configureBindings()
        }
    }

    val autonomousCommand: Command
        get() = Commands.print("No autonomous command configured")
}
