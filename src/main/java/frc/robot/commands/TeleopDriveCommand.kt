package frc.robot.commands

import MiscCalculations
import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import frc.robot.RobotContainer


class TeleopDriveCommand : Command() {
    init {
        addRequirements(RobotContainer.drivetrain)
    }

    override fun initialize() {
        RobotContainer.rightJoystick.button(2)
            .onTrue(RobotContainer.drivetrain.runOnce { RobotContainer.drivetrain.seedFieldCentric() })

        super.initialize()
    }

    override fun execute() {
        val deadzonedLeftY = MiscCalculations.calculateDeadzone(RobotContainer.leftJoystick.y, .5)

        RobotContainer.ControlledSpeed = MathUtil.clamp(
            RobotContainer.ControlledSpeed + (-deadzonedLeftY * RobotContainer.MaxSpeedConst * .02), 0.0,
            RobotContainer.MaxSpeedConst
        );
        RobotContainer.ControlledAngularRate = MathUtil.clamp(
            RobotContainer.ControlledAngularRate + (-deadzonedLeftY * RobotContainer.MaxAngularRateConst * .02),
            0.0, RobotContainer.MaxAngularRateConst
        );

        SmartDashboard.putNumber("ControlledSpeed", RobotContainer.ControlledSpeed)
        SmartDashboard.putNumber("ControlledAngularRate", RobotContainer.ControlledAngularRate)


        val fieldOriented = !RobotContainer.rightJoystick.button(2).asBoolean

        if (fieldOriented) {
            RobotContainer.drivetrain.applyRequest {
                RobotContainer.drive.withVelocityX(
                    -MiscCalculations.calculateDeadzone(
                        RobotContainer.rightJoystick.y,
                        .1
                    ) * RobotContainer.ControlledSpeed
                ) //
                    // Drive forward with
                    // negative Y (forward)
                    .withVelocityY(
                        -MiscCalculations.calculateDeadzone(
                            RobotContainer.rightJoystick.x,
                            .1
                        ) * RobotContainer.ControlledSpeed
                    ) // Drive
                    // left with negative X
                    // (left)
                    .withRotationalRate(
                        -MiscCalculations.calculateDeadzone(
                            RobotContainer.leftJoystick.x,
                            .1
                        ) * RobotContainer.ControlledAngularRate
                    )
            }
        } else {
            RobotContainer.drivetrain.applyRequest {
                RobotContainer.robotRelative.withVelocityX(
                    -MiscCalculations.calculateDeadzone(
                        RobotContainer.rightJoystick.y,
                        .1
                    ) * RobotContainer.ControlledSpeed
                ) //
                    // Drive forward with
                    // negative Y (forward)
                    .withVelocityY(
                        -MiscCalculations.calculateDeadzone(
                            RobotContainer.rightJoystick.x,
                            .1
                        ) * RobotContainer.ControlledSpeed
                    ) // Drive
                    // left with negative X
                    // (left)
                    .withRotationalRate(
                        -MiscCalculations.calculateDeadzone(
                            RobotContainer.leftJoystick.x,
                            .1
                        ) * RobotContainer.ControlledAngularRate
                    )
            }
        }


        super.execute()
    }
}
