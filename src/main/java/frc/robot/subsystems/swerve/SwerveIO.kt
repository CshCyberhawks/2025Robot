package frc.robot.subsystems.swerve

import com.ctre.phoenix6.swerve.SwerveDrivetrain
import com.ctre.phoenix6.swerve.SwerveRequest
import edu.wpi.first.wpilibj2.command.Command
import java.util.function.Consumer
import java.util.function.Supplier

interface SwerveIO {
    fun setDefaultCommand(command: Command)
    fun seedFieldCentric()
    fun applyDriveRequest(x: Double, y: Double, twist: Double)
    fun applyRobotRelativeDriveRequest(x: Double, y: Double, twist: Double)
}