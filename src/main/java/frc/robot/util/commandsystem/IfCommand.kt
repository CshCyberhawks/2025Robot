package frc.robot.util.commandsystem

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands

/**
 * A command that conditionally executes one of two commands based on the value of a given condition when the command is scheduled.
 *
 * @param condition The condition to evaluate.
 * @param ifBranch The command to execute if the condition is true.
 * @param elseBranch The command to execute if the condition is false.
 */
class IfCommand(
    private val condition: () -> Boolean,
    private val ifBranch: Command,
    private val elseBranch: Command = Commands.runOnce({})
) : Command() {
    private var branch = Commands.runOnce({})
    private var branchSet = false

    override fun initialize() {
        branch =
            if (condition()) {
                ifBranch
            } else {
                elseBranch
            }

        branch.schedule()
        branchSet = true
    }

    override fun isFinished(): Boolean {
        println("If finished: $branchSet && ${branch.isFinished}")
        return branchSet && branch.isFinished
    }
}