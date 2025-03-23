package frc.robot.subsystems.superstructure.intake.implementation

import au.grapplerobotics.LaserCan
import au.grapplerobotics.interfaces.LaserCanInterface
import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC
import com.ctre.phoenix6.controls.TorqueCurrentFOC
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.signals.InvertedValue
import cshcyberhawks.lib.math.Timer
import edu.wpi.first.math.util.Units
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.robot.constants.CANConstants
import frc.robot.subsystems.superstructure.intake.IntakeIO
import frc.robot.subsystems.superstructure.intake.IntakeState

class IntakeIOReal() : IntakeIO {
    private val intakeMotor = TalonFX(CANConstants.Intake.motorId)
    private val coralLaserCAN = LaserCan(CANConstants.Intake.coralLaserCANId)
    private val algaeLaserCAN = LaserCan(CANConstants.Intake.algaeLaserCANId)

    private val torqueRequest = TorqueCurrentFOC(0.0)

    private var currentIntakeState = IntakeState.Idle

    init {
        val coralIntakeMotorConfiguration = TalonFXConfiguration()
//        coralIntakeMotorConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive
        println("WHY")
//        coralIntakeMotorConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive

        val currentConfigs = coralIntakeMotorConfiguration.CurrentLimits
        currentConfigs.StatorCurrentLimitEnable = true
        currentConfigs.StatorCurrentLimit = 60.0

//        intakeMotor.configurator.apply(coralIntakeMotorConfiguration)

        coralLaserCAN.setRangingMode(LaserCanInterface.RangingMode.SHORT)
        //coralLaserCAN.setRegionOfInterest(RegionOfInterest(8, 8, 16, 16))
        coralLaserCAN.setTimingBudget(LaserCanInterface.TimingBudget.TIMING_BUDGET_33MS)

        algaeLaserCAN.setRangingMode(LaserCanInterface.RangingMode.SHORT)
        //algaeLaserCAN.setRegionOfInterest(RegionOfInterest(8, 8, 16, 16))
        algaeLaserCAN.setTimingBudget(LaserCanInterface.TimingBudget.TIMING_BUDGET_33MS)
    }

    override fun setIntakeState(state: IntakeState) {
        println("Intake State: ${state.name}")
        println("Intake state current: ${state.current}")
        if (state == IntakeState.AlgaeHolding) {
            intakeMotor.set(-0.02)
        } else {
            intakeMotor.setControl(torqueRequest.withOutput(state.current))
        }
        currentIntakeState = state
    }

    override fun hasAlgae(): Boolean {
        val measurement: LaserCanInterface.Measurement = algaeLaserCAN.measurement
        SmartDashboard.putNumber("Algae Measurement", measurement.distance_mm.toDouble())
        @Suppress("SENSELESS_COMPARISON")
        return (measurement != null && measurement.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT && measurement.distance_mm < Units.inchesToMeters(
//                12.0 // Other side is ~15.5in from sensor
            8.0 // Other side is ~15.5in from sensor

        ) * 1000.0
        )
    }

    override fun hasCoral(): Boolean {
        val measurement: LaserCanInterface.Measurement = coralLaserCAN.measurement
        SmartDashboard.putNumber("Coral Measurement", measurement.distance_mm.toDouble())
        @Suppress("SENSELESS_COMPARISON")
        return (measurement != null && measurement.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT && measurement.distance_mm < 40.0)
    }


    override fun periodic() {
//        SmartDashboard.putNumber("Intake position", intakeMotor.position.valueAsDouble)
//        SmartDashboard.putNumber("Intake velocity", intakeMotor.velocity.valueAsDouble)
//        SmartDashboard.putNumber("Intake acceleration", intakeMotor.acceleration.valueAsDouble)
    }
}
