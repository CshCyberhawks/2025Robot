package cshcyberhawks.lib.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup

//class ForCommand(private val max: Int, private val callback: (i: Int) -> Command) : Command() {
//    private var i = -1
//    private var currentCommand = Commands.runOnce({})
//
//    override fun initialize() {
//        i = 0
//        currentCommand = callback(i)
//        currentCommand.schedule()
//    }
//
//    override fun execute() {
//        SmartDashboard.putNumber("For Command i", i.toDouble())
//        SmartDashboard.putBoolean("For Command current finished", currentCommand.isFinished)
//        if (currentCommand.isFinished) {
//            println("current command for is finished")
//        }
////        println("current command for finished: " + currentCommand.isFinished)
//        if (currentCommand.isFinished && i < max) {
//            i++
//            currentCommand = callback(i)
//            currentCommand.schedule()
//        }
//    }
//
//    override fun isFinished(): Boolean {
//        SmartDashboard.putBoolean("For command finished", i >= max && currentCommand.isFinished)
//        return i >= max && currentCommand.isFinished
//    }
//}

/**
 * A command that executes a sequence of commands based on a loop counter.
 *
 * @param max The maximum value for the loop counter (exclusive)
 * @param callback A function that takes the loop counter as input and returns a Command to execute
 *
 * Example usage:
 * ```
 * ForCommand(5) { i ->
 *   Commands.runOnce { println("Loop iteration $i") }
 * }
 * ```
 * This would print the loop counter 5 times in sequence upon scheduling the ForCommand.
 */
class ForCommand(private val max: Int, private val callback: (i: Int) -> Command) : SequentialCommandGroup(
    *(0..<max).map { callback(it) }.toTypedArray()
)