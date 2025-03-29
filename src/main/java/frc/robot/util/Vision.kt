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
    val max_distance_m = 4.0

    val max_pose_diff = 2.0

    val limelightNames: Array<String> = when (RobotConfiguration.robotType) {
//        RobotType.Real -> arrayOf("limelight-tright", "limelight-btfront")
        RobotType.Real -> arrayOf("limelight-tleft", "limelight-tright", "limelight-bfrontl", "limelight-bfrontr")
        else -> emptyArray()
    }

    var hasInitM2 = false;

    fun updateOdometryFromDisabled() {
        var namesToSearch: Array<String>;

        var fullReset = SmartDashboard.getBoolean("Disabled vision mode", false)

        if (!fullReset) {
            return
        }

        namesToSearch = limelightNames

         val headingDeg: Double = RobotContainer.drivetrain.getSwervePose().rotation.degrees

        val bottomM2Reset = SmartDashboard.getBoolean("LL Bottom M2 Reset", false)

        if (bottomM2Reset && !hasInitM2) {
            hasInitM2 = true
        }



        for (llName in namesToSearch) {
            if (DriverStation.getAlliance().isEmpty) {
                println("DS alliance is empty; skipping vision")
                return
            }

            if (!bottomM2Reset) {
                if (llName == "limelight-tright" || llName == "limelight-tleft") {
//                if (llName == "limelight-tright") {
                    continue;
                }
            }

//            LimelightHelpers.SetRobotOrientation(llName, headingDeg, 0.0, 0.0, 0.0, 0.0, 0.0);

            if (LimelightHelpers.getBotPoseEstimate_wpiBlue(llName) == null) {
//                println(llName + " is null")
                SmartDashboard.putBoolean("ll " + llName + " is valid", false)
                continue;
            }

            SmartDashboard.putBoolean("ll " + llName + " is valid", true)


            if (hasInitM2 && bottomM2Reset) {
                LimelightHelpers.SetRobotOrientation(llName, headingDeg, 0.0, 0.0, 0.0, 0.0, 0.0);
            }

            var llMeasureRead: LimelightHelpers.PoseEstimate? = if(hasInitM2 && bottomM2Reset) LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(llName) else LimelightHelpers.getBotPoseEstimate_wpiBlue(llName)

            var llMeasure = LimelightHelpers.PoseEstimate()
            if (llMeasureRead != null) {
                llMeasure = llMeasureRead
            } else {
                continue
            }

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

                if (distanceToTag < max_distance_m) {
//                    println("doing the thingy")
                    var xyStds: Double
                    var degStds: Double

                    if (llMeasure.tagCount >= 2) {
                        xyStds = .75
                        degStds = 110.0
                    } else if (llMeasure.avgTagArea > .8) {
                        xyStds = 9.0
                        degStds = 260.0
                    } else if (llMeasure.avgTagArea > 0.2) {
                        xyStds = 10.0
                        degStds = 280.0
                    } else {
                        xyStds = 11.0
                        degStds = 300.0
                    }

                    if (llName == "limelight-tright") {

//                    if (llName == "limelight-tright" || llName == "limelight-tleft") {
                        xyStds *= 4.0
                        degStds *= 4.0
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
                continue
            }

            LimelightHelpers.SetRobotOrientation(llName, headingDeg, 0.0, 0.0, 0.0, 0.0, 0.0);


            if (LimelightHelpers.getBotPoseEstimate_wpiBlue(llName) == null) {
//                println(llName + " is null")
                SmartDashboard.putBoolean("ll " + llName + " is valid", false)
                continue;
            }

            SmartDashboard.putBoolean("ll " + llName + " is valid", true)

            var llMeasureRead: LimelightHelpers.PoseEstimate? =
//                LimelightHelpers.getBotPoseEstimate_wpiBlue(llName)
                LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(llName)


            var llMeasure = LimelightHelpers.PoseEstimate()
            if (llMeasureRead != null) {
                llMeasure = llMeasureRead
            } else {
                continue
            }


            if (llMeasure.tagCount >= tagCount && llMeasure.pose.x != 0.0 && llMeasure.pose.y != 0.0) {
                val poseDifference =
                    llMeasure.pose.translation.getDistance(RobotContainer.drivetrain.getSwervePose().translation)
                if (!poseDifferenceCheck || poseDifference < max_pose_diff) {
                    val distanceToTag = llMeasure.avgTagDist

                    if (distanceToTag < max_distance_m) {
                        var xyStds: Double
                        var degStds: Double


                        degStds = 1800.0;
                        xyStds = Math.sqrt((1.0/(2.0 * (llMeasure.avgTagArea + .25)))) - .15;

                        if (llMeasure.tagCount >= 2) {
                            xyStds *= .6;
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


                        if (llName == "limelight-tright") {
                            xyStds = 14.0
                            degStds = 1800.0
                        }
                        else if (llName == "limelight-tleft") {
                            if (llMeasure.avgTagDist < 1.75) {
                                xyStds = 0.35
                                degStds = 1800.0;
                            }
                            else {
                                xyStds *= 2.5
                            }
                        }
                        else {
                            if (llMeasure.avgTagDist > 2.45) {
                                xyStds *= 4.5
                            }
                        }


//                        var headingDeg = RobotContainer.drivetrain.getSwervePose().rotation

                        RobotContainer.drivetrain.setVisionMeasurementStdDevs(
                            VecBuilder.fill(xyStds, xyStds, Math.toRadians(degStds))
                        )

//                        println("avg tag dist: " + llMeasure.avgTagDist)

                        SmartDashboard.putNumber("LL ${llName} xy stds: ", xyStds)

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
