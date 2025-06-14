package com.annotation.tool.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for LabelRelationship entity
 */
public class LabelRelationshipDTO {
    
    private UUID id;
    
    @NotNull(message = "Source label ID is required")
    private UUID sourceLabelId;
    private String sourceLabelName;
    private String sourceLabelColor;
    
    @NotNull(message = "Target label ID is required")
    private UUID targetLabelId;
    private String targetLabelName;
    private String targetLabelColor;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    private LocalDateTime createdAt;
    
    // Constructors
    public LabelRelationshipDTO() {}
    
    public LabelRelationshipDTO(UUID sourceLabelId, UUID targetLabelId, String description) {
        this.sourceLabelId = sourceLabelId;
        this.targetLabelId = targetLabelId;
        this.description = description;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getSourceLabelId() {
        return sourceLabelId;
    }
    
    public void setSourceLabelId(UUID sourceLabelId) {
        this.sourceLabelId = sourceLabelId;
    }
    
    public String getSourceLabelName() {
        return sourceLabelName;
    }
    
    public void setSourceLabelName(String sourceLabelName) {
        this.sourceLabelName = sourceLabelName;
    }
    
    public String getSourceLabelColor() {
        return sourceLabelColor;
    }
    
    public void setSourceLabelColor(String sourceLabelColor) {
        this.sourceLabelColor = sourceLabelColor;
    }
    
    public UUID getTargetLabelId() {
        return targetLabelId;
    }
    
    public void setTargetLabelId(UUID targetLabelId) {
        this.targetLabelId = targetLabelId;
    }
    
    public String getTargetLabelName() {
        return targetLabelName;
    }
    
    public void setTargetLabelName(String targetLabelName) {
        this.targetLabelName = targetLabelName;
    }
    
    public String getTargetLabelColor() {
        return targetLabelColor;
    }
    
    public void setTargetLabelColor(String targetLabelColor) {
        this.targetLabelColor = targetLabelColor;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
