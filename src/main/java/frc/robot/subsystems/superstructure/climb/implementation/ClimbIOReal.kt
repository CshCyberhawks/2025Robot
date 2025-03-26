package frc.robot.subsystems.superstructure.climb.implementation

import com.ctre.phoenix6.configs.CANcoderConfiguration
import com.ctre.phoenix6.configs.MagnetSensorConfigs
import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.controls.TorqueCurrentFOC
import com.ctre.phoenix6.hardware.CANcoder
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.signals.InvertedValue
import com.ctre.phoenix6.signals.NeutralModeValue
import com.ctre.phoenix6.signals.SensorDirectionValue
import cshcyberhawks.lib.math.MiscCalculations
import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.robot.constants.CANConstants
import frc.robot.subsystems.superstructure.climb.ClimbConstants
import frc.robot.subsystems.superstructure.climb.ClimbIO
import frc.robot.subsystems.superstructure.funnel.FunnelConstants
import kotlin.math.sin

class ClimbIOReal() : ClimbIO {
    private val motor = TalonFX(CANConstants.Climb.motorId)
    private val encoder = CANcoder(CANConstants.Climb.encoderId)

    override var climbing = false

    override var disable = false
        set(value) {
            motor.setControl(torqueRequest.withOutput(0.0))
            field = value
        }

    private var neutralCoast = false


    private val pidController = ProfiledPIDController(
        1.2315,
        0.0,
        0.0315,
        TrapezoidProfile.Constraints(
            180.0,
            250.0,
        )
    )

    private var motorConfig = TalonFXConfiguration()

    private var encoderConfig = CANcoderConfiguration()
    private var encoderMagnetSensorConfig = MagnetSensorConfigs()

    private val torqueRequest = TorqueCurrentFOC(0.0)

    private var desiredAngle = ClimbConstants.stowAngle

    private val tbOffset = -120.2

    init {
        val slot0Configs = motorConfig.Slot0

        slot0Configs.kP = 0.0
        slot0Configs.kI = 0.0
        slot0Configs.kD = 0.0

        motorConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive

        val currentConfigs = motorConfig.CurrentLimits
        currentConfigs.StatorCurrentLimitEnable = true
        currentConfigs.StatorCurrentLimit = 60.0

        motor.configurator.apply(motorConfig)

        encoderMagnetSensorConfig.SensorDirection = SensorDirectionValue.Clockwise_Positive

        encoderConfig.withMagnetSensor(encoderMagnetSensorConfig)
        encoder.configurator.apply(encoderConfig)

        motor.setNeutralMode(NeutralModeValue.Brake)

        pidController.goal = TrapezoidProfile.State(desiredAngle, 0.0)

        SmartDashboard.putBoolean("Climb coast", false)
    }

    override fun getAngle(): Double {
        return MiscCalculations.wrapAroundAngles((MiscCalculations.wrapAroundAngles(encoder
            .absolutePosition.valueAsDouble * 360.0) - tbOffset))
    }

    override fun angleDegrees(angleDegrees: Double) {
        desiredAngle = angleDegrees
        pidController.goal = TrapezoidProfile.State(angleDegrees, 0.0)
    }

    override fun setMotor(current: Double) {
        motor.setControl(torqueRequest.withOutput(current))
    }

    override fun periodic() {
        if (disable) {
//            println("climb io disabled")
//            motor.setControl(torqueRequest.withOutput(0.0))
            return
        }


        val positionPIDOut = pidController.calculate(getAngle())

//        val gravityFF = 3.15 * sin(Math.toRadians(getAngle()))

        val motorOut = if (climbing && (ClimbConstants.climbAngle - getAngle()) > 10.0) {positionPIDOut + 30.0} else if (climbing) {positionPIDOut + 8.5} else positionPIDOut


//            println(motorOut)
            motor.setControl(torqueRequest.withOutput(motorOut))


        val sdCoast = SmartDashboard.getBoolean("Climb coast", false)
//        println(sdCoast)
        if (sdCoast != neutralCoast) {
            neutralCoast = sdCoast
            motor.setNeutralMode(
                if (neutralCoast) NeutralModeValue.Coast else NeutralModeValue.Brake
            )
        }

        SmartDashboard.putNumber("Climb Raw TB Angle", MiscCalculations.wrapAroundAngles(encoder.absolutePosition.valueAsDouble * 360.0))
    }
}