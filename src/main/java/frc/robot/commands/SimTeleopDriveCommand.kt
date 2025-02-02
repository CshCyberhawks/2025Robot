package frc.robot.commands

import MiscCalculations
import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import frc.robot.RobotContainer


class SimTeleopDriveCommand : Command() {
    init {
        addRequirements(RobotContainer.drivetrain)
    }

    override fun initialize() {
        RobotContainer.xbox.start()
            .onTrue(RobotContainer.drivetrain.runOnce { RobotContainer.drivetrain.seedFieldCentric() })

        super.initialize()
    }

    override fun execute() {
        val deadzonedLeftY = MiscCalculations.calculateDeadzone(RobotContainer.xbox.leftX, .5)

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


        val fieldOriented = !RobotContainer.xbox.a().asBoolean

        if (fieldOriented) {
            RobotContainer.drivetrain.applyRequest {
                RobotContainer.drive.withVelocityX(
                    -MiscCalculations.calculateDeadzone(
                        RobotContainer.xbox.rightY,
                        .1
                    ) * RobotContainer.ControlledSpeed
                ) //
                    // Drive forward with
                    // negative Y (forward)
                    .withVelocityY(
                        -MiscCalculations.calculateDeadzone(
                            RobotContainer.xbox.rightX,
                            .1
                        ) * RobotContainer.ControlledSpeed
                    ) // Drive
                    // left with negative X
                    // (left)
                    .withRotationalRate(
                        -MiscCalculations.calculateDeadzone(
                            RobotContainer.xbox.leftX,
                            .1
                        ) * RobotContainer.ControlledAngularRate
                    )
            }
        } else {
            RobotContainer.drivetrain.applyRequest {
                RobotContainer.robotRelative.withVelocityX(
                    -MiscCalculations.calculateDeadzone(
                        RobotContainer.xbox.rightY,
                        .1
                    ) * RobotContainer.ControlledSpeed
                ) //
                    // Drive forward with
                    // negative Y (forward)
                    .withVelocityY(
                        -MiscCalculations.calculateDeadzone(
                            RobotContainer.xbox.rightX,
                            .1
                        ) * RobotContainer.ControlledSpeed
                    ) // Drive
                    // left with negative X
                    // (left)
                    .withRotationalRate(
                        -MiscCalculations.calculateDeadzone(
                            RobotContainer.xbox.leftX,
                            .1
                        ) * RobotContainer.ControlledAngularRate
                    )
            }
        }


        super.execute()
    }
}
