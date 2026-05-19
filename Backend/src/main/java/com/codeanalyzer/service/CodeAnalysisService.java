package com.codeanalyzer.service;

import com.codeanalyzer.analyzer.CodeParser;
import com.codeanalyzer.model.AnalysisReport;
import com.codeanalyzer.model.CodeRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CodeAnalysisService {
    
    private final CodeParser codeParser;
    
    @Value("${app.analysis.max-code-length:10000}")
    private int maxCodeLength;
    
    public CodeAnalysisService(CodeParser codeParser) {
        this.codeParser = codeParser;
    }
    
    public AnalysisReport analyzeCode(CodeRequest request) {
        String code = request.getCode();
        
        // Validation
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Code cannot be empty");
        }
        
        if (code.length() > maxCodeLength) {
            throw new IllegalArgumentException("Code too long. Maximum " + maxCodeLength + " characters allowed");
        }
        
        // Analyze code
        return codeParser.analyzeCode(code);
    }
}