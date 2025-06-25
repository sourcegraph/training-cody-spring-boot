package com.sourcegraph.petstore.service;

import com.sourcegraph.petstore.entity.TagEntity;
import com.sourcegraph.petstore.openapi.generated.model.Tag;
import com.sourcegraph.petstore.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::convertToApiModel)
                .collect(Collectors.toList());
    }

    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .map(this::convertToApiModel)
                .orElse(null);
    }

    public Tag createTag(Tag tag) {
        TagEntity entity = new TagEntity(tag.getName());
        TagEntity saved = tagRepository.save(entity);
        return convertToApiModel(saved);
    }

    public Tag updateTag(Long id, Tag tag) {
        TagEntity entity = tagRepository.findById(id)
                .orElse(new TagEntity());
        entity.setId(id);
        entity.setName(tag.getName());
        TagEntity saved = tagRepository.save(entity);
        return convertToApiModel(saved);
    }

    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    public Tag getRandomTag() {
        TagEntity randomTag = tagRepository.findRandomTag();
        return randomTag != null ? convertToApiModel(randomTag) : null;
    }

    public List<Tag> getRandomTags(int count) {
        List<TagEntity> randomTags = tagRepository.findRandomTags(count);
        return randomTags.stream()
                .map(this::convertToApiModel)
                .collect(Collectors.toList());
    }

    private Tag convertToApiModel(TagEntity entity) {
        return new Tag()
                .id(entity.getId())
                .name(entity.getName());
    }
}
