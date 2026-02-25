package com.samsung.mes.controller;

import com.samsung.mes.dto.RecipeDTO;
import com.samsung.mes.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173)")

public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<List<RecipeDTO>> getRecipesByProduct(@RequestParam Long productId) {
        return ResponseEntity.ok(recipeService.getRecipesByProduct(productId));
    }

    @PostMapping
    public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeDTO dto) {
        return ResponseEntity.ok(recipeService.createRecipe(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeDTO> updateRecipe(@PathVariable Long id, @RequestBody RecipeDTO dto) {
        return ResponseEntity.ok(recipeService.updateRecipe(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}