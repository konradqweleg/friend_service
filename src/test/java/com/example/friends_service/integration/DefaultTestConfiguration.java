package com.example.friends_service.integration;


import com.example.friends_service.integration.dbutils.DatabaseActionUtilService;
import com.example.friends_service.integration.mocks.UserServicePortMock;
import com.example.friends_service.integration.request_util.RequestUtil;
import com.example.friends_service.port.in.UserServicePort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class DefaultTestConfiguration {


    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected DatabaseClient databaseClient;
    @Autowired
    protected DatabaseActionUtilService databaseActionUtilService;
    @LocalServerPort
    protected int serverPort;

    protected RequestUtil createRequestUtil() {
        return new RequestUtil(serverPort);
    }

    @BeforeEach
    public void clearAllDatabaseInDatabaseBeforeRunTest() {

    }

    @AfterEach
    public void clearAllDataInDatabaseAfterRunTest() {
        databaseActionUtilService.clearAllDataInDatabase();
    }


}
