package com.github.emraxxor.ivip.common.storage;

import java.io.InputStream;
import java.util.Optional;

public interface MinioStorage {

    void put(String bucketName, String objectName, byte[] data);

    Optional<String> getObjectUrl(String bucketName, String objectName);

    void remove(String bucketName, String objectName);

    Optional<InputStream> getObjectStream(String bucketName, String objectName);
}
