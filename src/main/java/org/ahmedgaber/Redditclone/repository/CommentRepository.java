package org.ahmedgaber.Redditclone.repository;

import org.ahmedgaber.Redditclone.model.Comment;
import org.ahmedgaber.Redditclone.model.Post;
import org.ahmedgaber.Redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
