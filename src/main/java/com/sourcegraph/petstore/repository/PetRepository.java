package com.sourcegraph.petstore.repository;

import com.sourcegraph.petstore.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Long> {

    List<PetEntity> findByStatus(PetEntity.PetStatus status);

    @Query("SELECT p FROM PetEntity p WHERE p.category.name = :categoryName")
    List<PetEntity> findByCategoryName(String categoryName);

    @Query(value = "SELECT * FROM pets ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<PetEntity> findRandomPets(int count);

    @Query(value = "SELECT * FROM pets ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    PetEntity findRandomPet();
}
