package org.ihiw.management.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class S3Configuration {
    private final Environment env;
    private final Logger log = LoggerFactory.getLogger(S3Configuration.class);

    public S3Configuration(Environment env) {
        this.env = env;
    }

    @Bean(name="s3bucket")
    public String bucket() {
        return env.getProperty("aws.s3.bucket");
    }

    @Bean
    public AmazonS3 s3Provider(){
        if (env != null){
            ClientConfiguration config = new ClientConfiguration();
            if (!env.getProperty("aws.s3.timeout").isEmpty()){
                config.setConnectionTimeout(Integer.parseInt(env.getProperty("aws.s3.timeout")));
                config.setSocketTimeout(Integer.parseInt(env.getProperty("aws.s3.timeout")));
            }
            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withRegion(env.getProperty("aws.s3.region"))
                .withClientConfiguration(config)
                .build();
            log.info("Created AWS S3 client.");

            return s3;
        }
        return null;
    }
}
