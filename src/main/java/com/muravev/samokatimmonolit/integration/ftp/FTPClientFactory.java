package com.muravev.samokatimmonolit.integration.ftp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

    public FTPClient getFTPClient() {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(host, port);

            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftpClient.disconnect();
                throw new IllegalStateException("FTP server refused connection...");
            }
            ftpClient.enterLocalPassiveMode();
            if (!ftpClient.login(username, password)) {
                throw new IllegalStateException("Couldn't login to FTP server");
            }
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient;
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't create FTP client", e);
        }
    }


    public void close(FTPClient client) {
        try {
            client.logout();
            client.disconnect();
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't close connection with FTP server", e);
        }
    }
}
