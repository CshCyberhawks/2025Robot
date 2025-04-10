package frc.robot.subsystems.superstructure.pivot.implementation


import com.ctre.phoenix6.configs.CANcoderConfiguration
import com.ctre.phoenix6.configs.MagnetSensorConfigs
import com.ctre.phoenix6.hardware.CANcoder
import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.controls.TorqueCurrentFOC
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.signals.InvertedValue
import com.ctre.phoenix6.signals.NeutralModeValue
import com.ctre.phoenix6.signals.SensorDirectionValue
import cshcyberhawks.lib.hardware.AbsoluteDutyCycleEncoder
import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.robot.constants.CANConstants
import cshcyberhawks.lib.math.MiscCalculations
import edu.wpi.first.math.controller.ArmFeedforward
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import frc.robot.subsystems.superstructure.pivot.PivotConstants
import frc.robot.subsystems.superstructure.pivot.PivotIO
import kotlin.math.sin

class PivotIOPID() : PivotIO {
    private val motor = TalonFX(CANConstants.Pivot.motorId)
//    private val encoderDutyCycle = DutyCycle()
//    private val encoder = AbsoluteDutyCycleEncoder(CANConstants.Pivot.encoderId)
    private val encoder = CANcoder(CANConstants.Pivot.encoderId)

    val pivotPIDController =
//            ProfiledPIDController(
//                    1.5,
//                    0.0,
//                    0.06,
//                    TrapezoidProfile.Constraints(
//                            PivotConstants.velocityDegrees,
//                            PivotConstants.accelerationDegrees
//                    )
//            )
        ProfiledPIDController(
            1.0,
            0.0,
            0.06,
            TrapezoidProfile.Constraints(
                PivotConstants.velocityDegrees,
                PivotConstants.accelerationDegrees
            )
        )

//    val correctivePID = PIDController(0.3, 0.0, 0.0)

    private var motorConfig = TalonFXConfiguration()

    private var encoderConfig = CANcoderConfiguration()
    private var encoderMagnetSensorConfig = MagnetSensorConfigs()

    private val torqueRequest = TorqueCurrentFOC(0.0)

    private var desiredAngle = 290.0

    //138.6 = 180
    private val tbOffset = -68
//    private val tbOffset = -23.2//-43.2
//    private fun getTBDegrees() =

    private var neutralCoast = false

    private var pidDistabled = false

    override fun killPID() {
        pidDistabled = true
    }

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

        encoderMagnetSensorConfig.SensorDirection = SensorDirectionValue.CounterClockwise_Positive
//        encoderMagnetSensorConfig.AbsoluteSensorDiscontinuityPoint

        encoderConfig.withMagnetSensor(encoderMagnetSensorConfig)
        encoder.configurator.apply(encoderConfig)

        motor.setNeutralMode(NeutralModeValue.Brake)


        // TODO: Also need to mess with this on bringup
        //        val currentPosition =
        //            encoder.get() * (24 / 32) * 360.0 // Clockwise should be positive and zero
        // should probably be 45 degrees up from motor zero (position we likely won't start at but
        // allowing for full rotation with the ratio)
        //
        //        motor.setPosition((currentPosition - 45.0))

        pivotPIDController.goal = TrapezoidProfile.State(290.0, 0.0)
        pivotPIDController.reset(getAngle())

        SmartDashboard.putBoolean("Pivot coast", false)

        motor.setNeutralMode(
            if (neutralCoast) NeutralModeValue.Coast else NeutralModeValue.Brake
        )
    }

    override fun getAngle(): Double {
        return MiscCalculations.wrapAroundAngles((MiscCalculations.wrapAroundAngles(encoder
            .absolutePosition.valueAsDouble * 360.0) - tbOffset))
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
        SmartDashboard.putNumber("Pivot Raw TB Angle", MiscCalculations.wrapAroundAngles(encoder.absolutePosition.valueAsDouble * 360.0))
        SmartDashboard.putNumber("Pivot Desired Angle", desiredAngle)
        SmartDashboard.putNumber("Pivot Velocity", encoder.velocity.valueAsDouble * 360.0)

        val sdCoast = SmartDashboard.getBoolean("Pivot coast", false)
        if (sdCoast != neutralCoast) {
            neutralCoast = sdCoast
            motor.setNeutralMode(
                if (neutralCoast) NeutralModeValue.Coast else NeutralModeValue.Brake
            )
        }

        if (pidDistabled || (desiredAngle == 295.0 && MiscCalculations.calculateDeadzone(desiredAngle, 4.0) == 0.0)) {
            motor.set(0.0)
            return
        }

//        val kG = if (getAngle() > 270) 8.25 else 10.5
        val kG = if (getAngle() > 270 && getAngle() < 305.0) 0.0 else 7.5
        val gravityFF = kG * -sin(Math.toRadians(getAngle() - 110.0))
        val positionPIDOut = pivotPIDController.calculate(getAngle())
//        val correctivePIDOut = correctivePID.calculate(getAngle(), desiredAngle)

        SmartDashboard.putNumber("Pivot Position Error", pivotPIDController.positionError)
        SmartDashboard.putNumber("Position PID Output", positionPIDOut)
        //SmartDashboard.putNumber("Corrective PID Output", correctivePIDOut)

        val motorSet = positionPIDOut + gravityFF// +
//        val motorSet = gravityFF// + correctivePIDOut


        SmartDashboard.putNumber("Pivot Output", motorSet)

        motor.setControl(torqueRequest.withOutput(motorSet))
    }
}
