package com.gg.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenAIController {

    private final ChatService chatService;

    @Autowired
    public GenAIController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/api/ask-ai")
    public ResponseEntity<String> getResponse(@RequestParam String prompt) {
        String response = chatService.getResponse(prompt);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Test endpoint working!");
    }
}
