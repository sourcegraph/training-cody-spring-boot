package com.sourcegraph.petstore.service;

import com.sourcegraph.petstore.entity.PetEntity;
import com.sourcegraph.petstore.openapi.generated.model.Category;
import com.sourcegraph.petstore.openapi.generated.model.Pet;
import com.sourcegraph.petstore.openapi.generated.model.Tag;
import com.sourcegraph.petstore.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet generateRandomPet() {
        PetEntity randomPet = petRepository.findRandomPet();
        return randomPet != null ? convertToApiModel(randomPet) : null;
    }

    public List<Pet> generateRandomPets(int count) {
        List<PetEntity> randomPets = petRepository.findRandomPets(count);
        return randomPets.stream()
                .map(this::convertToApiModel)
                .collect(Collectors.toList());
    }

    private Pet convertToApiModel(PetEntity entity) {
        Pet pet = new Pet()
                .id(entity.getId())
                .name(entity.getName())
                .photoUrls(entity.getPhotoUrls());

        if (entity.getCategory() != null) {
            Category category = new Category()
                    .id(entity.getCategory().getId())
                    .name(entity.getCategory().getName());
            pet.setCategory(category);
        }

        if (entity.getTags() != null) {
            List<Tag> tags = entity.getTags().stream()
                    .map(tagEntity -> new Tag()
                            .id(tagEntity.getId())
                            .name(tagEntity.getName()))
                    .collect(Collectors.toList());
            pet.setTags(tags);
        }

        if (entity.getStatus() != null) {
            pet.setStatus(convertToApiStatus(entity.getStatus()));
        }

        return pet;
    }

    private Pet.StatusEnum convertToApiStatus(PetEntity.PetStatus entityStatus) {
        return switch (entityStatus) {
            case PENDING -> Pet.StatusEnum.PENDING;
            case SOLD -> Pet.StatusEnum.SOLD;
            default -> Pet.StatusEnum.AVAILABLE;
        };
    }
}