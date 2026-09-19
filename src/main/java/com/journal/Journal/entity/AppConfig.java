package com.journal.Journal.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "app_config")
@Data
public class AppConfig {

    private String key;
    private String value;
}
