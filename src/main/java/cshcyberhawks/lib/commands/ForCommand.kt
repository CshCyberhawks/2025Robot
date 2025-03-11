package cshcyberhawks.lib.commands

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands

class ForCommand(private val max: Int, private val callback: (i: Int) -> Command) : Command() {
    private var i = -1
    private var currentCommand = Commands.runOnce({})

    override fun initialize() {
        i = 0
        currentCommand = callback(i)
        currentCommand.schedule()
    }

    override fun execute() {
        if (currentCommand.isFinished && i < max) {
            i++
            currentCommand = callback(i)
            currentCommand.schedule()
        }
    }

    override fun isFinished() = i >= max && currentCommand.isFinished
}