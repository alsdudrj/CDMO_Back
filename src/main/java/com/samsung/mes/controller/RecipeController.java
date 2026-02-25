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

    //(25.02.26 민영 수정) productId가 있으면 단건조회, 없으면 전체 조회로 변경
    @GetMapping
    public ResponseEntity<List<RecipeDTO>> getRecipesByProduct(@RequestParam(required = false) Long productId) {
        if (productId != null) {
            return ResponseEntity.ok(recipeService.getRecipesByProduct(productId));
        } else{
            return ResponseEntity.ok(recipeService.getAllRecipes());
        }
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