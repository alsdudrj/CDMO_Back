package com.samsung.mes.service;

import com.samsung.mes.dto.ProcessDTO;
import com.samsung.mes.dto.RecipeDTO;
import com.samsung.mes.entity.Process;
import com.samsung.mes.entity.Product;
import com.samsung.mes.entity.Recipe;
import com.samsung.mes.entity.Simulation;
import com.samsung.mes.repository.ProductRepository;
import com.samsung.mes.repository.RecipeRepository;
import com.samsung.mes.repository.SimulationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final ProductRepository productRepository;

    //26.02.26 민영 추가
    private final SimulationRepository simulationRepository;

    public List<RecipeDTO> getRecipesByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return recipeRepository.findByProduct(product).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RecipeDTO createRecipe(RecipeDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (Boolean.TRUE.equals(dto.getIsActive())) {
            deactivateOtherRecipes(product);
        }

        //레시피 생성
        Recipe recipe = new Recipe();
        updateEntityFromDto(recipe, dto, product);

        //(26.02.25 민영 추가) 레시피 생성에 따른 Simulation 생성
        Recipe savedRecipe = recipeRepository.save(recipe);

        Simulation sim = new Simulation();
        sim.setRecipe(savedRecipe);          // 레시피 연결
        sim.setProduct(product);             // 상품 연결
        sim.setName(savedRecipe.getName() + " 시뮬레이션");
        sim.setStepOrder(1);                 // 처음 단계
        sim.setDescription(savedRecipe.getDescription());
        sim.setTemp(0.0f);
        sim.setPh(0.0f);
        sim.setDoValue(0.0f);
        sim.setProgressRate(0.0f);
        sim.setTimeStamp(java.time.LocalDateTime.now());
        sim.setStatus("RUNNING");

        simulationRepository.save(sim);

        return toDto(savedRecipe);
    }

    @Transactional
    public RecipeDTO updateRecipe(Long id, RecipeDTO dto) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        Product product = recipe.getProduct(); // Product shouldn't change usually, but if it does, logic gets complex. Assuming product doesn't change.

        if (Boolean.TRUE.equals(dto.getIsActive()) && !Boolean.TRUE.equals(recipe.getIsActive())) {
            deactivateOtherRecipes(product);
        }

        updateEntityFromDto(recipe, dto, product);

        Recipe savedRecipe = recipeRepository.save(recipe);
        return toDto(savedRecipe);
    }

    @Transactional
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    private void deactivateOtherRecipes(Product product) {
        Optional<Recipe> activeRecipe = recipeRepository.findByProductAndIsActiveTrue(product);
        activeRecipe.ifPresent(r -> {
            r.setIsActive(false);
            recipeRepository.save(r);
        });
    }

    private void updateEntityFromDto(Recipe recipe, RecipeDTO dto, Product product) {
        recipe.setName(dto.getName());
        recipe.setDescription(dto.getDescription());
        recipe.setTargetQuantity(dto.getTargetQuantity());
        recipe.setUnit(dto.getUnit());
        recipe.setVersion(dto.getVersion());
        recipe.setStatus(dto.getStatus());
        recipe.setIsActive(dto.getIsActive());
        recipe.setProduct(product);

        // Handle processes
        // Simplification: Clear and re-add.
        // In a real app, we might want to update existing entities to preserve IDs if needed,
        // but since Process is weak entity here, replacement is acceptable for this task.
        recipe.getProcesses().clear();
        if (dto.getProcesses() != null) {
            for (ProcessDTO pDto : dto.getProcesses()) {
                Process process = new Process();
                process.setName(pDto.getName());
                process.setStepOrder(pDto.getStepOrder());
                process.setDescription(pDto.getDescription());
                process.setTemp(pDto.getTemp());
                process.setPh(pDto.getPh());
                process.setTime(pDto.getTime());
                process.setRecipe(recipe);
                recipe.getProcesses().add(process);
            }
        }
    }

    private RecipeDTO toDto(Recipe recipe) {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setDescription(recipe.getDescription());
        dto.setTargetQuantity(recipe.getTargetQuantity());
        dto.setUnit(recipe.getUnit());
        dto.setVersion(recipe.getVersion());
        dto.setStatus(recipe.getStatus());
        dto.setIsActive(recipe.getIsActive());
        if (recipe.getProduct() != null) {
            dto.setProductId(recipe.getProduct().getId());
        }

        //(26.02.25 민영추가) null 체크 추가
        if (recipe.getProduct() != null) {
            dto.setProductId(recipe.getProduct().getId());
        } else {
            dto.setProductId(null);
        }

        //(26.02.25 민영추가) processes가 null일 경우 빈 리스트로 초기화
        if (recipe.getProcesses() != null) {
            List<ProcessDTO> processDTOS = recipe.getProcesses().stream()
                    .map(p -> {
                        ProcessDTO pDto = new ProcessDTO();
                        pDto.setId(p.getId());
                        pDto.setName(p.getName());
                        pDto.setStepOrder(p.getStepOrder());
                        pDto.setDescription(p.getDescription());
                        pDto.setTemp(p.getTemp());
                        pDto.setPh(p.getPh());
                        pDto.setTime(p.getTime());
                        return pDto;
                    })
                    .collect(Collectors.toList());
            dto.setProcesses(processDTOS);
        } else {
            dto.setProcesses(new ArrayList<>());
        }

        return dto;
    }

    //(26.02.25 민영추가) 레시피 전체 조회
    public List<RecipeDTO> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}