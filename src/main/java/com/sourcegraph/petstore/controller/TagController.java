package com.sourcegraph.petstore.controller;

import com.sourcegraph.petstore.openapi.generated.model.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final Map<Long, Tag> tagStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final Random random = new Random();

    private final List<String> commonTags = Arrays.asList(
            "Friendly", "Active", "Playful", "Shy", "Trained", "Vaccinated",
            "Neutered", "Spayed", "Senior", "Puppy", "Kitten", "Rare",
            "Exotic", "Hypoallergenic", "Special Needs", "Rescue"
    );

    @GetMapping
    public List<Tag> getAllTags() {
        return new ArrayList<>(tagStore.values());
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Long id) {
        return tagStore.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tag createTag(@RequestBody Tag tag) {
        Long id = idCounter.getAndIncrement();
        tag.setId(id);
        tagStore.put(id, tag);
        return tag;
    }

    @PutMapping("/{id}")
    public Tag updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        tag.setId(id);
        tagStore.put(id, tag);
        return tag;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable Long id) {
        tagStore.remove(id);
    }

    @GetMapping("/random")
    public Tag getRandomTag() {
        return generateRandomTag();
    }

    @GetMapping("/random/{count}")
    public List<Tag> getRandomTags(@PathVariable int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> generateRandomTag())
                .collect(Collectors.toList());
    }

    private Tag generateRandomTag() {
        return new Tag()
                .id(random.nextLong(1000))
                .name(commonTags.get(random.nextInt(commonTags.size())));
    }
}
