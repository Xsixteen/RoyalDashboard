package com.ericulicny.camera;

import com.ericulicny.homedashboard.rest.CameraController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Camera {

    private String rootPath;
    private final static String CONST_SNAPCOMMAND = "raspistill";
    private static final Logger log = LoggerFactory.getLogger(Camera.class);

    public Camera(String rootPath) {
        this.rootPath = rootPath;
    }

    public boolean snapPicture() {
        String command = CONST_SNAPCOMMAND + "-o " + this.rootPath + "/" + "current.jpg";
        ProcessBuilder processBuilder = new ProcessBuilder(CONST_SNAPCOMMAND, "-o", this.rootPath+"/"+"current.jpg");
        log.info("Snapping picture by issuing command: " + command);

        try {
            Process process             = processBuilder.start();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public void archiveCurrentPicture() {

    }
}
