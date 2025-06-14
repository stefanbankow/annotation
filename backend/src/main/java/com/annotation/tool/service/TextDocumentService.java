package com.annotation.tool.service;

import com.annotation.tool.dto.TextDocumentDTO;
import com.annotation.tool.entity.TextDocument;
import com.annotation.tool.repository.TextDocumentRepository;
import com.annotation.tool.util.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing text documents created in the built-in editor
 * 
 * Handles CRUD operations for text documents created directly in the application
 */
@Service
@Transactional
public class TextDocumentService {
    
    @Autowired
    private TextDocumentRepository textDocumentRepository;
    
    @Autowired
    private DTOMapper dtoMapper;
    
    /**
     * Get all text documents
     */
    public List<TextDocumentDTO> getAllTextDocuments() {
        return textDocumentRepository.findAllByOrderByUpdatedAtDesc()
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get text document by ID
     */
    public Optional<TextDocumentDTO> getTextDocumentById(UUID id) {
        return textDocumentRepository.findById(id)
                .map(dtoMapper::toDTO);
    }
    
    /**
     * Create a new text document
     */
    public TextDocumentDTO createTextDocument(TextDocumentDTO textDocumentDTO) {
        // Validate input
        if (textDocumentDTO.getTitle() == null || textDocumentDTO.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        
        if (textDocumentDTO.getContent() == null || textDocumentDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content is required");
        }
        
        TextDocument textDocument = dtoMapper.toEntity(textDocumentDTO);
        TextDocument savedTextDocument = textDocumentRepository.save(textDocument);
        return dtoMapper.toDTO(savedTextDocument);
    }
    
    /**
     * Update an existing text document
     */
    public TextDocumentDTO updateTextDocument(UUID id, TextDocumentDTO textDocumentDTO) {
        TextDocument existingTextDocument = textDocumentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Text document not found"));
        
        // Validate input
        if (textDocumentDTO.getTitle() == null || textDocumentDTO.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        
        if (textDocumentDTO.getContent() == null || textDocumentDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content is required");
        }
        
        // Update fields
        existingTextDocument.setTitle(textDocumentDTO.getTitle());
        existingTextDocument.setContent(textDocumentDTO.getContent());
        
        TextDocument savedTextDocument = textDocumentRepository.save(existingTextDocument);
        return dtoMapper.toDTO(savedTextDocument);
    }
    
    /**
     * Delete a text document
     */
    public void deleteTextDocument(UUID id) {
        TextDocument textDocument = textDocumentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Text document not found"));
        
        textDocumentRepository.delete(textDocument);
    }
    
    /**
     * Search text documents by title
     */
    public List<TextDocumentDTO> searchTextDocumentsByTitle(String searchTerm) {
        return textDocumentRepository.findByTitleContainingIgnoreCase(searchTerm)
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Search text documents by content
     */
    public List<TextDocumentDTO> searchTextDocumentsByContent(String searchTerm) {
        return textDocumentRepository.findByContentContaining(searchTerm)
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get recently created text documents
     */
    public List<TextDocumentDTO> getRecentTextDocuments(int limit) {
        return textDocumentRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .limit(limit)
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get recently updated text documents
     */
    public List<TextDocumentDTO> getRecentlyUpdatedTextDocuments(int limit) {
        return textDocumentRepository.findAllByOrderByUpdatedAtDesc()
                .stream()
                .limit(limit)
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
