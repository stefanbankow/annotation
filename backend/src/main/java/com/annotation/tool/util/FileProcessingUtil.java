package com.annotation.tool.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Utility class for processing different file formats
 * 
 * Extracts text content from .txt, .docx, and .pdf files
 */
@Component
public class FileProcessingUtil {
    
    /**
     * Extract text content from a file based on its type
     */
    public String extractTextContent(String filePath, String fileType) {
        try {
            switch (fileType.toLowerCase()) {
                case "txt":
                    return extractTextFromTxt(filePath);
                case "docx":
                    return extractTextFromDocx(filePath);
                case "pdf":
                    return extractTextFromPdf(filePath);
                default:
                    throw new IllegalArgumentException("Unsupported file type: " + fileType);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract text from file: " + e.getMessage(), e);
        }
    }
    
    /**
     * Extract text from .txt file
     */
    private String extractTextFromTxt(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path);
    }
    
    /**
     * Extract text from .docx file using Apache POI
     */
    private String extractTextFromDocx(String filePath) throws IOException {
        StringBuilder text = new StringBuilder();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {
            
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                text.append(paragraph.getText()).append("\n");
            }
        }
        
        return text.toString();
    }
    
    /**
     * Extract text from .pdf file using Apache PDFBox
     */
    private String extractTextFromPdf(String filePath) throws IOException {
        try (PDDocument document = PDDocument.load(new java.io.File(filePath))) {
            PDFTextStripper textStripper = new PDFTextStripper();
            return textStripper.getText(document);
        }
    }
    
    /**
     * Get context around a specific position in text
     */
    public String getContext(String text, int position, int contextLength) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        
        int start = Math.max(0, position - contextLength);
        int end = Math.min(text.length(), position + contextLength);
        
        return text.substring(start, end);
    }
    
    /**
     * Get context before a specific position
     */
    public String getContextBefore(String text, int position, int contextLength) {
        if (text == null || text.isEmpty() || position <= 0) {
            return "";
        }
        
        int start = Math.max(0, position - contextLength);
        return text.substring(start, position);
    }
    
    /**
     * Get context after a specific position
     */
    public String getContextAfter(String text, int position, int contextLength) {
        if (text == null || text.isEmpty() || position >= text.length()) {
            return "";
        }
        
        int end = Math.min(text.length(), position + contextLength);
        return text.substring(position, end);
    }
    
    /**
     * Validate text positions
     */
    public boolean isValidPosition(String text, int startPosition, int endPosition) {
        if (text == null) {
            return false;
        }
        
        return startPosition >= 0 && 
               endPosition >= startPosition && 
               endPosition <= text.length();
    }
    
    /**
     * Extract text between positions
     */
    public String extractTextBetweenPositions(String text, int startPosition, int endPosition) {
        if (!isValidPosition(text, startPosition, endPosition)) {
            throw new IllegalArgumentException("Invalid text positions");
        }
        
        return text.substring(startPosition, endPosition);
    }
}
