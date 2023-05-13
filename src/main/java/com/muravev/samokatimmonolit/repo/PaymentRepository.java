package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.DepositEntity;
import com.muravev.samokatimmonolit.entity.PaymentEntity;
import com.muravev.samokatimmonolit.model.DespositStatus;
import com.muravev.samokatimmonolit.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findAllByStatusIsIn(Collection<PaymentStatus> status);
}