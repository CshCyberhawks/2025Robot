package frc.robot.subsystems.superstructure.pivot.implementation

import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.controls.TorqueCurrentFOC
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.signals.InvertedValue
import com.ctre.phoenix6.signals.NeutralModeValue
import cshcyberhawks.lib.hardware.AbsoluteDutyCycleEncoder
import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.robot.constants.CANConstants
import cshcyberhawks.lib.math.MiscCalculations
import frc.robot.subsystems.superstructure.pivot.PivotConstants
import frc.robot.subsystems.superstructure.pivot.PivotIO

class PivotIOPID() : PivotIO {
    private val motor = TalonFX(CANConstants.Pivot.motorId)
//    private val encoderDutyCycle = DutyCycle()
    private val encoder = AbsoluteDutyCycleEncoder(CANConstants.Pivot.encoderId)

    val pivotPIDController =
            ProfiledPIDController(
                    0.1,
                    0.0,
                    0.0,
                    TrapezoidProfile.Constraints(
                            PivotConstants.velocityDegrees,
                            PivotConstants.accelerationDegrees
                    )
            )

    private var motorConfig = TalonFXConfiguration()

    private val torqueRequest = TorqueCurrentFOC(0.0)

    private var desiredAngle = 290.0

    private val tbOffset = 70.0
//    private fun getTBDegrees() =


    init {
        val feedBackConfigs = motorConfig.Feedback

        // Converts 1 rotation to 1 in on the elevator
        feedBackConfigs.SensorToMechanismRatio = PivotConstants.conversionFactor

        val slot0Configs = motorConfig.Slot0

        slot0Configs.kP = 0.0 // A position error of 2.5 rotations results in 12 V output
        slot0Configs.kI = 0.0 // no output for integrated error
        slot0Configs.kD = 0.0 // A velocity error of 1 rps results in 0.1 V output

        motorConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive

        val rightCurrentConfigs = motorConfig.CurrentLimits
        rightCurrentConfigs.StatorCurrentLimitEnable = true
        rightCurrentConfigs.StatorCurrentLimit = 60.0

        motor.configurator.apply(motorConfig)

        motor.setNeutralMode(NeutralModeValue.Coast)


        // TODO: Also need to mess with this on bringup
        //        val currentPosition =
        //            encoder.get() * (24 / 32) * 360.0 // Clockwise should be positive and zero
        // should probably be 45 degrees up from motor zero (position we likely won't start at but
        // allowing for full rotation with the ratio)
        //
        //        motor.setPosition((currentPosition - 45.0))

        pivotPIDController.goal = TrapezoidProfile.State(290.0, 0.0)
    }

    override fun getAngle(): Double {
        return MiscCalculations.wrapAroundAngles((MiscCalculations.wrapAroundAngles(encoder
            .absolutePosition * 360.0) - tbOffset) * (32.0 / 24.0))
    }

    override fun getDesiredAngle(): Double {
        return desiredAngle
    }

    override fun atDesiredAngle(): Boolean =
            MiscCalculations.appxEqual(
                    getAngle(),
                    desiredAngle,
                    PivotConstants.angleToleranceDegrees
            )

    override fun setAngle(angleDegrees: Double) {
        desiredAngle = angleDegrees
        pivotPIDController.goal = TrapezoidProfile.State(angleDegrees, 0.0)
    }

    override fun periodic() {
        SmartDashboard.putNumber("Pivot Raw TB Angle", MiscCalculations.wrapAroundAngles(encoder.absolutePosition * 360.0))
        SmartDashboard.putNumber("Pivot Desired Angle", desiredAngle)

        val gravityFF = 0.0
        val positionPIDOut = pivotPIDController.calculate(getAngle())

        SmartDashboard.putNumber("Pivot Position Error", pivotPIDController.positionError)

        val motorSet = positionPIDOut + gravityFF

        SmartDashboard.putNumber("Pivot Output", motorSet)

        motor.setControl(torqueRequest.withOutput(motorSet))
    }
}
