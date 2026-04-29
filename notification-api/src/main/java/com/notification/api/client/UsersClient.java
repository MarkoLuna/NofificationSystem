package com.notification.api.client;

import com.notification.api.dto.UserDto;
import com.notification.api.model.NotificationCategory;
import com.notification.api.model.NotificationChannel;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.net.URIBuilder;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static java.net.http.HttpClient.newHttpClient;
import static java.time.temporal.ChronoUnit.SECONDS;

@Log4j2
@RequiredArgsConstructor
public class UsersClient {

    private final String usersServiceHost;
    private final ObjectMapper mapper;

    private final AuthorizationClient authorizationClient;
    private final String clientId;
    private final String clientSecret;

    /**
     * Get users by channel and category.
     * 
     * @param channel  notification channel
     * @param category notification category
     * @return list of users
     */
    public List<UserDto> getUsersByChannelAndCategory(
            @NonNull NotificationChannel channel,
            @NonNull NotificationCategory category) {

        var authClientCreds = generateClientCredentials();
        if (authClientCreds.isEmpty()) {
            log.error("unable to generate authentication with client credentials");
            return List.of();
        }

        HttpResponse<String> response = null;
        try (HttpClient client = newHttpClient()) {
            URI uri = new URIBuilder(usersServiceHost + "/users")
                    .addParameter("channel", channel.name())
                    .addParameter("category", category.name())
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(Duration.of(10, SECONDS))
                    .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                    .headers(HttpHeaders.AUTHORIZATION, authClientCreds.get())
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            log.warn("failed to get auth credentials token", e);
            return List.of();
        } catch (InterruptedException e) {
            log.warn("failed to get auth credentials token", e);
            return List.of();
        } catch (URISyntaxException e) {
            log.warn("failed to get auth credentials token", e);
            return List.of();
        }

        if (response.statusCode() == 200
                && response.body() != null) {
            return mapper.readValue(response.body(), new TypeReference<List<UserDto>>() {
            });
        }

        log.warn("failed to get auth credentials token response {}", response);
        return List.of();
    }

    /**
     * Generate client credentials.
     * 
     * @return optional token
     */
    private Optional<String> generateClientCredentials() {
        // TODO add this logic to an interceptor ?
        return authorizationClient.getClientCredentialsToken(clientId, clientSecret)
                .map(tokenInfo -> String.format("%s %s", tokenInfo.getTokenType(), tokenInfo.getAccessToken()));
    }
}
