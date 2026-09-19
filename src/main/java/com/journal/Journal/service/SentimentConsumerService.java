package com.journal.Journal.service;

import com.journal.Journal.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {
    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "weekly.sentiment",groupId = "weekly.sentiment.grp", autoStartup = "false")
    public void consume(SentimentData sentimentData) {
        sendMail(sentimentData);
        System.out.println("Mail Sent to :"+sentimentData.getEmail());
    }

    public void sendMail(SentimentData sentimentData) {
        emailService.sendMail(sentimentData.getEmail(),"Sentiment for Week",sentimentData.getSentiment());
    }
}
