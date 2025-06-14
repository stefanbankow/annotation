package com.annotation.tool.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object for Document entity
 */
public class DocumentDTO {
    
    private UUID id;
    private String name;
    private String originalFilename;
    private String fileType;
    private String content;
    private Long fileSize;
    private LocalDateTime uploadDate;
    
    private List<AnnotationDTO> annotations;
    private Long annotationCount;
    
    // Constructors
    public DocumentDTO() {}
    
    public DocumentDTO(String name, String originalFilename, String fileType, String content, Long fileSize) {
        this.name = name;
        this.originalFilename = originalFilename;
        this.fileType = fileType;
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
    
    public LocalDateTime getUploadDate() {
        return uploadDate;
    }
    
    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
    
    public List<AnnotationDTO> getAnnotations() {
        return annotations;
    }
    
    public void setAnnotations(List<AnnotationDTO> annotations) {
        this.annotations = annotations;
    }
    
    public Long getAnnotationCount() {
        return annotationCount;
    }
    
    public void setAnnotationCount(Long annotationCount) {
        this.annotationCount = annotationCount;
    }
}
