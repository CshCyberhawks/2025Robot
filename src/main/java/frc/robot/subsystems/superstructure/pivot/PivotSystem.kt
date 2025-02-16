package frc.robot.subsystems.superstructure.pivot

import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.subsystems.superstructure.pivot.PivotIO

// By making a subsystem a Kotlin object, we ensure there is only ever one instance of it.
// It also reduces the need to have reference variables for the subsystems to be passed around.
class PivotSystem(private val io: PivotIO) : SubsystemBase() {
    /**
     * Example command factory method.
     *
     * @return a command
     */
//    fun exampleMethodCommand(): Command = runOnce {
//        // Subsystem.runOnce() implicitly add `this` as a required subsystem.
//        // TODO: one-time action goes here
//    }

    /**
     * An example method querying a boolean state of the subsystem (for example, a digital sensor).
     *
     * @return value of some boolean subsystem state, such as a digital sensor.
     */
//    fun exampleCondition(): Boolean {
//        // Query some boolean state, such as a digital sensor.
//        return false
//    }

    override fun periodic() {
        // This method will be called once per scheduler run
    }

    override fun simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }

//    fun exampleAction()
//    {
//        // This action is called by the ExampleCommand
//        println("ExampleSubsystem.exampleAction has been called")
//    }

    fun getPos(): Double {
        TODO("Not yet implemented")
        return 0.0;
    }

    fun setPos(x: Double) {
        TODO("Not yet implemented")
    }
}
