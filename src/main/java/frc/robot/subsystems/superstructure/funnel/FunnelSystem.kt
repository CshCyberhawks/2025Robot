package frc.robot.subsystems.superstructure.funnel

import cshcyberhawks.lib.math.MiscCalculations
import cshcyberhawks.lib.requests.EmptyRequest
import cshcyberhawks.lib.requests.Request
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.subsystems.superstructure.climb.ClimbConstants

class FunnelSystem(private val io: FunnelIO) : SubsystemBase() {
    private fun setAngle(angle: Double) = Request.withAction { io.setAngle(angle) }

    fun stow() = setAngle(FunnelConstants.stowAngle)
    fun deploy() = setAngle(FunnelConstants.deployAngle)

    fun isStow(): Boolean {
        return MiscCalculations.calculateDeadzone(io.getAngle() - FunnelConstants.stowAngle, 5.0) == 0.0
    }

    override fun periodic() {
        SmartDashboard.putNumber("Funnel Angle", io.getAngle())

        io.periodic()
    }
}