package com.journal.Journal.scheduler;

import com.journal.Journal.cache.AppCache;
import com.journal.Journal.entity.JournalEntry;
import com.journal.Journal.entity.User;
import com.journal.Journal.enums.Sentiment;
import com.journal.Journal.model.SentimentData;
import com.journal.Journal.repository.UserRespositoryImpl;
import com.journal.Journal.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRespositoryImpl userRespository;
    @Autowired
    private AppCache appCache;
    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

//    @Scheduled(cron = "0 0 9 ? * SUN *")
    public void fetchUsersAndSendSAMail() {
        List<User> users = userRespository.getUserForSA();
        for(User user: users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x-> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x->x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment,Integer> sentimentCount = new HashMap<>();
            for(Sentiment sentiment : sentiments) {
                if(sentiment != null) {
                    sentimentCount.put(sentiment, sentimentCount.getOrDefault(sentiment,0)+1);
                }
            }
            Sentiment mostFrequestSentiment = null;
            int maxCount = 0;
            for(Map.Entry<Sentiment,Integer> entry : sentimentCount.entrySet()) {
                if(entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequestSentiment = entry.getKey();
                }
            }
            if(mostFrequestSentiment!=null) {
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days" + mostFrequestSentiment).build();
                try {
                    kafkaTemplate.send("weekly.sentiment",sentimentData.getEmail(),sentimentData);
                } catch (Exception e) {
                    emailService.sendMail(sentimentData.getEmail(),"Sentiment for Week",sentimentData.getSentiment());
                }
            }
        }
    }

//    @Scheduled(cron = "0 0/10 * 1/1 * ? *")
    public void refreshCache() {
        appCache.init();
    }
}
