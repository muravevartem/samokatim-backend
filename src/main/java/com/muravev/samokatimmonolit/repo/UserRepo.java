package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
    @Transactional
    @Modifying
    @Query("delete from ClientEntity u where u.createdAt < ?1 and u.notConfirmed = ?2")
    void deleteAllClientsLastInvites(@NonNull ZonedDateTime createdAt, @NonNull boolean notConfirmed);

    Optional<UserEntity> findByEmail(@Param("email") String email);

}
