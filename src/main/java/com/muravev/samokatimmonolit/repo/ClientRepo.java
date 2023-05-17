package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.user.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepo extends JpaRepository<ClientEntity, Long> {

}
