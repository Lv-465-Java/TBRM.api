package com.softserve.rms.service.impl;

import com.softserve.rms.entities.S3BucketTest;
import com.softserve.rms.repository.S3BucketTestRepository;
import com.softserve.rms.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
@RunWith(PowerMockRunner.class)
public class FileStorageServiceImplTest {
    @Mock
    private S3BucketTestRepository s3BucketTestRepository;
    @InjectMocks
    private FileStorageServiceImpl fileStorageService;

    @Test
    public void uploadFile() {
    }

    @Test
    public void updateFile() {
    }

    @Test
    public void deleteFile() {
    }

}