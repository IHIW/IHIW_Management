package org.ihiw.management.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Repository
public class FileRepository {
    private final AmazonS3 s3;
    private final String bucket;

    private final Logger log = LoggerFactory.getLogger(FileRepository.class);

    public FileRepository(AmazonS3 s3, String bucket) {
        this.s3 = s3;
        this.bucket = bucket;
    }

    public void storeFile(String fileName, byte[] data){
        InputStream stream = new ByteArrayInputStream(data);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(data.length);

        PutObjectRequest objectRequest = new PutObjectRequest(bucket, fileName, stream, metadata);
        s3.putObject(objectRequest);

        log.debug("Stored file " + fileName + " with length " + data.length + " in bucket " + bucket);
    }

    public byte[] getFile(String fileName){
        return new byte[4];
    }
}
