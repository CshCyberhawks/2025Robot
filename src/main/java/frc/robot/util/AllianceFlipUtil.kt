package frc.robot.util

import edu.wpi.first.math.geometry.*
import edu.wpi.first.wpilibj.DriverStation
import frc.robot.constants.FieldConstants

object AllianceFlipUtil {
    fun applyX(x: Double): Double {
        return if (shouldFlip()) FieldConstants.fieldLength - x else x
    }

    fun applyY(y: Double): Double {
        return if (shouldFlip()) FieldConstants.fieldWidth - y else y
    }

    fun apply(translation: Translation2d): Translation2d {
        return Translation2d(applyX(translation.x), applyY(translation.y))
    }

    fun apply(rotation: Rotation2d): Rotation2d {
        return if (shouldFlip()) rotation.rotateBy(Rotation2d.kPi) else rotation
    }

    fun apply(pose: Pose2d): Pose2d {
        return if (shouldFlip())
            Pose2d(apply(pose.translation), apply(pose.rotation))
        else
            pose
    }

    fun apply(translation: Translation3d): Translation3d {
        return Translation3d(
            applyX(translation.x), applyY(translation.y), translation.z
        )
    }

    fun apply(rotation: Rotation3d): Rotation3d {
        return if (shouldFlip()) rotation.rotateBy(Rotation3d(0.0, 0.0, Math.PI)) else rotation
    }

    fun apply(pose: Pose3d): Pose3d {
        return Pose3d(apply(pose.translation), apply(pose.rotation))
    }

//    fun apply(state: VehicleState): VehicleState {
//        return if (shouldFlip())
//            VehicleState.newBuilder()
//                .setX(applyX(state.getX()))
//                .setY(applyY(state.getY()))
//                .setTheta(apply(Rotation2d.fromRadians(state.getTheta())).getRadians())
//                .setVx(-state.getVx())
//                .setVy(-state.getVy())
//                .setOmega(state.getOmega())
//                .addAllModuleForces(
//                    state.getModuleForcesList().stream()
//                        .map { forces ->
//                            ModuleForce.newBuilder()
//                                .setFx(-forces.getFx())
//                                .setFy(-forces.getFy())
//                                .build()
//                        }
//                        .toList())
//                .build()
//        else
//            state
//    }

    fun shouldFlip(): Boolean {
        return DriverStation.getAlliance().isPresent
                && DriverStation.getAlliance().get() == DriverStation.Alliance.Red
    }
}
