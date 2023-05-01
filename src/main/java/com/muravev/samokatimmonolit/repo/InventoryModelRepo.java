package com.muravev.samokatimmonolit.repo;

import com.muravev.samokatimmonolit.entity.InventoryEntity;
import com.muravev.samokatimmonolit.entity.InventoryModelEntity;
import com.muravev.samokatimmonolit.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryModelRepo extends JpaRepository<InventoryModelEntity, Long> {
    @Query("""
            SELECT model FROM InventoryModelEntity model
            WHERE (model.approved OR model.createdBy = :userEntity)
            """)
    List<InventoryModelEntity> findAllByApproved(UserEntity userEntity);

    @Query("""
            SELECT model FROM InventoryModelEntity model
            WHERE LOWER(model.name) LIKE LOWER('%'||:keyword||'%')
                   AND (model.approved OR model.createdBy = :userEntity)
            """)
    List<InventoryModelEntity> findAllByApproved(UserEntity userEntity, String keyword);
}
