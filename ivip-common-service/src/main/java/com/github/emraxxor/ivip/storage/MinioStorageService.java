package com.github.emraxxor.ivip.storage;

import com.github.emraxxor.ivip.common.exception.BusinessValidationException;
import com.github.emraxxor.ivip.common.storage.MinioStorage;
import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@ConditionalOnProperty(value = "ivip.minio.enabled", havingValue = "true")
@Service
public class MinioStorageService implements MinioStorage {

    private final MinioClient minioClient;

    public MinioStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public void put(String bucketName, String objectName, byte[] data) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);

            Stream
                    .of(minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build()))
                    .filter(e -> !e)
                    .findAny()
                    .ifPresent(x -> this.createBucket(bucketName));

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(bais, bais.available(), -1)
                            .contentType(mimeType(objectName))
                            .build()
            );
        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                 XmlParserException | ServerException e) {
            log.error(e.getMessage(), e);
            throw new BusinessValidationException("The Bucket could not be created.");
        }
    }

    private void createBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new BusinessValidationException("Bucket could not be created.");
        }
    }

    @Override
    public void remove(String bucketName, String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                 XmlParserException | ServerException ex) {
            log.error(ex.getMessage(), ex);
            throw new BusinessValidationException("The bucket could not be removed.");
        }
    }

    @Override
    public Optional<String> getObjectUrl(String bucketName, String objectName) {
        try {
            return Optional.of(minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()));
        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                 XmlParserException | ServerException ex) {
            log.error(ex.getMessage(), ex);
        }
        return Optional.empty();
    }

    private String mimeType(String fileName) {
        final String extension = extractExtension(fileName);
        switch (extension) {
            case ".pdf":
                return "application/pdf";
            case ".docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case ".jpg":
                return "image/jpeg";
            case ".png":
                return "image/png";
            case ".zip":
                return "application/zip";
            default:
                return null;
        }
    }

    private String extractExtension(@NotNull String originalFileName) {
        return Stream
                .of(originalFileName.lastIndexOf("."))
                .filter(e -> e != -1)
                .map(originalFileName::substring)
                .findAny()
                .orElse("unsupported");
    }

    @Override
    public Optional<InputStream> getObjectStream(String bucketName, String objectName) {
        try {
            GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            return Optional.of(response);
        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException |
                 XmlParserException | ServerException e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }
}
