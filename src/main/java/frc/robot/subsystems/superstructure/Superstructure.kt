package frc.robot.subsystems.superstructure

import cshcyberhawks.lib.requests.CompleteRequest
import cshcyberhawks.lib.requests.EmptyRequest
import cshcyberhawks.lib.requests.Request
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.ConditionalCommand
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.RobotConfiguration
import frc.robot.RobotType
import frc.robot.subsystems.superstructure.elevator.ElevatorSystem
import frc.robot.subsystems.superstructure.elevator.implementation.ElevatorIOEmpty
import frc.robot.subsystems.superstructure.elevator.implementation.ElevatorIOReal
import frc.robot.subsystems.superstructure.elevator.implementation.ElevatorIOSim
import frc.robot.subsystems.superstructure.intake.implementation.IntakeIOEmpty
import frc.robot.subsystems.superstructure.pivot.PivotSystem
import frc.robot.subsystems.superstructure.intake.IntakeSystem
import frc.robot.subsystems.superstructure.intake.implementation.IntakeIOReal
import frc.robot.subsystems.superstructure.pivot.implementation.PivotIOEmpty
import frc.robot.subsystems.superstructure.pivot.implementation.PivotIOReal
import frc.robot.subsystems.superstructure.pivot.implementation.PivotIOSim
import frc.robot.util.commandsystem.IfCommand
import java.util.Optional

object Superstructure : SubsystemBase() {
    val pivotSystem = PivotSystem(
        when (RobotConfiguration.robotType) {
            RobotType.Real -> PivotIOReal()
            RobotType.Simulated -> PivotIOSim()
            RobotType.Empty -> PivotIOEmpty()
        }
    )
    val elevatorSystem = ElevatorSystem(
        when (RobotConfiguration.robotType) {
            RobotType.Real -> ElevatorIOReal()
            RobotType.Simulated -> ElevatorIOSim()
            RobotType.Empty -> ElevatorIOEmpty()
        }
    )
    val intakeSystem = IntakeSystem(
        when (RobotConfiguration.robotType) {
            RobotType.Real -> IntakeIOReal()
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
        clearRequestQueue();
    }

    override fun periodic() {
        if (hasNewRequest && activeRequest.isPresent) {
            activeRequest.get().execute()
            hasNewRequest = false;
        }

        if (activeRequest.isEmpty) {
            if (queuedRequests.isEmpty()) {
                allRequestsComplete = true;
            } else {
                request(queuedRequests.removeAt(0));
            }
        } else if (activeRequest.get().isFinished()) {
            activeRequest = Optional.empty();
        }
    }

    private fun awaitAtDesiredPosition() =
        Commands.parallel(elevatorSystem.awaitDesiredPosition(), pivotSystem.awaitDesiredAngle())

    // Should be able to handle stowing from any position
    fun stow() = Commands.sequence(
        elevatorSystem.stowPosition(),
        pivotSystem.stowAngle()
    )

    fun intakeFeeder() = Commands.sequence(
        Commands.parallel(
            elevatorSystem.feederPosition(),
            pivotSystem.feederAngle(),
            intakeSystem.coralIntake()
        ),
        Commands.parallel(
            elevatorSystem.stowPosition(),
            pivotSystem.stowAngle()
        )
    )

    fun prepL4() = Commands.sequence(
        Commands.parallel(
            pivotSystem.l4Angle(),
            elevatorSystem.safeUpPosition()
        ),
        pivotSystem.awaitSafeTravelUp(),
        elevatorSystem.l4Position(),
//        elevatorSystem.awaitDesiredPosition(),
//        pivotSystem.awaitDesiredAngle()
    )


    fun scoreL4() = SuperstructureAction(
        prepL4(), Commands.runOnce({}), Commands.sequence(
            Commands.parallel(
                pivotSystem.travelAngle(),
                elevatorSystem.safeDownPosition()
            ),
            pivotSystem.awaitSafeTravelDown(),
            Commands.parallel(
                pivotSystem.stowAngle(),
                elevatorSystem.stowPosition()
            )
        )
    )
}