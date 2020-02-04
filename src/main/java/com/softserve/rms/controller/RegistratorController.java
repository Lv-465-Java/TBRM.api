package com.softserve.rms.controller;

import com.softserve.rms.dto.FileStorageDto;
import com.softserve.rms.service.implementation.FileStorageServiceImpl;
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
    /**
     * Method for uploading files.
     *
     * @param file to save.
     * @return url of the saved file.
     */
    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return  ResponseEntity.status(HttpStatus.OK).
                body(fileStorageService.uploadFile(file));
    }

    /**
     * Method for updating files.
     *
     * @param file to save.
     * @return url of the updated file.
     */
    @PutMapping("{resourceId}/updateFile/")
    public ResponseEntity<String> updateFile(@RequestPart(value = "file") MultipartFile file,
                                             @PathVariable long resourceId) {
        return  ResponseEntity.status(HttpStatus.OK).
                body(fileStorageService.updateFile(file,resourceId));
    }

    /**
     * Method for deleting files.
     *
     * @param fileStorageDto file's url to delete.
     */
    @DeleteMapping("{resourceId}/deleteFile")
    public ResponseEntity deleteFile(@RequestBody FileStorageDto fileStorageDto,
                                     @PathVariable long resourceId) {
        fileStorageService.deleteFile(fileStorageDto, resourceId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
