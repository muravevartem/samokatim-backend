package com.muravev.samokatimmonolit.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class AuditEntity {

    @CreatedDate
    private ZonedDateTime createdAt;

    @CreatedBy
    private UserEntity createdBy;

    @LastModifiedDate
    private ZonedDateTime modifiedAt;

    @LastModifiedBy
    private UserEntity modifiedBy;

}
