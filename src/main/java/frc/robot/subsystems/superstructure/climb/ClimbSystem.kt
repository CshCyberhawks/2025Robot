package frc.robot.subsystems.superstructure.climb

import cshcyberhawks.lib.requests.EmptyRequest
import edu.wpi.first.wpilibj2.command.SubsystemBase

class ClimbSystem(private val io: ClimbIO): SubsystemBase() {
    fun deploy() = EmptyRequest()
    fun climb() = EmptyRequest()

    override fun periodic() {
        io.periodic()
    }
}