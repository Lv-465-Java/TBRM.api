package com.softserve.rms.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileStorageService {

    /**
     * Method for saving a file.
     *
     * @param multipartFile file for saving.
     * @return url of the saved file.
     */
    String uploadFile(MultipartFile multipartFile);

    /**
     * Method for deleting a file.
     *
     * @param fileName for deleting.
     */
    void deleteFile(String fileName);

}
