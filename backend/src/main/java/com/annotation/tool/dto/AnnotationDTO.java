package com.annotation.tool.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Annotation entity
 */
public class AnnotationDTO {
    
    private UUID id;
    
    @NotNull(message = "Document ID is required")
    private UUID documentId;
    private String documentName;
    
    @NotNull(message = "Label ID is required")
    private UUID labelId;
    private String labelName;
    private String labelColor;
    
    @NotNull(message = "Start position is required")
    @Min(value = 0, message = "Start position must be non-negative")
    private Integer startPosition;
    
    @NotNull(message = "End position is required")
    @Min(value = 0, message = "End position must be non-negative")
    private Integer endPosition;
    
    private String selectedText; // Automatically extracted from document content
    
    private String contextBefore;
    private String contextAfter;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public AnnotationDTO() {}
    
    public AnnotationDTO(UUID documentId, UUID labelId, Integer startPosition, 
                        Integer endPosition) {
        this.documentId = documentId;
        this.labelId = labelId;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getDocumentId() {
        return documentId;
    }
    
    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
    }
    
    public String getDocumentName() {
        return documentName;
    }
    
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
    
    public UUID getLabelId() {
        return labelId;
    }
    
    public void setLabelId(UUID labelId) {
        this.labelId = labelId;
    }
    
    public String getLabelName() {
        return labelName;
    }
    
    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
    
    public String getLabelColor() {
        return labelColor;
    }
    
    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }
    
    public Integer getStartPosition() {
        return startPosition;
    }
    
    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }
    
    public Integer getEndPosition() {
        return endPosition;
    }
    
    public void setEndPosition(Integer endPosition) {
        this.endPosition = endPosition;
    }
    
    public String getSelectedText() {
        return selectedText;
    }
    
    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }
    
    public String getContextBefore() {
        return contextBefore;
    }
    
    public void setContextBefore(String contextBefore) {
        this.contextBefore = contextBefore;
    }
    
    public String getContextAfter() {
        return contextAfter;
    }
    
    public void setContextAfter(String contextAfter) {
        this.contextAfter = contextAfter;
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
