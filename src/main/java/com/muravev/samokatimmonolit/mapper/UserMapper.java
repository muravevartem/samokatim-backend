package com.muravev.samokatimmonolit.mapper;

import com.muravev.samokatimmonolit.entity.ClientEntity;
import com.muravev.samokatimmonolit.entity.EmployeeEntity;
import com.muravev.samokatimmonolit.entity.UserEntity;
import com.muravev.samokatimmonolit.model.out.ClientOut;
import com.muravev.samokatimmonolit.model.out.EmployeeOut;
import com.muravev.samokatimmonolit.model.out.UserOut;
import org.mapstruct.Mapper;

@Mapper(uses = {
        OrganizationMapper.class
})
public interface UserMapper {
    EmployeeOut toDto(EmployeeEntity entity);

    UserOut toDto(UserEntity entity);

    ClientOut toDto(ClientEntity entity);
}
