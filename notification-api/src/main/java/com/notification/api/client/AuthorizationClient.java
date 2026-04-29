package com.notification.api.client;

import com.notification.api.dto.AuthTokenInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.hc.core5.http.HttpHeaders;
import tools.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Optional;

import static java.net.http.HttpClient.newHttpClient;
import static java.time.temporal.ChronoUnit.SECONDS;

@Log4j2
@RequiredArgsConstructor
public class AuthorizationClient {

    private final String authServiceHost;
    private final ObjectMapper mapper;

    /**
     * Get client credentials token.
     * 
     * @param clientId     client id
     * @param clientSecret client secret
     * @return optional token info
     */
    public Optional<AuthTokenInfo> getClientCredentialsToken(String clientId, String clientSecret) {
        var params = new HashMap<String, String>();
        params.put("grant_type", "client_credentials");
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);

        String jsonBody = mapper.writeValueAsString(params);

        HttpResponse<String> response = null;
        try (HttpClient client = newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(authServiceHost + "/realms/dev/protocol/openid-connect/token"))
                    .timeout(Duration.of(10, SECONDS))
                    .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | URISyntaxException | InterruptedException e) {
            log.warn("failed to get auth credentials token", e);
            return Optional.empty();
        }

        if (response.statusCode() == 200
                && response.body() != null) {
            AuthTokenInfo token = mapper.readValue(response.body(), AuthTokenInfo.class);
            return Optional.ofNullable(token);
        }

        log.warn("failed to get auth credentials token response {}", response);
        return Optional.empty();
    }
}
