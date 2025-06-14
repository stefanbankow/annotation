package com.annotation.tool.controller;

import com.annotation.tool.dto.LabelDTO;
import com.annotation.tool.service.LabelService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Label management
 * 
 * Handles HTTP requests for label operations including CRUD operations,
 * hierarchical management, and analytics
 */
@RestController
@RequestMapping("/api/labels")
public class LabelController {
    
    private static final Logger logger = LoggerFactory.getLogger(LabelController.class);
    
    @Autowired
    private LabelService labelService;
    
    /**
     * Get all labels
     */
    @GetMapping
    public ResponseEntity<List<LabelDTO>> getAllLabels() {
        logger.info("GET /api/labels - Retrieving all labels");
        try {
            List<LabelDTO> labels = labelService.getAllLabels();
            logger.info("Successfully retrieved {} labels", labels.size());
            return ResponseEntity.ok(labels);
        } catch (Exception e) {
            logger.error("Error retrieving all labels", e);
            throw e;
        }
    }
    
    /**
     * Get label by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<LabelDTO> getLabelById(@PathVariable UUID id) {
        logger.info("GET /api/labels/{} - Retrieving label by ID", id);
        
        return labelService.getLabelById(id)
                .map(label -> {
                    logger.info("Successfully found label: {}", label.getName());
                    return ResponseEntity.ok(label);
                })
                .orElseGet(() -> {
                    logger.warn("Label not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }
    
    /**
     * Get root labels (labels without parent)
     */
    @GetMapping("/roots")
    public ResponseEntity<List<LabelDTO>> getRootLabels() {
        logger.info("GET /api/labels/roots - Retrieving root labels");
        
        List<LabelDTO> rootLabels = labelService.getRootLabels();
        logger.info("Successfully retrieved {} root labels", rootLabels.size());
        return ResponseEntity.ok(rootLabels);
    }
    
    /**
     * Get child labels of a specific parent
     */
    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<LabelDTO>> getChildLabels(@PathVariable UUID parentId) {
        logger.info("GET /api/labels/{}/children - Retrieving child labels", parentId);
        
        List<LabelDTO> childLabels = labelService.getChildLabels(parentId);
        logger.info("Successfully retrieved {} child labels for parent {}", childLabels.size(), parentId);
        return ResponseEntity.ok(childLabels);
    }
    
    /**
     * Create a new label
     */
    @PostMapping
    public ResponseEntity<LabelDTO> createLabel(@Valid @RequestBody LabelDTO labelDTO) {
        logger.info("POST /api/labels - Creating new label: {}", labelDTO.getName());
        logger.debug("Full label data: {}", labelDTO);
        
        try {
            LabelDTO createdLabel = labelService.createLabel(labelDTO);
            logger.info("Successfully created label with ID: {}", createdLabel.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLabel);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to create label due to invalid argument: {}", e.getMessage());
            throw e; // Let GlobalExceptionHandler handle it
        } catch (Exception e) {
            logger.error("Unexpected error creating label: {}", e.getMessage(), e);
            throw e; // Let GlobalExceptionHandler handle it
        }
    }
    
    /**
     * Update an existing label
     */
    @PutMapping("/{id}")
    public ResponseEntity<LabelDTO> updateLabel(@PathVariable UUID id, 
                                               @Valid @RequestBody LabelDTO labelDTO) {
        logger.info("PUT /api/labels/{} - Updating label: {}", id, labelDTO.getName());
        
        try {
            LabelDTO updatedLabel = labelService.updateLabel(id, labelDTO);
            logger.info("Successfully updated label with ID: {}", id);
            return ResponseEntity.ok(updatedLabel);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to update label {}: {}", id, e.getMessage());
            throw e; // Let GlobalExceptionHandler handle it
        }
    }
    
    /**
     * Delete a label
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable UUID id) {
        logger.info("DELETE /api/labels/{} - Deleting label", id);
        
        try {
            labelService.deleteLabel(id);
            logger.info("Successfully deleted label with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to delete label {}: {}", id, e.getMessage());
            throw e; // Let GlobalExceptionHandler handle it
        }
    }
    
    /**
     * Search labels by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<LabelDTO>> searchLabels(@RequestParam String query) {
        List<LabelDTO> labels = labelService.searchLabelsByName(query);
        return ResponseEntity.ok(labels);
    }
    
    /**
     * Get unused labels
     */
    @GetMapping("/unused")
    public ResponseEntity<List<LabelDTO>> getUnusedLabels() {
        List<LabelDTO> unusedLabels = labelService.getUnusedLabels();
        return ResponseEntity.ok(unusedLabels);
    }
    
    /**
     * Get label usage statistics
     */
    @GetMapping("/statistics/usage")
    public ResponseEntity<List<Object[]>> getLabelUsageStatistics() {
        List<Object[]> statistics = labelService.getLabelUsageStatistics();
        return ResponseEntity.ok(statistics);
    }
}
