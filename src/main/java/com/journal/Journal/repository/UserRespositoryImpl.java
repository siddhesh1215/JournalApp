package com.journal.Journal.repository;

import com.journal.Journal.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRespositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;
    public List<User> getUserForSA() {
        Query query = new Query();
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
        query.addCriteria(Criteria.where("email").exists(true).ne(null).ne("").regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"));
        List<User> users = mongoTemplate.find(query, User.class);
        return users;

//        for using or operator or something like that
//        Criteria criteria = new Criteria();
//        List<User> users = query.addCriteria(criteria.orOperator(Criteria.where("sentimentAnalysis").is(true),Criteria.where("email").exists(true)));
//        return users;
    }
}
