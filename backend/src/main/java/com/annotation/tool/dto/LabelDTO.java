package com.annotation.tool.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object for Label entity
 * Used for API communication and validation
 */
public class LabelDTO {
    
    private UUID id;
    
    @NotBlank(message = "Label name is required")
    @Size(max = 255, message = "Label name must not exceed 255 characters")
    private String name;
    
    @NotBlank(message = "Color is required")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid hex code (e.g., #FF0000)")
    private String color;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    private UUID parentId;
    private String parentName;
    
    private List<LabelDTO> children;
    private List<LabelRelationshipDTO> outgoingRelationships;
    private List<LabelRelationshipDTO> incomingRelationships;
    
    private Long annotationCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public LabelDTO() {}
    
    public LabelDTO(String name, String color, String description) {
        this.name = name;
        this.color = color;
        this.description = description;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public UUID getParentId() {
        return parentId;
    }
    
    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
    
    public String getParentName() {
        return parentName;
    }
    
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    
    public List<LabelDTO> getChildren() {
        return children;
    }
    
    public void setChildren(List<LabelDTO> children) {
        this.children = children;
    }
    
    public List<LabelRelationshipDTO> getOutgoingRelationships() {
        return outgoingRelationships;
    }
    
    public void setOutgoingRelationships(List<LabelRelationshipDTO> outgoingRelationships) {
        this.outgoingRelationships = outgoingRelationships;
    }
    
    public List<LabelRelationshipDTO> getIncomingRelationships() {
        return incomingRelationships;
    }
    
    public void setIncomingRelationships(List<LabelRelationshipDTO> incomingRelationships) {
        this.incomingRelationships = incomingRelationships;
    }
    
    public Long getAnnotationCount() {
        return annotationCount;
    }
    
    public void setAnnotationCount(Long annotationCount) {
        this.annotationCount = annotationCount;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
