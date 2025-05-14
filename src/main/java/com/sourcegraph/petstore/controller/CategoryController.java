package com.sourcegraph.petstore.controller;

import com.sourcegraph.petstore.openapi.generated.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final Map<Long, Category> categoryStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final Random random = new Random();

    private final List<String> petCategories = Arrays.asList(
            "Dog", "Cat", "Bird", "Fish", "Reptile", "Amphibian", "Small Mammal",
            "Exotic", "Farm Animal", "Insect", "Arachnid"
    );

    @GetMapping
    public List<Category> getAllCategories() {
        return new ArrayList<>(categoryStore.values());
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryStore.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody Category category) {
        Long id = idCounter.getAndIncrement();
        category.setId(id);
        categoryStore.put(id, category);
        return category;
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        categoryStore.put(id, category);
        return category;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryStore.remove(id);
    }

    @GetMapping("/random")
    public Category getRandomCategory() {
        return generateRandomCategory();
    }

    @GetMapping("/random/{count}")
    public List<Category> getRandomCategories(@PathVariable int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> generateRandomCategory())
                .collect(Collectors.toList());
    }

    private Category generateRandomCategory() {
        return new Category()
                .id(random.nextLong(100))
                .name(petCategories.get(random.nextInt(petCategories.size())));
    }
}
