package Robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class DriveAndArm extends OpMode
{
    //Initiliaze Hardware Variables
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor BackLeft;
    private DcMotor BackRight;
    private DcMotor Arm;
    private DcMotor Elbow;

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
        Elbow = hardwareMap.dcMotor.get("Elbow");

        /*Makes sure right motors go in Reverse always so no later power
        values have to be inverted.*/
        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        BackRight.setDirection(DcMotor.Direction.REVERSE);
        Arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addLine("Status: Ready");
    }
    public void start()
    {
        /*Resets the clock so that the actual TeleOp loop (Hitting the play button)
          starts elapsed time at zero*/
        Time.reset();
        Arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void loop()
    {

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
            ArmMove(Arm, 0, 1);
            ArmMove(Elbow, 0, 1);
        }
        if (gamepad1.y)
        {
            ArmMove(Arm, 144,1);
            ArmMove(Elbow, 144, 1);
        }

        //Shows Telemetry data on the driver station
        //Shows time in seconds that have past since the play button has been pressed.
        telemetry.addData("Time:", Time.seconds());
        telemetry.addData("Position of the Arm: ", Arm.getCurrentPosition());
        telemetry.addData("Position of the Elbow: ", Elbow.getCurrentPosition());
        telemetry.update();
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
