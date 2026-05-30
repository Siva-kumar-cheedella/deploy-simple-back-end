package com.cleanlearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cleanlearn.entity.UserItem;
import com.cleanlearn.dto.UserItemProblemDto;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long> {

    @Query("SELECT new com.cleanlearn.dto.UserItemProblemDto(ui, p) " +
        "FROM UserItem ui LEFT JOIN Problem p ON ui.refId = p.problemId " +
        "WHERE ui.userId = :userId")
    List<UserItemProblemDto> findByUserIdWithStatus(@Param("userId") Long userId);

    Boolean existsByUserIdAndRefId(Long userId, Long refId);

    Optional<UserItem> findByUserIdAndItemId(Long userId, Long itemId);

    @Query("SELECT new com.cleanlearn.dto.UserItemProblemDto(ui, p) " +
            "FROM UserItem ui LEFT JOIN Problem p ON ui.refId = p.problemId " +
            "WHERE ui.userId = :userId AND ui.itemType = 'DSA' AND ui.faDate IS NOT NULL ORDER BY ui.faDate DESC NULLS LAST")
    List<UserItemProblemDto> fetchRecentUserItems(@Param("userId") Long userId,Pageable pageable);

    @Query("SELECT new com.cleanlearn.dto.UserItemProblemDto(ui, p) " +
            "FROM UserItem ui LEFT JOIN Problem p ON ui.refId = p.problemId " +
            "WHERE ui.userId = :userId AND ui.itemType = 'DSA' AND ui.faDate IS NULL")
    List<UserItemProblemDto> fetchUnattemptedProblems(@Param("userId") Long userId);

    @Query("SELECT p.problemDesc " +
        "FROM UserItem ui JOIN Problem p ON ui.refId = p.problemId " +
        "WHERE ui.userId = :userId " +
        "AND ui.itemType = 'DSA' " +
        "AND ui.status='A' " +
        "AND (" +
        "    (ui.faDate <= CURRENT_DATE AND (ui.faStatus != 'COMPLETED' OR ui.faStatus IS NULL)) " +
        "    OR (ui.r1Date <= CURRENT_DATE AND (ui.r1Status != 'COMPLETED' OR ui.r1Status IS NULL)) " +
        "    OR (ui.r2Date <= CURRENT_DATE AND (ui.r2Status != 'COMPLETED' OR ui.r2Status IS NULL))" +
        ")")
    List<String> fetchPendingProblemsForToday(@Param("userId") Long userId);

    Optional<UserItem> findByUserIdAndRefId(Long userId, Long refId);

    @Query(
        "SELECT ui FROM UserItem ui WHERE ui.userId = :userId AND (" +
        "    (ui.faStatus = 'PENDING' AND ui.faDate < CURRENT_DATE) " +
        "    OR (ui.r1Status = 'PENDING' AND ui.r1Date < CURRENT_DATE) " +
        "    OR (ui.r2Status = 'PENDING' AND ui.r2Date < CURRENT_DATE) " +
        ")"
    )
    List<UserItem> fetchMissedDeadlineProblems(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE "userItem"
        SET
            "FA_date" = CASE
                WHEN "FA_Status" = 'PENDING'
                THEN "FA_date" + INTERVAL '1 day'
                ELSE "FA_date"
            END,
            "R1_date" = CASE
                WHEN "R1_Status" = 'PENDING'
                THEN "R1_date" + INTERVAL '1 day'
                ELSE "R1_date"
            END,
            "R2_date" = CASE
                WHEN "R2_Status" = 'PENDING'
                THEN "R2_date" + INTERVAL '1 day'
                ELSE "R2_date"
            END,
            "updatedBy"='SYSTEM',
            "updatedDate"=CURRENT_TIMESTAMP
        WHERE
            (
                "FA_Status" = 'PENDING'
                OR "R1_Status" = 'PENDING'
                OR "R2_Status" = 'PENDING'
            )
            AND "userId" = :userId
        """,
        nativeQuery = true)
    int updateDatesForUserItems(@Param("userId") Long userId);
}