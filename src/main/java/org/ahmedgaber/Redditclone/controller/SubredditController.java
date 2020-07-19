package org.ahmedgaber.Redditclone.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ahmedgaber.Redditclone.dto.SubredditDto;
import org.ahmedgaber.Redditclone.exceptions.SpringRedditException;
import org.ahmedgaber.Redditclone.service.SubredditService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subredditService.save(subredditDto));
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddits() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subredditService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id) throws SpringRedditException {
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getSubreddit(id));
    }
}
