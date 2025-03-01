package frc.robot.commands

import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import frc.robot.RobotContainer
import cshcyberhawks.lib.math.MiscCalculations
import edu.wpi.first.wpilibj2.command.Commands
import frc.robot.RobotState
import frc.robot.subsystems.ExampleSubsystem.runOnce
import frc.robot.subsystems.swerve.SwerveConstants


class TeleopDriveCommand : Command() {
    init {
        addRequirements(RobotContainer.drivetrain)
    }

    override fun initialize() {

        super.initialize()
    }

    override fun execute() {

//        println("Execute")

        if (RobotState.autoDriving) return

        val deadzonedLeftY = MiscCalculations.calculateDeadzone(RobotContainer.leftJoystick.y, .5)

        SwerveConstants.ControlledSpeed = MathUtil.clamp(
            SwerveConstants.ControlledSpeed + (-deadzonedLeftY * SwerveConstants.MaxSpeedConst * .02), 0.0,
            SwerveConstants.MaxSpeedConst
        );
        SwerveConstants.ControlledAngularRate = MathUtil.clamp(
            SwerveConstants.ControlledAngularRate + (-deadzonedLeftY * SwerveConstants.MaxAngularRateConst * .02),
            0.0, SwerveConstants.MaxAngularRateConst
        );

        SmartDashboard.putNumber("ControlledSpeed", SwerveConstants.ControlledSpeed)
        SmartDashboard.putNumber("ControlledAngularRate", SwerveConstants.ControlledAngularRate)


        val fieldOriented = !RobotContainer.rightJoystick.button(1).asBoolean

        if (fieldOriented) {
            SmartDashboard.putNumber("drive x req", -MiscCalculations.calculateDeadzone(
                RobotContainer.rightJoystick.y,
                .1
            ) * SwerveConstants.ControlledSpeed)


            RobotContainer.drivetrain.applyDriveRequest(
                -MiscCalculations.calculateDeadzone(
                    RobotContainer.rightJoystick.y,
                    .1
                ) * SwerveConstants.ControlledSpeed, -MiscCalculations.calculateDeadzone(
                    RobotContainer.rightJoystick.x,
                    .1
                ) * SwerveConstants.ControlledSpeed, -MiscCalculations.calculateDeadzone(
                    RobotContainer.leftJoystick.x,
                    .1
                ) * SwerveConstants.ControlledAngularRate
            )
        } else {
            RobotContainer.drivetrain.applyRobotRelativeDriveRequest(
                -MiscCalculations.calculateDeadzone(
                    RobotContainer.rightJoystick.y,
                    .1
                ) * SwerveConstants.ControlledSpeed, -MiscCalculations.calculateDeadzone(
                    RobotContainer.rightJoystick.x,
                    .1
                ) * SwerveConstants.ControlledSpeed, -MiscCalculations.calculateDeadzone(
                    RobotContainer.leftJoystick.x,
                    .1
                ) * SwerveConstants.ControlledAngularRate
            )
        }

        super.execute()
    }
}
