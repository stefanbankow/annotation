package com.annotation.tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Document Annotation Tool
 * 
 * This application provides a web-based document annotation system that allows users to:
 * - Upload and annotate documents in .txt, .docx, and .pdf formats
 * - Create and manage hierarchical labels with custom colors
 * - Establish relationships between labels
 * - Generate analytics and visualizations of annotated content
 */
@SpringBootApplication
public class AnnotationToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnnotationToolApplication.class, args);
    }
}
