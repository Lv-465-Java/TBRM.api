package com.softserve.rms.controller;

import com.softserve.rms.dto.FileStorageDto;
import com.softserve.rms.service.impl.FileStorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class RegistratorController {
    private FileStorageServiceImpl fileStorageService;

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public RegistratorController(FileStorageServiceImpl fileStorageService) {
        this.fileStorageService = fileStorageService;
    }
    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return  ResponseEntity.status(HttpStatus.OK).
                body(fileStorageService.uploadFile(file));
    }

    @DeleteMapping("/deleteFile")
    public ResponseEntity deleteFile(@RequestBody FileStorageDto fileStorageDto) {
        fileStorageService.deleteFileFromS3Bucket(fileStorageDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
