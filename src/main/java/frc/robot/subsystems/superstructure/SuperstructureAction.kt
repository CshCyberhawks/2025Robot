package frc.robot.subsystems.superstructure

import cshcyberhawks.lib.requests.*
import frc.robot.RobotState

object SuperstructureAction {
    fun create(
        prepAction: Request,
        confirmAction: Request,
        returnAction: Request,
        confirmed: () -> Boolean = { RobotState.actionConfirmed },
        cancelled: () -> Boolean = { RobotState.actionCancelled }
    ): Request = SequentialRequest(
        Request.withAction {
            RobotState.actionCancelled = false
            RobotState.actionConfirmed = false
        },
        prepAction,
        AwaitRequest {
            confirmed() || cancelled()
        },
        IfRequest(confirmed, confirmAction),
        returnAction
    )
}