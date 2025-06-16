package com.annotation.tool.service;

import com.annotation.tool.dto.LabelDTO;
import com.annotation.tool.repository.AnnotationRepository;
import com.annotation.tool.repository.DocumentRepository;
import com.annotation.tool.repository.LabelRelationshipRepository;
import com.annotation.tool.repository.LabelRepository;
import com.annotation.tool.util.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for analytics and statistics
 * 
 * Provides comprehensive analytics for labels, annotations, documents, and relationships
 * Requirement 9: Средства за показване на обобщени данни за най-често срещани етикети, 
 * сегменти от съдържанието с най-голяма концентрация на избран етикет и др.
 */
@Service
@Transactional(readOnly = true)
public class AnalyticsService {
    
    @Autowired
    private LabelRepository labelRepository;
    
    @Autowired
    private AnnotationRepository annotationRepository;
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private LabelRelationshipRepository relationshipRepository;
    
    @Autowired
    private DTOMapper dtoMapper;
    
    /**
     * Get dashboard statistics summary
     */
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Basic counts
        stats.put("totalLabels", labelRepository.count());
        stats.put("totalDocuments", documentRepository.count());
        stats.put("totalAnnotations", annotationRepository.getTotalAnnotationCount());
        stats.put("totalRelationships", relationshipRepository.countTotalRelationships());
        
        // Most used labels
        List<Object[]> labelUsage = labelRepository.findLabelUsageStatistics();
        List<Map<String, Object>> mostUsedLabels = labelUsage.stream()
                .limit(5)
                .map(result -> {
                    Map<String, Object> labelStat = new HashMap<>();
                    labelStat.put("label", dtoMapper.toDTO((com.annotation.tool.entity.Label) result[0]));
                    labelStat.put("usageCount", result[1]);
                    return labelStat;
                })
                .collect(Collectors.toList());
        stats.put("mostUsedLabels", mostUsedLabels);
        
        // Document statistics by file type
        List<Object[]> documentStats = documentRepository.getDocumentStatsByFileType();
        Map<String, Long> documentsByType = documentStats.stream()
                .collect(Collectors.toMap(
                    result -> (String) result[0],
                    result -> (Long) result[1]
                ));
        stats.put("documentsByType", documentsByType);
        
        // Annotation distribution
        List<Object[]> annotationsByLabel = annotationRepository.countAnnotationsByLabel();
        List<Map<String, Object>> annotationDistribution = annotationsByLabel.stream()
                .limit(10)
                .map(result -> {
                    Map<String, Object> annoStat = new HashMap<>();
                    annoStat.put("label", dtoMapper.toDTO((com.annotation.tool.entity.Label) result[0]));
                    annoStat.put("count", result[1]);
                    return annoStat;
                })
                .collect(Collectors.toList());
        stats.put("annotationDistribution", annotationDistribution);
        
        return stats;
    }
    
    /**
     * Get most frequently used labels
     * Requirement 9: най-често срещани етикети
     */
    public List<Map<String, Object>> getMostFrequentLabels(int limit) {
        List<Object[]> labelUsage = labelRepository.findLabelUsageStatistics();
        
        return labelUsage.stream()
                .limit(limit)
                .map(result -> {
                    Map<String, Object> labelStat = new HashMap<>();
                    labelStat.put("label", dtoMapper.toDTO((com.annotation.tool.entity.Label) result[0]));
                    labelStat.put("usageCount", result[1]);
                    labelStat.put("percentage", calculatePercentage((Long) result[1], annotationRepository.getTotalAnnotationCount()));
                    return labelStat;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Get segments with highest concentration of a specific label
     * Requirement 9: сегменти от съдържанието с най-голяма концентрация на избран етикет
     */
    public List<Map<String, Object>> getHighestLabelConcentrationSegments(UUID labelId, int limit) {
        List<Object[]> concentrationData = annotationRepository.findDocumentsWithHighestLabelConcentration(labelId, 100);
        
        return concentrationData.stream()
                .limit(limit)
                .map(result -> {
                    Map<String, Object> concentrationStat = new HashMap<>();
                    concentrationStat.put("document", dtoMapper.toDTO((com.annotation.tool.entity.Document) result[0]));
                    concentrationStat.put("concentration", result[1]);
                    return concentrationStat;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Get label relationship statistics
     */
    public Map<String, Object> getLabelRelationshipStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Total relationships
        stats.put("totalRelationships", relationshipRepository.countTotalRelationships());
        
        // Labels with most outgoing relationships
        List<Object[]> outgoingRelationships = relationshipRepository.findLabelsWithMostOutgoingRelationships();
        List<Map<String, Object>> mostConnectedSources = outgoingRelationships.stream()
                .limit(5)
                .map(result -> {
                    Map<String, Object> relationshipStat = new HashMap<>();
                    relationshipStat.put("label", dtoMapper.toDTO((com.annotation.tool.entity.Label) result[0]));
                    relationshipStat.put("outgoingCount", result[1]);
                    return relationshipStat;
                })
                .collect(Collectors.toList());
        stats.put("mostConnectedSources", mostConnectedSources);
        
        // Labels with most incoming relationships
        List<Object[]> incomingRelationships = relationshipRepository.findLabelsWithMostIncomingRelationships();
        List<Map<String, Object>> mostConnectedTargets = incomingRelationships.stream()
                .limit(5)
                .map(result -> {
                    Map<String, Object> relationshipStat = new HashMap<>();
                    relationshipStat.put("label", dtoMapper.toDTO((com.annotation.tool.entity.Label) result[0]));
                    relationshipStat.put("incomingCount", result[1]);
                    return relationshipStat;
                })
                .collect(Collectors.toList());
        stats.put("mostConnectedTargets", mostConnectedTargets);
        
        return stats;
    }
    
    /**
     * Get document annotation statistics
     */
    public List<Map<String, Object>> getDocumentAnnotationStatistics(int limit) {
        List<Object[]> documentAnnotations = documentRepository.findDocumentsWithAnnotationCount();
        
        return documentAnnotations.stream()
                .limit(limit)
                .map(result -> {
                    Map<String, Object> docStat = new HashMap<>();
                    docStat.put("document", dtoMapper.toDTO((com.annotation.tool.entity.Document) result[0]));
                    docStat.put("annotationCount", result[1]);
                    return docStat;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Get unused labels for cleanup purposes
     */
    public List<LabelDTO> getUnusedLabels() {
        return labelRepository.findUnusedLabels()
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get documents without annotations
     */
    public List<Map<String, Object>> getDocumentsWithoutAnnotations() {
        return documentRepository.findDocumentsWithoutAnnotations()
                .stream()
                .map(document -> {
                    Map<String, Object> docInfo = new HashMap<>();
                    docInfo.put("document", dtoMapper.toDTO(document));
                    docInfo.put("uploadDate", document.getUploadDate());
                    docInfo.put("fileSize", document.getFileSize());
                    return docInfo;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Get label hierarchy statistics
     */
    public Map<String, Object> getLabelHierarchyStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<com.annotation.tool.entity.Label> allLabels = labelRepository.findAll();
        List<com.annotation.tool.entity.Label> rootLabels = labelRepository.findByParentIsNull();
        
        stats.put("totalLabels", allLabels.size());
        stats.put("rootLabels", rootLabels.size());
        stats.put("childLabels", allLabels.size() - rootLabels.size());
        
        // Calculate average depth
        double averageDepth = rootLabels.stream()
                .mapToInt(this::calculateLabelDepth)
                .average()
                .orElse(0.0);
        stats.put("averageHierarchyDepth", averageDepth);
        
        // Find deepest hierarchy
        int maxDepth = rootLabels.stream()
                .mapToInt(this::calculateLabelDepth)
                .max()
                .orElse(0);
        stats.put("maxHierarchyDepth", maxDepth);
        
        return stats;
    }
    
    /**
     * Get annotation trends over time
     */
    public Map<String, Object> getAnnotationTrends() {
        Map<String, Object> trends = new HashMap<>();
        
        // This would typically involve more complex time-based queries
        // For now, providing basic trend information
        Long totalAnnotations = annotationRepository.getTotalAnnotationCount();
        Long totalDocuments = documentRepository.count();
        
        double averageAnnotationsPerDocument = totalDocuments > 0 ? 
                (double) totalAnnotations / totalDocuments : 0.0;
        
        trends.put("totalAnnotations", totalAnnotations);
        trends.put("totalDocuments", totalDocuments);
        trends.put("averageAnnotationsPerDocument", averageAnnotationsPerDocument);
        
        return trends;
    }
    
    /**
     * Calculate percentage
     */
    private double calculatePercentage(Long count, Long total) {
        if (total == null || total == 0) {
            return 0.0;
        }
        return (count.doubleValue() / total.doubleValue()) * 100.0;
    }
    
    /**
     * Calculate depth of label hierarchy recursively
     */
    private int calculateLabelDepth(com.annotation.tool.entity.Label label) {
        if (label.getChildren().isEmpty()) {
            return 1;
        }
        
        return 1 + label.getChildren().stream()
                .mapToInt(this::calculateLabelDepth)
                .max()
                .orElse(0);
    }
    
    /**
     * Get comprehensive analytics data for the frontend dashboard
     */
    public Map<String, Object> getComprehensiveAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        // Basic statistics
        analytics.put("totalDocuments", documentRepository.count());
        analytics.put("totalAnnotations", annotationRepository.count());
        analytics.put("totalLabels", labelRepository.count());
        analytics.put("totalLabelRelationships", relationshipRepository.count());
        
        // Most used labels
        List<Map<String, Object>> mostUsedLabels = getMostFrequentLabels(10);
        analytics.put("mostUsedLabels", mostUsedLabels);
        
        // Label distribution (same as most used labels but formatted differently)
        List<Map<String, Object>> labelDistribution = mostUsedLabels.stream()
            .map(labelData -> {
                Map<String, Object> distribution = new HashMap<>();
                LabelDTO label = (LabelDTO) labelData.get("label");
                distribution.put("labelName", label.getName());
                distribution.put("count", labelData.get("usageCount"));
                distribution.put("color", label.getColor());
                return distribution;
            })
            .collect(Collectors.toList());
        analytics.put("labelDistribution", labelDistribution);
        
        // Annotation trends (placeholder - could be implemented based on creation dates)
        List<Map<String, Object>> annotationTrends = new ArrayList<>();
        analytics.put("annotationTrends", annotationTrends);
        
        // Recent activity (placeholder)
        List<Map<String, Object>> recentActivity = new ArrayList<>();
        analytics.put("recentActivity", recentActivity);
        
        return analytics;
    }
}
