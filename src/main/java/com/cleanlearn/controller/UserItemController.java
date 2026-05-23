package com.cleanlearn.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import com.cleanlearn.entity.Problem;
import com.cleanlearn.service.UserItemService;
import com.cleanlearn.entity.UserItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserItemController {

    private final UserItemService userItemService;
    
    @PostMapping("/user/{userId}/items/dsa/{sheetId}")
    public ResponseEntity<Object> addDsaSheetToUser(@PathVariable Long userId, @PathVariable Long sheetId) {
        
        try {
            List<UserItem> problems = userItemService.addDsaSheetToUser(userId, sheetId);
            return ResponseEntity.ok(problems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add DSA sheet to user: " + e.getMessage());
        }

    }

    @GetMapping("/user/items")
    public ResponseEntity<List<UserItem>> getItems(@RequestParam Long userId) {
        return ResponseEntity.ok(userItemService.getItems(userId));
    }

}