package frc.robot.subsystems.superstructure.intake

// Setting the motor positive will intake both coral and algae
enum class IntakeState(val current: Double) {
    Idle(0.0),
    CoralIntake(-40.0),
    CoralHalfSpit(15.0),
    CoralHolding(-1.0),
    CoralScore(200.0),
    AlgaeIntake(40.0),
    AlgaeScore(-40.0),
    AlgaeHolding(1.0)
}

enum class GamePieceState {
    Empty,
    Coral,
    Algae
}
