package frc.robot.subsystems.superstructure.intake

// Setting the motor positive will intake both coral and algae
enum class IntakeState(val current: Double) {
    Idle(0.0),
    CoralIntake(0.75),
    CoralScore(-0.5),
    AlgaeIntake(40.0),
    AlgaeScore(-40.0),
    AlgaeHolding(1.0)
}

enum class GamePieceState {
    Empty,
    Coral,
    Algae
}
