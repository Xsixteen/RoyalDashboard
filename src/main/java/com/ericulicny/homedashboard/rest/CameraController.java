package com.ericulicny.homedashboard.rest;

import com.ericulicny.camera.Camera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CameraController {

    @Value("${cameraDataPath}")
    private String cameraDataPath;

    private static final Logger log = LoggerFactory.getLogger(CameraController.class);

    @RequestMapping("/api/camera/snap")
    public String snapPicture() {
        Camera camera = new Camera(cameraDataPath);
        log.info("Snapping Picture!");
        if(camera.snapPicture()) {
            return "OK";
        } else {
            return "FAIL";
        }
    }

    @RequestMapping("/api/camera/time")
    public String snapPictureTime() {
        log.info("Retrieving last snapped time!");

        return "OK";
    }

    @RequestMapping("/api/camera/last")
    public String pictureLast(@RequestParam(value="latestpicture") String latestpicture) {
        log.info("Retrieving last Picture");

        return "OK";
    }


}