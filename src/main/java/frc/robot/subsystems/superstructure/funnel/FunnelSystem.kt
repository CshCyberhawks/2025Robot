package frc.robot.subsystems.superstructure.funnel

import cshcyberhawks.lib.requests.EmptyRequest
import cshcyberhawks.lib.requests.Request
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase

class FunnelSystem(private val io: FunnelIO) : SubsystemBase() {
    private fun setAngle(angle: Double) = Request.withAction { io.setAngle(angle) }

    fun stow() = setAngle(200.0)
    fun deploy() = setAngle(90.0)

    override fun periodic() {
        SmartDashboard.putNumber("Funnel Angle", io.getAngle())

        io.periodic()
    }
}