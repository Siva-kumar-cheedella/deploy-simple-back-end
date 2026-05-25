package com.cleanlearn.service.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.cleanlearn.dto.GroqDto.*;

import com.cleanlearn.dto.UserItemProblemDto;
import com.cleanlearn.service.UserItemService;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cleanlearn.dto.GroqResponseParser.*;
import com.cleanlearn.util.Prompts;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroqService {

    private final RestClient restClient;
    private final UserItemService userItemService;

    public StudyPlanResponse askGroq() {
        String refinedPrompt = refinePrompt(Prompts.GROQ_SYSTEM_PROMPT);
        GroqRequest request = new GroqRequest(
                "llama-3.1-8b-instant",
                List.of(new Message("user", refinedPrompt)),
                new ResponseFormat("json_object")
        );

        GroqResponse response = restClient.post()
                .uri("/chat/completions")
                .body(request)
                .retrieve()
                .body(GroqResponse.class);
        
        log.error("Groq response: {}", response);

        String content = response.choices().get(0).message().content();

        ObjectMapper mapper = new ObjectMapper();
        
        try {
            // Deserializing JSON directly into the Record
            StudyPlanResponse studyPlanResponse = mapper.readValue(content, StudyPlanResponse.class);
            
            // Accessing data (Records use method names identical to the field names, no "get" prefix)
            log.error("Time: " + studyPlanResponse.todayFocus().estimatedTotalTimeMinutes());
            log.error("First Problem: " + studyPlanResponse.recommendedProblems().get(0).problemName());
            log.error("Primary Goal: " + studyPlanResponse.todayFocus().primaryGoal());

            return studyPlanResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    private String refinePrompt(String prompt) {
        
        //fetch the poblems that are solved in the last 7 days and append to the         
        prompt+= "\n\nHere are some of the problems I have solved recently:\n";
        for (var item : userItemService.getUserItemsForGroq(2L)) {
            prompt += "\n\nRecently Solved Problem: " + item.getProblem().getProblemDesc() + 
                        "\nLink: " + item.getProblem().getProblemLink() + 
                        "\nDifficulty: " + item.getProblem().getLevel();
        }

        // fetch the pending problesm from the database and append to the promptproblem
        prompt += "\n\nHere are some of the problems I have pending:\n";
        for (var item : userItemService.getUserItemsForGroq(2L)) {
            prompt += "\n\nPending Problem: " + item.getProblem().getProblemDesc() +
                        "\nProblemId: " + item.getProblem().getProblemId() +
                      "\nLink: " + item.getProblem().getProblemLink() +
                      "\nDifficulty: " + item.getProblem().getLevel();
        }

        prompt += "\n\nBased on the above information, suggest me the next best problems to solve from Striver's Blind 75 sheet and also, the problem should must be selected only from the pending problems and should must include the problemId in the response so that I can track it in my database \n*****DO NEVER MAP PROBLEM IDS TO PROBLEM NAMES INCORRECTLY, I SAY NEVER MEANS NEVER, DOUBLE CHECK AND RESPOND*****.";

        return prompt;
    }
}