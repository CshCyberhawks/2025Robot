package frc.robot.subsystems.superstructure.climb

import cshcyberhawks.lib.math.MiscCalculations
import cshcyberhawks.lib.requests.Request
import cshcyberhawks.lib.requests.SequentialRequest
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase

class ClimbSystem(private val io: ClimbIO): SubsystemBase() {
    private fun setAngle(angleDegrees: Double) = Request.withAction { io.angleDegrees(angleDegrees) }
    private fun setClimbing(climbing: Boolean) = Request.withAction { io.climbing = climbing }

    fun deploy() = SequentialRequest(setClimbing(false), setAngle(ClimbConstants.deployAngle))
    fun climb() = SequentialRequest(setClimbing(true), setAngle(ClimbConstants.climbAngle))

    fun stow() = SequentialRequest(setClimbing(false), setAngle(ClimbConstants.stowAngle))

    fun isStow(): Boolean {
        return MiscCalculations.calculateDeadzone(io.getAngle() - ClimbConstants.stowAngle, 5.0) == 0.0
    }


    override fun periodic() {
        io.periodic()

        SmartDashboard.putNumber("Climb Angle", io.getAngle())
    }
}