package com.cleanlearn.service;

import com.cleanlearn.entity.UserItem;
import com.cleanlearn.repository.UserItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import com.cleanlearn.entity.Problem;
import com.cleanlearn.repository.ProblemRepository;
import com.cleanlearn.entity.User;
import com.cleanlearn.repository.UserRepository;
import com.cleanlearn.dto.UserItemProblemDto;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import com.cleanlearn.dto.MarkAsDoneDto;
import com.cleanlearn.util.constants;
import com.cleanlearn.service.NotesService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserItemService {

    private final UserItemRepository userItemRepository;
    private final UserService userService;
    private final ProblemRepository problemRepository;
    private final NotesService notesService;

    public List<UserItemProblemDto> getItems(Long userId) {
        return userItemRepository.findByUserIdWithStatus(userId);
    }

    public List<UserItemProblemDto> addDsaSheetToUser(Long userId, Long sheetId) throws Exception {

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

    public List<UserItemProblemDto> markAsDone(MarkAsDoneDto markAsDoneDto) throws Exception {
        
        userService.validateUserById(markAsDoneDto.getUserId());

        UserItem userItem = userItemRepository.findByUserIdAndItemId(markAsDoneDto.getUserId(), markAsDoneDto.getItemId())
            .orElseThrow(() -> new Exception("User item not found with id: " + markAsDoneDto.getItemId() + " for user with id: " + markAsDoneDto.getUserId()));

        
        if(markAsDoneDto.getNotes() == null || markAsDoneDto.getNotes().isEmpty()) {
            throw new Exception("Notes cannot be empty when marking an item as done");
        }
        
        log.debug("Marking item with id: {} as done for user with id: {}", markAsDoneDto.getItemId(), markAsDoneDto.getUserId());

        switch (markAsDoneDto.getRevision()) {
            case constants.FIRST_ATTEMPT:
                markFirstAttemptAsDone(userItem, markAsDoneDto.getUserName(), markAsDoneDto.getNotes());
                break;
            case constants.REVISION_1:
                markR1AsDone(userItem, markAsDoneDto.getUserName(), markAsDoneDto.getNotes());
                break;
            case constants.REVISION_2:
                markR2AsDone(userItem, markAsDoneDto.getUserName(), markAsDoneDto.getNotes());
                break;
            default:
                throw new Exception("Invalid revision type: " + markAsDoneDto.getRevision());
        }

        return getItems(markAsDoneDto.getUserId());

    }

    private void markFirstAttemptAsDone(UserItem userItem, String userName, String notes) throws Exception {
        
        Long noteId = notesService.saveNotes(notes, userItem.getUserId(), userName);

        userItem.setFaDate(LocalDateTime.now());
        userItem.setFaStatus(constants.COMPLETED_STATUS);
        userItem.setFaNoteId(noteId);

        userItem.setUpdatedBy(userName);
        userItem.setUpdatedDate(LocalDateTime.now());

        userItem.setR1Date(LocalDateTime.now().plusDays(3)); 

        userItemRepository.save(userItem);
    }

    private void markR1AsDone(UserItem userItem, String userName, String notes) throws Exception {
        
        Long noteId = notesService.saveNotes(notes, userItem.getUserId(), userName);

        userItem.setR1Date(LocalDateTime.now());
        userItem.setR1Status(constants.COMPLETED_STATUS);
        userItem.setR1NoteId(noteId);

        userItem.setUpdatedBy(userName);
        userItem.setUpdatedDate(LocalDateTime.now());

        userItem.setR2Date(LocalDateTime.now().plusDays(7));

        userItemRepository.save(userItem);
    }

    private void markR2AsDone(UserItem userItem, String userName, String notes) throws Exception {
        
        Long noteId = notesService.saveNotes(notes, userItem.getUserId(), userName);

        userItem.setR2Date(LocalDateTime.now());
        userItem.setR2Status(constants.COMPLETED_STATUS);
        userItem.setR2NoteId(noteId);

        userItem.setUpdatedBy(userName);
        userItem.setUpdatedDate(LocalDateTime.now());

        userItemRepository.save(userItem);
    }

    // ========================REPORT RELATED METHODS - IGNORE =========================
    public List<UserItemProblemDto> getRecentUserItems(Long userId) {
        return userItemRepository.fetchRecentUserItems(userId, PageRequest.of(0, 10));
    }

    public List<UserItemProblemDto> getUnattemptedProblems(Long userId) {
        return userItemRepository.fetchUnattemptedProblems(userId);
    }

    public List<String> getPendingRevisionsForToday(Long userId) {
        return userItemRepository.fetchPendingProblemsForToday(userId);
    }

    public void updateFaDateForProblem(Long userId, Long refId) {
        userItemRepository.findByUserIdAndRefId(userId, refId).ifPresent(userItem -> {
            
            userItem.setFaDate(LocalDateTime.now());
            userItem.setUpdatedDate(LocalDateTime.now());
            userItem.setFaStatus(constants.PENDING_STATUS);
            userItem.setUpdatedBy("SYSTEM");

            userItemRepository.save(userItem);
        });
    }
}