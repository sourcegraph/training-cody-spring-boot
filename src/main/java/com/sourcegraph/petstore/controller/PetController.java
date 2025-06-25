package com.sourcegraph.petstore.controller;

import com.sourcegraph.petstore.openapi.generated.model.Pet;
import com.sourcegraph.petstore.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

        private final PetService petService;

        @Autowired
        public PetController(PetService petService) {
            this.petService = petService;
        }

        @GetMapping("/random")
        public Pet getRandomPet() {
            return petService.generateRandomPet();
        }

        @GetMapping("/random/{count}")
        public List<Pet> getRandomPets(@PathVariable int count) {
            return petService.generateRandomPets(count);
        }
}
