package com.journal.Journal.cache;

import com.journal.Journal.entity.AppConfig;
import com.journal.Journal.repository.AppConfigRepository;
import com.journal.Journal.service.EmailService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {
    public enum keys{
        WEATHER_API_KEY,
        WEATHER_URI
    }
    public Map<String,String> APP_CACHE;
    @Autowired
    private AppConfigRepository appConfigRepository;

//    @Autowired
//    private EmailService emailService;

    @PostConstruct
    public void init() {
        APP_CACHE = new HashMap<>();
        List<AppConfig> all = appConfigRepository.findAll();
        for(AppConfig a : all) {
            APP_CACHE.put(a.getKey(),a.getValue());
        }
//        emailService.sendMail("kataleashitosh123@gmail.com","Hi","Hello");
    }
}
