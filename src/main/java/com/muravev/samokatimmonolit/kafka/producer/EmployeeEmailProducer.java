package com.muravev.samokatimmonolit.kafka.producer;

import com.muravev.samokatimmessage.EmailInviteClientMessage;
import com.muravev.samokatimmonolit.entity.user.UserEntity;
import com.muravev.samokatimmonolit.entity.UserInviteEntity;
import com.muravev.samokatimmonolit.kafka.TopicName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeEmailProducer {
    private static final String INVITE_LINK_PATTERN = "https://b2b.1304294-cu57808.tw1.ru/confirm/%s";


    private final KafkaTemplate<String, EmailInviteClientMessage> kafkaTemplate;


    public void sendInvite(UserInviteEntity invite) {
        UserEntity user = invite.getUser();

        EmailInviteClientMessage message = EmailInviteClientMessage.newBuilder()
                .setInviteLink(INVITE_LINK_PATTERN.formatted(invite.getId()))
                .setTo(user.getEmail())
                .setCode(invite.getCode())
                .build();
        kafkaTemplate.send(TopicName.EMAIL_INVITE_CLIENT, message);
    }
}
