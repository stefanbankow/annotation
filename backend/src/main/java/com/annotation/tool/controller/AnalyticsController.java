package com.annotation.tool.controller;

import com.annotation.tool.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * REST Controller for Analytics and Statistics
 * 
 * Handles HTTP requests for analytics operations including dashboard
 * statistics,
 * label usage analytics, and document concentration analysis
 * 
 * Requirement 9: Средства за показване на обобщени данни за най-често срещани
 * етикети,
 * сегменти от съдържанието с най-голяма концентрация на избран етикет и др.
 */
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    /**
     * Get comprehensive analytics data for the frontend dashboard
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAnalytics() {
        Map<String, Object> analytics = analyticsService.getComprehensiveAnalytics();
        return ResponseEntity.ok(analytics);
    }

    /**
     * Get dashboard statistics summary
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStatistics() {
        Map<String, Object> statistics = analyticsService.getDashboardStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get most frequently used labels
     * Requirement 9: най-често срещани етикети
     */
    @GetMapping("/labels/most-frequent")
    public ResponseEntity<List<Map<String, Object>>> getMostFrequentLabels(
            @RequestParam(defaultValue = "10") int limit) {
        List<Map<String, Object>> statistics = analyticsService.getMostFrequentLabels(limit);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get segments with highest concentration of a specific label
     * Requirement 9: сегменти от съдържанието с най-голяма концентрация на избран
     * етикет
     */
    @GetMapping("/labels/{labelId}/concentration")
    public ResponseEntity<List<Map<String, Object>>> getHighestLabelConcentrationSegments(
            @PathVariable UUID labelId,
            @RequestParam(defaultValue = "5") int limit) {
        List<Map<String, Object>> statistics = analyticsService.getHighestLabelConcentrationSegments(labelId, limit);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get label relationship statistics
     */
    @GetMapping("/relationships")
    public ResponseEntity<Map<String, Object>> getLabelRelationshipStatistics() {
        Map<String, Object> statistics = analyticsService.getLabelRelationshipStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get document annotation statistics
     */
    @GetMapping("/documents/annotation-stats")
    public ResponseEntity<List<Map<String, Object>>> getDocumentAnnotationStatistics(
            @RequestParam(defaultValue = "10") int limit) {
        List<Map<String, Object>> statistics = analyticsService.getDocumentAnnotationStatistics(limit);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get unused labels for cleanup purposes
     */
    @GetMapping("/labels/unused")
    public ResponseEntity<List<Map<String, Object>>> getUnusedLabels() {
        return ResponseEntity.ok(List.of(Map.of("unusedLabels", analyticsService.getUnusedLabels())));
    }

    /**
     * Get documents without annotations
     */
    @GetMapping("/documents/without-annotations")
    public ResponseEntity<List<Map<String, Object>>> getDocumentsWithoutAnnotations() {
        List<Map<String, Object>> statistics = analyticsService.getDocumentsWithoutAnnotations();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get label hierarchy statistics
     */
    @GetMapping("/labels/hierarchy")
    public ResponseEntity<Map<String, Object>> getLabelHierarchyStatistics() {
        Map<String, Object> statistics = analyticsService.getLabelHierarchyStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get annotation trends over time
     */
    @GetMapping("/trends")
    public ResponseEntity<Map<String, Object>> getAnnotationTrends() {
        Map<String, Object> trends = analyticsService.getAnnotationTrends();
        return ResponseEntity.ok(trends);
    }
}
