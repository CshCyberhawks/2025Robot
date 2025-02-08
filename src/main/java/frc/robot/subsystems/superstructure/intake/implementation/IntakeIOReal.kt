package frc.robot.subsystems.superstructure.intake.implementation

import au.grapplerobotics.LaserCan
import au.grapplerobotics.interfaces.LaserCanInterface
import au.grapplerobotics.interfaces.LaserCanInterface.Measurement
import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.signals.InvertedValue
import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkLowLevel
import com.revrobotics.spark.SparkMax
import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.math.util.Units
import frc.robot.subsystems.superstructure.intake.AlgaeIntakeState
import frc.robot.subsystems.superstructure.intake.AlgaeState
import frc.robot.subsystems.superstructure.intake.CoralIntakeState
import frc.robot.subsystems.superstructure.intake.CoralState
import frc.robot.subsystems.superstructure.intake.IntakeConstants
import frc.robot.subsystems.superstructure.intake.IntakeIO

class IntakeIOReal : IntakeIO {
    private val coralIntakeMotor = TalonFX(IntakeConstants.coralMotorId)
    private val algaeMotor = SparkMax(IntakeConstants.algaeMotorId, SparkLowLevel.MotorType.kBrushless)

    private val coralLaserCAN = LaserCan(IntakeConstants.coralLaserCANId)
    private val algaeLaserCAN = LaserCan(IntakeConstants.algaeLaserCANId)


    init {
        val coralIntakeMotorConfiguration = TalonFXConfiguration()
        coralIntakeMotorConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive
        coralIntakeMotor.configurator.apply(coralIntakeMotorConfiguration)

        val algaeMotorConfiguration = SparkMaxConfig()
        algaeMotorConfiguration.inverted(false)
        algaeMotor.configure(
            algaeMotorConfiguration,
            SparkBase.ResetMode.kResetSafeParameters,
            SparkBase.PersistMode.kPersistParameters
        )

        coralLaserCAN.setRangingMode(LaserCanInterface.RangingMode.SHORT)
        //coralLaserCAN.setRegionOfInterest(RegionOfInterest(8, 8, 16, 16))
        coralLaserCAN.setTimingBudget(LaserCanInterface.TimingBudget.TIMING_BUDGET_33MS)

        algaeLaserCAN.setRangingMode(LaserCanInterface.RangingMode.SHORT)
        //algaeLaserCAN.setRegionOfInterest(RegionOfInterest(8, 8, 16, 16))
        algaeLaserCAN.setTimingBudget(LaserCanInterface.TimingBudget.TIMING_BUDGET_33MS)
    }

    override fun setCoralIntakeState(state: CoralIntakeState) {
        coralIntakeMotor.set(state.speed)
    }

    override fun setAlgaeIntakeState(state: AlgaeIntakeState) {
        algaeMotor.set(state.speed)
    }

    override fun getCoralState(): CoralState {
        val measurement: Measurement = coralLaserCAN.measurement
        @Suppress("SENSELESS_COMPARISON")
        return if (measurement != null && measurement.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT && measurement.distance_mm < Units.inchesToMeters(
                3.5 // Plates are 4.75in apart
            )
        ) {
            CoralState.Stored
        } else {
            CoralState.Empty
        }
    }

    override fun getAlgaeState(): AlgaeState {
        val measurement: Measurement = coralLaserCAN.measurement
        @Suppress("SENSELESS_COMPARISON")
        return if (measurement != null && measurement.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT && measurement.distance_mm < Units.inchesToMeters(
                12.5 // Other side is ~17.5in from sensor
            )
        ) {
            AlgaeState.Stored
        } else {
            AlgaeState.Empty
        }
    }
}
