package com.cleanlearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cleanlearn.entity.UserItem;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long> {

    List<UserItem> findByUserIdAndStatus(Long userId, String status);

    Boolean existsByUserIdAndRefId(Long userId, Long refId);

}