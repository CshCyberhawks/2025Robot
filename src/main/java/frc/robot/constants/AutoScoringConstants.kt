package frc.robot.constants

import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Transform2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.util.Units
import frc.robot.util.input.CoralSide

object AutoScoringConstants {
//    private val reefFaceOffset = .6 + .74 - Units.inchesToMeters(4.0)
        private val reefFaceOffset = .6 + .74 - Units.inchesToMeters(2.65)
    private val branchOffset = Units.inchesToMeters(6.469)
//    private val branchOffset = Units.inchesToMeters(7.5)


    fun getReefPoseAtOffset(face: Int, side: CoralSide, x: Double): Pose2d {
        val poseDirection =
            Pose2d(FieldConstants.Reef.center, Rotation2d.fromDegrees((180 - (60 * face)).toDouble()))
        val adjustX: Double = reefFaceOffset + x
        val adjustY: Double = when (side) {
            CoralSide.Left -> -branchOffset
            CoralSide.Right -> branchOffset
        }

        return Pose2d(
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
    }

    fun getAlgaePoseAtOffset(face: Int, x: Double): Pose2d {
        val poseDirection =
            Pose2d(FieldConstants.Reef.center, Rotation2d.fromDegrees((180 - (60 * face)).toDouble()))
        val adjustX: Double = reefFaceOffset + x
        val adjustY: Double = 0.0

        return Pose2d(
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
    }

    enum class ReefPositions(var left: Pose2d, var right: Pose2d, var center: Pose2d) {
        A(Pose2d(), Pose2d(), Pose2d()),
        B(Pose2d(), Pose2d(), Pose2d()),
        C(Pose2d(), Pose2d(), Pose2d()),
        D(Pose2d(), Pose2d(), Pose2d()),
        E(Pose2d(), Pose2d(), Pose2d()),
        F(Pose2d(), Pose2d(), Pose2d())
    }

    private fun initializeReef() {
        for (face in 0..5) {
            val poseDirection =
                Pose2d(FieldConstants.Reef.center, Rotation2d.fromDegrees((180 - (60 * face)).toDouble()))
            val adjustX: Double = reefFaceOffset
            val adjustY: Double = branchOffset

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

            val centerPose = Pose2d(
                Translation2d(
                    poseDirection
                        .transformBy(Transform2d(adjustX, 0.0, Rotation2d()))
                        .x,
                    poseDirection
                        .transformBy(Transform2d(adjustX, 0.0, Rotation2d()))
                        .y
                ),
                Rotation2d(
                    poseDirection.rotation.radians
                ).rotateBy(Rotation2d.k180deg)
            )

            ReefPositions.entries[face].right = rightPose
            ReefPositions.entries[face].left = leftPose
            ReefPositions.entries[face].center = centerPose

//            println("$face Right: $rightPose, Left: $leftPose")
        }
    }

    fun initialize() {
        initializeReef()
    }
}