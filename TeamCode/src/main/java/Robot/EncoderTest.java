package Robot;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp

/*
 * This is the encoder test to see how many ticks per rotation for the REV motor, as well as testing
 * the actual code that will be later implemented in the TeleOp
 */

public class EncoderTest extends OpMode{

    //Declare Hardware Variables
    private DcMotor Arm;
    //Hardware mapping for the Hardware Variables
    @Override
    public void init()
    {
        Arm = hardwareMap.dcMotor.get("Arm");
    }
    //Calles the method for the TeleOp encoder for the Arm motor.
    public void loop()
    {
        ArmMove(Arm, 288, 1);
    }
    //Method used for TeleOp encoders, allows for boolean use and different "modes/positions" of motors.
    public void ArmMove(DcMotor Motor, int Ticks, double Power)
    {
        //Methods checks if motor is in correct position, and then moves it if it is not.
        if(Motor.getCurrentPosition() != Ticks)
        {
            Motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            Motor.setTargetPosition(Ticks);
            Motor.setPower(Power);
        }
    }
}
