package frc.robot

enum class RobotState {
    Real,
    Simulated,
    Empty
}

object RobotConfiguration {
    val robotState = RobotState.Empty
}