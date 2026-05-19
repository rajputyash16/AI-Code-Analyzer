package com.codeanalyzer.controller;

import com.codeanalyzer.model.AnalysisReport;
import com.codeanalyzer.model.CodeRequest;
import com.codeanalyzer.service.CodeAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.CorsConfigurationSource;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080"})
public class CodeController {
    
    @Autowired
    private CodeAnalysisService codeAnalysisService;
    
    @PostMapping("/analyze")
    public ResponseEntity<AnalysisReport> analyzeCode(@RequestBody CodeRequest request) {
        try {
            AnalysisReport report = codeAnalysisService.analyzeCode(request);
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("AI Code Reviewer Backend is running! 🚀");
    }
}