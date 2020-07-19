package org.ahmedgaber.Redditclone.service;


import lombok.AllArgsConstructor;
import org.ahmedgaber.Redditclone.dto.CommentsDto;
import org.ahmedgaber.Redditclone.exceptions.PostNotFoundException;
import org.ahmedgaber.Redditclone.exceptions.SpringRedditException;
import org.ahmedgaber.Redditclone.mapper.CommentMapper;
import org.ahmedgaber.Redditclone.model.Comment;
import org.ahmedgaber.Redditclone.model.NotificationEmail;
import org.ahmedgaber.Redditclone.model.Post;
import org.ahmedgaber.Redditclone.model.User;
import org.ahmedgaber.Redditclone.repository.CommentRepository;
import org.ahmedgaber.Redditclone.repository.PostRepository;
import org.ahmedgaber.Redditclone.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private CommentMapper commentMapper;
    private AuthService authService;
    private CommentRepository commentRepository;
    private MailContentBuilder mailContentBuilder;
    private MailService mailService;
    private static final String POST_URL = "";

    public void save(CommentsDto commentsDto) throws SpringRedditException {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());

        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());

    }

    private void sendCommentNotification(String message, User user) throws SpringRedditException {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }


    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found"));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }
}
