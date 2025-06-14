package com.annotation.tool.repository;

import com.annotation.tool.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Document entity operations
 * 
 * Provides methods for document management and analytics
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    
    /**
     * Find documents by file type
     */
    List<Document> findByFileType(String fileType);
    
    /**
     * Find documents by name containing specific text (case-insensitive)
     */
    List<Document> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find documents by original filename
     */
    List<Document> findByOriginalFilename(String originalFilename);
    
    /**
     * Find documents ordered by upload date (newest first)
     */
    List<Document> findAllByOrderByUploadDateDesc();
    
    /**
     * Find documents with annotation count
     */
    @Query("SELECT d, COUNT(a) as annotation_count " +
           "FROM Document d LEFT JOIN d.annotations a " +
           "GROUP BY d " +
           "ORDER BY COUNT(a) DESC")
    List<Object[]> findDocumentsWithAnnotationCount();
    
    /**
     * Find documents that contain a specific text in their content
     */
    @Query("SELECT d FROM Document d WHERE LOWER(d.content) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<Document> findByContentContaining(@Param("searchText") String searchText);
    
    /**
     * Get document statistics by file type
     */
    @Query("SELECT d.fileType, COUNT(d) as document_count " +
           "FROM Document d " +
           "GROUP BY d.fileType")
    List<Object[]> getDocumentStatsByFileType();
    
    /**
     * Find documents without annotations
     */
    @Query("SELECT d FROM Document d WHERE d.id NOT IN " +
           "(SELECT DISTINCT a.document.id FROM Annotation a)")
    List<Document> findDocumentsWithoutAnnotations();
}
