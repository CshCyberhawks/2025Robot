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
    private val intakeMotor = TalonFX(IntakeConstants.coralMotorId)

    private val algaeLaserCAN = LaserCan(IntakeConstants.algaeLaserCANId)


    init {
        val intakeMotorConfiguration = TalonFXConfiguration()
        intakeMotorConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive
        intakeMotor.configurator.apply(intakeMotorConfiguration)


        //TODO: no cans :( use motor current
        //TODO: removed the laser CAN


        algaeLaserCAN.setRangingMode(LaserCanInterface.RangingMode.SHORT)
        //algaeLaserCAN.setRegionOfInterest(RegionOfInterest(8, 8, 16, 16))
        algaeLaserCAN.setTimingBudget(LaserCanInterface.TimingBudget.TIMING_BUDGET_33MS)
    }

    override fun setCoralIntakeState(state: CoralIntakeState) {
        intakeMotor.set(state.speed)
    }

    override fun setAlgaeIntakeState(state: AlgaeIntakeState) {
        intakeMotor.set(state.speed)
    }

    override fun getCoralIntakeCurrent(): Double {
        return intakeMotor.getSupplyCurrent(true).valueAsDouble
    }
    override fun getAlgaeState(): AlgaeState {
        val measurement: Measurement = algaeLaserCAN.measurement
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

    override fun getCoralState():CoralState{
        return if (getCoralIntakeCurrent() > IntakeConstants.coralIntakeCurrentThreshold) CoralState.Stored else CoralState.Empty
    }
}
