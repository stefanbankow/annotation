package com.annotation.tool.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity class representing uploaded documents that can be annotated
 * 
 * Supports .txt, .docx, and .pdf file formats
 * Stores both the original file and extracted text content for annotation
 */
@Entity
@Table(name = "documents")
public class Document {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "original_filename", nullable = false)
    private String originalFilename;
    
    @Column(name = "file_type", nullable = false, length = 10)
    private String fileType; // txt, docx, pdf
    
    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;
    
    @Column(name = "content", columnDefinition = "TEXT")
    private String content; // Extracted text content
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Annotation> annotations = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
    
    // Constructors
    public Document() {}
    
    public Document(String name, String originalFilename, String fileType, 
                   String filePath, String content, Long fileSize) {
        this.name = name;
        this.originalFilename = originalFilename;
        this.fileType = fileType;
        this.filePath = filePath;
        this.content = content;
        this.fileSize = fileSize;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getOriginalFilename() {
        return originalFilename;
    }
    
    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public List<Annotation> getAnnotations() {
        return annotations;
    }
    
    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }
    
    public LocalDateTime getUploadDate() {
        return uploadDate;
    }
    
    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
}
