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
import frc.robot.util.input.CoralSide
import frc.robot.util.input.OperatorControls
import java.util.*
import kotlin.math.abs

class CoralReefAutoAlign() : Command() {
    val xController = ProfiledPIDController(SwerveConstants.translationPIDConstants.kP, SwerveConstants.translationPIDConstants.kI, SwerveConstants.translationPIDConstants.kD, TrapezoidProfile.Constraints(1.0, 1.0))
    val yController = ProfiledPIDController(SwerveConstants.translationPIDConstants.kP, SwerveConstants.translationPIDConstants.kI, SwerveConstants.translationPIDConstants.kD, TrapezoidProfile.Constraints(1.0, 1.0))
    val rotationController = ProfiledPIDController(
        SwerveConstants.rotationPIDConstants.kP, SwerveConstants.rotationPIDConstants.kI, SwerveConstants.rotationPIDConstants.kD, TrapezoidProfile.Constraints(
            Units.degreesToRadians(180.0), Units.degreesToRadians(180.0)))

    init {
        rotationController.enableContinuousInput(-180.0, 180.0)
    }

    var position = Pose2d()

    override fun initialize() {
        val reefSide = OperatorControls.reefPosition
//        val position = AutoScoringConstants.CoralScoringPositions.B.left

        position = when (OperatorControls.coralSide) {
            CoralSide.Left -> reefSide.left
            CoralSide.Right -> reefSide.right
        }

        val robotPose = RobotContainer.drivetrain.getSwervePose()
        val robotVel = ChassisSpeeds.fromRobotRelativeSpeeds(RobotContainer.drivetrain.getSpeeds(), robotPose.rotation)

        xController.reset(robotPose.x, robotVel.vxMetersPerSecond)
        yController.reset(robotPose.y, robotVel.vyMetersPerSecond)
        rotationController.reset(robotPose.rotation.radians, robotVel.omegaRadiansPerSecond)

        SmartDashboard.putString("Goal Position", position.toString())

        RobotContainer.currentDriveCommand = Optional.of(this);
        RobotState.autoDriving = true
    }

    override fun execute() {
        val currentPose = RobotContainer.drivetrain.getSwervePose()

        val xFeedback = xController.calculate(currentPose.x, position.x)
        val yFeedback = yController.calculate(currentPose.y, position.y)
        val rotFeedback = rotationController.calculate(currentPose.rotation.radians, position.rotation.radians)

        val xFeedforward = xController.setpoint.velocity
        val yFeedforward = yController.setpoint.velocity
        val rotFeedforward = rotationController.setpoint.velocity

        var xVel = xFeedforward + xFeedback
        var yVel = yFeedforward + yFeedback
        var rotVel = rotFeedforward + rotFeedback

        if (abs(currentPose.x - position.x) < SwerveConstants.positionDeadzone) {
            xVel = 0.0
        }

        if (abs(currentPose.y - position.y) < SwerveConstants.positionDeadzone) {
            yVel = 0.0
        }

        if (abs(currentPose.rotation.degrees - position.rotation.degrees) < SwerveConstants.rotationDeadzone) {
            rotVel = 0.0
        }

        RobotContainer.drivetrain.applyDriveRequest(xVel, yVel, rotVel)
    }

    override fun end(interrupted: Boolean) {
        RobotContainer.currentDriveCommand = Optional.empty()
        RobotState.autoDriving = false
    }
}