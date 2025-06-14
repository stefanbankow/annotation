package com.annotation.tool.repository;

import com.annotation.tool.entity.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Annotation entity operations
 * 
 * Provides methods for annotation management and analytics
 */
@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, UUID> {
    
    /**
     * Find all annotations for a specific document
     */
    List<Annotation> findByDocumentId(UUID documentId);
    
    /**
     * Find all annotations with a specific label
     */
    List<Annotation> findByLabelId(UUID labelId);
    
    /**
     * Find annotations by document and label
     */
    List<Annotation> findByDocumentIdAndLabelId(UUID documentId, UUID labelId);
    
    /**
     * Find annotations ordered by position in document
     */
    List<Annotation> findByDocumentIdOrderByStartPosition(UUID documentId);
    
    /**
     * Find annotations containing specific text
     */
    @Query("SELECT a FROM Annotation a WHERE LOWER(a.selectedText) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<Annotation> findBySelectedTextContaining(@Param("searchText") String searchText);
    
    /**
     * Count annotations by label for analytics
     */
    @Query("SELECT a.label, COUNT(a) as annotation_count " +
           "FROM Annotation a " +
           "GROUP BY a.label " +
           "ORDER BY COUNT(a) DESC")
    List<Object[]> countAnnotationsByLabel();
    
    /**
     * Count annotations by document for analytics
     */
    @Query("SELECT a.document, COUNT(a) as annotation_count " +
           "FROM Annotation a " +
           "GROUP BY a.document " +
           "ORDER BY COUNT(a) DESC")
    List<Object[]> countAnnotationsByDocument();
    
    /**
     * Find text segments with highest concentration of a specific label
     * This query finds overlapping or nearby annotations of the same label
     */
    @Query("SELECT a1.document, COUNT(a1) as concentration " +
           "FROM Annotation a1 JOIN Annotation a2 ON a1.document = a2.document " +
           "WHERE a1.label.id = :labelId AND a2.label.id = :labelId " +
           "AND ABS(a1.startPosition - a2.endPosition) <= :proximityThreshold " +
           "GROUP BY a1.document " +
           "ORDER BY COUNT(a1) DESC")
    List<Object[]> findDocumentsWithHighestLabelConcentration(
            @Param("labelId") UUID labelId, 
            @Param("proximityThreshold") int proximityThreshold);
    
    /**
     * Find annotations within a specific position range in a document
     */
    @Query("SELECT a FROM Annotation a " +
           "WHERE a.document.id = :documentId " +
           "AND a.startPosition >= :startPos " +
           "AND a.endPosition <= :endPos " +
           "ORDER BY a.startPosition")
    List<Annotation> findAnnotationsInRange(
            @Param("documentId") UUID documentId,
            @Param("startPos") int startPos,
            @Param("endPos") int endPos);
    
    /**
     * Get total annotation count for analytics
     */
    @Query("SELECT COUNT(a) FROM Annotation a")
    Long getTotalAnnotationCount();
}
