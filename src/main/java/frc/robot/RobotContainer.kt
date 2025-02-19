package frc.robot

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.button.CommandJoystick
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.commands.SimTeleopDriveCommand
import frc.robot.commands.TeleopDriveCommand
import frc.robot.subsystems.superstructure.Superstructure
import frc.robot.subsystems.superstructure.intake.implementation.IntakeIOEmpty
import frc.robot.subsystems.superstructure.intake.implementation.IntakeIOReal
import frc.robot.subsystems.superstructure.intake.IntakeSystem
import frc.robot.subsystems.swerve.SwerveIOBase
import frc.robot.subsystems.swerve.SwerveIOReal
import frc.robot.subsystems.swerve.SwerveIOSim
import frc.robot.util.IO.ManualOperatorInput
import frc.robot.util.VisionSystem

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

    val superstructure = Superstructure()

    init {
        configureBindings()
    }

    private fun configureBindings() {
        teleopDriveCommand.addRequirements(drivetrain)
        drivetrain.setDefaultCommand(teleopDriveCommand)

// We might need this?
//        drivetrain.registerTelemetry(logger::telemeterize)


        when (RobotConfiguration.operatorType) {
            OperatorType.Manual -> ManualOperatorInput.configureBindings()
            OperatorType.Testing -> TODO()
        }
    }

    val autonomousCommand: Command
        get() = Commands.print("No autonomous command configured")
}
