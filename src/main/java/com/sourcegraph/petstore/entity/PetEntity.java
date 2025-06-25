package com.sourcegraph.petstore.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "pets")
public class PetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ElementCollection
    @CollectionTable(name = "pet_photo_urls", joinColumns = @JoinColumn(name = "pet_id"))
    @Column(name = "photo_url")
    private List<String> photoUrls;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "pet_tags",
        joinColumns = @JoinColumn(name = "pet_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<TagEntity> tags;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PetStatus status;

    public enum PetStatus {
        AVAILABLE, PENDING, SOLD
    }

    public PetEntity() {}

    public PetEntity(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public List<TagEntity> getTags() {
        return tags;
    }

    public void setTags(List<TagEntity> tags) {
        this.tags = tags;
    }

    public PetStatus getStatus() {
        return status;
    }

    public void setStatus(PetStatus status) {
        this.status = status;
    }
}
