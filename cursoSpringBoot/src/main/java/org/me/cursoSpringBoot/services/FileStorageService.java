package org.me.cursoSpringBoot.services;

import org.me.cursoSpringBoot.config.FileStorageConfig;
import org.me.cursoSpringBoot.exceptions.FileStorageException;
import org.me.cursoSpringBoot.exceptions.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {

        Path path = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath().normalize();

        this.fileStorageLocation = path;

        try {

            Files.createDirectories(fileStorageLocation);

        } catch (Exception e) {

            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    public String storeFile(MultipartFile file){

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {

            if (fileName.contains("..")){

                throw new FileStorageException("Sorry file name contains invalid path sequence " + fileName);
            }

            Path targeLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targeLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception e) {

            throw new FileStorageException("Could not store file " + fileName +". Please try again.", e);
        }
    }

    public Resource loadFileAsResource(String fileName){

        try {

            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()){

                return resource;
            } else {

                throw new MyFileNotFoundException("File not found: " + fileName);    
            }

        } catch (Exception e) {

            throw new MyFileNotFoundException("File not found: " + fileName, e);
        }
    }
}
