package frc.robot.constants

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import edu.wpi.first.apriltag.AprilTagFieldLayout
import edu.wpi.first.math.geometry.*
import edu.wpi.first.math.util.Units
import edu.wpi.first.wpilibj.Filesystem
import java.io.IOException
import java.nio.file.Path
import java.util.*
import kotlin.math.cos


object FieldConstants {
    val fieldType: FieldType = FieldType.WELDED

    val fieldLength: Double = AprilTagLayoutType.OFFICIAL.layout.getFieldLength()
    val fieldWidth: Double = AprilTagLayoutType.OFFICIAL.layout.getFieldWidth()
    val startingLineX: Double = Units.inchesToMeters(299.438) // Measured from the inside of starting line
    val algaeDiameter: Double = Units.inchesToMeters(16.0)

    object Processor {
        val centerFace: Pose2d = Pose2d(
            AprilTagLayoutType.OFFICIAL.layout.getTagPose(16).get().getX(),
            0.0,
            Rotation2d.fromDegrees(90.0)
        )
    }

    object Barge {
        val farCage: Translation2d = Translation2d(Units.inchesToMeters(345.428), Units.inchesToMeters(286.779))
        val middleCage: Translation2d = Translation2d(Units.inchesToMeters(345.428), Units.inchesToMeters(242.855))
        val closeCage: Translation2d = Translation2d(Units.inchesToMeters(345.428), Units.inchesToMeters(199.947))

        // Measured from floor to bottom of cage
        val deepHeight: Double = Units.inchesToMeters(3.125)
        val shallowHeight: Double = Units.inchesToMeters(30.125)
    }

    object CoralStation {
        val stationLength: Double = Units.inchesToMeters(79.750)
        val rightCenterFace: Pose2d = Pose2d(
            Units.inchesToMeters(33.526),
            Units.inchesToMeters(25.824),
            Rotation2d.fromDegrees(144.011 - 90)
        )
        val leftCenterFace: Pose2d = Pose2d(
            rightCenterFace.x,
            fieldWidth - rightCenterFace.y,
            Rotation2d.fromRadians(-rightCenterFace.rotation.radians)
        )
    }

    object Reef {
        val faceLength: Double = Units.inchesToMeters(36.792600)
        val center: Translation2d = Translation2d(Units.inchesToMeters(176.746), fieldWidth / 2.0)
        val faceToZoneLine: Double = Units.inchesToMeters(12.0) // Side of the reef to the inside of the reef zone line

        val centerFaces: Array<Pose2d?> = arrayOfNulls(6) // Starting facing the driver station in clockwise order
        val branchPositions: MutableList<Map<ReefLevel, Pose3d>> =
            ArrayList() // Starting at the right branch facing the driver station in clockwise
        val branchPositions2d: MutableList<Map<ReefLevel, Pose2d>> = ArrayList()

        init {
            // Initialize faces
            val aprilTagLayout: AprilTagFieldLayout = AprilTagLayoutType.OFFICIAL.layout
            centerFaces[0] = aprilTagLayout.getTagPose(18).get().toPose2d()
            centerFaces[1] = aprilTagLayout.getTagPose(19).get().toPose2d()
            centerFaces[2] = aprilTagLayout.getTagPose(20).get().toPose2d()
            centerFaces[3] = aprilTagLayout.getTagPose(21).get().toPose2d()
            centerFaces[4] = aprilTagLayout.getTagPose(22).get().toPose2d()
            centerFaces[5] = aprilTagLayout.getTagPose(17).get().toPose2d()

            // Initialize branch positions
            for (face in 0..5) {
                val fillRight: MutableMap<ReefLevel, Pose3d> = HashMap()
                val fillLeft: MutableMap<ReefLevel, Pose3d> = HashMap()
                val fillRight2d: MutableMap<ReefLevel, Pose2d> = HashMap()
                val fillLeft2d: MutableMap<ReefLevel, Pose2d> = HashMap()
                for (level in ReefLevel.entries) {
                    val poseDirection = Pose2d(center, Rotation2d.fromDegrees((180 - (60 * face)).toDouble()))
                    val adjustX: Double = Units.inchesToMeters(30.738)
                    val adjustY: Double = Units.inchesToMeters(6.469)

                    val rightBranchPose =
                        Pose3d(
                            Translation3d(
                                poseDirection
                                    .transformBy(Transform2d(adjustX, adjustY, Rotation2d()))
                                    .x,
                                poseDirection
                                    .transformBy(Transform2d(adjustX, adjustY, Rotation2d()))
                                    .y,
                                level.height
                            ),
                            Rotation3d(
                                0.0,
                                Units.degreesToRadians(level.pitch),
                                poseDirection.rotation.radians
                            )
                        )
                    val leftBranchPose =
                        Pose3d(
                            Translation3d(
                                poseDirection
                                    .transformBy(Transform2d(adjustX, -adjustY, Rotation2d()))
                                    .x,
                                poseDirection
                                    .transformBy(Transform2d(adjustX, -adjustY, Rotation2d()))
                                    .y,
                                level.height
                            ),
                            Rotation3d(
                                0.0,
                                Units.degreesToRadians(level.pitch),
                                poseDirection.rotation.radians
                            )
                        )

                    fillRight[level] = rightBranchPose
                    fillLeft[level] = leftBranchPose
                    fillRight2d[level] = rightBranchPose.toPose2d()
                    fillLeft2d[level] = leftBranchPose.toPose2d()
                }
                branchPositions.add(fillRight)
                branchPositions.add(fillLeft)
                branchPositions2d.add(fillRight2d)
                branchPositions2d.add(fillLeft2d)
            }
        }
    }


    object StagingPositions {
        // Measured from the center of the ice cream
        val separation: Double = Units.inchesToMeters(72.0)
        val middleIceCream: Pose2d = Pose2d(Units.inchesToMeters(48.0), fieldWidth / 2.0, Rotation2d())
        val leftIceCream: Pose2d = Pose2d(Units.inchesToMeters(48.0), middleIceCream.y + separation, Rotation2d())
        val rightIceCream: Pose2d = Pose2d(Units.inchesToMeters(48.0), middleIceCream.y - separation, Rotation2d())
    }

    enum class ReefLevel // Degrees
        (val height: Double, val pitch: Double) {
        L1(Units.inchesToMeters(25.0), 0.0),
        L2(Units.inchesToMeters(31.875 - cos(Math.toRadians(35.0)) * 0.625), -35.0),
        L3(Units.inchesToMeters(47.625 - cos(Math.toRadians(35.0)) * 0.625), -35.0),
        L4(Units.inchesToMeters(72.0), -90.0);

        companion object {
            fun fromLevel(level: Int): ReefLevel {
                return Arrays.stream(entries.toTypedArray())
                    .filter { height: ReefLevel -> height.ordinal == level }
                    .findFirst()
                    .orElse(L4)
            }
        }
    }

    val aprilTagWidth: Double = Units.inchesToMeters(6.50)
    val aprilTagCount: Int = 22
    val defaultAprilTagType: AprilTagLayoutType = AprilTagLayoutType.NO_BARGE

    enum class AprilTagLayoutType(name: String) {
        OFFICIAL("2025-official"),
        NO_BARGE("2025-no-barge"),
        BLUE_REEF("2025-blue-reef"),
        RED_REEF("2025-red-reef"),
        FIELD_BORDER("2025-field-border");

        var layout: AprilTagFieldLayout = try {
            AprilTagFieldLayout(
                Path.of(
                    Filesystem.getDeployDirectory().path,
                    "apriltags",
                    fieldType.jsonFolder,
                    "$name.json"
                )
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        var layoutString: String = try {
            ObjectMapper().writeValueAsString(layout)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(
                "Failed to serialize AprilTag layout JSON " + toString() + "for Northstar"
            )
        }
    }

    enum class FieldType(val jsonFolder: String) {
        ANDYMARK("andymark"),
        WELDED("welded")
    }
}