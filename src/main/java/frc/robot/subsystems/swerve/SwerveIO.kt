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

interface SwerveIO {
    fun seedFieldCentric()

    fun getSwervePose(): Pose2d

    fun addVisionMeasurement(visionRobotPoseMeters: Pose2d, timestampSeconds: Double)
    fun setVisionMeasurementStdDevs(visionMeasurementStdDevs: Matrix<N3, N1>)

    fun applyDriveRequest(x: Double, y: Double, twistRadians: Double)
    fun applyRobotRelativeDriveRequest(x: Double, y: Double, twistRadians: Double)
}