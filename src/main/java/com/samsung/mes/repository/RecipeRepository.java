package com.samsung.mes.repository;

import com.samsung.mes.entity.Product;
import com.samsung.mes.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByProduct(Product product);
    Optional<Recipe> findByProductAndIsActiveTrue(Product product);
}