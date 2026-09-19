package com.journal.Journal.service;

import com.journal.Journal.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaPublishService {
    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    public void kafkaPublish(String topic,String key, SentimentData sentimentData) {
        kafkaTemplate.send(topic,key,sentimentData);
    }
}
