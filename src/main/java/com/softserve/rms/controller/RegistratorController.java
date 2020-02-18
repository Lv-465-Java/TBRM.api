package com.softserve.rms.controller;

import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.service.implementation.FileStorageServiceImpl;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 403,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
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
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 403,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @PutMapping("{resourceId}/updateFile/")
    public ResponseEntity<String> updateFile(@RequestPart(value = "file") MultipartFile file,
                                             @PathVariable long resourceId) {
        return  ResponseEntity.status(HttpStatus.OK).
                body(fileStorageService.updateFile(file,resourceId));
    }

    /**
     * Method for deleting files.
     *
     * @param resourceId
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 403,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @DeleteMapping("{resourceId}/deleteFile")
    public ResponseEntity deleteFile(@PathVariable long resourceId) {
        fileStorageService.deleteFile(resourceId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
