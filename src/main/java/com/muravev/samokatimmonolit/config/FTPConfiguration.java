package com.muravev.samokatimmonolit.config;

import com.muravev.samokatimmonolit.repo.FileRepo;
import com.muravev.samokatimmonolit.service.FileService;
import com.muravev.samokatimmonolit.service.impl.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FTPConfiguration {

    @Value("${integration.ftp.host}")
    private String host;

    @Value("${integration.ftp.port}")
    private int port;

    @Value("${integration.ftp.username}")
    private String username;

    @Value("${integration.ftp.password}")
    private String password;


    @Bean
    @SneakyThrows
    FTPClient ftpClient() {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(host, port);
        ftpClient.login(username, password);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        return ftpClient;
    }
}
