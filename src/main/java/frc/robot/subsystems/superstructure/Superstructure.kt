package frc.robot.subsystems.superstructure

import cshcyberhawks.lib.requests.*
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.RobotConfiguration
import frc.robot.RobotContainer
import frc.robot.RobotState
import frc.robot.RobotType
import frc.robot.constants.FieldConstants
import frc.robot.subsystems.superstructure.elevator.ElevatorSystem
import frc.robot.subsystems.superstructure.elevator.implementation.ElevatorIOEmpty
import frc.robot.subsystems.superstructure.elevator.implementation.ElevatorIOPID
import frc.robot.subsystems.superstructure.elevator.implementation.ElevatorIOSim
import frc.robot.subsystems.superstructure.intake.GamePieceState
import frc.robot.subsystems.superstructure.intake.IntakeSystem
import frc.robot.subsystems.superstructure.intake.implementation.IntakeIOEmpty
import frc.robot.subsystems.superstructure.intake.implementation.IntakeIOReal
import frc.robot.subsystems.superstructure.pivot.PivotSystem
import frc.robot.subsystems.superstructure.pivot.implementation.PivotIOEmpty
import frc.robot.subsystems.superstructure.pivot.implementation.PivotIOPID
import frc.robot.subsystems.superstructure.pivot.implementation.PivotIOSim
import frc.robot.util.AllianceFlipUtil
import java.util.*

object Superstructure : SubsystemBase() {
    val pivotSystem =
        PivotSystem(
            when (RobotConfiguration.robotType) {
                RobotType.Real -> PivotIOPID()
//                RobotType.Real -> PivotIOEmpty()
                RobotType.Simulated -> PivotIOSim()
                RobotType.Empty -> PivotIOEmpty()
            }
        )
    val elevatorSystem =
        ElevatorSystem(
            when (RobotConfiguration.robotType) {
                RobotType.Real -> ElevatorIOPID()
//                RobotType.Real -> ElevatorIOEmpty()
                RobotType.Simulated -> ElevatorIOSim()
                RobotType.Empty -> ElevatorIOEmpty()
            }
        )
    val intakeSystem =
        IntakeSystem(
            when (RobotConfiguration.robotType) {
                RobotType.Real -> IntakeIOReal()
//                RobotType.Real -> IntakeIOEmpty()
                RobotType.Simulated -> IntakeIOEmpty()
                RobotType.Empty -> IntakeIOEmpty()
            }
        )

    // Requests system
    private var activeRequest: Optional<Request> = Optional.empty()
    private var queuedRequests = mutableListOf<Request>()
    private var hasNewRequest = false
    private var allRequestsComplete = false

    fun requestsCompleted() = allRequestsComplete

    fun request(r: Request) {
        setActiveRequest(r)
        clearRequestQueue()
    }

    private fun setActiveRequest(request: Request) {
        activeRequest = Optional.of(request)
        hasNewRequest = true
        allRequestsComplete = false
    }

    private fun clearRequestQueue() {
        queuedRequests.clear()
    }

    private fun addRequestToQueue(req: Request) {
        queuedRequests.add(req)
    }

    // Should be called when robot is enabled (teleop and auto)
    fun initialize() {
        clearRequestQueue()
    }

    override fun periodic() {
        if (hasNewRequest && activeRequest.isPresent) {
            activeRequest.get().execute()
            hasNewRequest = false
        }

        if (activeRequest.isEmpty) {
            if (queuedRequests.isEmpty()) {
                allRequestsComplete = true
            } else {
                request(queuedRequests.removeAt(0))
            }
        } else if (activeRequest.get().isFinished()) {
            activeRequest = Optional.empty()
        }

        SmartDashboard.putBoolean("Has new request", hasNewRequest)
        SmartDashboard.putBoolean("All Requests Complete", allRequestsComplete)
    }

    fun requestSuperstructureAction(action: Request) {
        if (!RobotState.superstructureActionRunning) {
            request(action)
        } else {
            queuedRequests.clear()
            queuedRequests.add(action)
        }
    }

    fun awaitAtDesiredPosition() =
        ParallelRequest(elevatorSystem.awaitDesiredPosition(), pivotSystem.awaitDesiredAngle())

    fun safeToRetract(): Boolean {
        val swervePose = RobotContainer.drivetrain.getSwervePose()
        val blueReefDistance = FieldConstants.Reef.center.getDistance(swervePose.translation) > 1.7
        val redReefDistance =
            AllianceFlipUtil.apply(FieldConstants.Reef.center).getDistance(swervePose.translation) > 1.7
        val clearOfBarge = swervePose.x < 7.0 || swervePose.x > 10.5

        return blueReefDistance && redReefDistance && clearOfBarge
    }

    private fun stowRequest() =
        ParallelRequest(elevatorSystem.stowPosition(), pivotSystem.stowAngle())

    private fun safeRetractRequest() =
        SequentialRequest(
            ParallelRequest(pivotSystem.travelAngle(), elevatorSystem.safeDownPosition()),
            WaitRequest(0.1),
            ParallelRequest(
                elevatorSystem.stowPosition().withPrerequisite(pivotSystem.safeTravelDown()),
                pivotSystem.stowAngle().withPrerequisite(elevatorSystem.belowSafeUpPosition())
            )
        )

    fun stow() = requestSuperstructureAction(stowRequest())

    fun intakeFeeder() =
        requestSuperstructureAction(
            SuperstructureAction.create(
                ParallelRequest(
                    elevatorSystem.feederPosition(),
                    pivotSystem.feederAngle().withPrerequisite(elevatorSystem.safeIntakePosition()),
                    intakeSystem.coralIntake()
                ),
                EmptyRequest(),
                ParallelRequest(stowRequest(), intakeSystem.idle()),
                confirmed = { RobotState.gamePieceState == GamePieceState.Coral }
            )
        )

    fun scoreL2() =
        requestSuperstructureAction(
            SuperstructureAction.create(
                ParallelRequest(pivotSystem.l2Angle(), elevatorSystem.l2Position()),
                intakeSystem.coralScore(),
                stowRequest(),
                safeRetract = true
            )
        )

    fun scoreL3() =
        requestSuperstructureAction(
            SuperstructureAction.create(
                ParallelRequest(pivotSystem.l3Angle(), elevatorSystem.l3Position()),
                intakeSystem.coralScore(),
                stowRequest(),
                safeRetract = true
            )
        )

    private fun l4PrepRequest() = SequentialRequest(
        ParallelRequest(
            pivotSystem.l4Angle(),
            elevatorSystem.safeUpPosition(),
            intakeSystem.coralHalfSpit()
        ),
        elevatorSystem
            .l4Position()
            .withPrerequisite(pivotSystem.safeTravelUp()),
    )

    fun scoreL4() =
        requestSuperstructureAction(
            SuperstructureAction.create(
                l4PrepRequest(),
                intakeSystem.coralScore(),
                safeRetractRequest(),
                safeRetract = true
            )
        )

//    fun pivotTest() = requestSuperstructureAction(SuperstructureAction.create())

    fun removeAlgaeLow() =
        requestSuperstructureAction(
            SuperstructureAction.create(
                ParallelRequest(
                    pivotSystem.algaeRemoveAngle(),
                    elevatorSystem.algaeRemoveLowPosition(),
                    intakeSystem.algaeIntake()
                ),
                EmptyRequest(),
                ParallelRequest(pivotSystem.stowAngle(), elevatorSystem.stowPosition()),
                { RobotState.gamePieceState == GamePieceState.Algae },
                safeRetract = true
            )
        )

    fun removeAlgaeHigh() =
        requestSuperstructureAction(
            SuperstructureAction.create(
                SequentialRequest(
                    ParallelRequest(
                        pivotSystem.algaeRemoveAngle(),
                        elevatorSystem.safeUpPosition(),
                    ),
                    elevatorSystem
                        .algaeRemoveHighPosition()
                        .withPrerequisite(pivotSystem.safeTravelUp()),
                    intakeSystem.algaeIntake()
                ),
                EmptyRequest(),
                safeRetractRequest(),
                { RobotState.gamePieceState == GamePieceState.Algae },
                safeRetract = true
            )
        )

    fun scoreProcessor() = requestSuperstructureAction(
        SuperstructureAction.create(
            ParallelRequest(elevatorSystem.processorPosition(), pivotSystem.processorAngle()),
            intakeSystem.algaeScore(),
            stowRequest()
        )
    )

    fun scoreBarge() =
        requestSuperstructureAction(
            SuperstructureAction.create(
                SequentialRequest(
                    ParallelRequest(
                        pivotSystem.bargeAngle(),
                        elevatorSystem.safeUpPosition(),
                    ),
                    elevatorSystem
                        .bargePosition()
                        .withPrerequisite(pivotSystem.safeTravelUp()),
                ),
                intakeSystem.algaeScore(),
                safeRetractRequest(),
                safeRetract = true
            )
        )

    object Auto {
        fun prepL4() = requestSuperstructureAction(
            SuperstructureAction.create(
                l4PrepRequest(),
                EmptyRequest(),
                EmptyRequest(),
                { true },
                { true }
            )
        )

        fun justScoreL4() = requestSuperstructureAction(
            SuperstructureAction.create(
                EmptyRequest(),
                intakeSystem.coralScore(),
                safeRetractRequest(),
                { true },
                safeRetract = true
            )
        )
    }
}
