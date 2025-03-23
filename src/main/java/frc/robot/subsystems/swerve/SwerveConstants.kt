package frc.robot.subsystems.swerve

import com.pathplanner.lib.config.PIDConstants
import edu.wpi.first.units.Units.*

object SwerveConstants {
    val MaxSpeedConst = TunerConstants.kSpeedAt12Volts.`in`(MetersPerSecond) // kSpeedAt12Volts desired top

    // speed
    var ControlledSpeed = MaxSpeedConst
    val MaxAngularRateConst =
        RotationsPerSecond.of(1.1).`in`(RadiansPerSecond) // 3/4 of a rotation per second max angular velocity

    var ControlledAngularRate = MaxAngularRateConst


//    val maxAutoSpeed = 2.8
//    val maxAutoAccel = 2.5


    val maxAutoSpeed = 2.25
    val maxAutoAccel = 1.575

    val maxAutoTwistDegrees = 180.0
    val maxAutoTwistAccelDegrees = 180.0

//    val translationPIDConstants = PIDConstants(16.0, 0.0, 0.0)
    val translationPIDConstants = PIDConstants(9.0, 0.0, 0.005)

    val rotationPIDConstants = PIDConstants(1.9, 0.0, 0.05)

    val positionDeadzone = 0.01
    val rotationDeadzone = 1.5
}