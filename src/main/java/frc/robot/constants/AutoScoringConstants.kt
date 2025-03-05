package frc.robot.constants

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Transform2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.util.Units

object AutoScoringConstants {
    enum class CoralScoringPositions(var left: Pose2d, var right: Pose2d) {
        A(Pose2d(), Pose2d()),
        B(Pose2d(), Pose2d()),
        C(Pose2d(), Pose2d()),
        D(Pose2d(), Pose2d()),
        E(Pose2d(), Pose2d()),
        F(Pose2d(), Pose2d())
    }

    fun initialize() {
        for (face in 0..5) {
            val poseDirection =
                Pose2d(FieldConstants.Reef.center, Rotation2d.fromDegrees((180 - (60 * face)).toDouble()))
            val adjustX: Double = .6 + .74 - Units.inchesToMeters(5.5)
            val adjustY: Double = Units.inchesToMeters(6.469 + 3.0)

            val rightPose =
                Pose2d(
                    Translation2d(
                        poseDirection
                            .transformBy(Transform2d(adjustX, adjustY, Rotation2d()))
                            .x,
                        poseDirection
                            .transformBy(Transform2d(adjustX, adjustY, Rotation2d()))
                            .y
                    ),
                    Rotation2d(
                        poseDirection.rotation.radians
                    ).rotateBy(Rotation2d.k180deg)
                )

            val leftPose =
                Pose2d(
                    Translation2d(
                        poseDirection
                            .transformBy(Transform2d(adjustX, -adjustY, Rotation2d()))
                            .x,
                        poseDirection
                            .transformBy(Transform2d(adjustX, -adjustY, Rotation2d()))
                            .y
                    ),
                    Rotation2d(
                        poseDirection.rotation.radians
                    ).rotateBy(Rotation2d.k180deg)
                )

            CoralScoringPositions.entries[face].right = rightPose
            CoralScoringPositions.entries[face].left = leftPose
        }
    }

    object AlgaePickup {
        val A: Pose2d = Pose2d();

        val B: Pose2d = Pose2d();

        val C: Pose2d = Pose2d();

        val D: Pose2d = Pose2d();

        val E: Pose2d = Pose2d();

        val F: Pose2d = Pose2d();
    }
}