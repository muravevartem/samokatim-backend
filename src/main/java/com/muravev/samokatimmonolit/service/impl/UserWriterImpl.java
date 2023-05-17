package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.user.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.user.UserEntity;
import com.muravev.samokatimmonolit.entity.UserInviteEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.kafka.producer.ClientEmailProducer;
import com.muravev.samokatimmonolit.kafka.producer.EmployeeEmailProducer;
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

    private final ClientEmailProducer clientEmailProducer;
    private final EmployeeEmailProducer employeeEmailProducer;

    @Override
    @Transactional
    public void resetPassword(UserResetPasswordCommand command) {
        UserEntity user = userRepo.findByEmail(command.email().toLowerCase())
                .orElseThrow(() -> new ApiException(StatusCode.USER_NOT_FOUND));
        UserInviteEntity invite = userInviter.invite(user, Duration.ofMinutes(5));

        if (user instanceof EmployeeEntity) {
            employeeEmailProducer.sendInvite(invite);
        } else {
            clientEmailProducer.sendInvite(invite);
        }
    }
}
