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
import frc.robot.RobotState
import frc.robot.constants.CANConstants
import frc.robot.subsystems.superstructure.intake.GamePieceState
import frc.robot.subsystems.superstructure.intake.IntakeConstants
import frc.robot.subsystems.superstructure.intake.IntakeIO
import frc.robot.subsystems.superstructure.intake.IntakeState

class IntakeIOReal : IntakeIO {
    private val intakeMotor = TalonFX(CANConstants.Intake.motorId)

    private var intakeState = IntakeState.Idle

    private var watchingCurrent = false

    init {
        val coralIntakeMotorConfiguration = TalonFXConfiguration()
        coralIntakeMotorConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive
        intakeMotor.configurator.apply(coralIntakeMotorConfiguration)
    }

    override fun setIntakeState(state: IntakeState) {
        intakeState = state
        intakeMotor.set(state.speed)
    }

    override fun watchForIntake() {
        watchingCurrent = true
    }

    override fun periodic() {
        if (watchingCurrent) {
            if (intakeMotor.supplyCurrent.valueAsDouble > IntakeConstants.intakeCurrentThreshold) {
                when (intakeState) {
                    IntakeState.CoralIntake -> {
                        RobotState.gamePieceState = GamePieceState.Coral
                    }

                    IntakeState.AlgaeIntake -> {
                        RobotState.gamePieceState = GamePieceState.Algae
                    }

                    else -> {}
                }

                setIntakeState(IntakeState.Idle)

                watchingCurrent = false
            }
        }
    }
}
