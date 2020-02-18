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
     * @param recourseId for deleting.
     */
    void deleteFile( Long recourseId);

    /**
     * Method for updating a file.
     *
     * @param multipartFile for updating.
     * @return url of the deleted file.
     */
    String updateFile(MultipartFile multipartFile, Long recourseId);
}
