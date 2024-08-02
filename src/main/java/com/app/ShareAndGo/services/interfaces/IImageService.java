package com.app.ShareAndGo.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface IImageService {
    String saveImage(File path, MultipartFile profileImage, String imageName) throws IOException;
}
