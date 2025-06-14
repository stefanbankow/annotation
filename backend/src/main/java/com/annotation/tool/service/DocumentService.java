package com.annotation.tool.service;

import com.annotation.tool.dto.DocumentDTO;
import com.annotation.tool.entity.Document;
import com.annotation.tool.repository.DocumentRepository;
import com.annotation.tool.util.DTOMapper;
import com.annotation.tool.util.FileProcessingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing documents
 * 
 * Handles document upload, processing, and management
 * Supports .txt, .docx, and .pdf file formats
 */
@Service
@Transactional
public class DocumentService {
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private DTOMapper dtoMapper;
    
    @Autowired
    private FileProcessingUtil fileProcessingUtil;
    
    @Value("${app.file.upload-dir}")
    private String uploadDir;
    
    /**
     * Get all documents
     */
    public List<DocumentDTO> getAllDocuments() {
        return documentRepository.findAll()
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get document by ID
     */
    public Optional<DocumentDTO> getDocumentById(UUID id) {
        return documentRepository.findById(id)
                .map(dtoMapper::toDTO);
    }
    
    /**
     * Get document with annotations by ID
     */
    public Optional<DocumentDTO> getDocumentWithAnnotationsById(UUID id) {
        return documentRepository.findById(id)
                .map(dtoMapper::toDTOWithAnnotations);
    }
    
    /**
     * Upload and process a new document
     */
    public DocumentDTO uploadDocument(MultipartFile file, String documentName) {
        try {
            // Validate file
            validateFile(file);
            
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFilename = UUID.randomUUID().toString() + "." + fileExtension;
            Path filePath = uploadPath.resolve(uniqueFilename);
            
            // Save file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Extract text content based on file type
            String content = fileProcessingUtil.extractTextContent(filePath.toString(), fileExtension);
            
            // Create document entity
            Document document = new Document(
                documentName != null ? documentName : originalFilename,
                originalFilename,
                fileExtension,
                filePath.toString(),
                content,
                file.getSize()
            );
            
            Document savedDocument = documentRepository.save(document);
            return dtoMapper.toDTO(savedDocument);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update document name
     */
    public DocumentDTO updateDocument(UUID id, String newName) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
        
        document.setName(newName);
        Document savedDocument = documentRepository.save(document);
        return dtoMapper.toDTO(savedDocument);
    }
    
    /**
     * Delete a document
     */
    public void deleteDocument(UUID id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found"));
        
        // Delete physical file
        try {
            Path filePath = Paths.get(document.getFilePath());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log error but continue with database deletion
            System.err.println("Failed to delete physical file: " + e.getMessage());
        }
        
        // Delete from database (annotations will be deleted via cascade)
        documentRepository.delete(document);
    }
    
    /**
     * Get documents by file type
     */
    public List<DocumentDTO> getDocumentsByFileType(String fileType) {
        return documentRepository.findByFileType(fileType)
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Search documents by name
     */
    public List<DocumentDTO> searchDocumentsByName(String searchTerm) {
        return documentRepository.findByNameContainingIgnoreCase(searchTerm)
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Search documents by content
     */
    public List<DocumentDTO> searchDocumentsByContent(String searchTerm) {
        return documentRepository.findByContentContaining(searchTerm)
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get documents with annotation count
     */
    public List<Object[]> getDocumentsWithAnnotationCount() {
        return documentRepository.findDocumentsWithAnnotationCount();
    }
    
    /**
     * Get document statistics by file type
     */
    public List<Object[]> getDocumentStatsByFileType() {
        return documentRepository.getDocumentStatsByFileType();
    }
    
    /**
     * Get documents without annotations
     */
    public List<DocumentDTO> getDocumentsWithoutAnnotations() {
        return documentRepository.findDocumentsWithoutAnnotations()
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Validate uploaded file
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid filename");
        }
        
        String fileExtension = getFileExtension(originalFilename).toLowerCase();
        if (!fileExtension.matches("txt|docx|pdf")) {
            throw new IllegalArgumentException("Unsupported file type. Only .txt, .docx, and .pdf files are supported.");
        }
        
        // Check file size (50MB limit is set in application.properties)
        if (file.getSize() > 50 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds 50MB limit");
        }
    }
    
    /**
     * Extract file extension from filename
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            throw new IllegalArgumentException("File must have an extension");
        }
        return filename.substring(lastDotIndex + 1);
    }
}
