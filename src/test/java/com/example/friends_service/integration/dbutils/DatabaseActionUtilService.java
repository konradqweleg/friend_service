package com.example.friends_service.integration.dbutils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;

@Service
public class DatabaseActionUtilService {
    @Autowired
    private DatabaseClient databaseClient;

    public void clearAllDataInDatabase() {
        databaseClient.sql("DELETE FROM friends_schema.friends where 1=1").
                fetch().
                rowsUpdated()
                .block();
    }

}
