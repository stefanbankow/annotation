package com.annotation.tool.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity class representing a label (етикет) used for document annotation
 * 
 * Labels can be organized in hierarchical structure with parent-child relationships
 * Each label has a unique color and can be connected to other labels via relationships
 */
@Entity
@Table(name = "labels")
public class Label {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @Column(name = "color", nullable = false, length = 7)
    private String color; // HEX color code
    
    @Column(name = "description")
    private String description;
    
    // Self-referencing relationship for hierarchical structure
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Label parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Label> children = new ArrayList<>();
    
    // Relationships where this label is the source
    @OneToMany(mappedBy = "sourceLabel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LabelRelationship> outgoingRelationships = new ArrayList<>();
    
    // Relationships where this label is the target
    @OneToMany(mappedBy = "targetLabel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LabelRelationship> incomingRelationships = new ArrayList<>();
    
    @OneToMany(mappedBy = "label", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Annotation> annotations = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Label() {}
    
    public Label(String name, String color, String description) {
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
    
    public Label getParent() {
        return parent;
    }
    
    public void setParent(Label parent) {
        this.parent = parent;
    }
    
    public List<Label> getChildren() {
        return children;
    }
    
    public void setChildren(List<Label> children) {
        this.children = children;
    }
    
    public List<LabelRelationship> getOutgoingRelationships() {
        return outgoingRelationships;
    }
    
    public void setOutgoingRelationships(List<LabelRelationship> outgoingRelationships) {
        this.outgoingRelationships = outgoingRelationships;
    }
    
    public List<LabelRelationship> getIncomingRelationships() {
        return incomingRelationships;
    }
    
    public void setIncomingRelationships(List<LabelRelationship> incomingRelationships) {
        this.incomingRelationships = incomingRelationships;
    }
    
    public List<Annotation> getAnnotations() {
        return annotations;
    }
    
    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
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
