package frc.robot.subsystems.superstructure

import cshcyberhawks.lib.requests.*
import frc.robot.RobotState

object SuperstructureAction {
    fun create(
        prepAction: Request,
        confirmAction: Request,
        returnAction: Request,
        confirmed: () -> Boolean = { RobotState.actionConfirmed },
        cancelled: () -> Boolean = { RobotState.actionCancelled },
        safeRetract: Boolean = false
    ): Request = SequentialRequest(
        Request.withAction {
            RobotState.actionCancelled = false
            RobotState.actionConfirmed = false
            RobotState.superstructureActionRunning = true
        },
        prepAction,
        AwaitRequest {
            confirmed() || cancelled()
        },
        IfRequest(confirmed, confirmAction),
        IfRequest({ safeRetract }, AwaitRequest {
            Superstructure.safeToRetract()
        }),
        returnAction,
//        Superstructure.awaitAtDesiredPosition(),
        Request.withAction {
            RobotState.superstructureActionRunning = false
        }
    )
}