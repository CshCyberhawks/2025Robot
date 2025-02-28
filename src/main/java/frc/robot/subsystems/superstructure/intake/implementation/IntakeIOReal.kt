package frc.robot.subsystems.superstructure.intake.implementation

import au.grapplerobotics.LaserCan
import au.grapplerobotics.interfaces.LaserCanInterface
import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.signals.InvertedValue
import edu.wpi.first.math.util.Units
import frc.robot.RobotState
import frc.robot.constants.CANConstants
import frc.robot.subsystems.superstructure.intake.GamePieceState
import frc.robot.subsystems.superstructure.intake.IntakeConstants
import frc.robot.subsystems.superstructure.intake.IntakeIO
import frc.robot.subsystems.superstructure.intake.IntakeState

class IntakeIOReal() : IntakeIO {
    private val intakeMotor = TalonFX(CANConstants.Intake.motorId)
    private val coralLaserCAN = LaserCan(CANConstants.Intake.coralLaserCANId)
    private val algaeLaserCAN = LaserCan(CANConstants.Intake.algaeLaserCANId)

    private var watchingForIntake = false

    init {
        val coralIntakeMotorConfiguration = TalonFXConfiguration()
        coralIntakeMotorConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive
        intakeMotor.configurator.apply(coralIntakeMotorConfiguration)

        coralLaserCAN.setRangingMode(LaserCanInterface.RangingMode.SHORT)
        //coralLaserCAN.setRegionOfInterest(RegionOfInterest(8, 8, 16, 16))
        coralLaserCAN.setTimingBudget(LaserCanInterface.TimingBudget.TIMING_BUDGET_33MS)

        algaeLaserCAN.setRangingMode(LaserCanInterface.RangingMode.SHORT)
        //algaeLaserCAN.setRegionOfInterest(RegionOfInterest(8, 8, 16, 16))
        algaeLaserCAN.setTimingBudget(LaserCanInterface.TimingBudget.TIMING_BUDGET_33MS)
    }

    override fun setIntakeState(state: IntakeState) {
        intakeMotor.set(state.speed)
    }

    override fun watchForIntake() {
        watchingForIntake = true
    }

    override fun hasAlgae(): Boolean {
        val measurement: LaserCanInterface.Measurement = coralLaserCAN.measurement
        @Suppress("SENSELESS_COMPARISON")
        return (measurement != null && measurement.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT && measurement.distance_mm < Units.inchesToMeters(
                10.0 // Other side is ~15.5in from sensor
            )
        )
    }

    override fun hasCoral(): Boolean {
        val measurement: LaserCanInterface.Measurement = coralLaserCAN.measurement
        @Suppress("SENSELESS_COMPARISON")
        return (measurement != null && measurement.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT && measurement.distance_mm < Units.inchesToMeters(
                1.0 // Plates are 2in apart
            )
        )
    }

    override fun periodic() {
        if (watchingForIntake && (hasCoral() || hasAlgae())) {
            intakeMotor.set(IntakeState.Idle.speed)
            watchingForIntake = false
        }
    }
}
