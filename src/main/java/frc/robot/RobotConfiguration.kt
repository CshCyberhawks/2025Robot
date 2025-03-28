package frc.robot

enum class RobotType {
    Real,
    Simulated,
    Empty
}

enum class OperatorType {
    Manual,
    Testing
}

object RobotConfiguration {
    val robotType = RobotType.Real

    val operatorType = OperatorType.Manual
}

