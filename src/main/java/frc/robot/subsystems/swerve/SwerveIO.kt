package frc.robot.subsystems.swerve

import com.ctre.phoenix6.swerve.SwerveDrivetrain
import com.ctre.phoenix6.swerve.SwerveDrivetrain.SwerveDriveState
import com.ctre.phoenix6.swerve.SwerveRequest
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.wpilibj2.command.Command
import java.util.function.Consumer
import java.util.function.Supplier
import edu.wpi.first.math.numbers.N1
import edu.wpi.first.math.numbers.N3
import edu.wpi.first.math.Matrix
import edu.wpi.first.wpilibj2.command.SubsystemBase

open class SwerveIO() : SubsystemBase() {
    open fun seedFieldCentric() {}

    open fun getSwervePose(): Pose2d = Pose2d()

    open fun addVisionMeasurement(visionRobotPoseMeters: Pose2d, timestampSeconds: Double) {}
    open fun setVisionMeasurementStdDevs(visionMeasurementStdDevs: Matrix<N3, N1>) {}

    open fun applyDriveRequest(x: Double, y: Double, twistRadians: Double) {}
    open fun applyRobotRelativeDriveRequest(x: Double, y: Double, twistRadians: Double) {}
}