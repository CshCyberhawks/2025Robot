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


    val maxAutoSpeed = 2.8
    val maxAutoAccel = 2.5

    val maxAutoTwistDegrees = 180.0
    val maxAutoTwistAccelDegrees = 180.0

//    val translationPIDConstants = PIDConstants(16.0, 0.0, 0.0)
    val translationPIDConstants = PIDConstants(7.0, 0.0, 0.005)

    val rotationPIDConstants = PIDConstants(1.6, 0.0, 0.05)

    val positionDeadzone = 0.025
    val rotationDeadzone = 2.5
}