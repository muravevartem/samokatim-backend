package com.muravev.samokatimmonolit.mapper;

import com.muravev.samokatimmonolit.entity.EmployeeEntity;
import com.muravev.samokatimmonolit.model.out.EmployeeOut;
import org.mapstruct.Mapper;

@Mapper(uses = {
        OrganizationMapper.class
})
public interface UserMapper {
    EmployeeOut toDto(EmployeeEntity entity);
}
