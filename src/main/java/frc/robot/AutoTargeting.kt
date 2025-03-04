package frc.robot

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.path.PathConstraints
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.wpilibj2.command.Command
import frc.robot.constants.AutoScoringConstants
import frc.robot.subsystems.swerve.SwerveConstants

object AutoTargeting {
    val pathConstraint: PathConstraints = PathConstraints(SwerveConstants.maxAutoSpeed, SwerveConstants.maxAutoAccel, SwerveConstants.maxAutoTwist, SwerveConstants.maxAutoTwistAccel)

//    val leftCoralPoses = arrayOf(AutoScoringConstants.CoralScoring.A.left, AutoScoringConstants.CoralScoring.B.left, AutoScoringConstants.CoralScoring.C.left, AutoScoringConstants.CoralScoring.D.left, AutoScoringConstants.CoralScoring.E.left, AutoScoringConstants.CoralScoring.F.left)
//    val rightCoralPoses = arrayOf(AutoScoringConstants.CoralScoring.A.right, AutoScoringConstants.CoralScoring.B.right, AutoScoringConstants.CoralScoring.C.right, AutoScoringConstants.CoralScoring.D.right, AutoScoringConstants.CoralScoring.E.right, AutoScoringConstants.CoralScoring.F.right)
//    val centerCoralPoses = arrayOf(AutoScoringConstants.CoralScoring.A.center, AutoScoringConstants.CoralScoring.B.center, AutoScoringConstants.CoralScoring.C.center, AutoScoringConstants.CoralScoring.D.center, AutoScoringConstants.CoralScoring.E.center, AutoScoringConstants.CoralScoring.F.center)


    fun goToPose(pose2d: Pose2d): Command {
        return AutoBuilder.pathfindToPose(pose2d, pathConstraint, 0.0)
    }

//    fun autoToNearestCoral(targetSide: CoralSide, currentPose2d: Pose2d): Command {
//        var nearestCenter = currentPose2d.nearest(centerCoralPoses.toList())
//        var nearestCenterIdx = centerCoralPoses.indexOf(nearestCenter)
//
//        var targetPose: Pose2d;
//
//        if (targetSide == CoralSide.Left) {
//            targetPose = leftCoralPoses[nearestCenterIdx]
//        }
//        else {
//            targetPose = rightCoralPoses[nearestCenterIdx]
//        }
//
//        return goToPose(targetPose)
//    }

    enum class CoralSide {
        Left,
        Right
    }

    enum class CoralLevel {
        L2, L3, L4
    }
}