package frc.robot.subsystems.intake.implementation

import au.grapplerobotics.LaserCan
import au.grapplerobotics.interfaces.LaserCanInterface
import au.grapplerobotics.interfaces.LaserCanInterface.Measurement
import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.signals.InvertedValue
import edu.wpi.first.math.util.Units
import frc.robot.subsystems.intake.*

class IntakeIOReal : IntakeIO {
    private val coralIntakeMotor = TalonFX(IntakeConstants.coralMotorId)

    private val coralLaserCAN = LaserCan(IntakeConstants.coralLaserCANId)


    init {
        val coralIntakeMotorConfiguration = TalonFXConfiguration()
        // Make it so that setting the motor positive will both intake coral from substation and score onto reef
        coralIntakeMotorConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive

        coralIntakeMotor.configurator.apply(coralIntakeMotorConfiguration)

        coralLaserCAN.setRangingMode(LaserCanInterface.RangingMode.SHORT)
        //coralLaserCAN.setRegionOfInterest(RegionOfInterest(8, 8, 16, 16))
        coralLaserCAN.setTimingBudget(LaserCanInterface.TimingBudget.TIMING_BUDGET_33MS)
    }

    override fun setCoralIntakeState(state: CoralIntakeState) {
        coralIntakeMotor.set(state.speed)
    }

    override fun setAlgaeIntakeState(state: AlgaeIntakeState) {
        TODO("Not yet implemented")
    }

    override fun getCoralState(): CoralState {
        val measurement: Measurement = coralLaserCAN.measurement
        @Suppress("SENSELESS_COMPARISON")
        return if (measurement != null && measurement.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT && measurement.distance_mm < Units.inchesToMeters(
                3.5
            )
        ) {
            CoralState.Stored
        } else {
            CoralState.Empty
        }

    }

    override fun getAlgaeState(): AlgaeState {
        TODO("Not yet implemented")
    }
}
