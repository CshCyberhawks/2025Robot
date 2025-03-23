package frc.robot.subsystems.superstructure

import cshcyberhawks.lib.requests.*
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.RobotConfiguration
import frc.robot.RobotContainer
import frc.robot.RobotState
import frc.robot.RobotType
import frc.robot.constants.FieldConstants
import frc.robot.subsystems.superstructure.climb.ClimbSystem
import frc.robot.subsystems.superstructure.climb.implementation.ClimbIOEmpty
import frc.robot.subsystems.superstructure.climb.implementation.ClimbIOReal
import frc.robot.subsystems.superstructure.elevator.ElevatorSystem
import frc.robot.subsystems.superstructure.elevator.implementation.ElevatorIOEmpty
import frc.robot.subsystems.superstructure.elevator.implementation.ElevatorIOPID
import frc.robot.subsystems.superstructure.elevator.implementation.ElevatorIOSim
import frc.robot.subsystems.superstructure.funnel.FunnelSystem
import frc.robot.subsystems.superstructure.funnel.implementation.FunnelIOEmpty
import frc.robot.subsystems.superstructure.funnel.implementation.FunnelIOReal
import frc.robot.subsystems.superstructure.intake.GamePieceState
import frc.robot.subsystems.superstructure.intake.IntakeSystem
import frc.robot.subsystems.superstructure.intake.implementation.IntakeIOEmpty
import frc.robot.subsystems.superstructure.intake.implementation.IntakeIOReal
import frc.robot.subsystems.superstructure.pivot.PivotSystem
import frc.robot.subsystems.superstructure.pivot.implementation.PivotIOEmpty
import frc.robot.subsystems.superstructure.pivot.implementation.PivotIOPID
import frc.robot.subsystems.superstructure.pivot.implementation.PivotIOSim
import frc.robot.util.AllianceFlipUtil
import frc.robot.util.input.OperatorControls
import java.util.*

object Superstructure : SubsystemBase() {
    val pivotSystem =
        PivotSystem(
            when (RobotConfiguration.robotType) {
//                RobotType.Real -> PivotIOEmpty()
                RobotType.Real -> PivotIOPID()
                RobotType.Simulated -> PivotIOSim()
                RobotType.Empty -> PivotIOEmpty()
            }
        )
    val elevatorSystem =
        ElevatorSystem(
            when (RobotConfiguration.robotType) {
                RobotType.Real -> ElevatorIOPID()
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
    val climbSystem = ClimbSystem(
        when (RobotConfiguration.robotType) {
            RobotType.Real -> ClimbIOReal()
            RobotType.Simulated -> ClimbIOEmpty()
            RobotType.Empty -> ClimbIOEmpty()
        }
    )
    val funnelSystem = FunnelSystem(
        when (RobotConfiguration.robotType) {
            RobotType.Real -> FunnelIOReal()
            RobotType.Simulated -> FunnelIOEmpty()
            RobotType.Empty -> FunnelIOEmpty()
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

    const val safeRetractReefDistance = 1.6

    fun safeToRetract(): Boolean {
        val swervePose = RobotContainer.drivetrain.getSwervePose()
        val blueReefDistance = FieldConstants.Reef.center.getDistance(swervePose.translation) > safeRetractReefDistance
        val redReefDistance =
            AllianceFlipUtil.apply(FieldConstants.Reef.center)
                .getDistance(swervePose.translation) > safeRetractReefDistance
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

    private fun unHalfSpitRequest() = IfRequest(
        { !RobotState.actionConfirmed },
        SequentialRequest(WaitRequest(.25), intakeSystem.coralIntake(), WaitRequest(0.25), intakeSystem.idle())
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
                ParallelRequest(
                    IfRequest(
                        {RobotState.gamePieceState == GamePieceState.Coral && OperatorControls.highStowPosition},
                        ParallelRequest(
                            elevatorSystem.stowPosition(), pivotSystem.highStowAngle()
                        ),
                        stowRequest()
                    ),
                    IfRequest(
                        { RobotState.gamePieceState == GamePieceState.Coral },
                        intakeSystem.coralHolding(),
                        intakeSystem.idle()
                    )
                ),
                confirmed = { RobotState.gamePieceState == GamePieceState.Coral }
            )
        )

    fun correctIntake() = requestSuperstructureAction(
        SuperstructureAction.create(
            intakeSystem.coralIntake(),
            EmptyRequest(),
            SequentialRequest(
                WaitRequest(0.1),
                intakeSystem.idle()
            ),
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
                ParallelRequest(pivotSystem.l3Angle(), elevatorSystem.l3Position(), intakeSystem.coralHalfSpit().withPrerequisites(
                    elevatorSystem.prereqAtDesiredPosition(), pivotSystem.prereqAtDesiredAngle())),
                intakeSystem.coralScore(),
                stowRequest(),
                safeRetract = true
            )
        )

    private fun l4PrepRequest() = SequentialRequest(
        ParallelRequest(
            pivotSystem.l4Angle(),
            elevatorSystem.safeUpPosition(),
        ),
        ParallelRequest( // Sketchy fix to work around prereqs not working in Sequential Requests
            elevatorSystem
                .l4Position()
                .withPrerequisite(pivotSystem.safeTravelUp()),
            intakeSystem.coralHalfSpit().withPrerequisite(elevatorSystem.prereqAtDesiredPosition())
        ),
    )

    fun scoreL4() =
        requestSuperstructureAction(
            SuperstructureAction.create(
                l4PrepRequest(),
                intakeSystem.coralScore(),
//                safeRetractRequest(),
                SequentialRequest(
                    ParallelRequest(pivotSystem.travelAngle(), elevatorSystem.safeDownPosition()),
//                    WaitRequest(0.1),
                    ParallelRequest(
                        elevatorSystem.stowPosition().withPrerequisite(pivotSystem.safeTravelDown()),
                        pivotSystem.stowAngle().withPrerequisite(elevatorSystem.belowSafeUpPosition())
                    )
                ),
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
                ParallelRequest(
                    pivotSystem.stowAngle(),
                    elevatorSystem.stowPosition(),
                    IfRequest(
                        { RobotState.gamePieceState == GamePieceState.Empty },
                        intakeSystem.idle(),
                        intakeSystem.algaeHolding()
                    )
                ),
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
                ParallelRequest(
                    safeRetractRequest(),
                    IfRequest(
                        { RobotState.gamePieceState == GamePieceState.Empty },
                        intakeSystem.idle(),
                        intakeSystem.algaeHolding()
                    )
                ),
                { RobotState.gamePieceState == GamePieceState.Algae },
                safeRetract = true
            )
        )

    fun scoreProcessor() = requestSuperstructureAction(
        SuperstructureAction.create(
            ParallelRequest(elevatorSystem.processorPosition(), pivotSystem.processorAngle(), funnelSystem.deploy()),
            intakeSystem.algaeScore(),
            ParallelRequest(stowRequest(), funnelSystem.stow())
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

    fun climb() = requestSuperstructureAction(
        SuperstructureAction.create(
                ParallelRequest(
                pivotSystem.oldClimbAngle(),
                climbSystem.deploy(),
                funnelSystem.deploy()
                ),
            climbSystem.climb(),
            EmptyRequest()//IfRequest({RobotState.actionCancelled}, SequentialRequest(ParallelRequest(funnelSystem.stow(), climbSystem.stow())), pivotSystem.stowAngle())
        )
    )


    fun unclimb() = requestSuperstructureAction(
        SequentialRequest(
            ParallelRequest(
                climbSystem.deploy(),
                funnelSystem.deploy(),
                pivotSystem.climbStowAngle()
            ),
//            stowRequest().withPrerequisite(Prerequisite.withCondition { climbSystem.isStow() && funnelSystem.isStow() })
        )
    )

    fun climbStowThenStow() = requestSuperstructureAction(
        SequentialRequest(
            ParallelRequest(climbSystem.stow(), pivotSystem.climbStowAngle()),
            funnelSystem.stow().withPrerequisite(Prerequisite.withCondition{climbSystem.isStow()}),
            WaitRequest(2.5),
            stowRequest().withPrerequisite(Prerequisite.withCondition { climbSystem.isStow() && funnelSystem.isStow() })
        )
    )


//    object Auto {
//        fun prepL4() = requestSuperstructureAction(
//            SuperstructureAction.create(
//                l4PrepRequest(),
//                EmptyRequest(),
//                EmptyRequest(),
//                { true },
//                { true }
//            )
//        )
//
//        fun justScoreL4() = requestSuperstructureAction(
//            SuperstructureAction.create(
//                EmptyRequest(),
//                intakeSystem.coralScore(),
//                safeRetractRequest(),
//                { true },
//                safeRetract = true
//            )
//        )
//    }
}
