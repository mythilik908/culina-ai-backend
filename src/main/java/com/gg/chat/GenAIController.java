package com.gg.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gg.recipe.RecipeService;

@RestController
public class GenAIController {

    private static final Logger logger = LoggerFactory.getLogger(GenAIController.class);

    private final ChatService chatService;
    private final RecipeService recipeService;

    @Autowired
    public GenAIController(ChatService chatService, RecipeService recipeService) {
        this.chatService = chatService;
        this.recipeService = recipeService;
    }

    @GetMapping("/api/ask-ai")
    public ResponseEntity<String> getResponse(@RequestParam String prompt) {
        logger.info("Received request for prompt: {}", prompt);
        try {
            String response = chatService.getResponse(prompt);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing request: ", e);
            return ResponseEntity.internalServerError().body("Error processing request");
        }
    }

    @GetMapping("/api/recipe-generator")
    public String recipeGenerator(@RequestParam String ingredients,
            @RequestParam(defaultValue = "any") String cuisine,
            @RequestParam(defaultValue = "") String dietaryRestrictions) {
        return recipeService.createRecipe(ingredients, cuisine, dietaryRestrictions);
    }
}
