package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.user.UserEntity;
import com.muravev.samokatimmonolit.entity.UserInviteEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.model.in.command.user.UserInviteCompleteCommand;
import com.muravev.samokatimmonolit.repo.UserInviteRepo;
import com.muravev.samokatimmonolit.service.UserInviter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserInviterImpl implements UserInviter {
    private final UserInviteRepo inviteRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserInviteEntity invite(UserEntity user, Duration duration) {
        UserInviteEntity userInvite = new UserInviteEntity()
                .setUser(user)
                .setCode(randomInviteCode())
                .setExpirationTime(ZonedDateTime.now().plus(duration));
        return inviteRepo.save(userInvite);
    }

    private String randomInviteCode() {
        return RandomStringUtils.randomNumeric(6);
    }

    @Override
    @Transactional
    public UserEntity completeInvite(long inviteId, UserInviteCompleteCommand command) {
        ZonedDateTime now = ZonedDateTime.now();

        UserInviteEntity userInvite = inviteRepo.findById(inviteId)
                .orElseThrow(() -> new ApiException(StatusCode.USER_INVITE_IS_EXPIRED));

        ZonedDateTime expirationTime = userInvite.getExpirationTime();
        if (expirationTime.isBefore(now)) {
            throw new ApiException(StatusCode.USER_INVITE_IS_EXPIRED);
        }

        String code = userInvite.getCode();
        if (ObjectUtils.notEqual(command.code(), code))
            throw new ApiException(StatusCode.USER_INVALID_INVITE_CODE);

        inviteRepo.delete(userInvite);

        UserEntity user = userInvite.getUser();
        user.setEncodedPassword(passwordEncoder.encode(command.password()));
        user.setNotConfirmed(false);
        return user;
    }

    @Scheduled(cron = "0 0/30 * * * *", zone = "Europe/Samara")
    public void cleanInvites() {
        inviteRepo.deleteByExpirationTimeBefore(ZonedDateTime.now());
    }

}
