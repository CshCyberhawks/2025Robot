package frc.robot.commands

import MiscCalculations
import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import frc.robot.RobotContainer
import frc.robot.RobotContainer.ControlledAngularRate
import frc.robot.RobotContainer.ControlledSpeed
import frc.robot.RobotContainer.MaxAngularRateConst
import frc.robot.RobotContainer.MaxSpeedConst
import frc.robot.RobotContainer.drive
import frc.robot.RobotContainer.drivetrain
import frc.robot.RobotContainer.leftJoystick
import frc.robot.RobotContainer.rightJoystick
import frc.robot.RobotContainer.robotRelative

class TeleopDriveCommand : Command() {
    init {
        addRequirements(drivetrain)
    }

    override fun initialize() {
        rightJoystick.button(2).onTrue(drivetrain.runOnce { drivetrain.seedFieldCentric() })

        super.initialize()
    }

    override fun execute() {
        var deadzonedLeftY = MiscCalculations.calculateDeadzone(leftJoystick.y, .5)

        ControlledSpeed = MathUtil.clamp(
            ControlledSpeed + (-deadzonedLeftY * MaxSpeedConst * .02), 0.0,
            MaxSpeedConst
        );
        ControlledAngularRate = MathUtil.clamp(
            ControlledAngularRate + (-deadzonedLeftY * MaxAngularRateConst * .02),
            0.0, MaxAngularRateConst
        );

        SmartDashboard.putNumber("ControlledSpeed", ControlledSpeed)
        SmartDashboard.putNumber("ControlledAngularRate", ControlledAngularRate)


        val fieldOriented = !rightJoystick.button(2).asBoolean

        if (fieldOriented) {
            drivetrain.applyRequest {
                drive.withVelocityX(
                    -MiscCalculations.calculateDeadzone(
                        rightJoystick.y,
                        .1
                    ) * ControlledSpeed
                ) //
                    // Drive forward with
                    // negative Y (forward)
                    .withVelocityY(
                        -MiscCalculations.calculateDeadzone(
                            rightJoystick.x,
                            .1
                        ) * ControlledSpeed
                    ) // Drive
                    // left with negative X
                    // (left)
                    .withRotationalRate(
                        -MiscCalculations.calculateDeadzone(
                            leftJoystick.x,
                            .1
                        ) * ControlledAngularRate
                    )
            }
        } else {
            drivetrain.applyRequest {
                robotRelative.withVelocityX(
                    -MiscCalculations.calculateDeadzone(
                        rightJoystick.y,
                        .1
                    ) * ControlledSpeed
                ) //
                    // Drive forward with
                    // negative Y (forward)
                    .withVelocityY(
                        -MiscCalculations.calculateDeadzone(
                            rightJoystick.x,
                            .1
                        ) * ControlledSpeed
                    ) // Drive
                    // left with negative X
                    // (left)
                    .withRotationalRate(
                        -MiscCalculations.calculateDeadzone(
                            leftJoystick.x,
                            .1
                        ) * ControlledAngularRate
                    )
            }
        }


        super.execute()
    }
}
