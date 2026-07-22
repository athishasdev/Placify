package com.placify.service;

import com.placify.model.Resume;
import com.placify.model.Skill;
import com.placify.model.User;
import com.placify.repository.ResumeRepository;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final SkillExtractionService skillExtractionService;

    @Value("${placify.upload.dir:uploads/resumes}")
    private String uploadDir;

    public ResumeService(ResumeRepository resumeRepository, SkillExtractionService skillExtractionService) {
        this.resumeRepository = resumeRepository;
        this.skillExtractionService = skillExtractionService;
    }

    public Resume uploadAndProcess(MultipartFile file, User user) throws IOException {
        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Sanitize original file name and prevent path traversal
        String rawOriginalName = file.getOriginalFilename();
        String safeName = "resume.pdf";
        if (rawOriginalName != null && !rawOriginalName.trim().isEmpty()) {
            safeName = Paths.get(rawOriginalName).getFileName().toString().replaceAll("[^a-zA-Z0-9._-]", "_");
        }
        
        String storedName = UUID.randomUUID() + "_" + safeName;
        Path filePath = uploadPath.resolve(storedName).normalize();
        if (!filePath.startsWith(uploadPath)) {
            throw new IllegalArgumentException("Path traversal attempted: Invalid file path");
        }

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Extract text from PDF
        String extractedText = extractTextFromPdf(filePath);

        // Detect skills from extracted text
        Set<Skill> detectedSkills = skillExtractionService.extractSkills(extractedText);

        // Build and save resume entity
        Resume resume = Resume.builder()
                .user(user)
                .fileName(safeName)
                .filePath(filePath.toString())
                .extractedText(extractedText)
                .detectedSkills(detectedSkills)
                .build();

        return resumeRepository.save(resume);
    }

    private String extractTextFromPdf(Path filePath) throws IOException {
        try (PDDocument document = Loader.loadPDF(filePath.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    public Resume getLatestResume(Long userId) {
        return resumeRepository.findTopByUserIdOrderByUploadedAtDesc(userId).orElse(null);
    }

    public Resume getResumeById(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
    }
}
