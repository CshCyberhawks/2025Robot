package frc.robot.subsystems.superstructure

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.RobotState

class SuperstructureAction(
    private val prepAction: Command,
    private val confirmAction: Command,
    private val returnAction: Command,
    private val confirmed: () -> Boolean = { RobotState.actionConfirmed },
    private val cancelled: () -> Boolean = { RobotState.actionCancelled }
) : Command() {
    private var confirmActionScheduled = false
    private var returnActionScheduled = false
    private var cancelledCaught = false

    private var currentCommand = Commands.runOnce({})

    private fun setCurrentAction(cmd: Command) {
        currentCommand.cancel()
        currentCommand = cmd
        currentCommand.schedule()
    }

    override fun initialize() {
        RobotState.actionCancelled = false
        RobotState.actionConfirmed = false

        confirmActionScheduled = false
        returnActionScheduled = false
        cancelledCaught = false

        setCurrentAction(prepAction)
    }

    override fun execute() {
        SmartDashboard.putBoolean("Prep Finished", prepAction.isFinished)
        SmartDashboard.putBoolean("Confirm Finished", confirmAction.isFinished)
        SmartDashboard.putBoolean("Return Finished", returnAction.isFinished)
        SmartDashboard.putBoolean("Current Finished", currentCommand.isFinished)

        if (cancelledCaught) return

//        println(cancelled())

        if (cancelled()) {
            println("Cancelled")
            cancelledCaught = true
            RobotState.actionCancelled = false
            setCurrentAction(returnAction)
            returnActionScheduled = true
        }

        if (prepAction.isFinished && confirmed()) {
            println("Confirmed")
            RobotState.actionConfirmed = false
            setCurrentAction(confirmAction)
            confirmActionScheduled = true
        }

        if (confirmActionScheduled && confirmAction.isFinished) {
            setCurrentAction(returnAction)
            returnActionScheduled = true
        }
    }

    override fun isFinished(): Boolean {
        return returnActionScheduled && returnAction.isFinished
    }

    override fun end(interrupted: Boolean) {
        RobotState.actionCancelled = false
        RobotState.actionConfirmed = false
    }
}