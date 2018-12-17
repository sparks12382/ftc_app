package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

@TeleOp
@Disabled
public class VisionJavaExample extends LinearOpMode{
    MasterVision vision;
    SampleRandomizedPositions goldPosition;

    @Override
    public void runOpMode() throws InterruptedException {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;// recommended camera direction
        parameters.vuforiaLicenseKey = "ATT7wQb/////AAAAGb+seHrT4EU0h1e3FXROmeMQoCAjMgbsuk2Push7du+ijYP+uh2cp3E5jNLy2EiF2ecgKoOhp1g7YeaQGkFoWq3M4khj16tP/qLs+h7v2sI8BKGZAp3vTOn0ghr5UYfH7nUHgGcKgUOoK6+qGXqCCBR+2vyct1qMkptRscVdKAhXoja1non0eU30CdvtCthNoyOZ5uRFwpD61c92Btodf61sBQC/koYmhq99Ce4R858k1KzNSflN0WeWAmVF6UVP9AcJ+k63Fus9nGCuyw9wSfnxpO5AkfxUKfgJg2Xy2SPsF0D/Zkwgo46Rj77oCvTehULoyiCzzTvwGC+f+5Z7MNnOcpOKJw/TTNOWqHUpQLG1";

        vision = new MasterVision(parameters, hardwareMap, true, MasterVision.TFLiteAlgorithm.INFER_NONE);
        vision.init();// enables the camera overlay. this will take a couple of seconds
        vision.enable();// enables the tracking algorithms. this might also take a little time

        waitForStart();

        vision.disable();// disables tracking algorithms. this will free up your phone's processing power for other jobs.

        goldPosition = vision.getTfLite().getLastKnownSampleOrder();

        while(opModeIsActive()){
            telemetry.addData("goldPosition was", goldPosition);// giving feedback

            switch (goldPosition){ // using for things in the autonomous program
                case LEFT:
                    telemetry.addLine("going to the left");
                    break;
                case CENTER:
                    telemetry.addLine("going straight");
                    break;
                case RIGHT:
                    telemetry.addLine("going to the right");
                    break;
                case UNKNOWN:
                    telemetry.addLine("staying put");
                    break;
            }

            telemetry.update();
        }

        vision.shutdown();
    }
}
