package com.annotation.tool.controller;

import com.annotation.tool.dto.AnnotationDTO;
import com.annotation.tool.service.AnnotationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * REST Controller for Annotation management
 * 
 * Handles HTTP requests for annotation operations including creation,
 * updating, and analytics
 */
@RestController
@RequestMapping("/api/annotations")
public class AnnotationController {

    @Autowired
    private AnnotationService annotationService;

    /**
     * Get all annotations
     */
    @GetMapping
    public ResponseEntity<List<AnnotationDTO>> getAllAnnotations() {
        List<AnnotationDTO> annotations = annotationService.getAllAnnotations();
        return ResponseEntity.ok(annotations);
    }

    /**
     * Get annotation by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnnotationDTO> getAnnotationById(@PathVariable UUID id) {
        return annotationService.getAnnotationById(id)
                .map(annotation -> ResponseEntity.ok(annotation))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get annotations for a specific document
     */
    @GetMapping("/document/{documentId}")
    public ResponseEntity<List<AnnotationDTO>> getAnnotationsByDocumentId(@PathVariable UUID documentId) {
        List<AnnotationDTO> annotations = annotationService.getAnnotationsByDocumentId(documentId);
        return ResponseEntity.ok(annotations);
    }

    /**
     * Get annotations with a specific label
     */
    @GetMapping("/label/{labelId}")
    public ResponseEntity<List<AnnotationDTO>> getAnnotationsByLabelId(@PathVariable UUID labelId) {
        List<AnnotationDTO> annotations = annotationService.getAnnotationsByLabelId(labelId);
        return ResponseEntity.ok(annotations);
    }

    /**
     * Get annotations by document and label
     */
    @GetMapping("/document/{documentId}/label/{labelId}")
    public ResponseEntity<List<AnnotationDTO>> getAnnotationsByDocumentAndLabel(
            @PathVariable UUID documentId,
            @PathVariable UUID labelId) {
        List<AnnotationDTO> annotations = annotationService.getAnnotationsByDocumentAndLabel(documentId, labelId);
        return ResponseEntity.ok(annotations);
    }

    /**
     * Create a new annotation
     */
    @PostMapping
    public ResponseEntity<?> createAnnotation(@Valid @RequestBody AnnotationDTO annotationDTO) {
        try {
            AnnotationDTO createdAnnotation = annotationService.createAnnotation(annotationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAnnotation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update an existing annotation
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnnotation(@PathVariable UUID id,
            @Valid @RequestBody AnnotationDTO annotationDTO) {
        try {
            AnnotationDTO updatedAnnotation = annotationService.updateAnnotation(id, annotationDTO);
            return ResponseEntity.ok(updatedAnnotation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Delete an annotation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnnotation(@PathVariable UUID id) {
        try {
            annotationService.deleteAnnotation(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Search annotations by selected text
     */
    @GetMapping("/search")
    public ResponseEntity<List<AnnotationDTO>> searchAnnotations(@RequestParam String query) {
        List<AnnotationDTO> annotations = annotationService.searchAnnotationsByText(query);
        return ResponseEntity.ok(annotations);
    }

    /**
     * Get annotations within a specific position range
     */
    @GetMapping("/document/{documentId}/range")
    public ResponseEntity<List<AnnotationDTO>> getAnnotationsInRange(
            @PathVariable UUID documentId,
            @RequestParam int startPos,
            @RequestParam int endPos) {
        List<AnnotationDTO> annotations = annotationService.getAnnotationsInRange(documentId, startPos, endPos);
        return ResponseEntity.ok(annotations);
    }

    /**
     * Get annotation count by label for analytics
     */
    @GetMapping("/statistics/by-label")
    public ResponseEntity<List<Object[]>> getAnnotationCountByLabel() {
        List<Object[]> statistics = annotationService.getAnnotationCountByLabel();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get annotation count by document for analytics
     */
    @GetMapping("/statistics/by-document")
    public ResponseEntity<List<Object[]>> getAnnotationCountByDocument() {
        List<Object[]> statistics = annotationService.getAnnotationCountByDocument();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get documents with highest concentration of a specific label
     */
    @GetMapping("/statistics/concentration/{labelId}")
    public ResponseEntity<List<Object[]>> getHighestLabelConcentration(@PathVariable UUID labelId) {
        List<Object[]> statistics = annotationService.getDocumentsWithHighestLabelConcentration(labelId);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get total annotation count
     */
    @GetMapping("/statistics/total-count")
    public ResponseEntity<Long> getTotalAnnotationCount() {
        Long count = annotationService.getTotalAnnotationCount();
        return ResponseEntity.ok(count);
    }
}
