package frc.robot.subsystems.swerve

import com.ctre.phoenix6.swerve.SwerveDrivetrain
import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.config.RobotConfig
import com.pathplanner.lib.config.PIDConstants
import com.pathplanner.lib.controllers.PPHolonomicDriveController
import com.pathplanner.lib.commands.PathPlannerAuto
import edu.wpi.first.math.Matrix
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Transform2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.numbers.N1
import edu.wpi.first.math.numbers.N3
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.subsystems.swerve.SwerveIOBase

class SwerveIOSim() : SwerveIOBase() {
    private var robotPose = Pose2d()
    private var currentSpeed = ChassisSpeeds()

    private var lastLoopTime = 0.0

    private fun configurePathPlanner() {
        //        AutoBuilder.configureHolonomic(
        //            () -> this.getState().Pose, // Supplier of current robot pose
        //        this::seedFieldRelative,  // Consumer for seeding pose against auto
        //        this::getCurrentRobotChassisSpeeds,
        //        (speeds) -> this.setControl(AutoRequest.withSpeeds(speeds)), // Consumer of ChassisSpeeds to drive the robot
        //        new HolonomicPathFollowerConfig (new PIDConstants (5, 0, 0.0), // <- Translation
        //        new PIDConstants (1, 0, 0.1), // <- Rotation
        //        TunerConstants.kSpeedAt12VoltsMps,
        //        driveBaseRadius,
        //        new ReplanningConfig ()),
        //        () -> DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == DriverStation.Alliance.Red, // Change this if the path needs to be flipped on red vs blue
        //        this);
        AutoBuilder.configure(
            { getSwervePose() },
            { 
                robotPose = Pose2d()
                seedFieldCentric() },
            { currentSpeed },
            { speeds -> currentSpeed = speeds },
            PPHolonomicDriveController(
                PIDConstants(5.0, 0.0, 0.0),
                PIDConstants(1.0, 0.0, 0.1),
            ),
            RobotConfig.fromGUISettings(),
            {
                DriverStation.getAlliance().isPresent && DriverStation.getAlliance()
                    .get() == DriverStation.Alliance.Red
            }, this
        )
    }

    init {
        configurePathPlanner()
    }

    override fun seedFieldCentric() {
        robotPose = robotPose.rotateBy(robotPose.rotation.unaryMinus())
    }

    override fun getSwervePose(): Pose2d = robotPose

    override fun addVisionMeasurement(visionRobotPoseMeters: Pose2d, timestampSeconds: Double) {}

    override fun setVisionMeasurementStdDevs(visionMeasurementStdDevs: Matrix<N3, N1>) {}

    override fun applyDriveRequest(x: Double, y: Double, twistRadians: Double) {
        // TODO Actually implement this
        currentSpeed =
            ChassisSpeeds(x, y, twistRadians)
    }

    override fun applyRobotRelativeDriveRequest(x: Double, y: Double, twistRadians: Double) {
        currentSpeed =
            ChassisSpeeds(x, y, twistRadians)
    }

    override fun periodic() {
        val currentTime = Timer.getFPGATimestamp()
        val dtSeconds = currentTime - lastLoopTime

        robotPose = robotPose.plus(
            Transform2d(
                currentSpeed.vxMetersPerSecond * dtSeconds,
                currentSpeed.vyMetersPerSecond * dtSeconds,
                Rotation2d(currentSpeed.omegaRadiansPerSecond * dtSeconds)
            )
        )

        SmartDashboard.putNumber("Robot ChassisSpeed X", currentSpeed.vxMetersPerSecond)
        SmartDashboard.putNumber("Robot ChassisSpeed Y", currentSpeed.vyMetersPerSecond)

        lastLoopTime = currentTime
    }

    override fun getAutoPath(name: String): Command {
        return PathPlannerAuto(name)
    }
}