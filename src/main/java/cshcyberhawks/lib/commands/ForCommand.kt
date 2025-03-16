package cshcyberhawks.lib.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
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
        SmartDashboard.putNumber("For Command i", i.toDouble())
        SmartDashboard.putBoolean("For Command current finished", currentCommand.isFinished)
        if (currentCommand.isFinished && i < max) {
            i++
            currentCommand = callback(i)
            currentCommand.schedule()
        }
    }

    override fun isFinished(): Boolean {
        SmartDashboard.putBoolean("For command finished", i >= max && currentCommand.isFinished)
        return i >= max && currentCommand.isFinished
    }
}