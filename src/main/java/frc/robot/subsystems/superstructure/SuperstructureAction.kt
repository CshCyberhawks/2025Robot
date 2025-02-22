package frc.robot.subsystems.superstructure

import cshcyberhawks.lib.requests.*
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.RobotState

//class SuperstructureAction(
//    private val prepAction: Request,
//    private val confirmAction: Request,
//    private val returnAction: Request,
//    private val confirmed: () -> Boolean = { RobotState.actionConfirmed },
//    private val cancelled: () -> Boolean = { RobotState.actionCancelled }
//) : Request() {
//    private var confirmActionScheduled = false
//    private var returnActionScheduled = false
//    private var cancelledCaught = false
//
//    private var currentCommand = EmptyRequest()
//
//    private fun setCurrentAction(cmd: Request) {
//        currentCommand = cmd
//        currentCommand.execute()
//    }
//
//    override fun initialize() {
//        RobotState.actionCancelled = false
//        RobotState.actionConfirmed = false
//
//        confirmActionScheduled = false
//        returnActionScheduled = false
//        cancelledCaught = false
//
//        setCurrentAction(prepAction)
//    }
//
//    override fun execute() {
//        SmartDashboard.putBoolean("Prep Finished", prepAction.isFinished)
//        SmartDashboard.putBoolean("Confirm Finished", confirmAction.isFinished)
//        SmartDashboard.putBoolean("Return Finished", returnAction.isFinished)
//        SmartDashboard.putBoolean("Current Finished", currentCommand.isFinished)
//
//        if (cancelledCaught) return
//
////        println(cancelled())
//
//        if (cancelled()) {
//            println("Cancelled")
//            cancelledCaught = true
//            RobotState.actionCancelled = false
//            setCurrentAction(returnAction)
//            returnActionScheduled = true
//        }
//
//        if (prepAction.isFinished && confirmed()) {
//            println("Confirmed")
//            RobotState.actionConfirmed = false
//            setCurrentAction(confirmAction)
//            confirmActionScheduled = true
//        }
//
//        if (confirmActionScheduled && confirmAction.isFinished) {
//            setCurrentAction(returnAction)
//            returnActionScheduled = true
//        }
//    }
//
//    override fun isFinished(): Boolean {
//        return returnActionScheduled && returnAction.isFinished
//    }
//
//    override fun end(interrupted: Boolean) {
//        RobotState.actionCancelled = false
//        RobotState.actionConfirmed = false
//    }
//}

object SuperstructureAction {
    fun create(
        prepAction: Request,
        confirmAction: Request,
        returnAction: Request,
        confirmed: () -> Boolean = { RobotState.actionConfirmed },
        cancelled: () -> Boolean = { RobotState.actionCancelled }
    ): Request = SequentialRequest(
        prepAction,
        AwaitRequest { confirmed() || cancelled() },
        ParallelRequest(
            IfRequest(confirmed, confirmAction),
            Request.withAction {
                // Relying on that fact the IfRequest determines branching on execute even if blocked by Prereqs here
                // Makes it decide slightly before robot the action state is reset even if it can't act
                RobotState.actionCancelled = false
                RobotState.actionConfirmed = false
            }
        ),
        returnAction
    )
}