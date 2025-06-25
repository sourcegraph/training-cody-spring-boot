package com.sourcegraph.petstore.repository;

import com.sourcegraph.petstore.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByName(String name);

    @Query(value = "SELECT * FROM categories ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    CategoryEntity findRandomCategory();

    @Query(value = "SELECT * FROM categories ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<CategoryEntity> findRandomCategories(int count);
}
