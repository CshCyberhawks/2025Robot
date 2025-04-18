package frc.robot.util

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Pose3d
import edu.wpi.first.math.geometry.Rotation3d
import edu.wpi.first.math.geometry.Translation3d
import edu.wpi.first.math.util.Units
import edu.wpi.first.networktables.NetworkTableInstance
import frc.robot.RobotContainer
import frc.robot.subsystems.superstructure.Superstructure

object Visualizer {
    val robotPosePublisher = NetworkTableInstance.getDefault().getStructTopic("Robot Pose", Pose2d.struct).publish();
    val targetPosePublisher = NetworkTableInstance.getDefault().getStructTopic("Target Pose", Pose2d.struct).publish();

    val componentPosePublisher =
        NetworkTableInstance.getDefault().getStructArrayTopic("Component Poses", Pose3d.struct).publish()

    fun periodic() {
        robotPosePublisher.set(RobotContainer.drivetrain.getSwervePose())

        componentPosePublisher.set(
            arrayOf(
                Pose3d(
                    Translation3d(
                        0.0,
                        0.0,
                        Units.inchesToMeters(Superstructure.elevatorSystem.getPosition())
                    ),
                    Rotation3d()
                ),
                Pose3d(
                    Translation3d(
                        0.0,
                        0.0,
                        Units.inchesToMeters(39.750000 + Superstructure.elevatorSystem.getPosition())
                    ),
                    Rotation3d(0.0, Units.degreesToRadians(Superstructure.pivotSystem.getAngle()), 0.0)
                )
            )
        )
    }
}