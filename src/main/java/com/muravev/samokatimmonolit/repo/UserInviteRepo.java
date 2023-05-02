package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.UserInviteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface UserInviteRepo extends JpaRepository<UserInviteEntity, Long> {
}
