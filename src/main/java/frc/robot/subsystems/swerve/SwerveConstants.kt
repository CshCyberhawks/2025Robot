package frc.robot.subsystems.swerve

import edu.wpi.first.units.Units.*
import frc.robot.subsystems.swerve.TunerConstants

object SwerveConstants {
    val MaxSpeedConst = TunerConstants.kSpeedAt12Volts.`in`(MetersPerSecond) // kSpeedAt12Volts desired top

    // speed
    var ControlledSpeed = MaxSpeedConst
    val MaxAngularRateConst =
        RotationsPerSecond.of(0.75).`in`(RadiansPerSecond) // 3/4 of a rotation per second max angular velocity

    var ControlledAngularRate = MaxAngularRateConst
}