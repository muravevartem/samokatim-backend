package com.muravev.samokatimmonolit.entity.user;

import com.muravev.samokatimmonolit.entity.RentEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@DiscriminatorValue("CLIENT")
@Getter
@Setter
public class ClientEntity extends UserEntity {
    @OneToMany(mappedBy = "client")
    private List<RentEntity> rents;

}
