package frc.robot.subsystems.swerve

import com.pathplanner.lib.config.PIDConstants
import edu.wpi.first.units.Units.*

object SwerveConstants {
    val MaxSpeedConst = TunerConstants.kSpeedAt12Volts.`in`(MetersPerSecond) // kSpeedAt12Volts desired top

    // speed
    var ControlledSpeed = MaxSpeedConst
    val MaxAngularRateConst =
        RotationsPerSecond.of(0.75).`in`(RadiansPerSecond) // 3/4 of a rotation per second max angular velocity

    var ControlledAngularRate = MaxAngularRateConst


    val maxAutoSpeed = 1.0
    val maxAutoAccel = 1.0

    val maxAutoTwistDegrees = 180.0
    val maxAutoTwistAccelDegrees = 180.0

//    val translationPIDConstants = PIDConstants(16.0, 0.0, 0.0)
    val translationPIDConstants = PIDConstants(13.0, 0.0, 0.01)

    val rotationPIDConstants = PIDConstants(1.05, 0.0, 0.05)

    val positionDeadzone = 0.015
    val rotationDeadzone = 2.0
}