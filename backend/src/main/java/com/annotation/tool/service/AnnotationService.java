package com.annotation.tool.service;

import com.annotation.tool.dto.AnnotationDTO;
import com.annotation.tool.entity.Annotation;
import com.annotation.tool.entity.Document;
import com.annotation.tool.entity.Label;
import com.annotation.tool.repository.AnnotationRepository;
import com.annotation.tool.repository.DocumentRepository;
import com.annotation.tool.repository.LabelRepository;
import com.annotation.tool.util.DTOMapper;
import com.annotation.tool.util.FileProcessingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing annotations
 * 
 * Handles business logic for creating, updating, and analyzing annotations
 */
@Service
@Transactional
public class AnnotationService {
    
    @Autowired
    private AnnotationRepository annotationRepository;
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private LabelRepository labelRepository;
    
    @Autowired
    private DTOMapper dtoMapper;
    
    @Autowired
    private FileProcessingUtil fileProcessingUtil;
    
    private static final int CONTEXT_LENGTH = 50; // Characters before and after annotation
    
    /**
     * Get all annotations
     */
    public List<AnnotationDTO> getAllAnnotations() {
        return annotationRepository.findAll()
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get annotation by ID
     */
    public Optional<AnnotationDTO> getAnnotationById(UUID id) {
        return annotationRepository.findById(id)
                .map(dtoMapper::toDTO);
    }
    
    /**
     * Get annotations for a specific document
     */
    public List<AnnotationDTO> getAnnotationsByDocumentId(UUID documentId) {
        return annotationRepository.findByDocumentIdOrderByStartPosition(documentId)
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get annotations with a specific label
     */
    public List<AnnotationDTO> getAnnotationsByLabelId(UUID labelId) {
        return annotationRepository.findByLabelId(labelId)
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get annotations by document and label
     */
    public List<AnnotationDTO> getAnnotationsByDocumentAndLabel(UUID documentId, UUID labelId) {
        return annotationRepository.findByDocumentIdAndLabelId(documentId, labelId)
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Create a new annotation
     */
    public AnnotationDTO createAnnotation(AnnotationDTO annotationDTO) {
        // Validate that document and label exist
        Document document = documentRepository.findById(annotationDTO.getDocumentId())
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
        
        Label label = labelRepository.findById(annotationDTO.getLabelId())
                .orElseThrow(() -> new IllegalArgumentException("Label not found"));
        
        // Validate positions
        if (!fileProcessingUtil.isValidPosition(document.getContent(), 
                annotationDTO.getStartPosition(), annotationDTO.getEndPosition())) {
            throw new IllegalArgumentException("Invalid annotation positions");
        }
        
        // Extract selected text and context
        String selectedText = fileProcessingUtil.extractTextBetweenPositions(
                document.getContent(), 
                annotationDTO.getStartPosition(), 
                annotationDTO.getEndPosition());
        
        String contextBefore = fileProcessingUtil.getContextBefore(
                document.getContent(), 
                annotationDTO.getStartPosition(), 
                CONTEXT_LENGTH);
        
        String contextAfter = fileProcessingUtil.getContextAfter(
                document.getContent(), 
                annotationDTO.getEndPosition(), 
                CONTEXT_LENGTH);
        
        // Create annotation entity
        Annotation annotation = new Annotation(
                document,
                label,
                annotationDTO.getStartPosition(),
                annotationDTO.getEndPosition(),
                selectedText,
                contextBefore,
                contextAfter
        );
        
        Annotation savedAnnotation = annotationRepository.save(annotation);
        return dtoMapper.toDTO(savedAnnotation);
    }
    
    /**
     * Update an existing annotation
     */
    public AnnotationDTO updateAnnotation(UUID id, AnnotationDTO annotationDTO) {
        Annotation existingAnnotation = annotationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Annotation not found"));
        
        // Update label if changed
        if (!existingAnnotation.getLabel().getId().equals(annotationDTO.getLabelId())) {
            Label newLabel = labelRepository.findById(annotationDTO.getLabelId())
                    .orElseThrow(() -> new IllegalArgumentException("Label not found"));
            existingAnnotation.setLabel(newLabel);
        }
        
        // Update positions if changed
        if (!existingAnnotation.getStartPosition().equals(annotationDTO.getStartPosition()) ||
            !existingAnnotation.getEndPosition().equals(annotationDTO.getEndPosition())) {
            
            Document document = existingAnnotation.getDocument();
            
            // Validate new positions
            if (!fileProcessingUtil.isValidPosition(document.getContent(), 
                    annotationDTO.getStartPosition(), annotationDTO.getEndPosition())) {
                throw new IllegalArgumentException("Invalid annotation positions");
            }
            
            // Update positions and text
            existingAnnotation.setStartPosition(annotationDTO.getStartPosition());
            existingAnnotation.setEndPosition(annotationDTO.getEndPosition());
            
            String selectedText = fileProcessingUtil.extractTextBetweenPositions(
                    document.getContent(), 
                    annotationDTO.getStartPosition(), 
                    annotationDTO.getEndPosition());
            
            String contextBefore = fileProcessingUtil.getContextBefore(
                    document.getContent(), 
                    annotationDTO.getStartPosition(), 
                    CONTEXT_LENGTH);
            
            String contextAfter = fileProcessingUtil.getContextAfter(
                    document.getContent(), 
                    annotationDTO.getEndPosition(), 
                    CONTEXT_LENGTH);
            
            existingAnnotation.setSelectedText(selectedText);
            existingAnnotation.setContextBefore(contextBefore);
            existingAnnotation.setContextAfter(contextAfter);
        }
        
        Annotation savedAnnotation = annotationRepository.save(existingAnnotation);
        return dtoMapper.toDTO(savedAnnotation);
    }
    
    /**
     * Delete an annotation
     */
    public void deleteAnnotation(UUID id) {
        Annotation annotation = annotationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Annotation not found"));
        
        annotationRepository.delete(annotation);
    }
    
    /**
     * Search annotations by selected text
     */
    public List<AnnotationDTO> searchAnnotationsByText(String searchTerm) {
        return annotationRepository.findBySelectedTextContaining(searchTerm)
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get annotations within a specific position range
     */
    public List<AnnotationDTO> getAnnotationsInRange(UUID documentId, int startPos, int endPos) {
        return annotationRepository.findAnnotationsInRange(documentId, startPos, endPos)
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get annotation count by label for analytics
     */
    public List<Object[]> getAnnotationCountByLabel() {
        return annotationRepository.countAnnotationsByLabel();
    }
    
    /**
     * Get annotation count by document for analytics
     */
    public List<Object[]> getAnnotationCountByDocument() {
        return annotationRepository.countAnnotationsByDocument();
    }
    
    /**
     * Find documents with highest concentration of a specific label
     */
    public List<Object[]> getDocumentsWithHighestLabelConcentration(UUID labelId) {
        // Use proximity threshold of 100 characters
        return annotationRepository.findDocumentsWithHighestLabelConcentration(labelId, 100);
    }
    
    /**
     * Get total annotation count
     */
    public Long getTotalAnnotationCount() {
        return annotationRepository.getTotalAnnotationCount();
    }
    
    /**
     * Delete all annotations for a specific document
     */
    public void deleteAnnotationsByDocumentId(UUID documentId) {
        List<Annotation> annotations = annotationRepository.findByDocumentId(documentId);
        annotationRepository.deleteAll(annotations);
    }
    
    /**
     * Delete all annotations with a specific label
     */
    public void deleteAnnotationsByLabelId(UUID labelId) {
        List<Annotation> annotations = annotationRepository.findByLabelId(labelId);
        annotationRepository.deleteAll(annotations);
    }
}
