package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.ClientEntity;
import com.muravev.samokatimmonolit.service.ClientReader;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientReaderImpl implements ClientReader {
    private final SecurityService securityService;


    @Override
    @Transactional(readOnly = true)
    public ClientEntity me() {
        return securityService.getCurrentClient();
    }
}
