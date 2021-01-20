package com.antra.evaluation.reporting_system.repo;

import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExcelRepository extends JpaRepository<ExcelFile, String> {

    @Query(value = "select * from excel where id = ?1", nativeQuery = true)
    Optional<ExcelFile> findById(String id);

    ExcelFile deleteExcelFileByFileId(String id);
}
