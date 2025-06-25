package com.sourcegraph.petstore.repository;

import com.sourcegraph.petstore.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    Optional<TagEntity> findByName(String name);

    @Query(value = "SELECT * FROM tags ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    TagEntity findRandomTag();

    @Query(value = "SELECT * FROM tags ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<TagEntity> findRandomTags(int count);

    List<TagEntity> findByNameIn(List<String> names);
}
