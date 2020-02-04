package com.softserve.rms.service.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.softserve.rms.dto.FileStorageDto;
import com.softserve.rms.dto.RegistrationDto;
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

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private AmazonS3 s3client;
    private String bucketName;
    private String accessKey;
    private String secretAccessKey;
    private String endpointUrl;
    //TODO change it to generated tableNameRepository from dynamic db
    private S3BucketTestRepository s3BucketTestRepository;

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
            @Value("${ENDPOINT_URL}") String endpointUrl,
            //TODO change it to generated tableNameRepository from dynamic db
            S3BucketTestRepository s3BucketTestRepository) {
        this.bucketName = bucketName;
        this.accessKey = accessKey;
        this.secretAccessKey = secretAccessKey;
        this.endpointUrl = endpointUrl;
        //TODO change it to generated tableNameRepository from dynamic db
        this.s3BucketTestRepository = s3BucketTestRepository;
    }

    /**
     * Method that set credentials to amazon client
     *
     * @author Mariia Shchur
     */
    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretAccessKey);
        s3client = new AmazonS3Client(credentials);
    }

    /**
     * Method that convert MultipartFile to File
     *
     * @param  multipartFile
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
     * Method that generate unique name for file
     *
     * @param multiPart
     * @return String
     * @author Mariia Shchur
     */
    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
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
     * {@inheritDoc }
     *
     * @author Mariia Sh1chur
     */
    @Override
    @Transactional
    public String uploadFile(MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + fileName;
            //TODO change it to s3BucketTestRepository generated tableNameRepository from dynamic db
            //TODO change S3BucketTest to our table entity (with fileName field)
            s3BucketTestRepository.save(S3BucketTest.builder().fileName(fileName).build());
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    /**
     * {@inheritDoc }
     *
     * @author Mariia Shchur
     */
    @Override
    @Transactional
    public String updateFile(MultipartFile multipartFile, Long resourceId) {
        String fileUrl = "";
        try{
        File file = convertMultiPartToFile(multipartFile);
        String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + fileName;
            //TODO change it to s3BucketTestRepository generated tableNameRepository from dynamic db
            //TODO change S3BucketTest to our table entity (with fileName field)
            Optional<S3BucketTest> q = s3BucketTestRepository.findById(resourceId);
            s3client.deleteObject(bucketName,q.get().getFileName());
            s3BucketTestRepository.save(S3BucketTest.builder().id(resourceId).
                    fileName(fileName).build());
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();}
        return fileUrl;

    }

    /**
     * {@inheritDoc }
     *
     * @author Mariia Shchur
     */
    @Override
    @Transactional
    public void deleteFile(FileStorageDto fileStorageDto, Long resourceId) {
        String fileName = fileStorageDto.getFileUrl().
                substring(fileStorageDto.getFileUrl().lastIndexOf("/") + 1);
        //TODO change it to s3BucketTestRepository generated tableNameRepository from dynamic db
        s3BucketTestRepository.save(S3BucketTest.builder().id(resourceId).
                fileName(null).build());
        s3client.deleteObject(bucketName, fileName);

    }
}
