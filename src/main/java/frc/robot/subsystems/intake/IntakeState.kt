package frc.robot.subsystems.intake

enum class CoralIntakeState(val speed: Double) {
    Intaking(0.75),
    Idle(0.0),
    Scoring(0.5)
}

enum class AlgaeIntakeState {
    Intaking,
    Idle,
    Scoring
}

enum class CoralState {
    Empty,
    Stored
}

enum class AlgaeState {
    Empty,
    Stored
}