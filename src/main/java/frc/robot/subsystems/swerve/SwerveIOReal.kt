package frc.robot.subsystems.swerve

import com.ctre.phoenix6.swerve.SwerveDrivetrain
import com.ctre.phoenix6.swerve.SwerveModule
import com.ctre.phoenix6.swerve.SwerveRequest
import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.commands.PathPlannerAuto
import com.pathplanner.lib.config.PIDConstants
import com.pathplanner.lib.config.RobotConfig
import com.pathplanner.lib.controllers.PPHolonomicDriveController
import edu.wpi.first.math.Matrix
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.numbers.N1
import edu.wpi.first.math.numbers.N3
import edu.wpi.first.units.Units.*
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.RobotContainer
import frc.robot.subsystems.swerve.TunerConstants
import frc.robot.subsystems.swerve.SwerveConstants
import frc.robot.subsystems.swerve.SwerveIOBase
import frc.robot.util.Telemetry
import java.util.function.Consumer
import java.util.function.Supplier

class SwerveIOReal() : SwerveIOBase() {
    private val drivetrain = TunerConstants.createDrivetrain()

    private val logger: Telemetry = Telemetry(SwerveConstants.MaxSpeedConst)


    init {
        drivetrain.registerTelemetry(logger::telemeterize)
        configurePathPlanner()
    }

//    override fun setDefaultCommand(command: Command) {
//        drivetrain.defaultCommand = command
//    }

    override fun resetPose(pose2d: Pose2d) {
        drivetrain.resetPose(pose2d)
    }
    private fun configurePathPlanner() {
//        drivetrain.get
        AutoBuilder.configure(
            { getSwervePose() },
            {
                drivetrain.resetPose(Pose2d())
                seedFieldCentric()
            },
            { drivetrain.state.Speeds },
            { speeds -> applyRobotRelativeDriveRequest(speeds.vxMetersPerSecond, speeds.vyMetersPerSecond, speeds.omegaRadiansPerSecond) },
            PPHolonomicDriveController(
                SwerveConstants.translationPIDConstants,
                SwerveConstants.rotationPIDConstants,
            ),
            RobotConfig.fromGUISettings(),
            {
                DriverStation.getAlliance().isPresent && DriverStation.getAlliance()
                    .get() == DriverStation.Alliance.Red
            }, this
        )
    }

    override fun getSpeeds() = drivetrain.state.Speeds

    override fun seedFieldCentric() {
        drivetrain.seedFieldCentric()
    }

    override fun getSwervePose(): Pose2d = drivetrain.state.Pose

    override fun addVisionMeasurement(visionRobotPoseMeters: Pose2d, timestampSeconds: Double) {
        drivetrain.addVisionMeasurement(visionRobotPoseMeters, timestampSeconds)
    }

    override fun setVisionMeasurementStdDevs(visionMeasurementStdDevs: Matrix<N3, N1>) {
        drivetrain.setVisionMeasurementStdDevs(visionMeasurementStdDevs)
    }

    /* Setting up bindings for necessary control of the swerve drive platform */
    private val fieldCentric: SwerveRequest.FieldCentric = SwerveRequest.FieldCentric()
//        .withDeadband(SwerveConstants.MaxSpeedConst * 0.1)
//        .withRotationalDeadband(SwerveConstants.MaxAngularRateConst * 0.1) // Add a 10% deadband
        .withDriveRequestType(SwerveModule.DriveRequestType.OpenLoopVoltage) // Use open-loop control for drive motors

    private val robotRelative: SwerveRequest.RobotCentric = SwerveRequest.RobotCentric()
//        .withDeadband(SwerveConstants.MaxSpeedConst * 0.1)
//        .withRotationalDeadband(SwerveConstants.MaxAngularRateConst * 0.1) // Add a 10% deadband
        .withDriveRequestType(SwerveModule.DriveRequestType.OpenLoopVoltage) // Use open-loop control for drive motors


    private val brake = SwerveRequest.SwerveDriveBrake()
    private val point = SwerveRequest.PointWheelsAt()



    override fun applyDriveRequest(x: Double, y: Double, twistRadians: Double) {
//        println("x: " + x);
        drivetrain.setControl(
            fieldCentric.withVelocityX(
                x
            ) //
                // Drive forward with
                // negative Y (forward)
                .withVelocityY(
                    y
                ) // Drive
                // left with negative X
                // (left)
                .withRotationalRate(
                    twistRadians
                )
        )
//        drivetrain.applyRequest {
//
//        }.execute()
    }

    override fun applyRobotRelativeDriveRequest(x: Double, y: Double, twistRadians: Double) {
        drivetrain.setControl(
            robotRelative.withVelocityX(
                x
            ) //
                // Drive forward with
                // negative Y (forward)
                .withVelocityY(
                    y
                ) // Drive
                // left with negative X
                // (left)
                .withRotationalRate(
                    twistRadians
                )
        )
    }
}