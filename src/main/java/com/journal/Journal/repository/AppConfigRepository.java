package com.journal.Journal.repository;

import com.journal.Journal.entity.AppConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppConfigRepository extends MongoRepository<AppConfig,String> {

}
