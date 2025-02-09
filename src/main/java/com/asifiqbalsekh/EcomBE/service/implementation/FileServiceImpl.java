package com.asifiqbalsekh.EcomBE.service.implementation;

import com.asifiqbalsekh.EcomBE.exception.APIException;
import com.asifiqbalsekh.EcomBE.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadProductImage(String folderPath, MultipartFile image) {
        //Get original Image filename
        String originalImageName=image.getOriginalFilename();

        //generate new unique Image file name and file path

        String randomFileName= UUID.randomUUID().toString();
        //getting the extension and adding the image extension in to new image name
        String newImageName=randomFileName.concat(originalImageName.substring(originalImageName.lastIndexOf(".")));
        String filePath =folderPath+ File.separator+newImageName;


        //Saving the file

        File folder=new File(folderPath);
        if(!folder.exists()){
            folder.mkdirs();
        }
        try {
            Files.copy(image.getInputStream(), Paths.get(filePath));
        } catch (IOException e) {
            throw new APIException("Problem With File Uploading "+e);
        }


        return newImageName;
    }
}
