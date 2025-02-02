package frc.robot.subsystems.swerve

import edu.wpi.first.math.Matrix
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.numbers.N1
import edu.wpi.first.math.numbers.N3
import edu.wpi.first.wpilibj2.command.SubsystemBase

class SwerveSystem(private val io: SwerveIO) : SubsystemBase() {
    fun seedFieldCentric() {
        io.seedFieldCentric()
    }

    fun getSwervePose(): Pose2d = io.getSwervePose()

    fun addVisionMeasurement(visionRobotPoseMeters: Pose2d, timestampSeconds: Double) {
        io.addVisionMeasurement(visionRobotPoseMeters, timestampSeconds)
    }

    fun setVisionMeasurementStdDevs(visionMeasurementStdDevs: Matrix<N3, N1>) {
        io.setVisionMeasurementStdDevs(visionMeasurementStdDevs)
    }

    fun applyDriveRequest(x: Double, y: Double, twistRadians: Double) {
        io.applyDriveRequest(x, y, twistRadians)
    }

    fun applyRobotRelativeDriveRequest(x: Double, y: Double, twistRadians: Double) {
        io.applyRobotRelativeDriveRequest(x, y, twistRadians)
    }
}