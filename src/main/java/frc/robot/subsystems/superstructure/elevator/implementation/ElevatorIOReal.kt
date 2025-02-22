package frc.robot.subsystems.superstructure.elevator.implementation

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.controls.Follower
import com.ctre.phoenix6.controls.MotionMagicTorqueCurrentFOC
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.signals.InvertedValue
import frc.robot.constants.CANConstants
import frc.robot.math.MiscCalculations
import frc.robot.subsystems.superstructure.elevator.ElevatorConstants
import frc.robot.subsystems.superstructure.elevator.ElevatorIO

class ElevatorIOReal() : ElevatorIO {
    private val rightMotor = TalonFX(CANConstants.Elevator.rightMotorId)
    private val leftMotor = TalonFX(CANConstants.Elevator.leftMotorId)

    private var rightMotorConfiguration = TalonFXConfiguration()

    private val motionMagic = MotionMagicTorqueCurrentFOC(0.0)

    private var desiredPosition = 0.0

    init {
        val feedBackConfigs = rightMotorConfiguration.Feedback

        // Converts 1 rotation to 1 in on the elevator
        feedBackConfigs.SensorToMechanismRatio =
            ElevatorConstants.conversionFactor

        val slot0Configs = rightMotorConfiguration.Slot0;

        // TODO: These values need to be changed
        slot0Configs.kS = 0.25; // Add 0.25 V output to overcome static friction
        slot0Configs.kV = 0.12; // A velocity target of 1 rps results in 0.12 V output
        slot0Configs.kA = 0.01; // An acceleration of 1 rps/s requires 0.01 V output
        slot0Configs.kP = 4.8; // A position error of 2.5 rotations results in 12 V output
        slot0Configs.kI = 0.0; // no output for integrated error
        slot0Configs.kD = 0.1; // A velocity error of 1 rps results in 0.1 V output

        // set Motion Magic settings
        val motionMagicConfigs = rightMotorConfiguration.MotionMagic;
        motionMagicConfigs.MotionMagicCruiseVelocity = ElevatorConstants.velocityInches// Target cruise velocity in rps
        motionMagicConfigs.MotionMagicAcceleration = ElevatorConstants.accelationInches // Target acceleration in rps/s
//        motionMagicConfigs.MotionMagicJerk = 1600; Optional // Target jerk of 1600 rps/s/s (0.1 seconds)
        rightMotorConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive

        rightMotor.configurator.apply(rightMotorConfiguration);

        leftMotor.setControl(Follower(rightMotor.deviceID, true))
    }

    override fun getPosition(): Double {
        return rightMotor.position.valueAsDouble
    }

    override fun atDesiredPosition(): Boolean =
        MiscCalculations.appxEqual(getPosition(), desiredPosition, ElevatorConstants.positionTolerance)

    override fun setPosition(positionInches: Double) {
        desiredPosition = positionInches
        rightMotor.setControl(motionMagic.withPosition(positionInches))
    }

    override fun periodic() {}
}
