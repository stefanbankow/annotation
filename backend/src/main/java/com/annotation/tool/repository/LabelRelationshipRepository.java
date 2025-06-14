package com.annotation.tool.repository;

import com.annotation.tool.entity.LabelRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for LabelRelationship entity operations
 * 
 * Manages relationships between labels including bidirectional queries
 */
@Repository
public interface LabelRelationshipRepository extends JpaRepository<LabelRelationship, UUID> {
    
    /**
     * Find relationship between two specific labels
     */
    Optional<LabelRelationship> findBySourceLabelIdAndTargetLabelId(UUID sourceLabelId, UUID targetLabelId);
    
    /**
     * Find all relationships where the specified label is the source
     */
    List<LabelRelationship> findBySourceLabelId(UUID sourceLabelId);
    
    /**
     * Find all relationships where the specified label is the target
     */
    List<LabelRelationship> findByTargetLabelId(UUID targetLabelId);
    
    /**
     * Find all relationships involving a specific label (either as source or target)
     */
    @Query("SELECT lr FROM LabelRelationship lr " +
           "WHERE lr.sourceLabel.id = :labelId OR lr.targetLabel.id = :labelId")
    List<LabelRelationship> findAllRelationshipsForLabel(@Param("labelId") UUID labelId);
    
    /**
     * Count total number of relationships for analytics
     */
    @Query("SELECT COUNT(lr) FROM LabelRelationship lr")
    Long countTotalRelationships();
    
    /**
     * Find labels with most outgoing relationships
     */
    @Query("SELECT lr.sourceLabel, COUNT(lr) as relationship_count " +
           "FROM LabelRelationship lr " +
           "GROUP BY lr.sourceLabel " +
           "ORDER BY COUNT(lr) DESC")
    List<Object[]> findLabelsWithMostOutgoingRelationships();
    
    /**
     * Find labels with most incoming relationships
     */
    @Query("SELECT lr.targetLabel, COUNT(lr) as relationship_count " +
           "FROM LabelRelationship lr " +
           "GROUP BY lr.targetLabel " +
           "ORDER BY COUNT(lr) DESC")
    List<Object[]> findLabelsWithMostIncomingRelationships();
}
