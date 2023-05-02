package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.UserInviteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface UserInviteRepo extends JpaRepository<UserInviteEntity, Long> {
    @Transactional
    @Modifying
    void deleteByExpirationTimeBefore(ZonedDateTime expirationTime);
}
