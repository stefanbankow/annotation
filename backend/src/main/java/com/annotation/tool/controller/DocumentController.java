package com.annotation.tool.controller;

import com.annotation.tool.dto.DocumentDTO;
import com.annotation.tool.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * REST Controller for Document management
 * 
 * Handles HTTP requests for document operations including upload, processing,
 * and management of .txt, .docx, and .pdf files
 */
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    /**
     * Get all documents
     */
    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        List<DocumentDTO> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    /**
     * Get document by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocumentById(@PathVariable UUID id) {
        return documentService.getDocumentById(id)
                .map(document -> ResponseEntity.ok(document))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get document with annotations by ID
     */
    @GetMapping("/{id}/with-annotations")
    public ResponseEntity<DocumentDTO> getDocumentWithAnnotationsById(@PathVariable UUID id) {
        return documentService.getDocumentWithAnnotationsById(id)
                .map(document -> ResponseEntity.ok(document))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Upload a new document
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "name", required = false) String documentName) {
        try {
            DocumentDTO uploadedDocument = documentService.uploadDocument(file, documentName);
            return ResponseEntity.status(HttpStatus.CREATED).body(uploadedDocument);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload document: " + e.getMessage()));
        }
    }

    /**
     * Update document name
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDocument(@PathVariable UUID id,
            @RequestBody Map<String, String> request) {
        try {
            String newName = request.get("name");
            if (newName == null || newName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Document name is required"));
            }

            DocumentDTO updatedDocument = documentService.updateDocument(id, newName);
            return ResponseEntity.ok(updatedDocument);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Delete a document
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable UUID id) {
        try {
            documentService.deleteDocument(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get documents by file type
     */
    @GetMapping("/by-type/{fileType}")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByFileType(@PathVariable String fileType) {
        List<DocumentDTO> documents = documentService.getDocumentsByFileType(fileType);
        return ResponseEntity.ok(documents);
    }

    /**
     * Search documents by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<DocumentDTO>> searchDocuments(@RequestParam String query) {
        List<DocumentDTO> documents = documentService.searchDocumentsByName(query);
        return ResponseEntity.ok(documents);
    }

    /**
     * Search documents by content
     */
    @GetMapping("/search-content")
    public ResponseEntity<List<DocumentDTO>> searchDocumentsByContent(@RequestParam String query) {
        List<DocumentDTO> documents = documentService.searchDocumentsByContent(query);
        return ResponseEntity.ok(documents);
    }

    /**
     * Get documents with annotation count for analytics
     */
    @GetMapping("/statistics/annotation-count")
    public ResponseEntity<List<Object[]>> getDocumentsWithAnnotationCount() {
        List<Object[]> statistics = documentService.getDocumentsWithAnnotationCount();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get document statistics by file type
     */
    @GetMapping("/statistics/by-type")
    public ResponseEntity<List<Object[]>> getDocumentStatsByFileType() {
        List<Object[]> statistics = documentService.getDocumentStatsByFileType();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get documents without annotations
     */
    @GetMapping("/without-annotations")
    public ResponseEntity<List<DocumentDTO>> getDocumentsWithoutAnnotations() {
        List<DocumentDTO> documents = documentService.getDocumentsWithoutAnnotations();
        return ResponseEntity.ok(documents);
    }
}
