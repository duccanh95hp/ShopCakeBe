package com.example.shop_cake_be.minIO;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Service
public class MinioService {


    @Value("${minio.bucket}")
    String defaultBucketName;

    @Value("${minio.baseFolder}")
    String defaultBaseFolder;
    private final MinioClient minioClient;

    @Autowired
    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void uploadObject(String bucketName, String objectName, String filePath) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            System.out.println("Uploaded object: " + objectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
    public void uploadFile(String name, byte[] content) {
        File file = new File(name);
        file.canWrite();
        file.canRead();
        try {
            FileOutputStream iofs = new FileOutputStream(file);
            iofs.write(content);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(defaultBucketName)
                            .object(defaultBaseFolder + name)
                            .build()
            );

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
    @PostConstruct
    public void init() {
    }
}
