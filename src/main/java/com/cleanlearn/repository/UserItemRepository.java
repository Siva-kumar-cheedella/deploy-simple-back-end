package com.cleanlearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cleanlearn.entity.UserItem;
import com.cleanlearn.dto.UserItemProblemDto;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long> {

    @Query("SELECT new com.cleanlearn.dto.UserItemProblemDto(ui, p) " +
        "FROM UserItem ui LEFT JOIN Problem p ON ui.refId = p.problemId " +
        "WHERE ui.userId = :userId")
    List<UserItemProblemDto> findByUserIdWithStatus(@Param("userId") Long userId);

    Boolean existsByUserIdAndRefId(Long userId, Long refId);

}