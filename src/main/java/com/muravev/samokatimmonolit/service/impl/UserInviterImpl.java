package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.repo.UserRepo;
import com.muravev.samokatimmonolit.service.UserInviter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserInviterImpl implements UserInviter {
    private final UserRepo userRepo;

    @Scheduled(fixedDelayString = "PT1H")
    public void cleanInvites() {
        userRepo.deleteAllClientsLastInvites(ZonedDateTime.now().minusHours(1L), true);
        log.info("Cleaned clients");
    }
}
