package com.muravev.samokatimmonolit.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Entity
@DiscriminatorValue("CLIENT")
@Getter
@Setter
public class ClientEntity extends UserEntity {
    @OneToMany(mappedBy = "client")
    private List<RentEntity> rents;

}
