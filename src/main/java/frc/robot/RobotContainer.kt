package frc.robot

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.auto.NamedCommands
import com.pathplanner.lib.commands.PathPlannerAuto
import com.pathplanner.lib.path.PathPlannerPath
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.button.CommandJoystick
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.commands.AutoCommands
import frc.robot.commands.SimTeleopDriveCommand
import frc.robot.commands.TeleopDriveCommand
import frc.robot.constants.AutoScoringConstants
import frc.robot.subsystems.swerve.SwerveIOBase
import frc.robot.subsystems.swerve.SwerveIOReal
import frc.robot.subsystems.swerve.SwerveIOSim
import frc.robot.util.IO.ManualOperatorInput
import frc.robot.util.VisionSystem
import frc.robot.util.input.CoralSide
import frc.robot.util.input.ManualDriverInput
import frc.robot.util.input.TestingOperatorInput
import java.util.*


object RobotContainer {
    val leftJoystick: CommandJoystick = CommandJoystick(0)
    val rightJoystick: CommandJoystick = CommandJoystick(1)

    val xbox = CommandXboxController(2)

    // Just for testing
    val keyboard = GenericHID(4)

    val vision = VisionSystem()

    val drivetrain = when (RobotConfiguration.robotType) {
        RobotType.Real -> SwerveIOReal()
        RobotType.Simulated -> SwerveIOSim()
        RobotType.Empty -> SwerveIOBase()
    }

    val teleopDriveCommand = when (RobotConfiguration.robotType) {
        RobotType.Real -> TeleopDriveCommand()
//        RobotType.Real -> Commands.run({})
        RobotType.Simulated -> SimTeleopDriveCommand()
        RobotType.Empty -> Commands.run({})
    }

    var currentDriveCommand: Optional<Command> = Optional.empty();

    val autoCommand: Command

//    val autoChooser = AutoBuilder.buildAutoChooser()

    init {
        configureBindings()

        for (pathplannerCommand in AutoCommands.entries) {
            NamedCommands.registerCommand(pathplannerCommand.name, pathplannerCommand.cmd)
        }

        autoCommand = frc.robot.commands.auto.AutoBuilder.twoL4Left()
//        autoCommand = getAutonomousCommand()
//        SmartDashboard.putData("Auto Chooser", autoChooser)
    }

    fun getAutonomousCommand(): Command {
        try {
            // Load the path you want to follow using its name in the GUI
//            val path = PathPlannerPath.fromPathFile("3 L4 Left")

            // Create a path following command using AutoBuilder. This will also trigger event markers.
//            return AutoBuilder.followPath(path)
            return PathPlannerAuto("3 L4 Left");
        } catch (e: Exception) {
            DriverStation.reportError("Big oops: " + e.message, e.stackTrace)
//            throw Exception("Big oops: " + e.message, e)
            return Commands.none()
        }
    }

    private fun configureBindings() {
//        teleopDriveCommand.addRequirements(drivetrain)
        drivetrain.setDefaultCommand(teleopDriveCommand)

// We might need this?
//        drivetrain.registerTelemetry(logger::telemeterize)


        when (RobotConfiguration.operatorType) {
            OperatorType.Manual -> ManualOperatorInput.configureBindings()
            OperatorType.Testing -> TestingOperatorInput.configureBindings()
        }

        ManualDriverInput.configureBindings()

        ManualOperatorInput.configureBindings()
    }

//    val autonomousCommand: Command
//        get() = autoChooser.selected
}
