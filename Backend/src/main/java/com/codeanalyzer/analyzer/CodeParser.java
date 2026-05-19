package com.codeanalyzer.analyzer;

import com.codeanalyzer.model.AnalysisReport;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CodeParser {
    
    // Regex patterns for different programming languages
    private static final Map<String, Map<String, String>> LANGUAGE_PATTERNS = Map.of(
        "java", Map.of(
            "for", "\\bfor\\s*\\$",
            "while", "\\bwhile\\s*\\$",
            "if", "\\bif\\s*\\$",
            "else", "\\belse\\s*(?:if\\s*\\$)?",
            "nested_loop", "(?s)(for|while).*?(for|while)"
        ),
        "python", Map.of(
            "for", "\\bfor\\s+\\w+\\s+in\\s+",
            "while", "\\bwhile\\s+",
            "if", "\\bif\\s+",
            "else", "\\belse\\s*:",
            "nested_loop", "(?s)(for|while).*?(for|while)"
        ),
        "javascript", Map.of(
            "for", "\\bfor\\s*\\$",
            "while", "\\bwhile\\s*\\$",
            "if", "\\bif\\s*\\$",
            "else", "\\belse\\s*(?:if\\s*\\$)?",
            "nested_loop", "(?s)(for|while).*?(for|while)"
        ),
        "cpp", Map.of(
            "for", "\\bfor\\s*\\$",
            "while", "\\bwhile\\s*\\$",
            "if", "\\bif\\s*\\$",
            "else", "\\belse\\s*(?:if\\s*\\$)?",
            "nested_loop", "(?s)(for|while).*?(for|while)"
        )
    );

    public AnalysisReport analyzeCode(String code) {
        String normalizedCode = code.toLowerCase();
        String detectedLanguage = detectLanguage(normalizedCode);
        
        Map<String, String> patterns = LANGUAGE_PATTERNS.getOrDefault(detectedLanguage, LANGUAGE_PATTERNS.get("java"));
        
        // Count lines of code
        int linesOfCode = code.lines().count();
        
        // Parse code structure
        ParseResult result = parseCodeStructure(code, patterns);
        
        // Calculate quality score
        int qualityScore = calculateQualityScore(linesOfCode, result);
        
        // Generate suggestions
        List<AnalysisReport.Suggestion> suggestions = generateSuggestions(result, linesOfCode);
        
        // Determine time complexity
        String timeComplexity = determineTimeComplexity(result.nestedLoops, result.loopsCount);
        
        return new AnalysisReport(
            linesOfCode,
            result.loopsCount,
            result.nestedLoops,
            result.conditionalsCount,
            timeComplexity,
            qualityScore,
            code,
            suggestions
        );
    }

    private String detectLanguage(String code) {
        if (code.contains("public class") || code.contains("System.out")) return "java";
        if (code.contains("def ") || code.contains("print(")) return "python";
        if (code.contains("function ") || code.contains("console.log")) return "javascript";
        if (code.contains("#include") || code.contains("cout")) return "cpp";
        return "java"; // default
    }

    private ParseResult parseCodeStructure(String code, Map<String, String> patterns) {
        ParseResult result = new ParseResult();
        
        // Count loops
        result.loopsCount = countMatches(code, patterns.get("for")) + 
                           countMatches(code, patterns.get("while"));
        
        // Count nested loops
        result.nestedLoops = countMatches(code, patterns.get("nested_loop"));
        
        // Count conditionals
        result.conditionalsCount = countMatches(code, patterns.get("if")) + 
                                  countMatches(code, patterns.get("else"));
        
        return result;
    }

    private int countMatches(String text, String regex) {
        if (regex == null) return 0;
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return (int) matcher.results().count();
    }

    private int calculateQualityScore(int linesOfCode, ParseResult result) {
        int baseScore = 90;
        
        // Penalties
        if (linesOfCode > 500) baseScore -= 15;
        else if (linesOfCode > 200) baseScore -= 10;
        
        if (result.nestedLoops > 2) baseScore -= 20;
        else if (result.nestedLoops > 0) baseScore -= 10;
        
        if (result.loopsCount > 10) baseScore -= 15;
        
        // Bonus for good practices
        if (linesOfCode < 50 && result.loopsCount < 3) baseScore += 5;
        
        return Math.max(40, Math.min(100, baseScore));
    }

    private List<AnalysisReport.Suggestion> generateSuggestions(ParseResult result, int linesOfCode) {
        List<AnalysisReport.Suggestion> suggestions = new ArrayList<>();
        
        if (result.nestedLoops > 0) {
            suggestions.add(new AnalysisReport.Suggestion(
                "Avoid Nested Loops",
                "Nested loops create O(n²) complexity. Consider using hash maps or other optimized data structures.",
                "critical"
            ));
        }
        
        if (result.loopsCount > 5) {
            suggestions.add(new AnalysisReport.Suggestion(
                "Too Many Loops",
                "Consider combining loops or using functional programming approaches to reduce loop count.",
                "warning"
            ));
        }
        
        if (linesOfCode > 200) {
            suggestions.add(new AnalysisReport.Suggestion(
                "Long Codebase",
                "Consider breaking this into smaller, focused functions or classes for better maintainability.",
                "warning"
            ));
        }
        
        if (result.conditionalsCount > 10) {
            suggestions.add(new AnalysisReport.Suggestion(
                "Complex Conditional Logic",
                "Too many if/else statements. Consider using polymorphism or strategy pattern.",
                "warning"
            ));
        }
        
        // Always add positive feedback
        suggestions.add(new AnalysisReport.Suggestion(
            "Good Structure",
            "Code appears well-formatted. Continue maintaining consistent indentation and structure.",
            "good"
        ));
        
        if (result.loopsCount == 0) {
            suggestions.add(new AnalysisReport.Suggestion(
                "Efficient Algorithm",
                "No loops detected - likely O(1) or O(n) complexity. Excellent performance!",
                "good"
            ));
        }
        
        return suggestions;
    }

    private String determineTimeComplexity(int nestedLoops, int loopsCount) {
        if (nestedLoops > 0) return "O(n²)";
        if (loopsCount > 0) return "O(n)";
        return "O(1)";
    }

    private static class ParseResult {
        int loopsCount = 0;
        int nestedLoops = 0;
        int conditionalsCount = 0;
    }
}