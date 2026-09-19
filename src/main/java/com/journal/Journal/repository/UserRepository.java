package com.journal.Journal.repository;

import com.journal.Journal.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    public User findByUsername(String username);
    public User deleteByUsername(String username);
}
