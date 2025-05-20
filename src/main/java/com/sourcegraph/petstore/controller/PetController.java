package com.sourcegraph.petstore.controller;

import com.sourcegraph.petstore.openapi.generated.model.Category;
import com.sourcegraph.petstore.openapi.generated.model.Pet;
import com.sourcegraph.petstore.openapi.generated.model.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/pets")
public class PetController {

        private final Random random = new Random();

        private final List<String> petNames = Arrays.asList(
                "Buddy", "Max", "Charlie", "Lucy", "Cooper", "Bella", "Luna", "Daisy",
                "Rocky", "Sadie", "Milo", "Bailey", "Jack", "Oliver", "Chloe", "Pepper"
        );

        private final List<String> categories = Arrays.asList(
                "Dog", "Cat", "Bird", "Fish", "Reptile", "Rodent", "Exotic"
        );

        private final List<String> tagNames = Arrays.asList(
                "Friendly", "Playful", "Trained", "Young", "Adult", "Senior",
                "Vaccinated", "Neutered", "Spayed", "Rescue", "Purebred", "Hypoallergenic"
        );

        @GetMapping("/random")
        public Pet getRandomPet() {
            return generateRandomPet();
        }

        @GetMapping("/random/{count}")
        public List<Pet> getRandomPets(@PathVariable int count) {
            return IntStream.range(0, count)
                    .mapToObj(i -> generateRandomPet())
                    .collect(Collectors.toList());
        }

        @GetMapping("/{id}")
        public Pet getPetById(@PathVariable Long id) {
            // Generate a random pet but set the specified ID
            Pet pet = generateRandomPet();
            // Override the random ID with the one from the path variable
            pet.setId(id);
            return pet;
        }
        private Pet generateRandomPet() {
            // First generate the category so we can use it for photo URLs
            Category category = generateRandomCategory();

            // Create pet with required fields
            Pet pet = new Pet()
                    .id(random.nextLong(10000))
                    .name(getRandomElement(petNames))
                    .photoUrls(generatePhotoUrlsForCategory(category.getName()));

            // Add optional fields
            pet.setCategory(category);
            pet.setTags(generateRandomTags());
            pet.setStatus(getRandomStatus());

            return pet;
        }

        private List<String> generatePhotoUrlsForCategory(String categoryName) {
            int count = random.nextInt(3) + 1; // 1-3 photos
            return IntStream.range(0, count)
                    .mapToObj(i -> "/images/" + categoryName + ".jpg")
                    .collect(Collectors.toList());
        }

        private Category generateRandomCategory() {
            return new Category()
                    .id(random.nextLong(100))
                    .name(getRandomElement(categories));
        }

        private List<String> generateRandomPhotoUrls() {
            int count = random.nextInt(3) + 1; // 1-3 photos
            return IntStream.range(0, count)
                    .mapToObj(i -> "https://example.com/pet-photos/" + UUID.randomUUID() + ".jpg")
                    .collect(Collectors.toList());
        }

        private List<Tag> generateRandomTags() {
            int count = random.nextInt(4); // 0-3 tags
            return IntStream.range(0, count)
                    .mapToObj(i -> new Tag()
                            .id(random.nextLong(100))
                            .name(getRandomElement(tagNames)))
                    .collect(Collectors.toList());
        }

        private Pet.StatusEnum getRandomStatus() {
            Pet.StatusEnum[] statuses = Pet.StatusEnum.values();
            return statuses[random.nextInt(statuses.length)];
        }

        private <T> T getRandomElement(List<T> list) {
            return list.get(random.nextInt(list.size()));
        }
}
