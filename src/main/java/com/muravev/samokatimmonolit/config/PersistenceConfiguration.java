package com.muravev.samokatimmonolit.config;

import com.muravev.samokatimmonolit.entity.UserEntity;
import com.muravev.samokatimmonolit.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
@RequiredArgsConstructor
public class PersistenceConfiguration {
    private final SecurityService securityService;


    @Bean
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }

    @Bean
    public AuditorAware<UserEntity> auditorAware() {
        return new AuditorAware<>() {
            @Override
            @Transactional(propagation = Propagation.REQUIRES_NEW)
            public Optional<UserEntity> getCurrentAuditor() {
                return securityService.getOptCurrentUser();
            }
        };
    }
}
