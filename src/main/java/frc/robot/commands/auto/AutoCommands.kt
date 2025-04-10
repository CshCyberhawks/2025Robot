package frc.robot.commands.auto

import cshcyberhawks.lib.math.MiscCalculations
import cshcyberhawks.lib.math.Timer
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import frc.robot.RobotContainer
import frc.robot.commands.GoToPose
import frc.robot.constants.AutoScoringConstants
import frc.robot.constants.FieldConstants
import frc.robot.subsystems.swerve.SwerveConstants
import frc.robot.util.AllianceFlipUtil
import frc.robot.util.input.CoralSide
import kotlin.math.abs
import kotlin.math.min

object AutoCommands {
    const val timeStartPositionDeadzone = .05
    const val timeStartAngleDeadzone = 3.5

    fun coralReefAlign(position: AutoScoringConstants.ReefPositions, side: CoralSide): Command {

        val timer = Timer()

        return GoToPose({
            val goalPose = AllianceFlipUtil.apply(AutoScoringConstants.getReefPoseAtOffset(position.ordinal, side, 0.0))
            // Make the position get closer as it gets closer to the final goal
//            val adjustX: Double = 0.3
            var adjustX: Double =
                min(1.0, RobotContainer.drivetrain.getSwervePose().translation.getDistance(goalPose.translation) * 0.75)
//            val adjustX = 1.0

            // Once we're close enough we can just jump to the goal
            if (adjustX < 0.2) {
                adjustX = 0.0
            }

            AllianceFlipUtil.apply(AutoScoringConstants.getReefPoseAtOffset(position.ordinal, side, adjustX))
        }, {
            val goalPose = AllianceFlipUtil.apply(AutoScoringConstants.getReefPoseAtOffset(position.ordinal, side, 0.0))

            val currentPose = RobotContainer.drivetrain.getSwervePose()

            val minus = currentPose.minus(goalPose)
            //BAD CODE
//            val timerDeadzone: Boolean = !timer.isRunning && MiscCalculations.calculateDeadzone(minus.x, timeStartPositionDeadzone) == 0.0 && MiscCalculations.calculateDeadzone(minus.y, timeStartPositionDeadzone) == 0.0;

            //GOOD CODE
            val timerDeadzone: Boolean = !timer.isRunning && MiscCalculations.calculateDeadzone(minus.x, timeStartPositionDeadzone) == 0.0 && MiscCalculations.calculateDeadzone(minus.y, timeStartPositionDeadzone) == 0.0 && MiscCalculations.calculateDeadzone(minus.rotation.degrees, timeStartAngleDeadzone) == 0.0;

//            if (!timer.isRunning && (abs(currentPose.x - goalPose.x) < timeStartPositionDeadzone && abs(currentPose.y - goalPose.y) < timeStartPositionDeadzone && abs(
//                    abs(currentPose.rotation.degrees - goalPose.rotation.degrees)
//                ) < timeStartAngleDeadzone)) {
            if (timerDeadzone) {
                println("Starting the timer")
                timer.reset()
                timer.restart()
            }

            val timerComplete = timer.isRunning && timer.hasElapsed(1.35)
            if (timerComplete) {
                println("timer done")
            }

            if (timer.isRunning) {
                SmartDashboard.putNumber("Auto align timer", timer.get())
            }

            val atPose: Boolean = MiscCalculations.calculateDeadzone(minus.x, SwerveConstants.positionDeadzone) == 0.0 && MiscCalculations.calculateDeadzone(minus.y, SwerveConstants.positionDeadzone) == 0.0 && MiscCalculations.calculateDeadzone(minus.rotation.degrees, SwerveConstants.rotationDeadzone) == 0.0;
//            val atPose = abs(currentPose.x - goalPose.x) < SwerveConstants.positionDeadzone && abs(currentPose.y - goalPose.y) < SwerveConstants.positionDeadzone && abs(
//                abs(currentPose.rotation.degrees - goalPose.rotation.degrees)
//            ) < SwerveConstants.rotationDeadzone

            (timerComplete || atPose)
        })
    }

    fun algaeReefAlign(position: AutoScoringConstants.ReefPositions): Command {
//        val timer = Timer()

        return GoToPose({
            val goalPose = AllianceFlipUtil.apply(AutoScoringConstants.getAlgaePoseAtOffset(position.ordinal, 0.0))

            // Make the position get closer as it gets closer to the final goal
//            val adjustX: Double = 0.3
            var adjustX: Double =
                min(1.0, RobotContainer.drivetrain.getSwervePose().translation.getDistance(goalPose.translation) * 0.75)
//            val adjustX = 1.0

            // Once we're close enough we can just jump to the goal
            if (adjustX < 0.2) {
                adjustX = 0.0
            }

            AllianceFlipUtil.apply(AutoScoringConstants.getAlgaePoseAtOffset(position.ordinal, adjustX))
        }, {
            val goalPose = AllianceFlipUtil.apply(AutoScoringConstants.getAlgaePoseAtOffset(position.ordinal, 0.0))

            val currentPose = RobotContainer.drivetrain.getSwervePose()

            if (/*(timer.isRunning && timer
            +.hasElapsed(1.0)) || */(abs(currentPose.x - goalPose.x) < SwerveConstants.positionDeadzone && abs(currentPose.y - goalPose.y) < SwerveConstants.positionDeadzone && abs(
                currentPose.rotation.degrees - goalPose.rotation.degrees
            ) < SwerveConstants.rotationDeadzone)) {
                println("finished")
                return@GoToPose true
            }

            return@GoToPose false
        })
    }

    val unsafeReefPositions = arrayOf(
        AutoScoringConstants.ReefPositions.A,
        AutoScoringConstants.ReefPositions.C,
        AutoScoringConstants.ReefPositions.E,
        AutoScoringConstants.ReefPositions.D,
    )

    fun safeReefExit(lastTarget: AutoScoringConstants.ReefPositions) = if (unsafeReefPositions.contains(lastTarget)) {
        GoToPose({ AllianceFlipUtil.apply(AutoScoringConstants.getAlgaePoseAtOffset(lastTarget.ordinal, 1.0)) }, {
            // TODO: Probably need to tune this
            AllianceFlipUtil.apply(FieldConstants.Reef.center)
                .getDistance(RobotContainer.drivetrain.getSwervePose().translation) > 2.1 // Can continue once we're 2 meters out from the center of the reef
        })
    } else Commands.none()

    fun leftFeederAlign() = GoToPose({
        // TODO: Probably want to generate these positions from the field constants
        AllianceFlipUtil.apply(FieldConstants.leftFeederPosition)
    })

    fun rightFeederAlign() = GoToPose({
        AllianceFlipUtil.apply(FieldConstants.rightFeederPosition)
    })
}