package frc.robot.commands

import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.math.util.Units
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import frc.robot.RobotContainer
import frc.robot.RobotState
import frc.robot.subsystems.swerve.SwerveConstants
import java.util.*
import kotlin.math.abs

class GoToPose(
    val targetPoseGetter: () -> Pose2d, val endCondition: () -> Boolean = {
        val currentPose = RobotContainer.drivetrain.getSwervePose()
        val targetPose = targetPoseGetter()

        (abs(currentPose.x - targetPose.x) < SwerveConstants.positionDeadzone && abs(currentPose.y - targetPose.y) < SwerveConstants.positionDeadzone && abs(
            currentPose.rotation.degrees - targetPose.rotation.degrees
        ) < SwerveConstants.rotationDeadzone)
    }
) : Command() {
    val xController = ProfiledPIDController(
        SwerveConstants.translationPIDConstants.kP,
        SwerveConstants.translationPIDConstants.kI,
        SwerveConstants.translationPIDConstants.kD,
        TrapezoidProfile.Constraints(1.0, 1.0)
    )
    val yController = ProfiledPIDController(
        SwerveConstants.translationPIDConstants.kP,
        SwerveConstants.translationPIDConstants.kI,
        SwerveConstants.translationPIDConstants.kD,
        TrapezoidProfile.Constraints(1.0, 1.0)
    )
    val rotationController = ProfiledPIDController(
        SwerveConstants.rotationPIDConstants.kP,
        SwerveConstants.rotationPIDConstants.kI,
        SwerveConstants.rotationPIDConstants.kD,
        TrapezoidProfile.Constraints(Units.degreesToRadians(180.0), Units.degreesToRadians(180.0))
    )
    var targetPose = targetPoseGetter()


    init {
        rotationController.enableContinuousInput(-Math.PI, Math.PI)
    }

    override fun initialize() {
        targetPose = targetPoseGetter()

        val robotPose = RobotContainer.drivetrain.getSwervePose()
        val robotVel = ChassisSpeeds.fromRobotRelativeSpeeds(RobotContainer.drivetrain.getSpeeds(), robotPose.rotation)

        xController.reset(robotPose.x, robotVel.vxMetersPerSecond)
        yController.reset(robotPose.y, robotVel.vyMetersPerSecond)
        rotationController.reset(robotPose.rotation.radians, robotVel.omegaRadiansPerSecond)

        SmartDashboard.putString("Goal Position", targetPose.toString())

        RobotContainer.currentDriveCommand = Optional.of(this);
        RobotState.autoDriving = true
    }

    override fun execute() {
        val currentPose = RobotContainer.drivetrain.getSwervePose()

        val xFeedback = xController.calculate(currentPose.x, targetPose.x)
        val yFeedback = yController.calculate(currentPose.y, targetPose.y)
        val rotFeedback = rotationController.calculate(currentPose.rotation.radians, targetPose.rotation.radians)

        val xFeedforward = xController.setpoint.velocity
        val yFeedforward = yController.setpoint.velocity
        val rotFeedforward = rotationController.setpoint.velocity

        var xVel = xFeedforward + xFeedback
        var yVel = yFeedforward + yFeedback
        var rotVel = rotFeedforward + rotFeedback

        if (abs(currentPose.x - targetPose.x) < SwerveConstants.positionDeadzone) {
            xVel = 0.0
        }

        if (abs(currentPose.y - targetPose.y) < SwerveConstants.positionDeadzone) {
            yVel = 0.0
        }

        if (abs(currentPose.rotation.degrees - targetPose.rotation.degrees) < SwerveConstants.rotationDeadzone) {
            rotVel = 0.0
        }

        RobotContainer.drivetrain.applyDriveRequest(xVel, yVel, rotVel)
    }

    override fun isFinished(): Boolean = endCondition()

    override fun end(interrupted: Boolean) {
        println("drive command and then ending")
        RobotState.autoDriving = false
        RobotContainer.currentDriveCommand = Optional.empty()
    }
}