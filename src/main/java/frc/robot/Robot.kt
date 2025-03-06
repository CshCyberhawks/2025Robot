package frc.robot

import au.grapplerobotics.CanBridge
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
import frc.robot.subsystems.superstructure.Superstructure
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
    /**
     * The autonomous command to run. While a default value is set here,
     * the [autonomousInit] method will set it to the value selected in
     *the  AutoChooser on the dashboard.
     */
    private var autonomousCommand: Command = Commands.runOnce({})
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

        SmartDashboard.putBoolean("Action", false)
        SmartDashboard.putBoolean("Confirm", false)
        SmartDashboard.putBoolean("Cancel", false)

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

        SmartDashboard.putBoolean("full reset with vision", false)

        RobotContainer

        AutoScoringConstants.initialize()
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
    }

    /** This method is called once each time the robot enters Disabled mode.  */
    override fun disabledInit() {

    }

    override fun disabledPeriodic() {
        RobotContainer.vision.updateOdometryFromDisabled()
    }

    /** This autonomous runs the autonomous command selected by your [RobotContainer] class.  */
    override fun autonomousInit() {
        // We store the command as a Robot property in the rare event that the selector on the dashboard
        // is modified while the command is running since we need to access it again in teleopInit()
        autonomousCommand.schedule()


        Superstructure.initialize()
    }

    /** This method is called periodically during autonomous.  */
    override fun autonomousPeriodic() {
    }

    override fun teleopInit() {
        // This makes sure that the autonomous stops running when teleop starts running. If you want the
        // autonomous to continue until interrupted by another command, remove this line or comment it out.
        autonomousCommand.cancel()

//        TeleopDriveCommand().schedule()
        Superstructure.initialize()
    }

    /** This method is called periodically during operator control.  */
    override fun teleopPeriodic() {
        RobotContainer.vision.updateOdometry(1, false)
    }

    override fun testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll()
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
