package com.annotation.tool.repository;

import com.annotation.tool.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Label entity operations
 * 
 * Provides methods for managing labels including hierarchical queries
 * and statistics for analytics
 */
@Repository
public interface LabelRepository extends JpaRepository<Label, UUID> {
    
    /**
     * Find a label by its name
     */
    Optional<Label> findByName(String name);
    
    /**
     * Find all root labels (labels without parent)
     */
    List<Label> findByParentIsNull();
    
    /**
     * Find all child labels of a specific parent
     */
    List<Label> findByParentId(UUID parentId);
    
    /**
     * Find labels by color
     */
    List<Label> findByColor(String color);
    
    /**
     * Get label usage statistics - most frequently used labels
     */
    @Query("SELECT l, COUNT(a) as usage_count " +
           "FROM Label l LEFT JOIN l.annotations a " +
           "GROUP BY l " +
           "ORDER BY COUNT(a) DESC")
    List<Object[]> findLabelUsageStatistics();
    
    /**
     * Find labels with annotation count
     */
    @Query("SELECT l, COUNT(a) as annotation_count " +
           "FROM Label l LEFT JOIN l.annotations a " +
           "WHERE l.id = :labelId " +
           "GROUP BY l")
    Optional<Object[]> findLabelWithAnnotationCount(@Param("labelId") UUID labelId);
    
    /**
     * Find all labels that are not used in any annotation
     */
    @Query("SELECT l FROM Label l WHERE l.id NOT IN " +
           "(SELECT DISTINCT a.label.id FROM Annotation a)")
    List<Label> findUnusedLabels();
}
