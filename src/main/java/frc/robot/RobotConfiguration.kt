package frc.robot

import frc.robot.util.IO.InputSystem
import frc.robot.util.IO.ManualOperatorInput

enum class RobotType {
    Real,
    Simulated,
    Empty
}

enum class OperatorType {
    Manual
}

object RobotConfiguration {
    val robotType = RobotType.Empty

    val operatorType = OperatorType.Manual
}