package com.journal.Journal.controller;

import com.journal.Journal.api.response.WeatherApiResponse;
import com.journal.Journal.cache.AppCache;
import com.journal.Journal.entity.User;
import com.journal.Journal.model.SentimentData;
import com.journal.Journal.service.KafkaPublishService;
import com.journal.Journal.service.UserDetailsServiceImpl;
import com.journal.Journal.service.UserService;
import com.journal.Journal.service.WeatherService;
import com.journal.Journal.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {
    @Autowired
    private UserService userService;
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private AppCache appCache;
    @Autowired
    private KafkaPublishService kafkaPublishService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @GetMapping("/health-check")
    public String healthCheck(){
        return "OK";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        User userInDB = userService.findByUsername(user.getUsername());
        if(userInDB != null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User Already Exist");
        userService.saveNewUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt,HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error Occurred : ",e);
            return new ResponseEntity<>("Incorrect Username and Password",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/weather")
    public ResponseEntity<?> getWeatherInfo() {
        WeatherApiResponse weatherApiResponse = weatherService.getWeatherInfo("Mumbai");
        if(weatherApiResponse!=null)
            return new ResponseEntity<>("Hi, Todays weather feels like :"+weatherApiResponse.getMain().getFeelsLike(),HttpStatus.OK);

        return new ResponseEntity<>("Hi No Response to show",HttpStatus.OK);
    }

    @PostMapping("kafka-publish")
    public ResponseEntity<?> KafkaPublish(@RequestParam String topic, @RequestBody SentimentData sentimentData) {
        kafkaPublishService.kafkaPublish(topic,sentimentData.getEmail(),sentimentData);
        return new ResponseEntity<>("Message Published",HttpStatus.OK);
    }

    @GetMapping("/cache-refresh")
    public void AppChacheRefresh() {
        appCache.init();
    }
}
