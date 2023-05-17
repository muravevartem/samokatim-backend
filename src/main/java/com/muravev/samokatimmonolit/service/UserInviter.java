package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.user.UserEntity;
import com.muravev.samokatimmonolit.entity.UserInviteEntity;
import com.muravev.samokatimmonolit.model.in.command.user.UserInviteCompleteCommand;

import java.time.Duration;

public interface UserInviter {
    UserInviteEntity invite(UserEntity user, Duration duration);

    UserEntity completeInvite(long inviteId, UserInviteCompleteCommand command);
}
