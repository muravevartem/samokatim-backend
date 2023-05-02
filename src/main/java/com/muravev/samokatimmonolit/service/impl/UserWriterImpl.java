package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.UserEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.model.in.command.user.UserResetPasswordCommand;
import com.muravev.samokatimmonolit.repo.UserRepo;
import com.muravev.samokatimmonolit.service.UserInviter;
import com.muravev.samokatimmonolit.service.UserWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserWriterImpl implements UserWriter {
    private final UserRepo userRepo;

    private final UserInviter userInviter;


    @Override
    @Transactional
    public void resetPassword(UserResetPasswordCommand command) {
        UserEntity user = userRepo.findByEmail(command.email().toLowerCase())
                .orElseThrow(() -> new ApiException(StatusCode.USER_NOT_FOUND));
        userInviter.invite(user, Duration.ofMinutes(5));
    }
}
