package com.softserve.rms.service.implementation;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.softserve.rms.entities.S3BucketTest;
import com.softserve.rms.repository.S3BucketTestRepository;
import com.softserve.rms.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private AmazonS3 s3client;
    private String bucketName;
    private String accessKey;
    private String secretAccessKey;
    private String endpointUrl;

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public FileStorageServiceImpl(
            @Value("${BUCKET_NAME}") String bucketName,
            @Value("${ACCESS_KEY}") String accessKey,
            @Value("${SECRET_ACCESS_KEY}") String secretAccessKey,
            @Value("${ENDPOINT_URL}") String endpointUrl) {
        this.bucketName = bucketName;
        this.accessKey = accessKey;
        this.secretAccessKey = secretAccessKey;
        this.endpointUrl = endpointUrl;
    }

    /**
     * Method that set credentials to amazon client
     *
     * @author Mariia Shchur
     */
    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretAccessKey);
        s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }

    /**
     * Method that convert MultipartFile to File
     *
     * @param multipartFile
     * @return File
     * @author Mariia Shchur
     */
    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(multipartFile.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipartFile.getBytes());
        fos.close();
        return convFile;
    }

    /**
     * Method that upload file to s3 bucket
     *
     * @param fileName,file
     * @author Mariia Shchur
     */
    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    /**
     * Method that generate file name
     *
     * @return String
     * @author Mariia Shchur
     */
    private String generateFileName(){
        return UUID.randomUUID().toString();
    }

    /**
     * {@inheritDoc }
     *
     * @author Mariia Shchur
     */
    @Override
    @Transactional
    public String uploadFile(MultipartFile multipartFile) {
        String fileName = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            fileName = generateFileName();
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * {@inheritDoc }
     *
     * @author Mariia Shchur
     */
    @Override
    @Transactional
    public void deleteFile(String fileName) {
        s3client.deleteObject(bucketName,fileName);
    }
}
