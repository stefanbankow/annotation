package com.annotation.tool.controller;

import com.annotation.tool.dto.TextDocumentDTO;
import com.annotation.tool.service.TextDocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * REST Controller for TextDocument management
 * 
 * Handles HTTP requests for text document operations for documents
 * created using the built-in text editor
 */
@RestController
@RequestMapping("/api/text-documents")
public class TextDocumentController {

    @Autowired
    private TextDocumentService textDocumentService;

    /**
     * Get all text documents
     */
    @GetMapping
    public ResponseEntity<List<TextDocumentDTO>> getAllTextDocuments() {
        List<TextDocumentDTO> textDocuments = textDocumentService.getAllTextDocuments();
        return ResponseEntity.ok(textDocuments);
    }

    /**
     * Get text document by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TextDocumentDTO> getTextDocumentById(@PathVariable UUID id) {
        return textDocumentService.getTextDocumentById(id)
                .map(textDocument -> ResponseEntity.ok(textDocument))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new text document
     */
    @PostMapping
    public ResponseEntity<?> createTextDocument(@Valid @RequestBody TextDocumentDTO textDocumentDTO) {
        try {
            TextDocumentDTO createdTextDocument = textDocumentService.createTextDocument(textDocumentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTextDocument);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update an existing text document
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTextDocument(@PathVariable UUID id,
            @Valid @RequestBody TextDocumentDTO textDocumentDTO) {
        try {
            TextDocumentDTO updatedTextDocument = textDocumentService.updateTextDocument(id, textDocumentDTO);
            return ResponseEntity.ok(updatedTextDocument);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Delete a text document
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTextDocument(@PathVariable UUID id) {
        try {
            textDocumentService.deleteTextDocument(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Search text documents by title
     */
    @GetMapping("/search/title")
    public ResponseEntity<List<TextDocumentDTO>> searchTextDocumentsByTitle(@RequestParam String query) {
        List<TextDocumentDTO> textDocuments = textDocumentService.searchTextDocumentsByTitle(query);
        return ResponseEntity.ok(textDocuments);
    }

    /**
     * Search text documents by content
     */
    @GetMapping("/search/content")
    public ResponseEntity<List<TextDocumentDTO>> searchTextDocumentsByContent(@RequestParam String query) {
        List<TextDocumentDTO> textDocuments = textDocumentService.searchTextDocumentsByContent(query);
        return ResponseEntity.ok(textDocuments);
    }

    /**
     * Get recently created text documents
     */
    @GetMapping("/recent")
    public ResponseEntity<List<TextDocumentDTO>> getRecentTextDocuments(
            @RequestParam(defaultValue = "10") int limit) {
        List<TextDocumentDTO> textDocuments = textDocumentService.getRecentTextDocuments(limit);
        return ResponseEntity.ok(textDocuments);
    }

    /**
     * Get recently updated text documents
     */
    @GetMapping("/recently-updated")
    public ResponseEntity<List<TextDocumentDTO>> getRecentlyUpdatedTextDocuments(
            @RequestParam(defaultValue = "10") int limit) {
        List<TextDocumentDTO> textDocuments = textDocumentService.getRecentlyUpdatedTextDocuments(limit);
        return ResponseEntity.ok(textDocuments);
    }
}
