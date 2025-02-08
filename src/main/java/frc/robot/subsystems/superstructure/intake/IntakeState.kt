package frc.robot.subsystems.superstructure

// Setting the motor positive will both intake coral from substation and score onto reef
enum class CoralIntakeState(val speed: Double) {
    Intaking(0.75),
    Idle(0.0),
    Scoring(0.5)
}

// Positive is scoring and negative is intaking
enum class AlgaeIntakeState(val speed: Double) {
    Intaking(-0.8),
    Idle(0.0),
    Scoring(0.5),
    Holding(-0.05)
}

enum class CoralState {
    Empty,
    Stored
}

enum class AlgaeState {
    Empty,
    Stored
}