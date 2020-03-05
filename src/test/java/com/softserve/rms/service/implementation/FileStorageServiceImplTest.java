package com.softserve.rms.service.implementation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;


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