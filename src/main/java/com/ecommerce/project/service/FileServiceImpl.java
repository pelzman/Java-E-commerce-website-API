package com.ecommerce.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements  FileService{

     @Override
     public String uploadProductImage(String path, MultipartFile file) throws IOException {
        //Get the current file name
        String originalFileName = file.getOriginalFilename();
        // generate a random or unique file names for the image
        String randomId = UUID.randomUUID().toString();
        String fileName =  randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));
        String filePath = path + File.separator + fileName;
        //check if the folder path exist and create
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }
        // upload to the server
        Files.copy(file.getInputStream(), Paths.get(filePath));
        // return the file name
        return fileName;
    }
}
