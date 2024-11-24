package com.example.boardserver.service;

import software.amazon.awssdk.regions.Region;

import com.example.boardserver.config.AWSConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.sns.SnsClient;

@Service
@RequiredArgsConstructor
public class SnsService {

    private final AWSConfig awsConfig;

    //aws 인증 객체를 만든다.
    public AwsCredentialsProvider getAwsCredentials(String accessKeyId, String secretAccessKey) {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyId,
                secretAccessKey);
        return() -> awsBasicCredentials;
    }

    public SnsClient getSnsClient(){
        return SnsClient.builder()
                .credentialsProvider(
                        getAwsCredentials(awsConfig.getAccessKey(), awsConfig.getSecretKey())
                ).region(Region.of(awsConfig.getAwsRegion())) //실제 sns config 에 있는 region 값을 가져옴
                .build();
    }
}
