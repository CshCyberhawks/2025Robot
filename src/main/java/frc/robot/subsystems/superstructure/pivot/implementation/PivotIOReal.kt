package frc.robot.subsystems.superstructure.pivot.implementation

import com.ctre.phoenix6.configs.CurrentLimitsConfigs
import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.controls.MotionMagicTorqueCurrentFOC
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.controls.MotionMagicVoltage
import com.ctre.phoenix6.signals.InvertedValue
import edu.wpi.first.wpilibj.DutyCycleEncoder
import frc.robot.constants.CANConstants
import frc.robot.math.MiscCalculations
import frc.robot.subsystems.superstructure.pivot.PivotConstants
import frc.robot.subsystems.superstructure.pivot.PivotIO

class PivotIOReal() : PivotIO {
    private val motor = TalonFX(CANConstants.Pivot.motorId)
    private val encoder = DutyCycleEncoder(CANConstants.Pivot.encoderId)

    private var motorConfiguration = TalonFXConfiguration()

    private val motionMagic = MotionMagicTorqueCurrentFOC(0.0)

    private var desiredAngle = 290.0

    init {
        val feedBackConfigs = motorConfiguration.Feedback

        // Converts 1 rotation to 1 rotation of the pivot
        feedBackConfigs.SensorToMechanismRatio =
            PivotConstants.conversionFactor

        val slot0Configs = motorConfiguration.Slot0;

        // TODO: These values need to be changed
        slot0Configs.kS = 0.25; // Add 0.25 V output to overcome static friction
        slot0Configs.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
        slot0Configs.kA = 0.01; // An acceleration of 1 rps/s requires 0.01 V output
        slot0Configs.kP = 4.8; // A position error of 2.5 rotations results in 12 V output
        slot0Configs.kI = 0.0; // no output for integrated error
        slot0Configs.kD = 0.1; // A velocity error of 1 rps results in 0.1 V output

        // set Motion Magic settings
        val motionMagicConfigs = motorConfiguration.MotionMagic;
        motionMagicConfigs.MotionMagicCruiseVelocity =
            PivotConstants.velocityDegrees / 360.0 // Target cruise velocity in rps
        motionMagicConfigs.MotionMagicAcceleration =
            PivotConstants.accelerationDegrees / 360.0 // Target acceleration in rps/s
//        motionMagicConfigs.MotionMagicJerk = 1600; Optional // Target jerk of 1600 rps/s/s (0.1 seconds)
        motorConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive

        val currentConfigs = motorConfiguration.CurrentLimits
        currentConfigs.StatorCurrentLimitEnable = true
        currentConfigs.StatorCurrentLimit = 60.0

        motor.configurator.apply(motorConfiguration);

        // TODO: Also need to mess with this on bringup
        val currentPosition =
            encoder.get() * (24 / 32) * 360.0 // Clockwise should be positive and zero should probably be 45 degrees up from motor zero (position we likely won't start at but allowing for full rotation with the ratio)

        motor.setPosition((currentPosition - 45.0) / 360.0)
    }

    override fun getAngle(): Double {
        return motor.position.valueAsDouble * 360.0
    }

    override fun getDesiredAngle(): Double {
        return desiredAngle
    }

    override fun atDesiredAngle() =
        MiscCalculations.appxEqual(getAngle(), desiredAngle, PivotConstants.accelerationDegrees)

    override fun setAngle(angleDegrees: Double) {
        desiredAngle = angleDegrees
        motor.setControl(motionMagic.withPosition(angleDegrees / 360.0))
    }

    override fun periodic() {}
}
