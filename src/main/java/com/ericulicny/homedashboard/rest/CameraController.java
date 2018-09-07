package com.ericulicny.homedashboard.rest;

import com.ericulicny.camera.Camera;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    @GetMapping(
            value = "/api/camera/last",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    @ResponseBody
    public byte[] pictureLast() throws IOException {
        Camera camera = new Camera(cameraDataPath);
        log.info("Retrieving last Picture");
        File initialFile = new File(camera.getLastPictureLocation());
        InputStream in = new FileInputStream(initialFile);
        return IOUtils.toByteArray(in);
    }


}