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
}