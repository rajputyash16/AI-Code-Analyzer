package com.codeanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class CodeAnalyzerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CodeAnalyzerApplication.class, args);
        System.out.println("             
        🚀 AI Code Reviewer Backend Started!
        🌐 Frontend: http://localhost:8080/frontend/index.html
        🔧 API: http://localhost:8080/api/analyze
        📊 Ready for code analysis...
                
        ");
    }
}