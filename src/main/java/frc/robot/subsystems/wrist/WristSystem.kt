package frc.robot.subsystems.wrist

import edu.wpi.first.wpilibj2.command.SubsystemBase

class WristSystem(private val io: WristIO) : SubsystemBase() {
    /**
     * Example command factory method.
     *
     * @return a command
     */
//    fun exampleMethodCommand(): Command = runOnce {
//        // Subsystem.runOnce() implicitly add `this` as a required subsystem.
//        // TODO: one-time action goes here
//    }

    override fun periodic() {
        // This method will be called once per scheduler run
    }

    override fun simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}
