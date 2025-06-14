package com.annotation.tool.service;

import com.annotation.tool.dto.LabelDTO;
import com.annotation.tool.entity.Label;
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
 * Service class for managing labels (етикети)
 * 
 * Handles business logic for label operations including hierarchical management
 * and analytics
 */
@Service
@Transactional
public class LabelService {
    
    @Autowired
    private LabelRepository labelRepository;
    
    @Autowired
    private DTOMapper dtoMapper;
    
    /**
     * Get all labels
     */
    public List<LabelDTO> getAllLabels() {
        return labelRepository.findAll()
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get label by ID
     */
    public Optional<LabelDTO> getLabelById(UUID id) {
        return labelRepository.findById(id)
                .map(dtoMapper::toDTO);
    }
    
    /**
     * Get all root labels (labels without parent)
     */
    public List<LabelDTO> getRootLabels() {
        return labelRepository.findByParentIsNull()
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get child labels of a specific parent
     */
    public List<LabelDTO> getChildLabels(UUID parentId) {
        return labelRepository.findByParentId(parentId)
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Create a new label
     */
    public LabelDTO createLabel(LabelDTO labelDTO) {
        // Check if label with same name already exists
        if (labelRepository.findByName(labelDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Label with name '" + labelDTO.getName() + "' already exists");
        }
        
        Label label = dtoMapper.toEntity(labelDTO);
        
        // Set parent if specified
        if (labelDTO.getParentId() != null) {
            Optional<Label> parent = labelRepository.findById(labelDTO.getParentId());
            if (parent.isPresent()) {
                label.setParent(parent.get());
            } else {
                throw new IllegalArgumentException("Parent label not found");
            }
        }
        
        Label savedLabel = labelRepository.save(label);
        return dtoMapper.toDTO(savedLabel);
    }
    
    /**
     * Update an existing label
     */
    public LabelDTO updateLabel(UUID id, LabelDTO labelDTO) {
        Label existingLabel = labelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Label not found"));
        
        // Check if new name conflicts with existing labels (excluding current label)
        Optional<Label> labelWithSameName = labelRepository.findByName(labelDTO.getName());
        if (labelWithSameName.isPresent() && !labelWithSameName.get().getId().equals(id)) {
            throw new IllegalArgumentException("Label with name '" + labelDTO.getName() + "' already exists");
        }
        
        // Update fields
        existingLabel.setName(labelDTO.getName());
        existingLabel.setColor(labelDTO.getColor());
        existingLabel.setDescription(labelDTO.getDescription());
        
        // Update parent if specified
        if (labelDTO.getParentId() != null) {
            if (labelDTO.getParentId().equals(id)) {
                throw new IllegalArgumentException("Label cannot be its own parent");
            }
            
            Optional<Label> parent = labelRepository.findById(labelDTO.getParentId());
            if (parent.isPresent()) {
                // Check for circular reference
                if (isCircularReference(id, labelDTO.getParentId())) {
                    throw new IllegalArgumentException("Circular reference detected in label hierarchy");
                }
                existingLabel.setParent(parent.get());
            } else {
                throw new IllegalArgumentException("Parent label not found");
            }
        } else {
            existingLabel.setParent(null);
        }
        
        Label savedLabel = labelRepository.save(existingLabel);
        return dtoMapper.toDTO(savedLabel);
    }
    
    /**
     * Delete a label
     */
    public void deleteLabel(UUID id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Label not found"));
        
        // Check if label has annotations
        if (!label.getAnnotations().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete label that has annotations. Delete annotations first.");
        }
        
        // Check if label has children
        if (!label.getChildren().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete label that has child labels. Delete child labels first.");
        }
        
        labelRepository.delete(label);
    }
    
    /**
     * Get label usage statistics
     */
    public List<Object[]> getLabelUsageStatistics() {
        return labelRepository.findLabelUsageStatistics();
    }
    
    /**
     * Get unused labels
     */
    public List<LabelDTO> getUnusedLabels() {
        return labelRepository.findUnusedLabels()
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Search labels by name
     */
    public List<LabelDTO> searchLabelsByName(String searchTerm) {
        return labelRepository.findAll()
                .stream()
                .filter(label -> label.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Check for circular reference in label hierarchy
     */
    private boolean isCircularReference(UUID labelId, UUID potentialParentId) {
        Optional<Label> potentialParent = labelRepository.findById(potentialParentId);
        if (!potentialParent.isPresent()) {
            return false;
        }
        
        Label current = potentialParent.get();
        while (current.getParent() != null) {
            if (current.getParent().getId().equals(labelId)) {
                return true;
            }
            current = current.getParent();
        }
        
        return false;
    }
}
