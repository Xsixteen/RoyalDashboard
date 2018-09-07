package com.ericulicny.camera;

import com.ericulicny.homedashboard.rest.CameraController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Camera {

    private String rootPath;
    private final static String CONST_SNAPCOMMAND = "raspistill";
    private static final Logger log = LoggerFactory.getLogger(Camera.class);

    public Camera(String rootPath) {
        this.rootPath = rootPath + "/" + "current.jpg";
    }

    public boolean snapPicture() {
        String command = CONST_SNAPCOMMAND + "-o " + this.rootPath;
        ProcessBuilder processBuilder = new ProcessBuilder(CONST_SNAPCOMMAND, "-o", this.rootPath);
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

    public String getLastPictureLocation() {
        return this.rootPath;
    }
    public void archiveCurrentPicture() {

    }
}
