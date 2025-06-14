package com.annotation.tool.controller;

import com.annotation.tool.dto.LabelRelationshipDTO;
import com.annotation.tool.service.LabelRelationshipService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * REST Controller for Label Relationship management
 * 
 * Handles HTTP requests for label relationship operations including creation,
 * updating, and analytics for relationships between labels
 */
@RestController
@RequestMapping("/api/label-relationships")
public class LabelRelationshipController {

    @Autowired
    private LabelRelationshipService relationshipService;

    /**
     * Get all label relationships
     */
    @GetMapping
    public ResponseEntity<List<LabelRelationshipDTO>> getAllRelationships() {
        List<LabelRelationshipDTO> relationships = relationshipService.getAllRelationships();
        return ResponseEntity.ok(relationships);
    }

    /**
     * Get relationship by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<LabelRelationshipDTO> getRelationshipById(@PathVariable UUID id) {
        return relationshipService.getRelationshipById(id)
                .map(relationship -> ResponseEntity.ok(relationship))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all relationships for a specific label
     */
    @GetMapping("/label/{labelId}")
    public ResponseEntity<List<LabelRelationshipDTO>> getRelationshipsForLabel(@PathVariable UUID labelId) {
        List<LabelRelationshipDTO> relationships = relationshipService.getRelationshipsForLabel(labelId);
        return ResponseEntity.ok(relationships);
    }

    /**
     * Get outgoing relationships (where label is source)
     */
    @GetMapping("/source/{sourceLabelId}")
    public ResponseEntity<List<LabelRelationshipDTO>> getOutgoingRelationships(@PathVariable UUID sourceLabelId) {
        List<LabelRelationshipDTO> relationships = relationshipService.getOutgoingRelationships(sourceLabelId);
        return ResponseEntity.ok(relationships);
    }

    /**
     * Get incoming relationships (where label is target)
     */
    @GetMapping("/target/{targetLabelId}")
    public ResponseEntity<List<LabelRelationshipDTO>> getIncomingRelationships(@PathVariable UUID targetLabelId) {
        List<LabelRelationshipDTO> relationships = relationshipService.getIncomingRelationships(targetLabelId);
        return ResponseEntity.ok(relationships);
    }

    /**
     * Create a new label relationship
     */
    @PostMapping
    public ResponseEntity<?> createRelationship(@Valid @RequestBody LabelRelationshipDTO relationshipDTO) {
        try {
            LabelRelationshipDTO createdRelationship = relationshipService.createRelationship(relationshipDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRelationship);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update an existing label relationship
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRelationship(@PathVariable UUID id,
            @Valid @RequestBody LabelRelationshipDTO relationshipDTO) {
        try {
            LabelRelationshipDTO updatedRelationship = relationshipService.updateRelationship(id, relationshipDTO);
            return ResponseEntity.ok(updatedRelationship);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Delete a label relationship
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRelationship(@PathVariable UUID id) {
        try {
            relationshipService.deleteRelationship(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get total relationship count
     */
    @GetMapping("/statistics/total-count")
    public ResponseEntity<Long> getTotalRelationshipCount() {
        Long count = relationshipService.getTotalRelationshipCount();
        return ResponseEntity.ok(count);
    }

    /**
     * Get labels with most outgoing relationships
     */
    @GetMapping("/statistics/most-outgoing")
    public ResponseEntity<List<Object[]>> getLabelsWithMostOutgoingRelationships() {
        List<Object[]> statistics = relationshipService.getLabelsWithMostOutgoingRelationships();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get labels with most incoming relationships
     */
    @GetMapping("/statistics/most-incoming")
    public ResponseEntity<List<Object[]>> getLabelsWithMostIncomingRelationships() {
        List<Object[]> statistics = relationshipService.getLabelsWithMostIncomingRelationships();
        return ResponseEntity.ok(statistics);
    }
}
