package frc.robot.commands

import MiscCalculations
import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import frc.robot.RobotContainer
import frc.robot.subsystems.ExampleSubsystem.runOnce
import frc.robot.subsystems.swerve.SwerveConstants


class SimTeleopDriveCommand : Command() {
    init {
//        addRequirements(RobotContainer.drivetrain)
    }

    override fun initialize() {
        RobotContainer.xbox.start()
            .onTrue(runOnce { RobotContainer.drivetrain.seedFieldCentric() })

        super.initialize()
    }

    override fun execute() {
        val deadzonedLeftY = MiscCalculations.calculateDeadzone(RobotContainer.xbox.leftX, .5)

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


        val fieldOriented = !RobotContainer.xbox.a().asBoolean

        if (fieldOriented) {
            RobotContainer.drivetrain.applyDriveRequest(
                -MiscCalculations.calculateDeadzone(
                    RobotContainer.xbox.rightY,
                    .1
                ) * SwerveConstants.ControlledSpeed, -MiscCalculations.calculateDeadzone(
                    RobotContainer.xbox.rightX,
                    .1
                ) * SwerveConstants.ControlledSpeed, -MiscCalculations.calculateDeadzone(
                    RobotContainer.xbox.leftX,
                    .1
                ) * SwerveConstants.ControlledAngularRate
            )
        } else {
            RobotContainer.drivetrain.applyRobotRelativeDriveRequest(
                -MiscCalculations.calculateDeadzone(
                    RobotContainer.xbox.rightY,
                    .1
                ) * SwerveConstants.ControlledSpeed, -MiscCalculations.calculateDeadzone(
                    RobotContainer.xbox.rightX,
                    .1
                ) * SwerveConstants.ControlledSpeed, -MiscCalculations.calculateDeadzone(
                    RobotContainer.xbox.leftX,
                    .1
                ) * SwerveConstants.ControlledAngularRate
            )

        }

        super.execute()
    }
}
