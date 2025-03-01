package frc.robot.subsystems.superstructure.intake

// Setting the motor positive will intake both coral and algae
enum class IntakeState(val speed: Double) {
    Idle(0.0),
    CoralIntake(0.75),
    CoralScore(-0.5),
    AlgaeIntake(0.8),
    AlgaeScore(-0.5)
}

enum class GamePieceState {
    Empty,
    Coral,
    Algae
}
