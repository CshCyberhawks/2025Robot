package frc.robot.util

import com.ctre.phoenix6.Utils
import edu.wpi.first.math.VecBuilder
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.robot.*

//import frc.robot.constants.FieldConstants
//import frc.robot.constants.LimelightConstants
//import frc.robot.util.AllianceFlip
//import frc.robot.util.visualiztion.Field2d

class VisionSystem {
    val max_distance_m = 6.0

    val limelightNames: Array<String> = when (RobotConfiguration.robotType) {
//        RobotType.Real -> arrayOf("limelight-tright", "limelight-btfront")
        RobotType.Real -> arrayOf("limelight-tleft", "limelight-tright", "limelight-btfront")

        else -> emptyArray()
    }

    fun updateOdometryFromDisabled() {
        var namesToSearch: Array<String>;

        var fullReset = SmartDashboard.getBoolean("full reset with vision", false)

        namesToSearch = limelightNames

         val headingDeg: Double = RobotContainer.drivetrain.getSwervePose().rotation.degrees

        for (llName in namesToSearch) {
            if (DriverStation.getAlliance().isEmpty) {
                println("DS alliance is empty; skipping vision")
                return
            }

//            LimelightHelpers.SetRobotOrientation(llName, headingDeg, 0.0, 0.0, 0.0, 0.0, 0.0);

            if (LimelightHelpers.getBotPoseEstimate_wpiBlue(llName) == null) {
//                println(llName + " is null")
                SmartDashboard.putBoolean("ll " + llName + " is valid", false)
                return;
            }

            SmartDashboard.putBoolean("ll " + llName + " is valid", true)

            var llMeasure: LimelightHelpers.PoseEstimate = LimelightHelpers.getBotPoseEstimate_wpiBlue(llName)

//            println("ll measure x: " + llMeasure.pose.x)
//            println("ll measure y: " + llMeasure.pose.y)

            if (llMeasure.pose.x != 0.0 && llMeasure.pose.y != 0.0) {

                val poseDifference =
                    llMeasure.pose.translation.getDistance(RobotContainer.drivetrain.getSwervePose().translation)

                val distanceToTag = llMeasure.avgTagDist
//                SmartDashboard.putNumber("distance to tag", distanceToTag)

//                SmartDashboard.putNumber("ll update pose x: ", llMeasure.pose.x)
//                SmartDashboard.putNumber("ll update pose y: ", llMeasure.pose.y)
//                SmartDashboard.putNumber("ll timestampt: ", llMeasure.timestampSeconds)

                if (fullReset) {
                    RobotContainer.drivetrain.resetPose(llMeasure.pose)
                    return;
                }

                if (distanceToTag < max_distance_m) {
//                    println("doing the thingy")
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
                        Utils.fpgaToCurrentTime(llMeasure.timestampSeconds)
                    )
                }
            }
        }
    }

    fun updateOdometry(tagCount: Int, poseDifferenceCheck: Boolean) {

        var namesToSearch: Array<String>;


        namesToSearch = limelightNames

        val headingDeg: Double = RobotContainer.drivetrain.getSwervePose().rotation.degrees

        for (llName in namesToSearch) {
            if (DriverStation.getAlliance().isEmpty) {
//                println("DS alliance is empty; skipping vision")
                return
            }

            LimelightHelpers.SetRobotOrientation(llName, headingDeg, 0.0, 0.0, 0.0, 0.0, 0.0);


            if (LimelightHelpers.getBotPoseEstimate_wpiBlue(llName) == null) {
//                println(llName + " is null")
                SmartDashboard.putBoolean("ll " + llName + " is valid", false)
                return;
            }

            SmartDashboard.putBoolean("ll " + llName + " is valid", true)

            var llMeasure: LimelightHelpers.PoseEstimate =
                LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(llName)


            if (llMeasure.tagCount >= tagCount && llMeasure.pose.x != 0.0 && llMeasure.pose.y != 0.0) {
                val poseDifference =
                    llMeasure.pose.translation.getDistance(RobotContainer.drivetrain.getSwervePose().translation)
                if (!poseDifferenceCheck || poseDifference < max_distance_m) {
                    val distanceToTag = llMeasure.avgTagDist

                    if (distanceToTag < max_distance_m) {
                        var xyStds: Double
                        var degStds: Double

                            if (llMeasure.tagCount >= 2) {
                                xyStds = 0.5
                                degStds = 250.0
                            } else if (llMeasure.avgTagArea > 0.8 && poseDifference < 0.5) {
                                xyStds = 1.0
                                degStds = 800.0
                            } else if (llMeasure.avgTagArea > 0.1 && poseDifference < 0.3) {
                                xyStds = 2.0
                                degStds = 1300.0
                            } else {
                                xyStds = 4.0
                                degStds = 25000.0
                            }


//                        var headingDeg = RobotContainer.drivetrain.getSwervePose().rotation

                        RobotContainer.drivetrain.setVisionMeasurementStdDevs(
                            VecBuilder.fill(xyStds, xyStds, Math.toRadians(degStds))
                        )

//                        println("updating odometry with ll")
//                        println("Updating with LL ${llName}: X = " + llMeasure.pose.x + " Y = " + llMeasure.pose.y)

//                        val finalPose = Pose2d(llMeasure.pose.x, llMeasure.pose.y, Rotation2d.fromDegrees(headingDeg))

                        RobotContainer.drivetrain.addVisionMeasurement(
                            llMeasure.pose,
                            Utils.fpgaToCurrentTime(llMeasure.timestampSeconds)
                        )

                    }
                }
            }
        }
    }
}
