package frc.robot.subsystems.superstructure.climb

import cshcyberhawks.lib.math.MiscCalculations
import cshcyberhawks.lib.requests.Request
import cshcyberhawks.lib.requests.SequentialRequest
import cshcyberhawks.lib.requests.WaitRequest
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase

class ClimbSystem(private val io: ClimbIO): SubsystemBase() {
    private fun setAngle(angleDegrees: Double) = Request.withAction { io.angleDegrees(angleDegrees) }
    private fun setClimbing(climbing: Boolean) = Request.withAction { io.climbing = climbing }
    fun setDisable(disable: Boolean) = Request.withAction {
        println("set disable to: " + disable)
        io.disable = disable
    }
    fun setMotor(current: Double) = Request.withAction { io.setMotor(current) }

    fun deploy() = SequentialRequest(setClimbing(false), setDisable(false), setAngle(ClimbConstants.deployAngle))
    fun climb() = SequentialRequest(setClimbing(true), setDisable(false), setAngle(ClimbConstants.climbAngle))

    fun stow() = SequentialRequest(setClimbing(false), setDisable(false), setAngle(ClimbConstants.stowAngle))

    fun unspoolForTime() = SequentialRequest(
        setDisable(true),
        setMotor(-40.0),
        WaitRequest(8.0),
        setMotor(0.0)
    )

    fun isStow(): Boolean {
        return MiscCalculations.calculateDeadzone(io.getAngle() - ClimbConstants.stowAngle, 5.0) == 0.0
    }


    override fun periodic() {
        io.periodic()

        SmartDashboard.putNumber("Climb Angle", io.getAngle())
    }
}