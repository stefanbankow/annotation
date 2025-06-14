package com.annotation.tool.repository;

import com.annotation.tool.entity.TextDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for TextDocument entity operations
 * 
 * Manages text documents created in the built-in editor
 */
@Repository
public interface TextDocumentRepository extends JpaRepository<TextDocument, UUID> {
    
    /**
     * Find text documents by title containing specific text (case-insensitive)
     */
    List<TextDocument> findByTitleContainingIgnoreCase(String title);
    
    /**
     * Find text documents by content containing specific text (case-insensitive)
     */
    @Query("SELECT td FROM TextDocument td WHERE LOWER(td.content) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<TextDocument> findByContentContaining(@Param("searchText") String searchText);
    
    /**
     * Find all text documents ordered by creation date (newest first)
     */
    List<TextDocument> findAllByOrderByCreatedAtDesc();
    
    /**
     * Find all text documents ordered by last update (most recently updated first)
     */
    List<TextDocument> findAllByOrderByUpdatedAtDesc();
}
