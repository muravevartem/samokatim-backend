package com.muravev.samokatimmonolit.kafka.listener;

import com.muravev.samokatimmessage.GeoPointReceivedMessage;
import com.muravev.samokatimmonolit.kafka.TopicName;
import com.muravev.samokatimmonolit.model.InventoryStatus;
import com.muravev.samokatimmonolit.service.InventorySaver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeoPointReceivedMessageListener {
    private final InventorySaver inventorySaver;


    @KafkaListener(topics = TopicName.GEO_POINT_RECEIVED)
    public void handle(GeoPointReceivedMessage message) {
        inventorySaver.savePoint(message);
    }
}
