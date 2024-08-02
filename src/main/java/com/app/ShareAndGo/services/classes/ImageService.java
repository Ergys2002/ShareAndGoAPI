package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.services.interfaces.IImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@Service
@AllArgsConstructor
public class ImageService implements IImageService {
    @Override
    public String saveImage(File path, MultipartFile profileImage, String imageName) throws IOException {

        File uploadRootDir = new File(String.valueOf(path));

        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }

        try {
            String originalFilename = profileImage.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = imageName + extension;
            File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + fileName);


            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(profileImage.getBytes());
            stream.close();
            return serverFile.getName();

        } catch (IOException e) {
            throw new IOException("Problem with file saving");
        }
    }

}
