package frc.robot.commands

import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.math.util.Units
import edu.wpi.first.wpilibj2.command.Command
import frc.robot.RobotConfiguration
import frc.robot.RobotContainer
import frc.robot.subsystems.swerve.SwerveConstants

class GoToPose(val pose: Pose2d): Command() {
    val xController = ProfiledPIDController(SwerveConstants.translationPIDConstants.kP, SwerveConstants.translationPIDConstants.kI, SwerveConstants.translationPIDConstants.kD, TrapezoidProfile.Constraints(3.0, 4.0))
    val yController = ProfiledPIDController(SwerveConstants.translationPIDConstants.kP, SwerveConstants.translationPIDConstants.kI, SwerveConstants.translationPIDConstants.kD, TrapezoidProfile.Constraints(3.0, 4.0))
    val rotationController = ProfiledPIDController(SwerveConstants.rotationPIDConstants.kP, SwerveConstants.rotationPIDConstants.kI, SwerveConstants.rotationPIDConstants.kD, TrapezoidProfile.Constraints(540.0, 720.0))

    override fun initialize() {
        val robotPose = RobotContainer.drivetrain.getSwervePose()
        val robotVel = RobotContainer.drivetrain.getSpeeds()

        xController.reset(robotPose.x, robotVel.vxMetersPerSecond)
        yController.reset(robotPose.y, robotVel.vyMetersPerSecond)
        rotationController.reset(robotPose.rotation.degrees, Units.radiansToDegrees(robotVel.omegaRadiansPerSecond))
    }
}