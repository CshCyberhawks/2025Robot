package frc.robot

import MiscCalculations
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType
import com.ctre.phoenix6.swerve.SwerveRequest
import edu.wpi.first.units.Units.*
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.FunctionalCommand
import edu.wpi.first.wpilibj2.command.button.CommandJoystick
import frc.robot.commands.TeleopDriveCommand
import frc.robot.constants.TunerConstants
import frc.robot.subsystems.CommandSwerveDrivetrain

object RobotContainer {
    public val MaxSpeedConst = TunerConstants.kSpeedAt12Volts.`in`(MetersPerSecond) // kSpeedAt12Volts desired top
    // speed
    public var ControlledSpeed = MaxSpeedConst
    public val MaxAngularRateConst =
        RotationsPerSecond.of(0.75).`in`(RadiansPerSecond) // 3/4 of a rotation per second max angular velocity

    public var ControlledAngularRate = MaxAngularRateConst

    /* Setting up bindings for necessary control of the swerve drive platform */
    public val drive: SwerveRequest.FieldCentric = SwerveRequest.FieldCentric()
        .withDeadband(MaxSpeedConst * 0.1).withRotationalDeadband(MaxAngularRateConst * 0.1) // Add a 10% deadband
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage) // Use open-loop control for drive motors

    public val robotRelative: SwerveRequest.RobotCentric = SwerveRequest.RobotCentric()
        .withDeadband(MaxSpeedConst * 0.1).withRotationalDeadband(MaxAngularRateConst * 0.1) // Add a 10% deadband
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage) // Use open-loop control for drive motors


    private val brake = SwerveRequest.SwerveDriveBrake()
    private val point = SwerveRequest.PointWheelsAt()

    private val logger: Telemetry = Telemetry(MaxSpeedConst)

//    private val xbox = CommandXboxController(0)

    val leftJoystick: CommandJoystick = CommandJoystick(0)
    val rightJoystick: CommandJoystick = CommandJoystick(1)

    val vision = VisionSystem()


    val drivetrain: CommandSwerveDrivetrain = TunerConstants.createDrivetrain()

    val teleopDriveCommand = TeleopDriveCommand()

    init {
        configureBindings()
    }

    private fun configureBindings() {

        drivetrain.setDefaultCommand(teleopDriveCommand)

        drivetrain.registerTelemetry(logger::telemeterize)
    }

    val autonomousCommand: Command
        get() = Commands.print("No autonomous command configured")
}
