package frc.robot.subsystems.swerve

import com.ctre.phoenix6.swerve.SwerveDrivetrain
import edu.wpi.first.math.Matrix
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Transform2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.numbers.N1
import edu.wpi.first.math.numbers.N3
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase

class SwerveIOSim() : SwerveIO() {
    private var robotPose = Pose2d()
    private var currentSpeed = ChassisSpeeds()

    private var lastLoopTime = 0.0

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
}