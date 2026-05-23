package com.cleanlearn.service;

import com.cleanlearn.entity.UserItem;
import com.cleanlearn.repository.UserItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserItemService {

    private final UserItemRepository repository;

    public List<UserItem> getItems(Long userId) {

        return repository.findByUserId(userId);
    }
}