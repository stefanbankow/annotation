package com.annotation.tool.util;

import com.annotation.tool.dto.*;
import com.annotation.tool.entity.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Utility class for mapping between entities and DTOs
 * 
 * Handles conversion between domain entities and data transfer objects
 * used for API communication
 */
@Component
public class DTOMapper {
    
    // Label mappings
    public LabelDTO toDTO(Label label) {
        if (label == null) return null;
        
        LabelDTO dto = new LabelDTO();
        dto.setId(label.getId());
        dto.setName(label.getName());
        dto.setColor(label.getColor());
        dto.setDescription(label.getDescription());
        dto.setCreatedAt(label.getCreatedAt());
        dto.setUpdatedAt(label.getUpdatedAt());
        
        if (label.getParent() != null) {
            dto.setParentId(label.getParent().getId());
            dto.setParentName(label.getParent().getName());
        }
        
        // Map children (avoid infinite recursion by limiting depth)
        if (label.getChildren() != null && !label.getChildren().isEmpty()) {
            dto.setChildren(label.getChildren().stream()
                    .map(this::toSimpleDTO)
                    .collect(Collectors.toList()));
        }
        
        // Count annotations
        if (label.getAnnotations() != null) {
            dto.setAnnotationCount((long) label.getAnnotations().size());
        }
        
        return dto;
    }
    
    public LabelDTO toSimpleDTO(Label label) {
        if (label == null) return null;
        
        LabelDTO dto = new LabelDTO();
        dto.setId(label.getId());
        dto.setName(label.getName());
        dto.setColor(label.getColor());
        dto.setDescription(label.getDescription());
        dto.setCreatedAt(label.getCreatedAt());
        dto.setUpdatedAt(label.getUpdatedAt());
        
        if (label.getParent() != null) {
            dto.setParentId(label.getParent().getId());
            dto.setParentName(label.getParent().getName());
        }
        
        return dto;
    }
    
    public Label toEntity(LabelDTO dto) {
        if (dto == null) return null;
        
        Label label = new Label();
        label.setId(dto.getId());
        label.setName(dto.getName());
        label.setColor(dto.getColor());
        label.setDescription(dto.getDescription());
        
        return label;
    }
    
    // LabelRelationship mappings
    public LabelRelationshipDTO toDTO(LabelRelationship relationship) {
        if (relationship == null) return null;
        
        LabelRelationshipDTO dto = new LabelRelationshipDTO();
        dto.setId(relationship.getId());
        dto.setDescription(relationship.getDescription());
        dto.setCreatedAt(relationship.getCreatedAt());
        
        if (relationship.getSourceLabel() != null) {
            dto.setSourceLabelId(relationship.getSourceLabel().getId());
            dto.setSourceLabelName(relationship.getSourceLabel().getName());
            dto.setSourceLabelColor(relationship.getSourceLabel().getColor());
        }
        
        if (relationship.getTargetLabel() != null) {
            dto.setTargetLabelId(relationship.getTargetLabel().getId());
            dto.setTargetLabelName(relationship.getTargetLabel().getName());
            dto.setTargetLabelColor(relationship.getTargetLabel().getColor());
        }
        
        return dto;
    }
    
    public LabelRelationship toEntity(LabelRelationshipDTO dto) {
        if (dto == null) return null;
        
        LabelRelationship relationship = new LabelRelationship();
        relationship.setId(dto.getId());
        relationship.setDescription(dto.getDescription());
        
        return relationship;
    }
    
    // Document mappings
    public DocumentDTO toDTO(Document document) {
        if (document == null) return null;
        
        DocumentDTO dto = new DocumentDTO();
        dto.setId(document.getId());
        dto.setName(document.getName());
        dto.setOriginalFilename(document.getOriginalFilename());
        dto.setFileType(document.getFileType());
        dto.setContent(document.getContent());
        dto.setFileSize(document.getFileSize());
        dto.setUploadDate(document.getUploadDate());
        
        // Count annotations
        if (document.getAnnotations() != null) {
            dto.setAnnotationCount((long) document.getAnnotations().size());
        }
        
        return dto;
    }
    
    public DocumentDTO toDTOWithAnnotations(Document document) {
        if (document == null) return null;
        
        DocumentDTO dto = toDTO(document);
        
        // Map annotations
        if (document.getAnnotations() != null) {
            dto.setAnnotations(document.getAnnotations().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    public Document toEntity(DocumentDTO dto) {
        if (dto == null) return null;
        
        Document document = new Document();
        document.setId(dto.getId());
        document.setName(dto.getName());
        document.setOriginalFilename(dto.getOriginalFilename());
        document.setFileType(dto.getFileType());
        document.setContent(dto.getContent());
        document.setFileSize(dto.getFileSize());
        
        return document;
    }
    
    // Annotation mappings
    public AnnotationDTO toDTO(Annotation annotation) {
        if (annotation == null) return null;
        
        AnnotationDTO dto = new AnnotationDTO();
        dto.setId(annotation.getId());
        dto.setStartPosition(annotation.getStartPosition());
        dto.setEndPosition(annotation.getEndPosition());
        dto.setSelectedText(annotation.getSelectedText());
        dto.setContextBefore(annotation.getContextBefore());
        dto.setContextAfter(annotation.getContextAfter());
        dto.setCreatedAt(annotation.getCreatedAt());
        dto.setUpdatedAt(annotation.getUpdatedAt());
        
        if (annotation.getDocument() != null) {
            dto.setDocumentId(annotation.getDocument().getId());
            dto.setDocumentName(annotation.getDocument().getName());
        }
        
        if (annotation.getLabel() != null) {
            dto.setLabelId(annotation.getLabel().getId());
            dto.setLabelName(annotation.getLabel().getName());
            dto.setLabelColor(annotation.getLabel().getColor());
        }
        
        return dto;
    }
    
    public Annotation toEntity(AnnotationDTO dto) {
        if (dto == null) return null;
        
        Annotation annotation = new Annotation();
        annotation.setId(dto.getId());
        annotation.setStartPosition(dto.getStartPosition());
        annotation.setEndPosition(dto.getEndPosition());
        annotation.setSelectedText(dto.getSelectedText());
        annotation.setContextBefore(dto.getContextBefore());
        annotation.setContextAfter(dto.getContextAfter());
        
        return annotation;
    }
    
    // TextDocument mappings
    public TextDocumentDTO toDTO(TextDocument textDocument) {
        if (textDocument == null) return null;
        
        TextDocumentDTO dto = new TextDocumentDTO();
        dto.setId(textDocument.getId());
        dto.setTitle(textDocument.getTitle());
        dto.setContent(textDocument.getContent());
        dto.setCreatedAt(textDocument.getCreatedAt());
        dto.setUpdatedAt(textDocument.getUpdatedAt());
        
        return dto;
    }
    
    public TextDocument toEntity(TextDocumentDTO dto) {
        if (dto == null) return null;
        
        TextDocument textDocument = new TextDocument();
        textDocument.setId(dto.getId());
        textDocument.setTitle(dto.getTitle());
        textDocument.setContent(dto.getContent());
        
        return textDocument;
    }
}
