package com.muravev.samokatimmonolit.service;

import com.muravev.samokatimmonolit.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface EmployeeReader {
    Page<EmployeeEntity> findAllColleagues(String keyword, boolean showRetired, Pageable pageable);

    EmployeeEntity findByIdAsEmployee(long id);

    EmployeeEntity findById(long id);
}
