package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@TeleOp(name = "Concept: TensorFlow Object Detection", group = "Concept")

public class TensorTest extends LinearOpMode {
    private int goldPosition;
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = " ATT7wQb/////AAAAGb+seHrT4EU0h1e3FXROmeMQoCAjMgbsuk2Push7du+ijYP+uh2cp3E5jNLy2EiF2ecgKoOhp1g7YeaQGkFoWq3M4khj16tP/qLs+h7v2sI8BKGZAp3vTOn0ghr5UYfH7nUHgGcKgUOoK6+qGXqCCBR+2vyct1qMkptRscVdKAhXoja1non0eU30CdvtCthNoyOZ5uRFwpD61c92Btodf61sBQC/koYmhq99Ce4R858k1KzNSflN0WeWAmVF6UVP9AcJ+k63Fus9nGCuyw9wSfnxpO5AkfxUKfgJg2Xy2SPsF0D/Zkwgo46Rj77oCvTehULoyiCzzTvwGC+f+5Z7MNnOcpOKJw/TTNOWqHUpQLG1 ";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    @Override
    public void runOpMode()
    {
        initEngine();
        runEngine();
        waitForStart();

        while (opModeIsActive())
        {
            switch (goldPosition){
                case -1:
                    break;
                case 0:
                    break;
                case 1:
                    break;
            }

        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
        tfodParameters.minimumConfidence = 0.75;
    }
    public void initEngine()
    {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
    }
    public void runEngine()
    {
        if (tfod != null) {
            tfod.activate();
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() >= 2) {
                    int goldMineralX = -1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getLeft();
                        } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                        } else if (silverMineral2X == -1){
                            silverMineral2X = (int) recognition.getLeft();
                        }
                    }

                        if (silverMineral2X == -1 && goldMineralX < silverMineral1X) {
                            telemetry.addData("Gold Mineral Position", "Left");
                            goldPosition = -1;
                        } else if (goldMineralX == -1) {
                            telemetry.addData("Gold Mineral Position", "Right");
                            goldPosition = 1;
                        } else if (silverMineral2X == -1 && silverMineral2X > goldMineralX){
                            telemetry.addData("Gold Mineral Position", "Center");
                            goldPosition = 0;
                        } else {
                            telemetry.addData("Gold Mineral Position", "Unknown");
                        }

                }
                telemetry.update();
            }
            sleep(500);
            tfod.shutdown();
        }
    }
}
