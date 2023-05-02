package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.model.in.command.user.UserResetPasswordCommand;

public interface UserWriter {
    void resetPassword(UserResetPasswordCommand command);
}
