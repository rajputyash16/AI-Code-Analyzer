// class CodeReviewer {
//     constructor() {
//         this.initializeElements();
//         this.bindEvents();
//     }

//     initializeElements() {
//         this.codeInput = document.getElementById('codeInput');
//         this.analyzeBtn = document.getElementById('analyzeBtn');
//         this.resultsSection = document.getElementById('resultsSection');
//     }

//     bindEvents() {
//         this.analyzeBtn.addEventListener('click', () => this.analyzeCode());

//         this.codeInput.addEventListener('keydown', (e) => {
//             if (e.ctrlKey && e.key === 'Enter') {
//                 this.analyzeCode();
//             }
//         });
//     }

//     async analyzeCode() {
//         const code = this.codeInput.value.trim();

//         if (!code) {
//             this.showError("⚠️ Please enter some code");
//             return;
//         }

//         this.setLoading(true);

//         try {
//             const response = await fetch("http://localhost:8080/analyze", {
//                 method: "POST",
//                 headers: {
//                     "Content-Type": "application/json"
//                 },
//                 body: JSON.stringify({ code })
//             });

//             if (!response.ok) throw new Error();

//             const report = await response.json();

//             this.displayResults(report);

//         } catch (err) {
//             console.log(err);

//             // fallback (frontend analysis if backend fails)
//             const fakeReport = this.localAnalyze(code);
//             this.displayResults(fakeReport);

//         } finally {
//             this.setLoading(false);
//         }
//     }

//     setLoading(loading) {
//         this.analyzeBtn.disabled = loading;

//         if (loading) {
//             this.analyzeBtn.innerHTML = "⏳ Analyzing...";
//         } else {
//             this.analyzeBtn.innerHTML = "🚀 Analyze Code";
//         }
//     }

//     displayResults(report) {
//         document.getElementById('linesOfCode').textContent = report.lines;
//         document.getElementById('loopsCount').textContent = report.loops;
//         document.getElementById('timeComplexity').textContent = report.complexity;
//         document.getElementById('conditionalsCount').textContent = report.conditions;
//         document.getElementById('scoreValue').textContent = report.score;

//         this.updateSuggestions(report.suggestions);

//         document.getElementById('codePreview').textContent = report.code;

//         this.resultsSection.classList.remove('hidden');
//         this.resultsSection.classList.add('show');

//         if (window.Prism) Prism.highlightAll();
//     }

//     updateSuggestions(suggestions) {
//         const container = document.getElementById('suggestionsList');
//         container.innerHTML = "";

//         suggestions.forEach((s, i) => {
//             const div = document.createElement("div");
//             div.className = "suggestion-item";
//             div.innerHTML = `✔ ${s}`;
//             div.style.animationDelay = `${i * 0.1}s`;
//             container.appendChild(div);
//         });
//     }

//     showError(msg) {
//         alert(msg);
//     }

//     // 💀 fallback analyzer (VERY IMPORTANT)
//     localAnalyze(code) {
//         let lines = code.split("\n").length;

//         let loops = (code.match(/for|while/g) || []).length;
//         let conditions = (code.match(/if|else/g) || []).length;

//         let nested = code.indexOf("for") !== code.lastIndexOf("for");

//         let complexity = "O(1)";
//         if (loops === 1) complexity = "O(n)";
//         if (nested) complexity = "O(n²)";

//         let score = 100;
//         let suggestions = [];

//         if (nested) {
//             suggestions.push("Avoid nested loops");
//             score -= 20;
//         }

//         if (code.includes("System.out.println")) {
//             suggestions.push("Remove unnecessary print statements");
//             score -= 10;
//         }

//         if (suggestions.length === 0) {
//             suggestions.push("Code looks optimized");
//         }

//         return {
//             lines,
//             loops,
//             conditions,
//             complexity,
//             score,
//             suggestions,
//             code
//         };
//     }
// }

// document.addEventListener("DOMContentLoaded", () => {
//     new CodeReviewer();
// });

class CodeReviewer {
    constructor() {
        this.initializeElements();
        this.bindEvents();
    }

    initializeElements() {
        this.codeInput = document.getElementById('codeInput');
        this.analyzeBtn = document.getElementById('analyzeBtn');
        this.resultsSection = document.getElementById('resultsSection');
    }

    bindEvents() {
        this.analyzeBtn.addEventListener('click', () => this.analyzeCode());

        this.codeInput.addEventListener('keydown', (e) => {
            if (e.ctrlKey && e.key === 'Enter') {
                this.analyzeCode();
            }
        });
    }

    async analyzeCode() {
        const code = this.codeInput.value.trim();

        if (!code) {
            this.showError("⚠️ Please enter some code");
            return;
        }

        this.setLoading(true);

        try {
            const response = await fetch("http://localhost:8080/analyze", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ code })
            });

            if (!response.ok) throw new Error();

            const report = await response.json();

            this.displayResults(report);

        } catch (err) {

            console.log(err);

            // Frontend fallback analyzer
            const fakeReport = this.localAnalyze(code);

            this.displayResults(fakeReport);

        } finally {

            this.setLoading(false);

        }
    }

    setLoading(loading) {

        this.analyzeBtn.disabled = loading;

        if (loading) {
            this.analyzeBtn.innerHTML = "⏳ Analyzing...";
        } else {
            this.analyzeBtn.innerHTML = "🚀 Analyze Code";
        }
    }

    displayResults(report) {

        document.getElementById('linesOfCode').textContent = report.lines;

        document.getElementById('loopsCount').textContent = report.loops;

        document.getElementById('timeComplexity').textContent = report.complexity;

        document.getElementById('conditionalsCount').textContent = report.conditions;

        document.getElementById('scoreValue').textContent = report.score;

        this.updateSuggestions(report.suggestions);

        // Improved code preview
        document.getElementById('codePreview').textContent = report.code;

        this.resultsSection.classList.remove('hidden');

        this.resultsSection.classList.add('show');

        if (window.Prism) Prism.highlightAll();
    }

    updateSuggestions(suggestions) {

        const container = document.getElementById('suggestionsList');

        container.innerHTML = "";

        suggestions.forEach((s, i) => {

            const div = document.createElement("div");

            div.className = "suggestion-item";

            div.innerHTML = `✔ ${s}`;

            div.style.animationDelay = `${i * 0.1}s`;

            container.appendChild(div);

        });
    }

    showError(msg) {
        alert(msg);
    }

    // FRONTEND AI ANALYZER
    localAnalyze(code) {

        let lines = code.split("\n").length;

        let loops = (code.match(/for|while/g) || []).length;

        let conditions = (code.match(/if|else/g) || []).length;

        let nested = code.indexOf("for") !== code.lastIndexOf("for");

        let complexity = "O(1)";

        if (loops === 1) complexity = "O(n)";

        if (nested) complexity = "O(n²)";

        let score = 100;

        let suggestions = [];

        // IMPROVED CODE
        let improvedCode = code;

        // Replace print statements
        improvedCode = improvedCode.replaceAll(
            "System.out.println",
            "logger.info"
        );

        // Better variable names
        improvedCode = improvedCode.replaceAll(
            "String code",
            "String optimizedCode"
        );

        // Suggestions
        if (nested) {

            suggestions.push("Avoid nested loops");

            improvedCode += "\n\n// Optimized using better loop structure";

            score -= 20;
        }

        if (code.includes("System.out.println")) {

            suggestions.push("Remove unnecessary print statements");

            score -= 10;
        }

        if (code.includes("for")) {

            suggestions.push("Use enhanced for loop if possible");
        }

        if (code.includes("if")) {

            suggestions.push("Simplify conditional statements");
        }

        if (suggestions.length === 0) {

            suggestions.push("Code looks optimized");
        }

        return {

            lines,
            loops,
            conditions,
            complexity,
            score,
            suggestions,

            // SHOW IMPROVED CODE
            code: improvedCode
        };
    }
}

document.addEventListener("DOMContentLoaded", () => {
    new CodeReviewer();
});