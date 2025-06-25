package com.sourcegraph.petstore.config;

import com.sourcegraph.petstore.entity.CategoryEntity;
import com.sourcegraph.petstore.entity.PetEntity;
import com.sourcegraph.petstore.entity.TagEntity;
import com.sourcegraph.petstore.repository.CategoryRepository;
import com.sourcegraph.petstore.repository.PetRepository;
import com.sourcegraph.petstore.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DataLoader implements ApplicationRunner {

    private final PetRepository petRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final Random random = new Random();

    private final List<String> petNames = Arrays.asList(
            "Buddy", "Max", "Charlie", "Lucy", "Cooper", "Bella", "Luna", "Daisy",
            "Rocky", "Sadie", "Milo", "Bailey", "Jack", "Oliver", "Chloe", "Pepper",
            "Zeus", "Rex", "Duke", "Bear", "Tucker", "Murphy", "Bentley", "Gus",
            "Oscar", "Louie", "Felix", "Simba", "Jasper", "Buster", "Toby", "Finn"
    );

    private final List<String> categories = Arrays.asList(
            "Dog", "Cat", "Bird", "Fish", "Reptile", "Rodent", "Exotic"
    );

    private final List<String> tagNames = Arrays.asList(
            "Friendly", "Playful", "Trained", "Young", "Adult", "Senior",
            "Vaccinated", "Neutered", "Spayed", "Rescue", "Purebred", "Hypoallergenic",
            "Energetic", "Calm", "Social", "Independent", "Loyal", "Protective"
    );

    @Autowired
    public DataLoader(PetRepository petRepository, CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.petRepository = petRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (categoryRepository.count() == 0 && tagRepository.count() == 0 && petRepository.count() == 0) {
            loadSampleData();
        }
    }

    private void loadSampleData() {
        System.out.println("Loading sample data...");
        
        // Load categories first
        List<CategoryEntity> categoryEntities = categories.stream()
                .map(CategoryEntity::new)
                .collect(Collectors.toList());
        categoryRepository.saveAll(categoryEntities);
        System.out.println("Loaded " + categoryEntities.size() + " categories");
        
        // Load tags
        List<TagEntity> tagEntities = tagNames.stream()
                .map(TagEntity::new)
                .collect(Collectors.toList());
        tagRepository.saveAll(tagEntities);
        System.out.println("Loaded " + tagEntities.size() + " tags");
        
        // Load pets
        List<PetEntity> pets = IntStream.range(0, 50)
                .mapToObj(i -> createRandomPetEntity())
                .collect(Collectors.toList());
        
        petRepository.saveAll(pets);
        System.out.println("Loaded " + pets.size() + " pets");
        
        System.out.println("Sample data loading completed!");
    }

    private PetEntity createRandomPetEntity() {
        CategoryEntity category = getRandomExistingCategory();
        List<TagEntity> tags = getRandomExistingTags();
        
        PetEntity pet = new PetEntity();
        pet.setName(getRandomElement(petNames));
        pet.setCategory(category);
        pet.setPhotoUrls(generatePhotoUrlsForCategory(category.getName()));
        pet.setTags(tags);
        pet.setStatus(getRandomEntityStatus());
        
        return pet;
    }

    private List<String> generatePhotoUrlsForCategory(String categoryName) {
        int count = random.nextInt(3) + 1;
        return IntStream.range(0, count)
                .mapToObj(i -> "/images/" + categoryName.toLowerCase() + "_" + (i + 1) + ".jpg")
                .collect(Collectors.toList());
    }

    private CategoryEntity getRandomExistingCategory() {
        List<CategoryEntity> allCategories = categoryRepository.findAll();
        return getRandomElement(allCategories);
    }

    private List<TagEntity> getRandomExistingTags() {
        List<TagEntity> allTags = tagRepository.findAll();
        int count = random.nextInt(4) + 1;
        return IntStream.range(0, count)
                .mapToObj(i -> getRandomElement(allTags))
                .distinct()
                .collect(Collectors.toList());
    }

    private PetEntity.PetStatus getRandomEntityStatus() {
        PetEntity.PetStatus[] statuses = PetEntity.PetStatus.values();
        return statuses[random.nextInt(statuses.length)];
    }

    private <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}
