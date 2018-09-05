package com.ericulicny.camera;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Camera {

    private String rootPath;
    private final static String CONST_SNAPCOMMAND = "raspistill -o  ";

    public Camera(String rootPath) {
        this.rootPath = rootPath;
    }

    public boolean snapPicture() {

        ProcessBuilder processBuilder = new ProcessBuilder(CONST_SNAPCOMMAND + this.rootPath);

// 		System.out.println("Executed this command:\n\t" + command.toString());
// 		pb.redirectErrorStream(true);
// 		pb.redirectOutput(
// 				new File(System.getProperty("user.home") + File.separator +
// 						"Desktop" + File.separator + "RPiCamera.out"));
        try {
            Process process             = processBuilder.start();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    public void archiveCurrentPicture() {

    }
}
