package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class DriveAndArm extends OpMode
{
    //Initiliaze Hardware Variables
    DcMotor FrontLeft;
    DcMotor FrontRight;
    DcMotor BackLeft;
    DcMotor BackRight;
    DcMotor Arm;

    //Creates new Elapsed time so that time elapsed can be seen on the driver station
    ElapsedTime Time = new ElapsedTime();

    public void init()
    {
        //Map variables to configuration
        FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
        FrontRight = hardwareMap.dcMotor.get("FrontRight");
        BackLeft = hardwareMap.dcMotor.get("BackLeft");
        BackRight = hardwareMap.dcMotor.get("BackRight");
        Arm = hardwareMap.dcMotor.get("Arm");

        /*Makes sure right motors go in Reverse always so no later power
        values have to be inverted.*/
        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        BackRight.setDirection(DcMotor.Direction.REVERSE);
        telemetry.addLine("Status: Ready");
    }
    public void start()
    {
        /*Resets the clock so that the actual TeleOp loop (Hitting the play button)
          starts elapsed time at zero*/
        Time.reset();
    }
    public void loop()
    {
        /**
         * Basic POV system, Turn is done with left stick horizontally,
         * Drive or forward and backward motion, is done with the right
         * stick vertically
         */

        //Creates variables Turn and Drive to abstract the inputs from the Power
        double Turn;
        double Drive;
        /*Initializes two variables left and right power to further process the
         *inputs and to only need two variables for four motors
         */
        double LeftDrive;
        double RightDrive;

        //Sets the variables of turn and drive to their respective game-pad inputs.
        Turn = gamepad1.left_stick_x;
        Drive = gamepad1.right_stick_y;

        //Processes the values into two variables to drive each side's two motors.
        LeftDrive = Turn + Drive;
        RightDrive = Drive - Turn;

        //Set motor powers to processed inputs in the form of variables
        FrontLeft.setPower(LeftDrive);
        FrontRight.setPower(RightDrive);
        BackLeft.setPower(LeftDrive);
        BackRight.setPower(RightDrive);

        //Calls encoder methods to change the DcMotor Arm to certain positions
        if (gamepad1.a)
        {
            ArmMove(Arm, 1, 0.5);
        }
        if (gamepad1.x)
        {
            ArmMove(Arm, 144, 0.5);
        }
        if (gamepad1.y)
        {
            ArmMove(Arm, 216,0.5);
        }
        if (gamepad1.b)
        {
            ArmMove(Arm,72,0.5);
        }
        //Shows Telemetry data on the driver station
        //Shows time in seconds that have past since the play button has been pressed.
        telemetry.addData("Time:", Time.seconds());
    }
    //Lets TeleOp utilize encoders so that the arm has different positions
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
