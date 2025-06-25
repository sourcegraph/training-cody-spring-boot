package com.sourcegraph.petstore.service;

import com.sourcegraph.petstore.entity.CategoryEntity;
import com.sourcegraph.petstore.openapi.generated.model.Category;
import com.sourcegraph.petstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToApiModel)
                .collect(Collectors.toList());
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToApiModel)
                .orElse(null);
    }

    public Category createCategory(Category category) {
        CategoryEntity entity = new CategoryEntity(category.getName());
        CategoryEntity saved = categoryRepository.save(entity);
        return convertToApiModel(saved);
    }

    public Category updateCategory(Long id, Category category) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElse(new CategoryEntity());
        entity.setId(id);
        entity.setName(category.getName());
        CategoryEntity saved = categoryRepository.save(entity);
        return convertToApiModel(saved);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category getRandomCategory() {
        CategoryEntity randomCategory = categoryRepository.findRandomCategory();
        return randomCategory != null ? convertToApiModel(randomCategory) : null;
    }

    public List<Category> getRandomCategories(int count) {
        List<CategoryEntity> randomCategories = categoryRepository.findRandomCategories(count);
        return randomCategories.stream()
                .map(this::convertToApiModel)
                .collect(Collectors.toList());
    }

    private Category convertToApiModel(CategoryEntity entity) {
        return new Category()
                .id(entity.getId())
                .name(entity.getName());
    }
}
