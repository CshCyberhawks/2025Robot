package frc.robot

enum class RobotType {
    Real,
    Simulated,
    Empty
}

object RobotConfiguration {
    val robotType = RobotType.Empty
}