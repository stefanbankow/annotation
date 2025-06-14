package com.annotation.tool.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class representing an annotation on a document
 * 
 * An annotation marks a specific text segment in a document with a label
 * It includes position information and context for precise text location
 */
@Entity
@Table(name = "annotations")
public class Annotation {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id", nullable = false)
    private Label label;
    
    @Column(name = "start_position", nullable = false)
    private Integer startPosition;
    
    @Column(name = "end_position", nullable = false)
    private Integer endPosition;
    
    @Column(name = "selected_text", nullable = false, columnDefinition = "TEXT")
    private String selectedText;
    
    @Column(name = "context_before", columnDefinition = "TEXT")
    private String contextBefore;
    
    @Column(name = "context_after", columnDefinition = "TEXT")
    private String contextAfter;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Annotation() {}
    
    public Annotation(Document document, Label label, Integer startPosition, 
                     Integer endPosition, String selectedText, 
                     String contextBefore, String contextAfter) {
        this.document = document;
        this.label = label;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.selectedText = selectedText;
        this.contextBefore = contextBefore;
        this.contextAfter = contextAfter;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public Document getDocument() {
        return document;
    }
    
    public void setDocument(Document document) {
        this.document = document;
    }
    
    public Label getLabel() {
        return label;
    }
    
    public void setLabel(Label label) {
        this.label = label;
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
