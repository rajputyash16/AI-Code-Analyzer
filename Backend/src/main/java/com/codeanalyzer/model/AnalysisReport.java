package com.codeanalyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AnalysisReport {
    private int linesOfCode;
    private int loopsCount;
    private int nestedLoops;
    private int conditionalsCount;
    private String timeComplexity;
    private int qualityScore;
    private String code;
    private List<Suggestion> suggestions;

    // Default constructor
    public AnalysisReport() {}

    public AnalysisReport(int linesOfCode, int loopsCount, int nestedLoops, int conditionalsCount, 
                         String timeComplexity, int qualityScore, String code, List<Suggestion> suggestions) {
        this.linesOfCode = linesOfCode;
        this.loopsCount = loopsCount;
        this.nestedLoops = nestedLoops;
        this.conditionalsCount = conditionalsCount;
        this.timeComplexity = timeComplexity;
        this.qualityScore = qualityScore;
        this.code = code;
        this.suggestions = suggestions;
    }

    // Getters and Setters
    public int getLinesOfCode() { return linesOfCode; }
    public void setLinesOfCode(int linesOfCode) { this.linesOfCode = linesOfCode; }

    public int getLoopsCount() { return loopsCount; }
    public void setLoopsCount(int loopsCount) { this.loopsCount = loopsCount; }

    public int getNestedLoops() { return nestedLoops; }
    public void setNestedLoops(int nestedLoops) { this.nestedLoops = nestedLoops; }

    public int getConditionalsCount() { return conditionalsCount; }
    public void setConditionalsCount(int conditionalsCount) { this.conditionalsCount = conditionalsCount; }

    public String getTimeComplexity() { return timeComplexity; }
    public void setTimeComplexity(String timeComplexity) { this.timeComplexity = timeComplexity; }

    public int getQualityScore() { return qualityScore; }
    public void setQualityScore(int qualityScore) { this.qualityScore = qualityScore; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public List<Suggestion> getSuggestions() { return suggestions; }
    public void setSuggestions(List<Suggestion> suggestions) { this.suggestions = suggestions; }

    public static class Suggestion {
        private String title;
        private String description;
        @JsonProperty("priority")
        private String priority; // "critical", "warning", "info", "good"

        public Suggestion(String title, String description, String priority) {
            this.title = title;
            this.description = description;
            this.priority = priority;
        }

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
    }
}