package com.cleanlearn.service;

import com.cleanlearn.entity.UserItem;
import com.cleanlearn.repository.UserItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import com.cleanlearn.entity.Problem;
import com.cleanlearn.repository.ProblemRepository;
import com.cleanlearn.entity.User;

@Service
@RequiredArgsConstructor
public class UserItemService {

    private final UserItemRepository userItemRepository;
    private final UserService userService;
    private final ProblemRepository problemRepository;

    public List<UserItem> getItems(Long userId) {
        return userItemRepository.findByUserIdAndStatus(userId, "A");
    }

    public List<UserItem> addDsaSheetToUser(Long userId, Long sheetId) throws Exception {

        User user = userService.validateUserById(userId);

        Boolean sheetExists = problemRepository.existsBySheetId(sheetId);

        if (!sheetExists) {
            throw new Exception("DSA sheet not found with id: " + sheetId);
        }

        List<Problem> problems = problemRepository.getProblemsBySheetId(sheetId);
        
        Boolean alreadyAdded = userItemRepository.existsByUserIdAndRefId(userId, problems.get(0).getProblemId());

        if (alreadyAdded) {
            throw new Exception("DSA sheet with id: " + sheetId + " is already added to user with id: " + userId);
        }

        for (Problem problem : problems) {
            UserItem userItem = new UserItem();
            userItem.setUserId(userId);
            userItem.setRefId(problem.getProblemId());
            userItem.setItemLink(problem.getProblemLink());
            userItem.setItemType("DSA");
            userItem.setCreatedBy(user.getName());
            userItem.setUpdatedBy(user.getName());
            userItemRepository.save(userItem);
        }

        return getItems(userId);

    }
}