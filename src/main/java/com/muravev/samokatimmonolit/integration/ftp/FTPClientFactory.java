package com.muravev.samokatimmonolit.integration.ftp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FTPClientFactory {
    @Value("${integration.ftp.host}")
    private String host;

    @Value("${integration.ftp.port}")
    private int port;

    @Value("${integration.ftp.username}")
    private String username;

    @Value("${integration.ftp.password}")
    private String password;

    @SneakyThrows
    public FTPClient getFTPClient() {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(host, port);
        ftpClient.login(username, password);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        return ftpClient;
    }
}
