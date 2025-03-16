//package frc.robot.commands
//
//import edu.wpi.first.wpilibj2.command.Command
//import edu.wpi.first.wpilibj2.command.Commands
//import frc.robot.RobotState
//import frc.robot.subsystems.superstructure.Superstructure
//import frc.robot.subsystems.superstructure.intake.GamePieceState
//
//enum class AutoCommands(val cmd: Command) {
//    PrepL4(Commands.runOnce({ Superstructure.Auto.prepL4() })),
//    ScoreL4(
//        Commands.runOnce({ Superstructure.Auto.justScoreL4() })
//            .andThen(Commands.waitUntil { RobotState.gamePieceState == GamePieceState.Empty })
//            .andThen(Commands.waitSeconds(0.2))
//    ),
//    IntakeFeeder(
//        Commands.runOnce({ Superstructure.intakeFeeder() })
//            .andThen(Commands.waitUntil { RobotState.gamePieceState == GamePieceState.Coral })
//    ),
//    AwaitIntake(Commands.waitUntil { RobotState.gamePieceState == GamePieceState.Coral })
//}