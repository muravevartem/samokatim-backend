package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.DepositEntity;
import com.muravev.samokatimmonolit.model.DespositStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositRepo extends JpaRepository<DepositEntity, Long> {
    List<DepositEntity> findAllByStatus(DespositStatus status);
}
