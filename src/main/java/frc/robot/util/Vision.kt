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
    val max_distance_m = 5.5

    val max_pose_diff = 2.0

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
                        xyStds = 0.05
                        degStds = 20.0
                    } else if (llMeasure.avgTagArea > .8) {
                        xyStds = 1.0
                        degStds = 40.0
                    } else if (llMeasure.avgTagArea > 0.2) {
                        xyStds = 2.5
                        degStds = 60.0
                    } else {
                        xyStds = 4.0
                        degStds = 80.0
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

            var llMeasureRead: LimelightHelpers.PoseEstimate? =
//                LimelightHelpers.getBotPoseEstimate_wpiBlue(llName)
                LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(llName)


            var llMeasure = LimelightHelpers.PoseEstimate()
            if (llMeasureRead != null) {
                llMeasure = llMeasureRead
            } else {
                return
            }


            if (llMeasure.tagCount >= tagCount && llMeasure.pose.x != 0.0 && llMeasure.pose.y != 0.0) {
                val poseDifference =
                    llMeasure.pose.translation.getDistance(RobotContainer.drivetrain.getSwervePose().translation)
                if (!poseDifferenceCheck || poseDifference < max_pose_diff) {
                    val distanceToTag = llMeasure.avgTagDist

                    if (distanceToTag < max_distance_m) {
                        var xyStds: Double
                        var degStds: Double

                        if (llName == "limelight-btfront") {
//                            println(llMeasure.avgTagArea)
                        }

                        degStds = 1800.0;
                        xyStds = Math.sqrt((1.0/(2.0 * (llMeasure.avgTagArea + .25)))) - .15;

                        if (llMeasure.tagCount >= 2) {
                            xyStds *= .4;
                        }

//                            if (llMeasure.tagCount >= 2) {
//                                xyStds = 0.05
//                                degStds = 1250.0
////                            } else if (llMeasure.avgTagArea > 0.55 && poseDifference < 0.6) {
//                            } else if (llMeasure.avgTagArea > 1.75 && llName == "limelight-btfront") {
//                                xyStds = if (llMeasure.avgTagArea > 3.0) .1 else .2
//                                degStds = 1800.0
//
//                            } else if (llMeasure.avgTagArea > 0.4 && poseDifference < 0.3) {
//                                xyStds = 0.5
//                                degStds = 1600.0
//                            }
//                            else if (llMeasure.avgTagArea > .8) {
//                                xyStds = 1.05
//                                degStds = 1900.0
//                            }
//                            else {
//                                xyStds = 1.25
//                                degStds = 25000.0
//                            }


//                        var headingDeg = RobotContainer.drivetrain.getSwervePose().rotation

                        RobotContainer.drivetrain.setVisionMeasurementStdDevs(
                            VecBuilder.fill(xyStds, xyStds, Math.toRadians(degStds))
                        )

//                        println("updating odometry with ll")
//                        println("Updating with LL ${llName}: X = " + llMeasure.pose.x + " Y = " + llMeasure.pose.y)

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
