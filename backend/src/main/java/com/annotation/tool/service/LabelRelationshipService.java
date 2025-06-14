package com.annotation.tool.service;

import com.annotation.tool.dto.LabelRelationshipDTO;
import com.annotation.tool.entity.Label;
import com.annotation.tool.entity.LabelRelationship;
import com.annotation.tool.repository.LabelRelationshipRepository;
import com.annotation.tool.repository.LabelRepository;
import com.annotation.tool.util.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing label relationships (връзки между етикети)
 * 
 * Handles business logic for creating and managing relationships between labels
 */
@Service
@Transactional
public class LabelRelationshipService {
    
    @Autowired
    private LabelRelationshipRepository relationshipRepository;
    
    @Autowired
    private LabelRepository labelRepository;
    
    @Autowired
    private DTOMapper dtoMapper;
    
    /**
     * Get all label relationships
     */
    public List<LabelRelationshipDTO> getAllRelationships() {
        return relationshipRepository.findAll()
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get relationship by ID
     */
    public Optional<LabelRelationshipDTO> getRelationshipById(UUID id) {
        return relationshipRepository.findById(id)
                .map(dtoMapper::toDTO);
    }
    
    /**
     * Get all relationships for a specific label
     */
    public List<LabelRelationshipDTO> getRelationshipsForLabel(UUID labelId) {
        return relationshipRepository.findAllRelationshipsForLabel(labelId)
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get outgoing relationships (where label is source)
     */
    public List<LabelRelationshipDTO> getOutgoingRelationships(UUID sourceLabelId) {
        return relationshipRepository.findBySourceLabelId(sourceLabelId)
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get incoming relationships (where label is target)
     */
    public List<LabelRelationshipDTO> getIncomingRelationships(UUID targetLabelId) {
        return relationshipRepository.findByTargetLabelId(targetLabelId)
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Create a new label relationship
     */
    public LabelRelationshipDTO createRelationship(LabelRelationshipDTO relationshipDTO) {
        // Validate that source and target labels exist
        Label sourceLabel = labelRepository.findById(relationshipDTO.getSourceLabelId())
                .orElseThrow(() -> new IllegalArgumentException("Source label not found"));
        
        Label targetLabel = labelRepository.findById(relationshipDTO.getTargetLabelId())
                .orElseThrow(() -> new IllegalArgumentException("Target label not found"));
        
        // Check if relationship already exists
        Optional<LabelRelationship> existingRelationship = relationshipRepository
                .findBySourceLabelIdAndTargetLabelId(
                        relationshipDTO.getSourceLabelId(), 
                        relationshipDTO.getTargetLabelId());
        
        if (existingRelationship.isPresent()) {
            throw new IllegalArgumentException("Relationship between these labels already exists");
        }
        
        // Prevent self-referencing relationships
        if (relationshipDTO.getSourceLabelId().equals(relationshipDTO.getTargetLabelId())) {
            throw new IllegalArgumentException("Label cannot have a relationship with itself");
        }
        
        LabelRelationship relationship = new LabelRelationship();
        relationship.setSourceLabel(sourceLabel);
        relationship.setTargetLabel(targetLabel);
        relationship.setDescription(relationshipDTO.getDescription());
        
        LabelRelationship savedRelationship = relationshipRepository.save(relationship);
        return dtoMapper.toDTO(savedRelationship);
    }
    
    /**
     * Update an existing label relationship
     */
    public LabelRelationshipDTO updateRelationship(UUID id, LabelRelationshipDTO relationshipDTO) {
        LabelRelationship existingRelationship = relationshipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Relationship not found"));
        
        // Update description (source and target labels cannot be changed to maintain data integrity)
        existingRelationship.setDescription(relationshipDTO.getDescription());
        
        LabelRelationship savedRelationship = relationshipRepository.save(existingRelationship);
        return dtoMapper.toDTO(savedRelationship);
    }
    
    /**
     * Delete a label relationship
     */
    public void deleteRelationship(UUID id) {
        LabelRelationship relationship = relationshipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Relationship not found"));
        
        relationshipRepository.delete(relationship);
    }
    
    /**
     * Get relationship statistics
     */
    public Long getTotalRelationshipCount() {
        return relationshipRepository.countTotalRelationships();
    }
    
    /**
     * Get labels with most outgoing relationships
     */
    public List<Object[]> getLabelsWithMostOutgoingRelationships() {
        return relationshipRepository.findLabelsWithMostOutgoingRelationships();
    }
    
    /**
     * Get labels with most incoming relationships
     */
    public List<Object[]> getLabelsWithMostIncomingRelationships() {
        return relationshipRepository.findLabelsWithMostIncomingRelationships();
    }
}
