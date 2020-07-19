package org.ahmedgaber.Redditclone.controller;


import lombok.AllArgsConstructor;
import org.ahmedgaber.Redditclone.dto.CommentsDto;
import org.ahmedgaber.Redditclone.exceptions.SpringRedditException;
import org.ahmedgaber.Redditclone.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) throws SpringRedditException {

        commentService.save(commentsDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.status(OK).body(commentService.getAllCommentsForPost(postId));
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String userName){
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForUser(userName));
    }

}
