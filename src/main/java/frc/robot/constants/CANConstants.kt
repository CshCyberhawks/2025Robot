package frc.robot.constants

object CANConstants {
    // Drivetrain 1-9 (in Tuner Constants)

    object Elevator {
        const val rightMotorId = 20 // Looking from intake side of robot
        const val leftMotorId = 21
    }

    object Pivot {
        const val motorId = 30
        const val encoderId = 31 // Roborio DIO not CAN
    }

    object Intake {
        const val motorId = 40
        const val coralLaserCANId = 41
        const val algaeLaserCANId = 42
    }

    object Climb {
        const val motorId = 45
        const val encoderId = 46
    }

    object Funnel {
        const val motorId = 47
        const val encoderId = 48
    }
}
