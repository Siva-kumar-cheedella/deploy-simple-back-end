package com.cleanlearn.dto;

import com.cleanlearn.entity.UserItem;
import com.cleanlearn.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor 
@NoArgsConstructor
public class UserItemProblemDto {
    private UserItem userItem;
    private Problem problem; // This will be null if the LEFT JOIN finds no match
}