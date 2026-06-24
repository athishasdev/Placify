package com.placify.repository;

import com.placify.model.AnalysisReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnalysisReportRepository extends JpaRepository<AnalysisReport, Long> {
    List<AnalysisReport> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT ar.missingSkills FROM AnalysisReport ar WHERE ar.missingSkills IS NOT NULL")
    List<String> findAllMissingSkills();
}
