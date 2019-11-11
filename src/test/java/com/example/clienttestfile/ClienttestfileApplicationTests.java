package com.example.clienttestfile;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.*;

@SpringBootTest
@Slf4j
class ClienttestfileApplicationTests {


    @Autowired
    RestTemplate restTemplate;



    // La memoire du teste explose  //Big FAIL
    @Test
    void downloadFile() throws IOException, InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("applicationKey", "macle");
        HttpEntity httpEntity = new HttpEntity(headers);
        Thread.sleep(10000);
        ResponseEntity<Resource> response = restTemplate.exchange("http://localhost:8081/download/1", HttpMethod.GET, httpEntity, Resource.class);
        File tempFile = File.createTempFile("test-download", ".pdf");
        try (InputStream inputStream = response.getBody().getInputStream()) {
            try (OutputStream os = new FileOutputStream(tempFile)) {
                IOUtils.copy(inputStream, os);
            }
        }
        Thread.sleep(10000);
    }


    @Test
    void downloadFile_test2() throws IOException, InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("applicationKey", "macle");

        RequestCallback requestCallback = new RequestCallback() {
            @Override
            public void doWithRequest(ClientHttpRequest clientHttpRequest) throws IOException {
                clientHttpRequest.getHeaders().addAll(headers);
            }
        };

        Thread.sleep(10000);
        restTemplate.execute("http://localhost:8082/download", HttpMethod.GET, requestCallback, clientHttpResponse -> {
            File tempFile = File.createTempFile("test2-download", ".pdf");
            try (OutputStream os = new FileOutputStream(tempFile)) {
                IOUtils.copy(clientHttpResponse.getBody(), os);
            }
            return clientHttpResponse.getBody();
        });
        Thread.sleep(10000);
    }


    @Test
    void contextLoads() {
    }

}
