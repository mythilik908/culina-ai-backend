package com.gg.users;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class Controller {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/api/token")
    public String getAccessToken() {
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();

        String clientRegistrationId = authentication.getAuthorizedClientRegistrationId();

        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                clientRegistrationId, authentication.getName());

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        return accessToken.getTokenValue();
    }

    @GetMapping("/api/user")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }

        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "name", principal.getAttribute("name"),
                "picture", principal.getAttribute("picture")
        ));
    }

    @PostMapping("/api/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String clientRegistrationId = authentication.getAuthorizedClientRegistrationId();
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(clientRegistrationId, authentication.getName());
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        if (accessToken != null) {
            // Revoke the Google access token by calling Google's revoke endpoint
            String revokeUrl = "https://oauth2.googleapis.com/revoke?token=" + accessToken.getTokenValue();
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity(revokeUrl, null, String.class);
        }

        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/auth-status")
    public ResponseEntity<String> getAuthStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }
        return ResponseEntity.ok().build();
    }
}
