package Robot;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp

public class EncoderTest extends OpMode{

    private DcMotor Arm;
    
    public void init()
    {
        Arm = hardwareMap.dcMotor.get("Arm");
    }
    public void loop()
    {
        
    }
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
