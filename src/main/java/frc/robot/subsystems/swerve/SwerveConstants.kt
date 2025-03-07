package frc.robot.subsystems.swerve

import com.pathplanner.lib.config.PIDConstants
import edu.wpi.first.units.Units.*
import frc.robot.subsystems.swerve.TunerConstants

object SwerveConstants {
    val MaxSpeedConst = TunerConstants.kSpeedAt12Volts.`in`(MetersPerSecond) // kSpeedAt12Volts desired top

    // speed
    var ControlledSpeed = MaxSpeedConst
    val MaxAngularRateConst =
        RotationsPerSecond.of(0.75).`in`(RadiansPerSecond) // 3/4 of a rotation per second max angular velocity

    var ControlledAngularRate = MaxAngularRateConst


    val maxAutoSpeed = 2.0
    val maxAutoAccel = 3.0

    val maxAutoTwist = 350.0
    val maxAutoTwistAccel = 450.0

    val translationPIDConstants = PIDConstants(12.0, 0.0, 0.0)
    val rotationPIDConstants = PIDConstants(4.0, 0.0, 0.1)

    val positionDeadzone = 0.01
    val rotationDeadzone = 0.5
}