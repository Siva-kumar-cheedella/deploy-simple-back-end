package com.cleanlearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cleanlearn.entity.Problem;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    @Query("SELECT p FROM Problem p WHERE p.sheetId = :sheetId")
    List<Problem> getProblemsBySheetId(@Param("sheetId") Long sheetId);

    Boolean existsBySheetId(Long sheetId);

}