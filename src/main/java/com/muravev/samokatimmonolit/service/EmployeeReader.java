package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.user.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeReader {
    Page<EmployeeEntity> findAllColleagues(String keyword, boolean showRetired, Pageable pageable);

    EmployeeEntity findByIdAsEmployee(long id);

    EmployeeEntity findById(long id);

    Page<EmployeeEntity> findAll(long orgId, Pageable pageale);
}
