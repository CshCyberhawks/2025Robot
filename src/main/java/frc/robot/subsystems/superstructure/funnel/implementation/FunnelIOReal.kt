package frc.robot.subsystems.superstructure.funnel.implementation

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
import frc.robot.subsystems.superstructure.funnel.FunnelConstants
import frc.robot.subsystems.superstructure.funnel.FunnelIO

class FunnelIOReal(): FunnelIO {
    private val motor = TalonFX(CANConstants.Funnel.motorId)
    private val encoder = CANcoder(CANConstants.Funnel.encoderId)

    private val pidController = ProfiledPIDController(
        1.0,
        0.0,
        0.06,
        TrapezoidProfile.Constraints(
            FunnelConstants.velocityDegrees,
            FunnelConstants.accelerationDegrees
        )
    )

    private var motorConfig = TalonFXConfiguration()

    private var encoderConfig = CANcoderConfiguration()
    private var encoderMagnetSensorConfig = MagnetSensorConfigs()

    private val torqueRequest = TorqueCurrentFOC(0.0)

    private var desiredAngle = 200.0

    private val tbOffset = 0.0

    init {
        val slot0Configs = motorConfig.Slot0

        slot0Configs.kP = 0.0
        slot0Configs.kI = 0.0
        slot0Configs.kD = 0.0

        motorConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive

        val currentConfigs = motorConfig.CurrentLimits
        currentConfigs.StatorCurrentLimitEnable = true
        currentConfigs.StatorCurrentLimit = 60.0

        motor.configurator.apply(motorConfig)

        encoderMagnetSensorConfig.SensorDirection = SensorDirectionValue.CounterClockwise_Positive

        encoderConfig.withMagnetSensor(encoderMagnetSensorConfig)
        encoder.configurator.apply(encoderConfig)

        motor.setNeutralMode(NeutralModeValue.Brake)

        pidController.goal = TrapezoidProfile.State(desiredAngle, 0.0)
    }

    override fun getAngle(): Double {
        return MiscCalculations.wrapAroundAngles((MiscCalculations.wrapAroundAngles(encoder
            .absolutePosition.valueAsDouble * 360.0) - tbOffset))
    }

    override fun setAngle(angleDegrees: Double) {
        desiredAngle = angleDegrees
        pidController.goal = TrapezoidProfile.State(angleDegrees, 0.0)
    }

    override fun periodic() {
        val positionPIDOut = pidController.calculate(getAngle())

        motor.setControl(torqueRequest.withOutput(positionPIDOut))

        SmartDashboard.putNumber("Funnel Raw TB Angle", MiscCalculations.wrapAroundAngles(encoder.absolutePosition.valueAsDouble * 360.0))
    }
}