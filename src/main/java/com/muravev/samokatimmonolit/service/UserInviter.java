package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.UserEntity;
import com.muravev.samokatimmonolit.model.in.command.user.UserInviteCompleteCommand;

import java.time.Duration;

public interface UserInviter {
    void invite(UserEntity user, Duration duration);

    UserEntity completeInvite(long inviteId, UserInviteCompleteCommand command);
}
