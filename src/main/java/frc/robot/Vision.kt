package frc.robot

import edu.wpi.first.math.VecBuilder
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.robot.LimelightHelpers
import frc.robot.RobotContainer
import kotlin.math.max

//import frc.robot.constants.FieldConstants
//import frc.robot.constants.LimelightConstants
//import frc.robot.util.AllianceFlip
//import frc.robot.util.visualiztion.Field2d

class VisionSystem {
    val max_distance_m = 6.0

    val limelightNames: Array<String> = when (RobotConfiguration.robotType) {
        RobotType.Real -> arrayOf("limelight-fleft", "limelight-tright", "limelight-bfront")
        else -> emptyArray()
    }

    fun updateOdometryFromDisabled() {
        var namesToSearch: Array<String>;


        namesToSearch = limelightNames


        for (llName in namesToSearch) {

            if (DriverStation.getAlliance().isEmpty) {
//                println("DS alliance is empty; skipping vision")
                return
            }


            var llMeasure: LimelightHelpers.PoseEstimate = LimelightHelpers.getBotPoseEstimate_wpiBlue(llName)

            if (llMeasure.pose.x != 0.0 && llMeasure.pose.y != 0.0) {
                val poseDifference =
                    llMeasure.pose.translation.getDistance(RobotContainer.drivetrain.getSwervePose().translation)

                val distanceToTag = llMeasure.avgTagDist

                if (distanceToTag < max_distance_m) {
                    var xyStds: Double
                    var degStds: Double

                    if (llMeasure.tagCount >= 2) {
                        xyStds = 0.1
                        degStds = 6.0
                    } else if (llMeasure.avgTagArea > 0.8 && poseDifference < 0.5) {
                        xyStds = .3
                        degStds = 8.0
                    } else if (llMeasure.avgTagArea > 0.1 && poseDifference < 0.3) {
                        xyStds = .5
                        degStds = 13.0
                    } else {
                        xyStds = .8
                        degStds = 25.0
                    }


                    RobotContainer.drivetrain.setVisionMeasurementStdDevs(
                        VecBuilder.fill(xyStds, xyStds, Math.toRadians(degStds))
                    )

                    RobotContainer.drivetrain.addVisionMeasurement(
                        llMeasure.pose,
                        llMeasure.timestampSeconds
                    )
                }
            }
        }
    }

    fun updateOdometry(tagCount: Int, poseDifferenceCheck: Boolean) {

        var namesToSearch: Array<String>;

        namesToSearch = limelightNames

        for (llName in namesToSearch) {
            if (DriverStation.getAlliance().isEmpty) {
//                println("DS alliance is empty; skipping vision")
                return
            }

            var llMeasure: LimelightHelpers.PoseEstimate =
                LimelightHelpers.getBotPoseEstimate_wpiBlue(llName)

            if (llMeasure.tagCount >= tagCount && llMeasure.pose.x != 0.0 && llMeasure.pose.y != 0.0) {
                val poseDifference =
                    llMeasure.pose.translation.getDistance(RobotContainer.drivetrain.getSwervePose().translation)
                if (!poseDifferenceCheck || poseDifference < max_distance_m) {
                    val distanceToTag = llMeasure.avgTagDist

                    if (distanceToTag < max_distance_m) {
                        var xyStds: Double
                        var degStds: Double

                        if (llName == "limelight-rightsi") {
                            if (llMeasure.tagCount >= 2) {
                                xyStds = 1.0
                                degStds = 12.0
                            } else if (llMeasure.avgTagArea > 0.8 && poseDifference < 0.5) {
                                xyStds = 2.0
                                degStds = 30.0
                            } else if (llMeasure.avgTagArea > 0.1 && poseDifference < 0.3) {
                                xyStds = 4.0
                                degStds = 50.0
                            } else {
                                xyStds = 6.0
                                degStds = 80.0
                            }
                        } else {
                            if (llMeasure.tagCount >= 2) {
                                xyStds = 0.5
                                degStds = 6.0
                            } else if (llMeasure.avgTagArea > 0.8 && poseDifference < 0.5) {
                                xyStds = 1.0
                                degStds = 12.0
                            } else if (llMeasure.avgTagArea > 0.1 && poseDifference < 0.3) {
                                xyStds = 2.0
                                degStds = 30.0
                            } else {
                                xyStds = 4.0
                                degStds = 50.0
                            }
                        }

                        RobotContainer.drivetrain.setVisionMeasurementStdDevs(
                            VecBuilder.fill(xyStds, xyStds, Math.toRadians(degStds))
                        )
//                        println("updating odometry with ll")
//                        println("Updating with LL ${llName}: X = " + llMeasure.pose.x + " Y = " + llMeasure.pose.y)

                        RobotContainer.drivetrain.addVisionMeasurement(
                            llMeasure.pose,
                            llMeasure.timestampSeconds
                        )
                    }
                }
            }
        }
    }
}
