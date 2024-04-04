package com.example.friends_service.integration.request_util;

import java.net.URI;
import java.net.URISyntaxException;

public class RequestUtil {
    private int serverPort;
    private static String prefixHttp = "http://localhost:";
    private static String prefixServicesApiV1 = "/friendsService/api/v1/friends";

    public RequestUtil(int serverPort) {
        this.serverPort = serverPort;
    }

    public URI createRequestCreateFriends() throws URISyntaxException {
        return new URI(prefixHttp + serverPort + prefixServicesApiV1 + "/createFriends");
    }

    public URI createRequestIsFriends(Long idFirstFriends, Long idSecondFriends) throws URISyntaxException {
        return new URI(prefixHttp + serverPort + prefixServicesApiV1 + "/isFriends?friendFirstId="+idFirstFriends+"&friendSecondId="+idSecondFriends);
    }

    public URI createRequestGetFriends(Long idUser) throws URISyntaxException {
        return new URI(prefixHttp + serverPort + prefixServicesApiV1 + "/getFriends?idUser="+idUser);
    }


}
