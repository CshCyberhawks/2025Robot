package frc.robot

import au.grapplerobotics.CanBridge
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.signals.NeutralModeValue
import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.commands.PathPlannerAuto
import edu.wpi.first.hal.FRCNetComm.tInstances
import edu.wpi.first.hal.FRCNetComm.tResourceType
import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj.util.WPILibVersion
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.constants.AutoScoringConstants
import frc.robot.constants.CANConstants
import frc.robot.constants.FieldConstants
import frc.robot.subsystems.superstructure.Superstructure
import frc.robot.subsystems.superstructure.climb.ClimbConstants
import frc.robot.util.Visualizer
import frc.robot.util.input.OperatorControls

/**
 * The VM is configured to automatically run this object (which basically functions as a singleton class),
 * and to call the functions corresponding to each mode, as described in the TimedRobot documentation.
 * This is written as an object rather than a class since there should only ever be a single instance, and
 * it cannot take any constructor arguments. This makes it a natural fit to be an object in Kotlin.
 *
 * If you change the name of this object or its package after creating this project, you must also update
 * the `Main.kt` file in the project. (If you use the IDE's Rename or Move refactorings when renaming the
 * object or package, it will get changed everywhere.)
 */
object Robot : TimedRobot() {

//    private var autonomousCommand: Command = Commands.sequence(
//        Commands.run({ RobotContainer.drivetrain.applyDriveRequest(-1.0, 0.0, 0.0) }).raceWith(Commands.waitSeconds(1.0)),
//        Commands.runOnce({ RobotContainer.drivetrain.applyDriveRequest(0.0, 0.0, 0.0) }),
//    )
//    private var autonomousCommand = Commands.run({ RobotContainer.drivetrain.applyDriveRequest(-1.0, 0.0, 0.0) })

//    val elevatorPosePublisher =
//        NetworkTableInstance.getDefault().getStructTopic("Elevator Pose", Pose3d.struct).publish();
//    val pivotPosePublisher = NetworkTableInstance.getDefault().getStructTopic("Pivot Pose", Pose3d.struct).publish();
//    val wristPosePublisher = NetworkTableInstance.getDefault().getStructTopic("Wrist Pose", Pose3d.struct).publish();
//

    init {
        AutoScoringConstants.initialize()

        SmartDashboard.putBoolean("Action", false)
        SmartDashboard.putBoolean("Confirm", false)
        SmartDashboard.putBoolean("Cancel", false)

        SmartDashboard.putBoolean("Start by unclimbing?", false)

        SmartDashboard.putBoolean("Start with a disabled climb", false)

        CanBridge.runTCP()
    }

    /**
     * This method is run when the robot is first started up and should be used for any
     * initialization code.
     */
    override fun robotInit() {
        // Report the use of the Kotlin Language for "FRC Usage Report" statistics
        HAL.report(tResourceType.kResourceType_Language, tInstances.kLanguage_Kotlin, 0, WPILibVersion.Version)
        // Access the RobotContainer object so that it is initialized. This will perform all our
        // button bindings, and put our autonomous chooser on the dashboard.

        SmartDashboard.putBoolean("Disabled vision mode", false)

        SmartDashboard.putBoolean("LL Bottom M2 Reset", false)

        SmartDashboard.putBoolean("Teleop pose difference?", true)




        RobotContainer
        OperatorControls
    }

    /**
     * This method is called every 20 ms, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     * This runs after the mode specific periodic methods, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    override fun robotPeriodic() {
        // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
        // commands, running already-scheduled commands, removing finished or interrupted commands,
        // and running subsystem periodic() methods.  This must be called from the robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run()

//        SmartDashboard.putNumber("robot pose x: ", RobotContainer.drivetrain.getSwervePose().x)

        Visualizer.periodic()

        SmartDashboard.putString("Driver Action", OperatorControls.action.name)
        SmartDashboard.putString("Reef Position", OperatorControls.reefPosition.name)
        SmartDashboard.putString("Reef Side", OperatorControls.coralSide.name)
        SmartDashboard.putNumber("Climb Target Climb Angle", ClimbConstants.climbAngle)

        SmartDashboard.putNumber("Blue Reef Distance", FieldConstants.Reef.center.getDistance(RobotContainer.drivetrain.getSwervePose().translation))
    }

    /** This method is called once each time the robot enters Disabled mode.  */
    override fun disabledInit() {

        RobotContainer.autoCommand.cancel()
//        Superstructure.
    }

    override fun disabledPeriodic() {

        RobotContainer.startByUnclimbing = SmartDashboard.getBoolean("Start by unclimbing?", false)
//        println(RobotContainer.startByUnclimbing)

        RobotContainer.startWithADisabledClimb = SmartDashboard.getBoolean("Start with a disabled climb", false)


        RobotContainer.vision.updateOdometryFromDisabled()
    }

    /** This autonomous runs the autonomous command selected by your [RobotContainer] class.  */
    override fun autonomousInit() {
        // We store the command as a Robot property in the rare event that the selector on the dashboard
        // is modified while the command is running since we need to access it again in teleopInit()

//        autonomousCommand = RobotContainer.autonomousCommand
//        autonomousCommand = RobotContainer.drivetrain.getAutoPath("3 L4 Left")
//        autonomousCommand = RobotContainer.drivetrain.getAutoPath("Test")
//        autonomousCommand.execute()

        Superstructure.initialize()


        RobotContainer.vision.updateOdometry(1, true)

        RobotContainer.autoCommand.schedule()
    }

    /** This method is called periodically during autonomous.  */
    override fun autonomousPeriodic() {
        RobotContainer.vision.updateOdometry(1, true)

    }

    override fun teleopInit() {


        // This makes sure that the autonomous stops running when teleop starts running. If you want the
        // autonomous to continue until interrupted by another command, remove this line or comment it out.
        RobotContainer.autoCommand.cancel()

//        TeleopDriveCommand().schedule()
        Superstructure.initialize()

        RobotContainer.vision.updateOdometry(1, true)

        RobotState.autoDriving = false

        println("teleop initing")

        if (RobotContainer.startByUnclimbing == true) {
            println("unclilmbing")
            Superstructure.unclimb()
        }

        if (RobotContainer.startWithADisabledClimb) {
            println("starting with climb disabled")
            Superstructure.disableClimb()
        }

//        } else if (!Superstructure.climbSystem.isStow() || !Superstructure.funnelSystem.isStow()) {
//            Superstructure.climbStowThenStow()
//        } else {
//            Superstructure.stow()
//        }
    }

    /** This method is called periodically during operator control.  */
    override fun teleopPeriodic() {

        val poseDiff = SmartDashboard.getBoolean("Teleop pose difference?", true)

        RobotContainer.vision.updateOdometry(1, poseDiff)
    }


    override fun testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll()

        //Run through a climb store sequence
//        Superstructure.climbStowThenStow()
    }

    /** This method is called periodically during test mode.  */
    override fun testPeriodic() {
    }

//    var lastLoopTime = 0.0
    /** This method is called once when the robot is first started up.  */
    override fun simulationInit() {
//        lastLoopTime = MiscCalculations.getCurrentTime()
    }

    /** This method is called periodically whilst in simulation.  */
    override fun simulationPeriodic() {
//        val currentTime = MiscCalculations.getCurrentTime()
//        val dtSeconds = (currentTime - lastLoopTime) / 1000
//
//        RobotContainer.drivetrain.updateSimState(dtSeconds, RobotController.getBatteryVoltage())
//
//        lastLoopTime = currentTime
    }
}
