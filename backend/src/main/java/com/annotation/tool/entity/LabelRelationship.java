package com.annotation.tool.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class representing relationships between labels (връзки между етикети)
 * 
 * Relationships are unidirectional, meaning a relationship from Label A to Label B
 * does not imply a relationship from Label B to Label A
 */
@Entity
@Table(name = "label_relationships", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"source_label_id", "target_label_id"}))
public class LabelRelationship {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_label_id", nullable = false)
    private Label sourceLabel;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_label_id", nullable = false)
    private Label targetLabel;
    
    @Column(name = "description")
    private String description;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public LabelRelationship() {}
    
    public LabelRelationship(Label sourceLabel, Label targetLabel, String description) {
        this.sourceLabel = sourceLabel;
        this.targetLabel = targetLabel;
        this.description = description;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public Label getSourceLabel() {
        return sourceLabel;
    }
    
    public void setSourceLabel(Label sourceLabel) {
        this.sourceLabel = sourceLabel;
    }
    
    public Label getTargetLabel() {
        return targetLabel;
    }
    
    public void setTargetLabel(Label targetLabel) {
        this.targetLabel = targetLabel;
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
